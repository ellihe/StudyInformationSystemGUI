/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fi.tuni.prog3.sisufxml;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
/**
 *
 * @author arttu
 */
public class DataStructureTest {
    
    private static DataStructure datastruct;
    
    @BeforeAll
    public static void createDataStruct() {
        datastruct = new DataStructure();
        datastruct.populateDataStruct();
        
        for (ModuleEntry each : datastruct.getModuleList()) {
            if (each.getName() == null) {
                assertTrue(false);
            }
        }
    }
    
    
    @Test
    public void testGetModuleList() {
        ArrayList<ModuleEntry> a = datastruct.getModuleList();
        if (!a.isEmpty()) {
            for (ModuleEntry each : a) {
                if (each.getName() == null) {
                    assertTrue(false);
                }
            }
            assertTrue(true);
        }
        else {
            assertTrue(false);
        }
    }
    

    @Test
    public void testGetChildrenModules() {
        ModuleEntry m = datastruct.getModuleList().get(0);
        
        String s1 = m.getEntryData().get("rule").getAsJsonObject().get("rules")
                .getAsJsonArray().get(0).getAsJsonObject().get("moduleGroupId")
                .getAsString();
        
        m.getChildren().addAll(datastruct.getChildrenModules(m
                                    .getEntryData(), m));
        m.setChildrenAdded(true);
        String s2 = m.getChildren().get(0).getId();
        
        
        assertEquals(s1,s2);
    }
    
}
