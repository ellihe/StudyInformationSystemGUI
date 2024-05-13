/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt 
to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this 
template
 */
package fi.tuni.prog3.sisufxml;



import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.TreeMap;
import java.nio.charset.Charset;

/**
 * A class representing a data structure, that consists of  ModuleEntry objects.
 * Has the functions needed to initialize, modify and/or expand the structure. 
 * Uses Sisu API as a source for the module data and information.
 * @author A
 */
public class DataStructure {
    private ArrayList<JsonObject> degrees;
    private ArrayList<ModuleEntry> moduleList;
            
    /**
     * Constructs an empty DataStructure.
     */
    DataStructure() {
        this.degrees = new ArrayList<>();
        this.moduleList = new ArrayList<>();
        
    }
    
    /**
     * Finds the name of a module from EntryData. Prioritizes english over
     * finnish.
     * @param o the JSONobject to be searched
     * @return the name of the module
     */
    private static String getNameFromData(JsonObject o) {
        try {
            return o.get("name").getAsJsonObject().get("en").getAsString();
        }
        catch (Exception e) {
            return o.get("name").getAsJsonObject().get("fi").getAsString();
        }
    }
    
    /**
     * Returns the moduleList of the DataStructure.
     * @return ArrayList, list of all degrees.
     */
    public ArrayList<ModuleEntry> getModuleList() {
        return this.moduleList;
    }
    
    
    
    /**
     * Downloads a json file that includes all the root degrees. Creates 
     * a list of these JsonObjects, and assigns this list to be this 
     * DataStructures degrees.
     */
    public void getDegrees() {
        
        try{
            String stringurl = "https://sis-tuni.funidata.fi/kori/api/"
                    + "module-search?curriculumPeriodId=uta-lvv-2021"
                    + "&universityId=tuni-university-root-id&moduleType="
                    + "DegreeProgramme&limit=1000";
            
            JsonObject o = getJsonFromUrl(stringurl);

            
            JsonArray arr = o.get("searchResults").getAsJsonArray();
            
            
            ArrayList<JsonElement> tutkintoOhjelmat = new ArrayList<>();
            
            
            int x = arr.size();
            
            for (int i = 0; i < arr.size(); ++i){
                this.degrees.add(arr.get(i).getAsJsonObject());
            }
        }
        
        catch (Exception e) {
            
        }
    }

