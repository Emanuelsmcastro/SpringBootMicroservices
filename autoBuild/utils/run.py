import importlib.util
import logging
import os

from managers import configManager

from .log import logger


def getError():
    input("Press any key to exit...")
    exit()


def importSettings(moduleName: str, settingsDir: str):
    try:
        fullPathSettings = os.path.join(settingsDir, moduleName + '.py')
        spec = importlib.util.spec_from_file_location(moduleName, fullPathSettings)
        module = importlib.util.module_from_spec(spec)
        spec.loader.exec_module(module)
        logger.info(f'Using settings: {module}')
        return module
    except FileNotFoundError as e:
        logger.error(e)
        getError()
    
    
def getEnvOrError(key: str):
    value = os.getenv(key)
    if not value:
        logger.error(f'Value {key} was not found.')
        raise ValueError(f'{key} not found.')
    logger.info(f'{key} found.')
    return value
    

def loadConfigs():
    logger.info(f'Using ConfigManager: {configManager}')
    configModule = getEnvOrError('BUILDER_SETTINGS_MODULE')
    configDir = getEnvOrError("DEFAULT_SETTINGS_DIR")
    return importSettings(configModule, configDir)


def runBuilder(cls):
    os.system('cls' if os.name == 'nt' else 'clear')
    logger.info(f"Runner found: {cls.__name__}")
    runner = cls(loadConfigs())
    runner.build()
