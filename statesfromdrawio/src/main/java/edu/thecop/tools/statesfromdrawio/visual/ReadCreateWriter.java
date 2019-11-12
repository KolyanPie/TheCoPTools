package edu.thecop.tools.statesfromdrawio.visual;

import edu.thecop.tools.statesfromdrawio.code.CodeCreator;
import edu.thecop.tools.statesfromdrawio.diagram.DiagramException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class ReadCreateWriter {
    private CodeCreator codeCreator;
    private DirectoryChooser directoryChooser;
    private FileChooser fileChooser;
    private Alert dialog;

    @FXML
    private TextField xmlFiled;
    @FXML
    private TextField packageField;
    @FXML
    private TextField folderField;
    @FXML
    private Button writeButton;

    public void initialize() {
        directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose folder for save .java files");

        fileChooser = new FileChooser();
        fileChooser.setTitle("Choose xml file from draw.io Extras/Edit Diagram...");
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("xml file (*.xml)", "*.xml"));

        dialog = new Alert(Alert.AlertType.ERROR);
        dialog.setTitle("We have a trouble");
        dialog.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        xmlFiled.textProperty().addListener((observable, oldValue, newValue) -> folderField.setText(newValue.replaceFirst("(\\..*$)", "")));
        folderField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue.replaceFirst(".*/", "").equals(packageField.getText().replaceFirst(".*\\.", ""))) {
                packageField.setText(packageField.getText().replaceFirst("[^.]*$", newValue.replaceFirst(".*/", "")));
            } else {
                packageField.setText(packageField.getText() + "." + newValue.replaceFirst(".*/", ""));
            }
            if (packageField.getText().replaceFirst(".*\\.", "").isEmpty()) {
                packageField.setText(packageField.getText().replaceFirst("\\.$", ""));
            }
        });
    }

    public void read() {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(xmlFiled.getText());
            codeCreator = new CodeCreator(document);
            writeButton.setDisable(false);
        } catch (DiagramException e) {
            showErrorDialog(e);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            showErrorDialog(new Exception("Trouble with file"));
        }
    }

    public void write() {
        codeCreator.create(packageField.getText());
        try {
            codeCreator.write(new File(folderField.getText()));
        } catch (IOException e) {
            showErrorDialog(new Exception("Trouble with file"));
        }
    }

    public void chooseXml() {
        try {
            xmlFiled.setText(fileChooser.showOpenDialog(null).getAbsolutePath());
        } catch (NullPointerException ignored) {

        }
    }

    public void chooseFolder() {
        try {
            folderField.setText(directoryChooser.showDialog(null).getAbsolutePath());
        } catch (NullPointerException ignored) {

        }
    }

    private void showErrorDialog(Exception e) {
        System.out.println(e.toString());
        dialog.setContentText(e.getMessage());
        dialog.getDialogPane().autosize();
        dialog.show();
    }
}
