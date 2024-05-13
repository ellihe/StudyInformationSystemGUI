package fi.tuni.prog3.sisufxml;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;

/**
 * Controlls the visual view of secondWindow.fxml
 * @author A, Ellinoora, N
 */
public class SecondWindowController {
    
    private static boolean rootViewAdded;
    @FXML private TreeView studyTreeView;
    @FXML private ChoiceBox chooseMenu;
    @FXML private TextField emailField;
    @FXML private TextField startYearField;
    @FXML private TextField studyDirField;
    @FXML private TextField studentIDField;
    @FXML private TextField nameField;
    @FXML private WebView courseInfoWebView;
    @FXML private Label infoName;
    @FXML private Label infoCredits;
    @FXML private Label infoGrading;
    @FXML private Label degreeLabel;
    @FXML private CheckBox completeBox;
    @FXML private Label courseCredit;
    @FXML private Label totalCredits;
    private static JsonObject currentIDObject;
    
    
    /**
     * Gets the boolean value if rootview is added to fxml or not
     * @return returs the boolean value if rootview is added or not
     */
    public static boolean isRootViewAdded() {
        return rootViewAdded;
    }
    
    /**
     * Sets the boolean value if rootview is added or not
     * @param rootViewAdded gives the value if rootview is added to fxml or not
     */
    public static void setRootViewAdded(boolean rootViewAdded) {
        SecondWindowController.rootViewAdded = rootViewAdded;
    }
    
   @FXML
    /**
     * Switches back to log in view
     */
    private void switchToStartWindow() throws IOException {
        Sisu.setRoot("StartWindow");
        rootViewAdded = false;
    }
    
    /**
     * Creates the initial window when logging in.
     */
    @SuppressWarnings("unchecked")
    public void setRoot(){
        
        if (Sisu.getCurrentDegree() == null) {
            degreeLabel.setText("Select degree from Student Information tab!");
            displayInfo(null);
        }
        if (!rootViewAdded) {
            addStudentInfo();
            saveStudentData();
        }
        
    }
    
    /**
     * Initially sets up the study structure field with the users selected
     * degree. Creates the modules at the top of the hierarchy.
     * @param m - The root module of a study structure.
     */
    public void setRoot(ModuleEntry m) {
        TreeItem root = new TreeItem();
        if (!m.isChildrenAdded()) {
            m.getChildren().addAll(Sisu.getDataStruct().getChildrenModules(m
                    .getEntryData(), m));
            m.setChildrenAdded(true);
        }
        m.setTreeItem(root);
        for (ModuleEntry each : m.getChildren()) {
            TreeItem i = new TreeItem(each);
            root.getChildren().add(i);
            each.getChildren().addAll(Sisu.getDataStruct()
                    .getChildrenModules(each.getEntryData(), each));
            each.setChildrenAdded(true);
            each.setTreeItem(i);
            if (each.getType().equals("Course")) {
                String name = "";
                if (each.isCompleted()) {
                    name = "käytykurssi.png";
                }
                else {
                    name = "käymätönkurssi.png";
                }
                try {
                    each.getTreeItem().setGraphic(new ImageView(new Image(
                        new FileInputStream(new File("src/main/resources/"
                                + "fi/tuni/prog3/sisufxml/seppele_" + name)))));
                }
                catch (FileNotFoundException ee) {
                }
            }
            for (ModuleEntry child : each.getChildren()) {
                TreeItem ii = new TreeItem(child);
                i.getChildren().add(ii);
                child.setTreeItem(ii);
                if (child.getType().equals("Course")) {
                    String name;
                    if (child.isCompleted()) {
                        name = "käytykurssi.png";
                    }
                    else {
                        name = "käymätönkurssi.png";
                    }
                    try {
                        child.getTreeItem().setGraphic(new ImageView(new Image(
                        new FileInputStream(new File("src/main/resources/fi/"
                                + "tuni/prog3/sisufxml/seppele_" + name)))));
                    }
                    catch (FileNotFoundException ee) {
                    }
                }
            }
            
        }
        studyTreeView.setRoot(root);
        studyTreeView.setShowRoot(false);
        rootViewAdded = true;
        degreeLabel.setText(String.format("%s" , m.getName()));
        degreeLabel.setText(m.getName());
        displayInfo(m);
    }
    
