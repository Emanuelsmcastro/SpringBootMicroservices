import asyncio
import subprocess
import time
from datetime import datetime
from pathlib import Path

import aiofiles
from utils.log import logger


class RunnerBase:
    
    DEBUG = True
    performBackup = True
    gradleBuildPattern = "**/build.gradle"
    dockerFileName = "Dockerfile"
    buildJarPattern = "**/build/libs/*.jar"
    buildJar = "app.jar"
    excludeBuildJarPattern = "-plain"
    buildPath: Path = Path('./build/libs')
    
    
    gradleList = []
    gradleWarns = 0
    gradlErrors = 0
    gradleSuccess = 0
    
    dockerFileList = []
    
    processes = []
    
    def __init__(self, settings) -> None:
        self.settings: Path = settings
        
    def timer(func):
        def wrapper(self, *args, **kwargs):
            startTime = time.time()
            result = func(self, *args, **kwargs)
            endTime = time.time()
            logger.info(f'Elapsed time | {func.__name__}: {endTime - startTime:.2f}s')
            return result
        return wrapper
    
    @timer
    def build(self):
        loop = asyncio.get_event_loop()
        try:
            self.__preparerBuildJarsBackup(loop)
            loop.run_until_complete(self.__buildGradle())
            loop.run_until_complete(self.__updateDockerFile())
        except Exception as e:
            logger.exception(f'An error occurred: {e}')
            self.__cleaningUp(loop)
        except KeyboardInterrupt:
            logger.warning("Received exit, exiting")
            self.__cleaningUp(loop)
        finally:
            loop.close()
    
    def __preparerBuildJarsBackup(self, loop: asyncio.AbstractEventLoop):
        logger.info(f'Perfom Backup: {self.performBackup}')
        jars = self.settings.ROOT_DIR.glob(self.buildJarPattern)
        if any(jars):
            if self.performBackup:
                logger.info("Jars found, starting backup...")
                parentPath = self.settings.ROOT_DIR
                backupPath: Path = parentPath / Path("backup")
                backupPath.mkdir(parents=True, exist_ok=True)
                nowDir = backupPath / Path(datetime.now().strftime("%Y-%m-%d_%H-%M-%S"))
                nowDir.mkdir(parents=True, exist_ok=True)
            else:
                logger.info("Jars found, starting cleaning...")
            futures = [loop.run_in_executor(None, self.__buildJarMakeBackup, jar, nowDir) for jar in jars]
            loop.run_until_complete(asyncio.gather(*futures))
            logger.info("No Jars found, backup or cleaning will not be done...")
    
    def __buildJarMakeBackup(self, jar: Path, nowDir: Path):
        if self.DEBUG:
            logger.info(f'Found jar: {jar}')
        if self.performBackup:
            jarName = jar.name
            jar.rename(nowDir / Path(jarName))
        else:
            jar.unlink(missing_ok=True)
        
        
    def __cleaningUp(self, loop):
        logger.info("Cleaning up")
        pending_tasks = [
            task for task in asyncio.all_tasks(loop) if not task.done()
        ]
        for process in self.processes:
            try:
                process.terminate()
            except Exception as e:
                logger.exception(f"An error occurred: {e}")
            except KeyboardInterrupt:
                logger.exception(f"KeyboardInterrupt in cleaningUp")
        loop.run_until_complete(asyncio.gather(*pending_tasks))

        
    def __findGradles(self):
        path: Path = self.settings.ROOT_DIR
        buildGradleList = path.glob(self.gradleBuildPattern)
        for gradle in buildGradleList:
            logger.info(f'Gradle found: {gradle}')
            self.gradleList.append(gradle)

    def __findDockerFile(self, gradle: Path):
        dockerFile: Path = gradle.parent / Path(self.dockerFileName)
        if dockerFile.exists():
            logger.info(f'Dockerfile found: {dockerFile}')
            self.gradleSuccess += 1
            self.dockerFileList.append(dockerFile)
        else:
            logger.warning(f'Dockerfile is missing: {dockerFile}')
            self.gradleWarns += 1
            
    def showInf(self, warns, erros):
        logger.warning(f'{"Warnins count":<14}: {warns:<3}')
        logger.error(f'{"Errors count":<14}: {erros:<3}')
        
    async def __writeDockerFile(self, dockerFile: Path, newLines: str):
        async with aiofiles.open(dockerFile, mode='w') as file:
            await file.write(newLines)
        
    async def __openDockerFile(self, dockerFile: Path):
        async with aiofiles.open(dockerFile, mode='r+') as file:
            try:
                contents = await file.read()
                contentList = contents.split('\n')
                for i, content in enumerate(contentList):
                    if content.startswith("COPY"):
                        jar = [jar for jar in list(dockerFile.parent.glob(self.buildJarPattern)) if not jar.name.__contains__(self.excludeBuildJarPattern)]
                        newPath = self.buildPath / jar[0].name
                        print(newPath.as_posix())
                        newLine = f'COPY {newPath.as_posix()} {self.buildJar}'
                        if self.DEBUG:
                            logger.info(f'Found target line: {content}')
                            logger.info(f'Replace by: {newLine}')
                        contentList[i] = newLine
                    newContents = '\n'.join(contentList)
            except Exception as e:
                logger.error(f'An error occurred: {e}')
            finally:
                await self.__writeDockerFile(dockerFile, newContents)
                return dockerFile
    
    async def __updateDockerFile(self):
        logger.info("Start update Dockerfile")
        tasks = []
        for _, dockerFile in enumerate(self.dockerFileList):
            taskName = dockerFile.parent.name + " | " +  self.dockerFileName
            task = asyncio.create_task(self.__openDockerFile(dockerFile), name=taskName)
            tasks.append(task)
            logger.info(f'Prepare task: {taskName}')
        
        for task in asyncio.as_completed(tasks):
            result = await task
            logger.info(f'Finished: {result}')
    
    async def __buildGradle(self):
        tasks = []
        self.__findGradles()
        for _, gradle in enumerate(self.gradleList):
            taskName = gradle.parent.name
            task = asyncio.create_task(self.__getGradle(gradle), name=taskName)
            tasks.append(task)
            logger.info(f'Prepare task: {taskName}')
            self.gradleSuccess += 1
        
        for task in asyncio.as_completed(tasks):
            result = await task
            logger.info(f'Finished: {result}')
            self.gradleSuccess += 1
            self.__findDockerFile(result)
        self.showInf(self.gradleWarns, self.gradlErrors)
        logger.info(f'Finished gradle build !')
            
    async def __getGradle(self, gradle):
        result = await self.__execBuildGradle(gradle)
        await asyncio.sleep(0)
        return result
        
    async def __execBuildGradle(self, gradle: Path) -> None:
        command = f'cd {gradle.parent} && gradle build'
        logger.info(f'Apply: {command}')
        process = await asyncio.create_subprocess_shell(
            command, stdout=subprocess.PIPE,
            stderr=subprocess.PIPE
        )
        self.processes.append(process)
        if not self.DEBUG:
            stdout, stderr = await process.communicate()
            if stdout:
                logger.info(f'[stdout]\n{stdout.decode()}')
                self.gradleSuccess += 1
            if stderr:
                logger.error(f'[stderr]\n{stderr.decode()}')
                self.gradlErrors += 1
        else:
            while True:
                output = await process.stdout.readline()
                if output:
                    logger.info(f'{gradle.parent.name:<10} | {output.decode().strip()}')
                elif process.stdout.at_eof():
                    break
        return gradle