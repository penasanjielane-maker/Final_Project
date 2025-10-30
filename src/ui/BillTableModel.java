/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.Bill;

/**
 *
 * @author Admin
 */
public class BillTableModel extends AbstractTableModel {
    private List<Bill> bills;
    private final String[] COLUMN_NAMES = {"Student ID","Name",
        "Date of Fully Paid", "Due date", "Status"};

    public BillTableModel(){
        this.bills = new ArrayList<>();
    
    }
    
    @Override
    public int getRowCount() {
         return this.bills.size();
        
         }

    @Override
    public int getColumnCount() {
         return this.COLUMN_NAMES.length;
        
        }

    @Override
    public String getColumnName(int index) {
      return COLUMN_NAMES[index]; 
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
         Bill bill = bills.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> bill.getStudentId();
            case 1 -> bill.getStudentName();
            case 2 -> bill.getDateFullyPaid();
            case 3 -> bill.getDueDate();
            case 4 -> bill.getStatus();
            default -> null;
        };
        
        }
     public void setBill(List<Bill> bills){
        this.bills = bills;
        fireTableDataChanged();
    }
    
    public String getBillId(int rowIndex){
        return bills.get(rowIndex).getStudentId();
    }
}
