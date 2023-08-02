package check

import scala.util.*

import javafx.geometry.*
import javafx.scene.layout.*
import javafx.scene.control.*

import org.sphix.*
import org.sphix.ui.editor.*

import org.shaqal._
import org.shaqal.jtds.JtdsFactory
import org.shaqal.adapter._
import org.sphix.ui.dialog.ExceptionDialog
import org.sphix.ui.dialog.InfoDialog
import org.sphix.ui.FormUtils

import no.vedaadata.xml.*
import org.sphix.ui.FileChoosing
import scala.xml.XML

case class ConnectionForm(
  server: String,
  port: Int,
  database: String,
  username: String,
  password: String)
  derives XmlEncoder, XmlDecoder

class ConnectionPane extends BorderPane with FormUtils:

  val editor = Editor[ConnectionForm]

  val loadXmlButton = new Button("Load")
  val saveXmlButton = new Button("Save")

  val checkButton = new Button("Check"):
    setDefaultButton(true)

  val buttonBar = new ButtonBar:
    getButtons.add(checkButton)

  checkButton.disableProperty <== editor.status.map(!_.toBoolean)

  val tools = vbox(loadXmlButton, saveXmlButton)

  val content =
    vbox(
      hbox(
        editor.container(Some("Connection properties")).layout(false),
        tools
      ),
      buttonBar
    )

  setCenter(padded(content))

object ConnectionModule:

  val pane = new ConnectionPane

  pane.checkButton.setOnAction { _ =>
    pane.editor.value().toOption foreach { form =>
      given Connector[Any] = new DataSourceDBC[Any](form.server, form.database, form.port, form.username, form.password, JtdsFactory):
        def adapter = MsSqlAdapter
        def name = "default" 
      sql"select 1".execute() match
        case Success(_) => 
          new InfoDialog("OK").show()
        case Failure(ex) => 
          new ExceptionDialog(ex).show()
    }    
  }

  pane.saveXmlButton.setOnAction { _ =>
    pane.editor.value().toOption foreach { form =>
      FileChoosing.withFileForSave("XML", "xml") { file =>
        val connectionXml = XmlEncoder.encode(form, "Connection")  
        XML.save(file.getAbsolutePath, connectionXml, "UTF-8")
      }
    }
  }

  pane.loadXmlButton.setOnAction { _ =>
    FileChoosing.withFileForOpen("XML", "xml") { file =>
      val decodedForm = for 
        connectionXml <- Try { XML.loadFile(file) }
        form <- XmlDecoder.decode[ConnectionForm](connectionXml)
      yield form
      decodedForm match
        case Success(form) =>
          pane.editor.set(form)
        case Failure(ex) =>
          new ExceptionDialog(ex).show()
    }
  }