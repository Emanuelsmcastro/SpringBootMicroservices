from runners.runnerBase import RunnerBase
from utils.run import runBuilder

@runBuilder
class BuildGradleAndCreateContainer(RunnerBase):
    
    def __init__(self, settings) -> None:
        super().__init__(settings)

    def build(self):
        print(self.settings)
        pass