/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import model.Bill;
import model.BillManager;
import ui.MainForm;


public class BillController {
    private final BillManager model;
    private MainForm view;
    
    
    public BillController(BillManager model) {
        this.model = model;
    }
    
    public void setView(MainForm view){
    this.view = view;
    }
    
    public void handleAddCostumer(String name, String duedate) {
        if (name.trim().isEmpty() || duedate.trim().isEmpty()) {
            return;
        }
        Bill bill = new Bill();
        bill.setStudentName(name);
        bill.setDueDate(duedate);
        this.model.addBill(bill);
        this.model.saveBills();
        if (this.view != null) {
            this.view.refreshBillList();
        }
    }
    
    public boolean handleDeleteBill(String id) {
        Bill bill = this.model.deleteBill(id);
        if (bill != null) {
            this.model.saveBills();
            System.out.println("Deleted Bill: " + bill.getStudentName());
            if (this.view != null) {
                this.view.refreshBillList();
            }
            return true;
        } else {
            System.out.println("Bill not found");
            return false;
        }
    }

    public boolean handleUpdateBill(String id, String name, String duedate, String status) {
        if (name.trim().isEmpty() || duedate.trim().isEmpty()
                || status.trim().isEmpty()) {
            return false;
        }
        Bill bill = this.model.findBill(id);
        if (bill == null) {
            return false;
        }
        bill.setStudentName(name);
        bill.setDueDate(duedate);
        bill.setStatus(status);
        this.model.saveBills();
        if (this.view != null) {
            this.view.refreshBillList();
        }
        return true;
    }
    
     public List<Bill> getAllBill() {
        Collection<Bill> billsFromMap = this.model.findAll().values();
        ArrayList<Bill> billList = new ArrayList<>(billsFromMap);
        return billList;
    }

    public Bill getBillDetails(String id) {
        return this.model.findBill(id);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

}
