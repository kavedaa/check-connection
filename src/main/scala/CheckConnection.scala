package check

import javafx.application.*
import javafx.stage.*
import javafx.scene.Scene
import javafx.scene.layout.*

object CheckConnection:

  System.setProperty("logback.configurationFile", "logback.xml")
  
  @main def main = Application.launch(classOf[CheckConnection])

class CheckConnection extends Application:

  def start(stage: Stage) =
    given Window = stage
    def root = ConnectionModule.pane
    val scene = new Scene(root)
    stage.setScene(scene)
    stage.setTitle("Check connection")
    stage.show()