    /**
     * Adds the children of a module to the TreeView, creating a new dropdown
     * with all the content under the parent.
     * @param parent - Parent study module
     * @param visualItem - TreeItem which is set to have new child items
     */
    public static void addChildrenTreeView(ModuleEntry parent,
            TreeItem visualItem){
        // Testi onko kurssi
        if (parent.getType().equals("Course")) {
            return;
            
        }
        
        ArrayList<ModuleEntry> arr = Sisu.getDataStruct()
                .getChildrenModules(parent.getEntryData(), parent);
        parent.getChildren().addAll(arr);
        parent.setChildrenAdded(true);
        
        for (ModuleEntry each : parent.getChildren()) {
            if (each.isChildrenAdded()) {
                continue;
            }
            
            TreeItem i = new TreeItem(each);
            visualItem.getChildren().add(i);
            each.setTreeItem(i);
            if (each.getType().equals("Course")) {
                String name = "";
                if (each.isCompleted()) {
                    name = "käytykurssi.png";
                }
                else {
                    name = "käymätönkurssi.png";
                }
                try {
                    each.getTreeItem().setGraphic(new ImageView(new Image(
                        new FileInputStream(new File("src/main/resources/fi/"
                                + "tuni/prog3/sisufxml/seppele_" + name)))));
                }
                catch (FileNotFoundException ee) {
                }
            }
        }
    }
    
    /**
     * Handles clicks in the treeView UI.
     * @param e - MouseEvent pointing to the clicked element.
     */
    public void treeViewMouseEvent(MouseEvent e){
        
        Node node = e.getPickResult().getIntersectedNode();
       
        
        if (node instanceof Text) {
            TreeItem t = (TreeItem)(studyTreeView.getSelectionModel()
                    .getSelectedItem());
            ModuleEntry m = (ModuleEntry) ((TreeItem)(studyTreeView
                    .getSelectionModel().getSelectedItem())).getValue();

            displayInfo(m);
            
            if (m.getType().equals("Course")) {
                return;
            }
            
            if (t.isExpanded()) {
                t.setExpanded(false);
            }
            else {
                t.setExpanded(true);
            }
            
            if (!m.isChildrenAdded()) {
                addChildrenTreeView(m, t);
                m.setChildrenAdded(true);
            }
            
            for(ModuleEntry each : m.getChildren()) {
                if (each.isChildrenAdded()) {
                    continue;
                }
                TreeItem ti = each.getTreeItem();
                addChildrenTreeView(each, ti);
            }
        }
    }
    
    /**
     * When loading a users study structure, sets the status of the completed
     * courses to the correct status based on the student data
     * @param m - The selected study module.
     */
    private void courseCompleted(ModuleEntry m) {
        
        JsonArray a = currentIDObject.get("Completed Courses").getAsJsonArray();
        for (JsonElement each : a) {
            try {
                String s = each.getAsJsonObject().get(m.getId()).getAsString();
                completeBox.setSelected(true);
                return;
            }
            catch (Exception e){
            }
        }
        completeBox.setSelected(false);
        
    }
    
    /**
     * Updates information in the student JSON file when a new course is set to
     * "Completed" status, adds the earned credits to the students data.
     * If the "Completed" status is removed, removes credits and completion for
     * the course from the data.
     * @param e - MouseEvent pointing to the used checkbox.
     */
    public void handleCheckBox(MouseEvent e) {

        
            ModuleEntry m = (ModuleEntry) ((TreeItem)(studyTreeView
                    .getSelectionModel().getSelectedItem())).getValue();
            if(completeBox.isSelected()) {
                if (!currentIDObject.get("Completed Courses").getAsJsonArray()
                    .contains(new JsonPrimitive(m.getId()))) {
                    
                    JsonObject o = new JsonObject();
                    o.add(m.getId(), new JsonPrimitive(m.getCredits()));
                    
                    currentIDObject.get("Completed Courses").getAsJsonArray()
                            .add(o);
                    
                    
                    
                    Integer credits = Integer.parseInt((currentIDObject
                            .get("Total Credits").getAsJsonPrimitive()
                            .toString()));

                    currentIDObject.add("Total Credits", 
                                new JsonPrimitive(credits + m.getCredits()));
                    m.setCompleted(true);
                    DataStructure.courseCreditsToParent(m);
                    try {
                        m.getTreeItem().setGraphic(new ImageView(new Image(
                            new FileInputStream(new File("src/main/resources/"
                                    + "fi/tuni/prog3/sisufxml/"
                                    + "seppele_käytykurssi.png")))));
                    }
                    catch (FileNotFoundException ee) {
                        
                    }
                    
                }
            } else {
                 if (hasModule(m.getId())) {
                    removeModule(m);
                 
                    Integer credits = Integer.parseInt((currentIDObject
                            .get("Total Credits").getAsJsonPrimitive()
                            .toString()));
                    currentIDObject.add("Total Credits", 
                                new JsonPrimitive(credits - m.getCredits()));
                    
                    m.setCompleted(false);
                    DataStructure.courseCreditsToParent(m);
                    try {
                        m.getTreeItem().setGraphic(new ImageView(new Image(
                            new FileInputStream(new File("src/main/resources/fi"
                                    + "/tuni/prog3/sisufxml"
                                    + "/seppele_käymätönkurssi.png")))));
                    }
                    catch (FileNotFoundException ee) {
                        
                    }
                }
            }
    }
    
