import os
from pathlib import Path

os.environ.setdefault('BUILDER_SETTINGS_MODULE', 'globalSettings')
os.environ.setdefault('DEFAULT_SETTINGS_DIR', os.path.join(Path(__file__).parent.parent, 'settings'))