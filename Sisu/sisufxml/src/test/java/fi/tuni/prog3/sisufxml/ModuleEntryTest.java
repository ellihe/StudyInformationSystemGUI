/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package fi.tuni.prog3.sisufxml;

import com.google.gson.JsonObject;
import java.util.ArrayList;
import javafx.scene.control.TreeItem;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author nikox
 */
public class ModuleEntryTest {
    
    /**
     * Test of getEntryData method, of class ModuleEntry.
     */
    @Test
    public void testGetEntryData() {
        System.out.println("getEntryData");
        ModuleEntry instance = new ModuleEntry();
        JsonObject expResult = new JsonObject();
        JsonObject result = instance.getEntryData();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getCredits method, of class ModuleEntry.
     */
    @Test
    public void testGetCredits() {
        System.out.println("getCredits");
        ModuleEntry instance = new ModuleEntry();
        instance.setCredits(10);
        int expResult = 10;
        int result = instance.getCredits();
        assertEquals(expResult, result);
    }


    /**
     * Test of getTreeItem method, of class ModuleEntry.
     */
    @Test
    public void testGetTreeItem() {
        System.out.println("getTreeItem");
        ModuleEntry instance = new ModuleEntry();
        TreeItem expResult = null;
        TreeItem result = instance.getTreeItem();
        assertEquals(expResult, result);
    }

    /**
     * Test of getParent method, of class ModuleEntry.
     */
    @Test
    public void testGetParent() {
        System.out.println("getParent");
        ModuleEntry instance = new ModuleEntry();
        ModuleEntry instanceParent = new ModuleEntry();
        instance.setParent(instanceParent);
        ModuleEntry result = instance.getParent();
        assertEquals(instanceParent, result);
    }


    /**
     * Test of getId method, of class ModuleEntry.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        ModuleEntry instance = new ModuleEntry();
        instance.setId("H3LL0");
        String expResult = "H3LL0";
        String result = instance.getId();
        assertEquals(expResult, result);
    }
    
}
