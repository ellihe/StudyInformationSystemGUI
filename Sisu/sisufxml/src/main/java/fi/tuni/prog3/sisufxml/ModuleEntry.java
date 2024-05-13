/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt 
to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit 
this template
 */
package fi.tuni.prog3.sisufxml;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import javafx.scene.control.TreeItem;

/**
 * A class for representing a module in the study structure.
 * Several types of modules are supported.
 * Contains all relevant information about an individual module.
 * If module is not a course, can contain child modules.
 * @author A
 */
public class ModuleEntry {
    private String name;
    private JsonObject entryData;
    private ArrayList<ModuleEntry> children;
    private int credits;
    private boolean childrenAdded;
    private TreeItem treeItem;
    private ModuleEntry parent;
    private String id;
    private boolean completed;
    private String type;
    
    /**
     * Constructs an empty module
     */
    public ModuleEntry() {
        name = "";
        entryData = new JsonObject();
        children = new ArrayList<>();
        credits = 0;
        treeItem = null;
        id = "";
    }
    
    /**
     * Constructs a module with the name and data of the module
     * @param namee Name for the ModuleEntry
     * @param entrydata JsonObject which contains the data of this ModuleEntry
     * @param parentt If the constructed ModuleEntry has a parent, includes 
     * the parent ModuleEntry. If not, is null.
     */
    public ModuleEntry(String namee, JsonObject entrydata, ModuleEntry parentt){
        name = namee;
        entryData = entrydata;
        children = new ArrayList<>();
        type = entrydata.get("type").getAsString();
        treeItem = null;
        id = entrydata.get("id").getAsString();
        parent = parentt;
        
        credits = 0;
        
        if (type.equals("DegreeProgramme")) {
            return;
        }
        
        JsonArray a = new JsonArray();
        
        if (Sisu.getStudentData() != null) {
            a = Sisu.getStudentData().getStudentData()
                .get(Sisu.getCurrentID()).getAsJsonObject()
                .get("Completed Courses").getAsJsonArray();
        }
        
        for (JsonElement each : a) {
            try {
                String s = each.getAsJsonObject().get(id).getAsString();
                completed = true;
                credits = Integer.parseInt(s);
                return;
            }
            catch (Exception e){
            }
        }
        completed = false;
    }
    
    /**
     * Constructs a module with the name, data and information on whether the
     * module is a course or 
     * @param namee Name for the ModuleEntry
     * @param entrydata JsonObject which contains the data of this ModuleEntry
     * @param parentt If the constructed ModuleEntry has a parent, includes 
     * the parent ModuleEntry. If not, is null.
     * @param iscourse Boolean value that tell wether the module is a course or
     * not.
     */
    public ModuleEntry(String namee, JsonObject entrydata, ModuleEntry parentt,
            boolean iscourse) {
        name = namee;
        entryData = entrydata;
        children = new ArrayList<>();
        parent = parentt;
        
        if (iscourse) {
            type = "Course";
        }
        else {
            type = entrydata.get("type").getAsString();
        }
        treeItem = null;
        id = entrydata.get("id").getAsString();
        
        switch (type) {
            case "Course": {
                credits = Integer.parseInt(entryData.get("credits")
                        .getAsJsonObject().get("min").getAsString());
                break;
            }
            case "StudyModule": {
                credits = 0;
                break;
            }
            default: {
                credits = 0;
                break;
            }
        }
        JsonArray a = Sisu.getStudentData().getStudentData().
                get(Sisu.getCurrentID()).getAsJsonObject()
                .get("Completed Courses").getAsJsonArray();
        for (JsonElement each : a) {
            try {
                String s = each.getAsJsonObject().get(id).getAsString();
                completed = true;
                return;
            }
            catch (Exception e){
            }
        }
        completed = false;
    }
    
    /**
     * Returns the String representation of the module. Depending on the type of
     * module, can include credits and/or minimum credits. May vary depending
     * on the state of the module.
     * @return name String representation of the module in its current state.
     */
    @Override
    public String toString(){
        
        switch (type) {
            case "Course": {
                if (completed) {
                    return name + " " + this.credits + "op";
                }
                else {
                    return name + " " + this.credits + "op";
                }
            }
            
            case "StudyModule": {
                return String.format("%s %d/%s op", name, credits, 
                        entryData.get("targetCredits").getAsJsonObject()
                                .get("min").getAsString());
            }
            
            default: {
                return name;
            }
        }
    }
    
    /**
     * Returns module name
     * @return module name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets the name of the module
     * @param name new module name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the module entry data
     * @return Entry data as JSONObject
     */
    public JsonObject getEntryData() {
        return entryData;
    }

    /**
     * Sets the entryData for the module
     * @param entryData the JSONObject to be set as entry data
     */
    public void setEntryData(JsonObject entryData) {
        this.entryData = entryData;
    }

    /**
     * Returns a list of the child modules of this module
     * @return ArrayList of ModuleEntry objects
     */
    public ArrayList<ModuleEntry> getChildren() {
        return children;
    }

    /**
     * Sets a new list as the children of this module
     * @param children ArrayList of the child modules to be set
     */
    public void setChildren(ArrayList<ModuleEntry> children) {
        this.children = children;
    }

    /**
     * Returns the credits of this study module
     * @return int value of credits
     */
    public int getCredits() {
        return credits;
    }

    /**
     * Sets the credits to be earned from this module
     * @param credits  int value of credits to be set
     */
    public void setCredits(int credits) {
        this.credits = credits;
    }

    /**
     * Checks if child modules were added
     * @return Boolean value, true if children are added.
     */
    public boolean isChildrenAdded() {
        return childrenAdded;
    }

    /**
     * Sets the children added status as true or false
     * @param childrenAdded Boolean value to be set.
     */
    public void setChildrenAdded(boolean childrenAdded) {
        this.childrenAdded = childrenAdded;
    }

    /**
     * Returns TreeItem of this module.
     * @return TreeItem of this module.
     */
    public TreeItem getTreeItem() {
        return treeItem;
    }

    /**
     * Sets a new TreeItem as this modules TreeItem.
     * @param treeItem the item to be set as the TreeItem.
     */
    public void setTreeItem(TreeItem treeItem) {
        this.treeItem = treeItem;
    }

    /**
     * Returns the parent module of this module.
     * @return ModuleEntry, the parent module. Null, if doesn't have a parent.
     */
    public ModuleEntry getParent() {
        return parent;
    }

    /**
     * Sets a new parent module.
     * @param parent the new parent to be set.
     */
    public void setParent(ModuleEntry parent) {
        this.parent = parent;
    }

    /**
     * Returns the ID of this module.
     * @return String of this module ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets a new ID to this module.
     * @param id the new ID to be set.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns true or false depending on module completion
     * @return Boolean value, true if course is completed or some child
     * modules have been completed.
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Sets the module as completed.
     * @param isCompleted flag of course completion.
     */
    public void setCompleted(boolean isCompleted) {
        this.completed = isCompleted;
    }

    /**
     * Returns the type of module.
     * @return String of module type.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets a new type to study module.
     * @param type the type to be set.
     */
    public void setType(String type) {
        this.type = type;
    }
}
