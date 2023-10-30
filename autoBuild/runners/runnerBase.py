from pathlib import Path

class RunnerBase:
    
    def __init__(self, settings) -> None:
        self.settings = settings
    
    def build(self):
        pass
    
    def getBuildGradle(self):
        print(Path(__file__))
        assert TypeError("Error")