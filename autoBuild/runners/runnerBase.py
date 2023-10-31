import asyncio
import subprocess
from pathlib import Path

from utils.log import logger


class RunnerBase:
    
    DEBUG = True
    gradleBuildPattern = "**/build.gradle"
    gradleList = []
    
    def __init__(self, settings) -> None:
        self.settings: Path = settings
    
    def build(self):
        asyncio.run(self.__buildGradle())
    
    def __findGradles(self):
        path: Path = self.settings.ROOT_DIR
        buildGradleList = path.glob(self.gradleBuildPattern)
        for gradle in buildGradleList:
            logger.info(f'Gradle found: {gradle}')
            self.gradleList.append(gradle)
    
    async def __buildGradle(self):
        tasks = []
        self.__findGradles()
        loop = asyncio.get_event_loop()
        for _, gradle in enumerate(self.gradleList):
            taskName = gradle.parent.name
            task = asyncio.create_task(self.__getGradle(gradle, loop), name=taskName)
            tasks.append(task)
            logger.info(f'Prepare task: {taskName}')
        
        for task in asyncio.as_completed(tasks):
            result = await task
            logger.info(f'Finished: {result}')
            
    async def __getGradle(self, gradle, loop: asyncio.AbstractEventLoop,):
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
        if not self.DEBUG:
            stdout, stderr = await process.communicate()
            if stdout:
                logger.info(f'[stdout]\n{stdout.decode()}')
            if stderr:
                logger.error(f'[stderr]\n{stderr.decode()}')
        else:
            while True:
                output = await process.stdout.readline()
                if output:
                    logger.info(f'{gradle.parent.name:<10} | {output.decode().strip()}')
                elif process.stdout.at_eof():
                    break
        return gradle