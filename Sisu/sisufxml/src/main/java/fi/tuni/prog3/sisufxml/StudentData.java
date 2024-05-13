/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package fi.tuni.prog3.sisufxml;

import com.google.gson.JsonObject;

/**
 * Includes students' study information as JSONObject
 * @author Ellinoora
 */
public class StudentData {

    private JsonObject studentData;

    /**
     * Constructor to store studentData
     * @param studentData JsonObject that class will store
     */
    public StudentData(JsonObject studentData) {
        this.studentData = studentData;
    }

    /**
     * Returns studentData
     * @return studentData the data structure of all students' information

     */
    public JsonObject getStudentData() {
        return studentData;
    }

    /**
     * Sets new value to studentData
     * @param studentData 
     * Sets student data
     */
    public void setStudentData(JsonObject studentData) {
        this.studentData = studentData;
    }
    
}
