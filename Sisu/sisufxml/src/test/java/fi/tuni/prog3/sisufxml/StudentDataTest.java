/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package fi.tuni.prog3.sisufxml;

/**
 *
 * @author User
 */
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StudentDataTest {
    
    // Elukan osio
    @Test
    public void testGetStudentData() {
        JsonObject json = new JsonObject();
        StudentData studentData = new StudentData(json);
        JsonObject expResult = json;
        JsonObject result = studentData.getStudentData();
        
        assertEquals(expResult, result);
    }
    
    @Test
    public void testSetStudentData() {
        JsonObject json = new JsonObject();
        StudentData studentData = new StudentData(json);
        
        JsonObject json2 = new JsonObject();
        json2.add("testObject", new JsonObject());
        
        studentData.setStudentData(json2);
        
        JsonObject expResult = json2;
        JsonObject result = studentData.getStudentData();
        
        assertEquals(expResult, result);
    }
    
    public void testStudentDataConstructor() {
        JsonObject json = new JsonObject();
        StudentData studentData = new StudentData(json);
        JsonObject expResult = json;
        JsonObject result = studentData.getStudentData();
        
        assertEquals(expResult, result);
    }
    
    
}