    /**
     * Shows the available information on the chosen study module. Shows
     * different fields depending on the module type. Shows available credits,
     * loads label and info of the chosen module. If the module is a course,
     * an option to set it to "Completed" status is available.
     * @param m - The chosen study module
     */
    private void displayInfo(ModuleEntry m) {
        
        if (m == null) {
            courseInfoWebView.setVisible(false);
            courseCredit.setVisible(false);
            completeBox.setVisible(false);
            infoCredits.setVisible(false);
            infoName.setVisible(false);
            infoGrading.setVisible(false);
            return;
        }
        infoName.setVisible(true);
        infoGrading.setVisible(true);
        
        courseInfoWebView.setVisible(true);
        if(!"".equals(m.getCredits())) {
            courseCredit.setText(m.getCredits() + "op");
        }
        
        else {
            courseCredit.setVisible(false);
        }

        if (m.getType().equals("Course")) {
            courseInfoWebView.getEngine().loadContent(String.format("%s%s",
                    DataStructure.getContent(m), DataStructure.getOutcomes(m)));
            
            infoName.setText(m.getName());
            infoGrading.setText(DataStructure.getGrading(m));
            
            StudentData s = Sisu.getStudentData();
            
            completeBox.setVisible(true);
            courseCompleted(m);
        }
        else {
            completeBox.setVisible(false);
            infoCredits.setVisible(false);
            
            String type = m.getEntryData().get("type").getAsString();
            
            
            switch (type) {
                case "DegreeProgramme": {
                    courseInfoWebView.getEngine().loadContent(DataStructure
                            .getProgrammeInfo(m));
                    if("".equals(DataStructure.getProgrammeInfo(m))) {
                        courseInfoWebView.setVisible(false);
                    }
                    courseInfoWebView.getEngine().loadContent(DataStructure
                            .getProgrammeInfo(m));
                    infoGrading.setText("");
                    infoName.setText(m.getName());
                    break;
                }
                
                case "StudyModule": {
                    courseInfoWebView.getEngine().loadContent(DataStructure
                            .getStudyModuleInfo(m));
                    if("".equals(DataStructure.getProgrammeInfo(m))) {
                        courseInfoWebView.setVisible(false);
                    }
                    courseInfoWebView.getEngine().loadContent(DataStructure
                            .getStudyModuleInfo(m));
                    infoGrading.setText("");
                    infoName.setText(m.getName());
                    break;
                }
                
                case "GroupingModule": {
                    if("".equals(DataStructure.getProgrammeInfo(m))) {
                        courseInfoWebView.setVisible(false);
                    }
                    infoGrading.setText("");
                    infoName.setText(m.getName());
                    break;
                }

                default: {
                    String s = "";
                }
            }
        }
    }
    
    /**
     * When logging into the application, sets the account credentials to the
     * UI fields to show the account currently logged in.
     * Adds all of the possible degrees to the dropdown menu.
     */
    public void addStudentInfo() {
        nameField.setText(Sisu.getCurrentName());
        studentIDField.setText(Sisu.getCurrentID());

        String s = (String) currentIDObject.get("Start Year")
                .getAsJsonPrimitive().getAsString();
        if (!s.equals("null")) {
            startYearField.setText(s);
        }
        
        s = currentIDObject.get("Email")
                .getAsString();
        if (!s.equals("null")) {
            emailField.setText(s);
        }
        
        s = currentIDObject.get("Study Direction")
                .getAsString();
        if (!s.equals("null")) {
            studyDirField.setText(s);
        }
        
        //asggasgasga
        
        if (chooseMenu.getItems().isEmpty()) {
            for (ModuleEntry entry : Sisu.getDataStruct().getModuleList()) {
               chooseMenu.getItems().add(entry);
               if (!currentIDObject.get("Degree").equals("null") && 
                       ("\"" + entry.getName() + "\"").equals(currentIDObject
                               .get("Degree").toString())) {
                   chooseMenu.getSelectionModel().select(entry);
               }
            }
        }
        
        totalCredits.setText(String.valueOf(currentIDObject
                .get("Total Credits").getAsInt()));
    }
    
