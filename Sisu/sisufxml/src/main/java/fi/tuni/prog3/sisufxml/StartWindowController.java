/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package fi.tuni.prog3.sisufxml;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Class representing the functionality of the initial log in window
 * to the application
 * @author Ellinoora
 */
public class StartWindowController {
    @FXML private TextField nameField;
    @FXML private Label errorLabel;
    @FXML private TextField studentIDField;
    @FXML ArrayList<ModuleEntry> theList;
    

    @FXML
    /**
     * Switches to the second/main window if a valid student ID and name are
     * entered. Checks if the Student ID is already in use with a different name
     * If a user is found, retrieves the user data from the JSON file.
     * 
     */
    private void switchToSecondWindow() throws IOException {
        if ("".equals(this.nameField.getText()) || "".equals(this.studentIDField
                .getText())) {
            this.errorLabel.setText("Give name and student ID");

        } else {
            String name = this.nameField.getText();
            String studentID = this.studentIDField.getText();
            
            
            if (Sisu.getStudentData().getStudentData().has(studentID)) {
                
                String s = Sisu.getStudentData()
                        .getStudentData().get(studentID).getAsJsonObject()
                        .get("Name").getAsString();
                
                if (name.equals(Sisu.getStudentData()
                        .getStudentData().get(studentID).getAsJsonObject()
                        .get("Name").getAsString())) {
                    
                    Sisu.setCurrentID(studentID);
                    Sisu.setCurrentName(name);
                    SecondWindowController.setCurrentIDObject(
                            Sisu.getStudentData().getStudentData()
                            .get(Sisu.getCurrentID()).getAsJsonObject());
                    
                    Sisu.setRoot("secondWindow");
                } 
                else {
                    this.errorLabel.setText("Student ID and name don't match!");
                    return;
                }
            } else {
                Sisu.setCurrentID(studentID);
                Sisu.setCurrentName(name);
                
                Sisu.getStudentData().getStudentData().add(studentID
                        , new JsonObject());
                
                SecondWindowController.setCurrentIDObject(Sisu.getStudentData()
                            .getStudentData().get(Sisu.getCurrentID())
                            .getAsJsonObject());
                
                
                SecondWindowController.getCurrentIDObject()
                        .add("Name", new JsonPrimitive(name));
                SecondWindowController.getCurrentIDObject()
                        .add("Email", new JsonPrimitive("null"));
                SecondWindowController.getCurrentIDObject()
                        .add("Study Direction", new JsonPrimitive("null"));
                SecondWindowController.getCurrentIDObject()
                        .add("Start Year", new JsonPrimitive("null"));
                SecondWindowController.getCurrentIDObject()
                        .add("Total Credits", new JsonPrimitive(0));
                SecondWindowController.getCurrentIDObject()
                        .add("Degree", new JsonPrimitive("null"));
                SecondWindowController.getCurrentIDObject()
                        .add("Completed Courses", new JsonArray());
                
                }
                Sisu.setRoot("secondWindow");
            }
        }
    
    /**
     * When clicking enter, attempts to log in with the current values in
     * credential fields
     * 
     * @param event Keyboard press event
     * @throws IOException Throws IOException
     */
    @FXML
    public void enterEvent(KeyEvent event) throws IOException {
        if (event.getCode().equals(KeyCode.ENTER)) {
            this.switchToSecondWindow();
        }
    }
}