    /**
     * Populates the DataStructure with the information found inside the
     * different degrees, downloaded from Sisu. Creates ModuleEntries
     * from all of them, and adds them to this DataStructures moduleList.
     */
    public void populateDataStruct() {
        this.getDegrees();
        
        for (JsonObject each : this.degrees) {
            
            String id = each.get("groupId").getAsString();
            
            
            String type = "ModuleRule";
            
            String nimi = each.get("name").getAsString();
            
            
            JsonObject dada = this.getDataFromId(id, type);
            
            ModuleEntry m = new ModuleEntry(nimi, dada, null);
            this.moduleList.add(m);
            
        }
    }
    /**
     * Downloads a JSON file from a given URL address.
     * @param url the URL from which the data will be downloaded.
     * @return the JSONObject that was found.
     * @throws Exception if an error is encountered during downloading or 
     * parsing of the Json file.
     */
    private static JsonObject getJsonFromUrl(String url) throws Exception {
        InputStream input = new URL(url).openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input,
                Charset.forName("UTF-8")));
        StringBuilder builder = new StringBuilder();

        int ii = 0;

        while ((ii = reader.read()) != -1) {
            builder.append((char) ii);
        }

        String text = builder.toString();
        
        try {
            return new JsonParser().parse(text).getAsJsonObject();
        }
        catch (Exception e) {
            JsonArray a = new JsonParser().parse(text).getAsJsonArray();
            return (JsonObject) a.get(0);
        }
    }
    
    /**
     * Finds the data of a module using the module id and its type. Different 
     * types of modules require different kinds of url:s for Sisus API.
     * @param id - The module id.
     * @param type - The type of the module.
     * @return j - the retrieved Json Object.
     */
    private JsonObject getDataFromId(String id, String type) {
        
        JsonObject j = new JsonObject();
        try {
            switch (type) {
                
                case ("ModuleRule") :
                    {
                        try {
                            
                            j = getJsonFromUrl("https://sis-tuni.funidata.fi"
                                    + "/kori/api/modules/" + id);
                            break;
                        }
                        catch (Exception e) {
                            j = getJsonFromUrl("https://sis-tuni.funidata.fi"
                                    + "/kori/api/modules/by-group-id?groupId=" 
                                    + id + 
                                    "&universityId=tuni-university-root-id");
                            break;
                        }
                        
                    }
                case "CourseUnitRule":
                    {
                        j = getJsonFromUrl("https://sis-tuni.funidata.fi/kori"
                                + "/api/course-units/by-group-id?groupId=" 
                                + id + "&universityId=tuni-university-root-id");
                        break;
                    }
                case "GroupIdModule":
                    {
                        j = getJsonFromUrl("https://sis-tuni.funidata.fi/kori"
                                + "/api/modules/by-group-id?groupId=" + id + 
                                "&universityId=tuni-university-root-id");
                        break;
                    }
                    
                default:
                    throw new Exception();
            }
            
        }
        catch (Exception e) {
            j = null;
        }
        return j;
        
    }

    /**
     * Retrieves the child modules of a parent module, and returns a list which
     * includes them all. 
     * @param parentObject - data of the parent module to be searched for 
     * child modules.
     * @param parentModule - parent Module.
     * @return l - the list containing all child modules.
     */
    public ArrayList<ModuleEntry> getChildrenModules(JsonObject parentObject, 
            ModuleEntry parentModule) {
        try{
            ArrayList<ModuleEntry> l = new ArrayList<>();
            String type = "";
            
            try {
                String courseType = parentObject.get("courseUnitType")
                        .getAsString();
                type = "CourseUnitRule";
            }
            catch (Exception e) {
                type = parentObject.get("type").getAsString();
            }
            
            
            
            
            switch(type) {
                case "StudyModule": {
                    JsonObject x = parentObject.get("rule").getAsJsonObject();
                    l.addAll(this.getChildrenModules(x, parentModule));
                    break;
                }
                
                case "DegreeProgramme": {
                    JsonObject x = parentObject.get("rule").getAsJsonObject();
                    l.addAll(this.getChildrenModules(x, parentModule));
                    break;
                }
                
                case "CreditsRule": {
                    JsonObject x = parentObject.get("rule").getAsJsonObject();
                    l.addAll(this.getChildrenModules(x, parentModule));
                    break;
                }
                
                case "GroupingModule": {
                    JsonObject x = parentObject.get("rule").getAsJsonObject();
                    l.addAll(this.getChildrenModules(x, parentModule));
                    break;
                }
                
                case "CompositeRule": {
                    JsonArray arr = parentObject.get("rules").getAsJsonArray();
                    for (JsonElement each : arr) {
                        ArrayList<ModuleEntry> m = this.getChildrenModules(
                                each.getAsJsonObject(), parentModule);
                        
                        l.addAll(m);
                    }
                    break;
                }
                
                case "ModuleRule": {
                    String id = parentObject.get("moduleGroupId").getAsString();
                    JsonObject data = this.getDataFromId(id, type);
                    String name = getNameFromData(data);
                    ModuleEntry m = new ModuleEntry(name, data, parentModule);
                    l.add(m);
                    break;
                }
                
                case "CourseUnitRule": {
                    String id = parentObject.get("courseUnitGroupId")
                            .getAsString();
                    JsonObject data = this.getDataFromId(id, type);
                    String name = getNameFromData(data);
                    ModuleEntry m = new ModuleEntry(name, data, parentModule,
                            true);
                    l.add(m);
                    break;
                }
                
                default: {
                    break;
                }
            }
            
            return l;
            
        }
        catch (Exception e) {
            return null;
        }
        
    }
    
    /**
     * Gets a courses information and returns it as a string.
     * @param m The module to be searched.
     * @return String of module info in form: content, outcomes, grading.
     * Is in HTML suitable format.
     */
    public static String getCourseInfo(ModuleEntry m) {
        if (!m.getType().equals("Course")) {
            return null;
        }
        try {
            String outcomes = getOutcomes(m);
            String content = getContent(m);
            String grading = getGrading(m);
            return String.format("%s%s<li>%s</li>", content, outcomes, grading);
            
        } catch (Exception e) {
            return null;
        }
        
    }
   
    /**
     * Gets the information of a single Programme.
     * If the module has no outcome listed, an empty field will be shown.
     * @param m - the module to be searched.
     * @return learningOutcomes - the modules learning outcome information.
     */
    public static String getProgrammeInfo(ModuleEntry m) {
        String learningOutcomes = getLearningOutcomes(m);
        if (learningOutcomes == null) {
            learningOutcomes = "";
        }
        return learningOutcomes;
    }
    
    /**
     * Gets the information of a single StudyModule.
     * @param m - the ModuleEntry which data will be retrieved.
     * @return outcomes - String of the information.
     */
    public static String getStudyModuleInfo(ModuleEntry m) {
        String outcomes = getOutcomes(m);
        if (outcomes == null) {
            outcomes = "";
        }
        return outcomes;
        
    }
    
   
    /**
     * Finds the learning outcomes of a module.
     * @param m The module to be searched
     * @return outcomes String of module learning outcomes.
     */
    private static String getLearningOutcomes(ModuleEntry m) {
        String outcomes = "";
            try {
                outcomes = "Learning outcomes: " + m.getEntryData().get(
                        "learningOutcomes").getAsJsonObject().get("en")
                        .getAsString();
            } catch (Exception e) {
                try {
                    outcomes = "Learning outcomes: " + m.getEntryData().get(
                            "learningOutcomes").getAsJsonObject().get("fi")
                            .getAsString();
                } catch (Exception ee) {
                    outcomes = "";
                }
            }
        return outcomes;
    }
    
    /**
     * Finds the outcomes for a module.
     * @param m The module to be searched.
     * @return outcomes String of module outcomes.
     */
    public static String getOutcomes(ModuleEntry m) {
        String outcomes = "";
            try {
                outcomes = "Outcomes: " + m.getEntryData().get("outcomes")
                        .getAsJsonObject().get("en").getAsString();
            } catch (Exception e) {
                try {
                    outcomes = "Outcomes: " + m.getEntryData().get("outcomes")
                            .getAsJsonObject().get("fi").getAsString();
                } catch (Exception ee) {
                    outcomes = "";
                }
            }
        return outcomes;
    }
    
    /**
     * Finds the grading scale of a course module.
     * @param m - the module to be searched.
     * @return Grading information on the module as a String.
     */
    public static String getGrading(ModuleEntry m){
        return "Grading: " + m.getEntryData().get("gradeScaleId").getAsString()
                .substring(4);
    }
    
    /**
     * Finds the module contents and returns as string.
     * @param m the module to be searched.
     * @return Module contents as a String.
     */
    
    public static String getContent(ModuleEntry m) {
        String content = "";
            try {
                content = "Content: " + m.getEntryData().get("content")
                        .getAsJsonObject().get("en").getAsString();
            } catch (Exception e) {
                try {
                content = "Content: " + m.getEntryData().get("content")
                        .getAsJsonObject().get("en").getAsString();
                } catch (Exception ee) {
                    content = "";
                }
            }
        return content;
    }
    
    /**
     * Changes the courses parent modules credits accordingly.
     * @param m The course that has been done / undone.
     */
    public static void courseCreditsToParent(ModuleEntry m) {
        String addremove = "";
        if (m.isCompleted()) {
            m.getParent().setCredits(m.getParent().getCredits() 
                    + m.getCredits());
            addremove = "add";
        }
        else {
            m.getParent().setCredits(m.getParent().getCredits() 
                    - m.getCredits());
            addremove = "remove";
        }
        m.getParent().setCompleted(true);
        creditsToParent(m.getParent(), m.getCredits(), addremove);
        
        if (!SecondWindowController.hasModule(m.getParent().getId())) {
            JsonObject o = new JsonObject();
            o.add(m.getParent().getId(), new JsonPrimitive(m.getCredits()));

            Sisu.getStudentData().getStudentData().get(Sisu.getCurrentID())
            .getAsJsonObject().get("Completed Courses").getAsJsonArray()
                    .add(o);
        }
        else {
            int counter = 0;
            for (JsonElement each : Sisu.getStudentData().getStudentData()
                    .get(Sisu.getCurrentID()).getAsJsonObject()
                    .get("Completed Courses").getAsJsonArray()) {
                try {
                    int s = Integer.parseInt(each.getAsJsonObject()
                            .get(m.getParent().getId()).getAsString());
                    
                    if (addremove.equals("add")) {
                        s += m.getCredits();
                    }
                    else {
                        s -= m.getCredits();
                    }
                    
                    Sisu.getStudentData().getStudentData()
                            .get(Sisu.getCurrentID()).getAsJsonObject()
                            .get("Completed Courses").getAsJsonArray()
                            .remove(counter);
                    
                    
                    JsonObject o = new JsonObject();
                    o.add(m.getParent().getId(), new JsonPrimitive(s));
                    Sisu.getStudentData().getStudentData()
                            .get(Sisu.getCurrentID()).getAsJsonObject()
                            .get("Completed Courses").getAsJsonArray().add(o);
                    break;
                }
                catch (Exception e) {
                    
                }
                ++counter;
                
            }
            
            
            Sisu.getStudentData().getStudentData().get(Sisu.getCurrentID())
            .getAsJsonObject().get("Completed Courses").getAsJsonArray();
        }
        
    }
    
    /**
     * Sends the credits of a module to its parent, in order to change its 
     * completed credits accordingly. Works recursively until the root module.
     * @param m The child module.
     * @param op Credits.
     * @param addremove Tells wether the credits need to be added or removed.
     */
    public static void creditsToParent(ModuleEntry m, int op, String addremove){
        if (m.getParent() != null) {
            if (addremove.equals("add")) {
                m.getParent().setCredits(m.getParent().getCredits() + op);
            }
            else {
                m.getParent().setCredits(m.getParent().getCredits() - op);
            }
            creditsToParent(m.getParent(), op, addremove);
            m.getParent().setCompleted(true);
            
            if (!SecondWindowController.hasModule(m.getParent().getId())) {
                JsonObject o = new JsonObject();
                o.add(m.getId(), new JsonPrimitive(m.getCredits()));

                Sisu.getStudentData().getStudentData().get(Sisu.getCurrentID())
                .getAsJsonObject().get("Completed Courses").getAsJsonArray()
                        .add(o);
            }
            else {
                int counter = 0;
                for (JsonElement each : Sisu.getStudentData().getStudentData()
                        .get(Sisu.getCurrentID()).getAsJsonObject()
                        .get("Completed Courses").getAsJsonArray()) {
                    try {
                        int s = Integer.parseInt(each.getAsJsonObject().get(
                                m.getParent().getId()).getAsString());

                        if (addremove.equals("add")) {
                            s += m.getCredits();
                        }
                        else {
                            s -= m.getCredits();
                        }

                        Sisu.getStudentData().getStudentData().get(
                                Sisu.getCurrentID()).getAsJsonObject()
                                .get("Completed Courses").getAsJsonArray()
                                .remove(counter);


                        JsonObject o = new JsonObject();
                        o.add(m.getParent().getId(), new JsonPrimitive(s));
                        Sisu.getStudentData().getStudentData()
                                .get(Sisu.getCurrentID()).getAsJsonObject()
                                .get("Completed Courses").getAsJsonArray()
                                .add(o);
                        break;
                    }
                    catch (Exception e) {

                    }
                    ++counter;

                }
            }
        }
        
        
    }
}