    /**
     * Saves the additional student data to the JSON file which contains student
     * information.
     */
    public void saveStudentData() {
        addItemsToStudentData("Start Year", startYearField);
        addItemsToStudentData("Email", emailField);
        addItemsToStudentData("Study Direction", studyDirField);
        
        ModuleEntry m = (ModuleEntry) chooseMenu.getSelectionModel()
                .getSelectedItem();
        if (m != null) {
            setRoot(m);
            Sisu.setCurrentDegree(m);
        }
        if (Sisu.getCurrentDegree() != null) {
            if (!Sisu.getCurrentDegree().getName().equals(currentIDObject
                    .get("Degree").getAsString())) {
                addDegree();
            }
        }
    }
    
    /**
     * Handles closing of the application, when closing saves the student JSON
     * file to save any changes made to student information I.E; new completed
     * courses or other student information
     * @throws IOException - If file is not found, throws exception.
     */
    public void closeButtonHandler() throws IOException {
 
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        try (FileWriter file = new FileWriter("students.json")) {
            String json = gson.toJson(Sisu.getStudentData().getStudentData());
            
            file.write(json);
            file.close();
        } catch(IOException e) {
            
        }
        Sisu.getStage1().close();
    }
    /**
     * Adds a chosen degree to the student information JSON file. The degree
     * added is chosen from the dropdown menu.
     */
    public void addDegree() {
        
        if (chooseMenu.getSelectionModel().getSelectedItem() 
                instanceof ModuleEntry) {
            String degree = chooseMenu.getSelectionModel().getSelectedItem()
                    .toString();
            currentIDObject
                    .add("Degree", new JsonPrimitive(degree));
            currentIDObject.add("Completed Courses", new JsonArray());
            currentIDObject.add("Total Credits", new JsonPrimitive(0));
            
        }
    }
    
    /**
     * Adds a single information field to the student data JSON file by getting
     * the value of the text entry field
     * @param  item - Name of the data to be added
     * @param textField - The object of the entry field
     */
    public void addItemsToStudentData(String item, TextField textField) {

            String value = textField.getText();
            if(!value.equals("")) {
                currentIDObject.add(item, new JsonPrimitive(value));
            }
            else {
                
                currentIDObject.add(item, new JsonPrimitive("null"));
            }
    }
    
    /**
     * Checks if a Student has a certain module in their "Completed Courses"
     * @param id The ID of the module
     * @return Boolean, true if found, false if not
     */
    public static boolean hasModule(String id) {
        JsonArray a = currentIDObject.get("Completed Courses").getAsJsonArray();
        for (JsonElement each : a) {
            try {
                String s = each.getAsJsonObject().get(id).getAsString();
                return true;
            }
            catch (Exception e){
            }   
        }
        return false;
    }
    
    /**
     * Removes a module from the "Completed Courses"
     * @param m the module to be removed
     */
    private static void removeModule(ModuleEntry m) {
        JsonArray a = currentIDObject.get("Completed Courses").getAsJsonArray();
        int laskuri = 0;
        for (JsonElement each : a) {
            
            try {
                String s = each.getAsJsonObject().get(m.getId()).getAsString();
                break;
            }
            catch (Exception e){
            }
            laskuri += 1;
        }
        currentIDObject.get("Completed Courses").getAsJsonArray()
                .remove(laskuri);
    }
    
    private void updateView() {
        
    }
    
    /**
     * Returns the value of current student's study information
     * @return the value of current student's study information
     */
    public static JsonObject getCurrentIDObject() {
        return currentIDObject;
    }

    /**
     * Sets the value of current student's study information
     * @param currentIDObject1 the value of current student's study information
     */
    public static void setCurrentIDObject(JsonObject currentIDObject1) {
        currentIDObject = currentIDObject1;
    }
}
