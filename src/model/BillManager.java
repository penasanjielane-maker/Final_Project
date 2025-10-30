/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.IdGenerator;

/**
 *
 * @author Admin
 */
public class BillManager {
     private HashMap<String, Bill> bills;
    private final String FILE_PATH = "billslist.ser";

    public BillManager() {
        bills = new HashMap();
    }
     
    public void addBill(Bill bill) {
        String s = bill.getDueDate().substring(0, 4);
        String id = IdGenerator.generateId(Integer.parseInt(s));
        if (bills != null) {
            while (bills.containsKey(id)) {
                id = IdGenerator.generateId(Integer.parseInt(s));
            }
        }
        bill.setStudentId(id);
        bills.put(id, bill);
    }
     public Bill deleteBill(String id) {
        return bills.remove(id);
    }
    
     public Bill findBill(String id) {
        return bills.get(id);
    }
    
      public HashMap<String, Bill> findAll() {
        return bills;
    }
     
      public void setBill(Bill bill){
        bills.replace(bill.getStudentId(), bill);
    }
     
      public void loadBills() {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(FILE_PATH);
            try (ObjectInputStream ois = new ObjectInputStream(fis)) {
                Object readObj = ois.readObject();
                this.bills = (HashMap<String, Bill>) readObj;
            }
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            Logger.getLogger(BillManager.class.getName()).log(Level.SEVERE, null, e);
        }
    }
     
     public int saveBills() {
        FileOutputStream fos = null;
        int errorCode = 0; 
        try {
            fos = new FileOutputStream(this.FILE_PATH);
            try ( ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(bills);
            }
            fos.close();
        } catch (IOException e) {
            errorCode = 1;
            Logger.getLogger(BillManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return errorCode;
    }
     
     
     
     
     
}
