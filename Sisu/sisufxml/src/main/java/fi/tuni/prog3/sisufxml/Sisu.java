package fi.tuni.prog3.sisufxml;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileReader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;
import static javafx.application.Application.launch;

/**
 * A class representing the main application. Responsible for the start of 
 * the program, sets everything up and has the attributes that hold core data
 * needed to run the program.
 * @author A, Ellinoora, N
 */
public class Sisu extends Application {

    private static Scene scene;
    private static Stage stage1;
    private static DataStructure dataStruct;
    private static StudentData studentData;
    private static String currentID;
    private static String currentName;
    private static ModuleEntry currentDegree;

     /**
     * Constructs stage for the GUI.
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        stage1 = stage;
        scene = new Scene(loadFXML("StartWindow"), 1080, 600);

        this.createStudentData();
        this.downloadRootData();
        
        stage.setScene(scene);
        stage.show();
        
        SecondWindowController.setRootViewAdded(false);
        currentDegree = null;
    }

    /**
     * Creates a new DataStructure
     */
    private void downloadRootData() {
        dataStruct = new DataStructure();
        dataStruct.populateDataStruct();
    }
    
    /**
     * Creates new root for the study structure
     * @param String fxml
     * @throws IOException
     */
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                Sisu.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    /**
     * Launches program
     * @param args startup arguments
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Initializes a StudentData object.
     */
    public void createStudentData() {
        JsonObject studentsJson = new JsonObject();
        studentData = new StudentData(studentsJson);
        
        JsonParser parser = new JsonParser();
        try (FileReader file = new FileReader("students.json")) {
            Object obj = parser.parse(file);
                JsonObject studentData1 = (JsonObject) obj;
                Sisu.getStudentData().setStudentData(studentData1);

        } 
        catch(Exception e) {
        }
    }

    
    
    /**
     * Returns the scene object
     * @return current scene object
     */
    public static Scene getScene() {
        return scene;
    }
    
    /**
     * Returns the stage object on which the GUI is built upon.
     * @return stage1 the GUI
     */
    public static Stage getStage1() {
        return stage1;
    }

    /**
     * Gives the object of datastructure
     * @return datastructure of one DataStructure object
     */
    public static DataStructure getDataStruct() {
        return dataStruct;
    }

    /**
     * Returns the StudentData object of the currently logged in user
     * @return studentData the StudentData object of the user currenty logged in
     */
    public static StudentData getStudentData() {
        return studentData;
    }

    /**
     * Returns the Student ID of the currently logged in user
     * @return currentID the Student ID of the user logged in
     */
    public static String getCurrentID() {
        return currentID;
    }

    /**
     * Returns the name of the currently logged in user
     * @return currentName - the name of the user logged in
     */
    public static String getCurrentName() {
        return currentName;
    }

    /**
     * Sets the current users student ID
     * @param currentID - the student ID of the user logged in
     */
    public static void setCurrentID(String currentID) {
        Sisu.currentID = currentID;
    }

    /**
     * Sets the current users name
     * @param currentName - the name of the user logged in
     */
    public static void setCurrentName(String currentName) {
        Sisu.currentName = currentName;
    }
    
    /**
     * Returns the current chosen degree programme.
     * @return currentDegree - the chosen module
     */
    public static ModuleEntry getCurrentDegree() {
        return currentDegree;
    }
    
    /**
     * Sets the current degree after choosing from dropdown menu
     * @param m - the chosen module
     */
    public static void setCurrentDegree(ModuleEntry m) {
        currentDegree = m;
    }
}