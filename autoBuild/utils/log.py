import logging

from colorama import Fore, Style, init


class ColorizingStreamHandler(logging.StreamHandler):
    color_map = {
        logging.DEBUG: Fore.BLUE,
        logging.INFO: Fore.GREEN,
        logging.WARNING: Fore.YELLOW,
        logging.ERROR: Fore.RED,
        logging.CRITICAL: Fore.RED + Style.BRIGHT,
    }

    def __init__(self, stream, color_map=None):
        logging.StreamHandler.__init__(self, init(strip=False))
        if color_map is not None:
            self.color_map = color_map

    def format(self, record):
        record.msg = f"{self.color_map.get(record.levelno)}{record.msg}{Style.RESET_ALL}"
        return logging.StreamHandler.format(self, record)

logger = logging.getLogger(__name__)
handler = ColorizingStreamHandler(logging.StreamHandler())
handler.setFormatter(logging.Formatter("%(asctime)s - %(levelname)s - %(message)s (%(filename)s:%(lineno)d)"))
logger.addHandler(handler)
logger.setLevel(logging.DEBUG)