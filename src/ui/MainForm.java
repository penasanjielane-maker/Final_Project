package ui;

import controller.BillController;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import model.Bill;

/**
 *
 * @author jheso
 */
public class MainForm extends javax.swing.JFrame {

    /**
     * Creates new form MainForm
     */
    public MainForm(BillController controller) {
        this.controller = controller;
        this.tableModel = new BillTableModel();

        initComponents();
        setUpDefaultDisplay();

        setupListTable();
        setUpRowSelectionListener();

        refreshBillList();

    }

    private void setUpDefaultDisplay(){
        this.jLabelstatus.setVisible(false);
        this.jLabelpaid.setVisible(false);
        this.jTextAreastatus.setVisible(false);
        this.jTextAreapaid.setVisible(false);
        
        this.jTextAreapaid.setEditable(true);

        this.jTextAreastatus.setText("");
        this.jTextAreapaid.setText("");
        this.jTextAreaname.setText("");
        this.jTextAreaduedate.setText(""); 
        this.buttondelete.setEnabled(false);
        this.saveMode = "ADD";
    }

    private void setupListTable() {
        jTable1.setModel(tableModel);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.setAutoCreateColumnsFromModel(true);
        jTable1.createDefaultColumnsFromModel();
        setupTableColumnWidths();
    }

    public void refreshBillList(){
        String savedSelectedId = this.selectedTaskId; 
        System.out.println("Selected Student id:" + savedSelectedId);

        List<Bill> bills = controller.getAllBill();
        for (Bill bill : bills) {
            System.out.println(bill);
        }
        this.tableModel.setBill(bills);

        if (savedSelectedId != null) {
            int newModelRow = -1;
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (savedSelectedId.equals(tableModel.getBillId(i))) {
                    newModelRow = i;
                    break;
                }
            }
            if (newModelRow != -1) {
                int newTableRow = jTable1.convertRowIndexToView(newModelRow);
                jTable1.setRowSelectionInterval(newTableRow, newTableRow);
            } else {
                this.selectedTaskId = null;
                setUpDefaultDisplay();
            }
        }
    }

    private void setupTableColumnWidths(){
        javax.swing.table.TableColumnModel columnModel = jTable1.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(500);
        columnModel.getColumn(0).setWidth(100);
        columnModel.getColumn(0).setMinWidth(70);
        columnModel.getColumn(1).setPreferredWidth(500);
        columnModel.getColumn(1).setWidth(200);

        columnModel.getColumn(2).setPreferredWidth(500);
        columnModel.getColumn(2).setWidth(150);
        columnModel.getColumn(2).setMinWidth(70);

        columnModel.getColumn(3).setPreferredWidth(500);
        columnModel.getColumn(3).setWidth(100);
        columnModel.getColumn(3).setMinWidth(70);

        columnModel.getColumn(4).setPreferredWidth(50);
        columnModel.getColumn(4).setMaxWidth(70);
        columnModel.getColumn(4).setMinWidth(50);

        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
    }

    private void setUpRowSelectionListener(){
        jTable1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = jTable1.getSelectedRow();
                if (selectedRow != -1) {
                    int modelRow = jTable1.convertRowIndexToModel(selectedRow);
                    String studentId = this.tableModel.getBillId(modelRow);
                    Bill selectedBill = controller.getBillDetails(studentId);
                    displaySelectedBill(selectedBill);
                } else {
                    setUpDefaultDisplay();
                }
            }
        });
    }

    private void displaySelectedBill(Bill bill){
        this.jTextAreaname.setText(bill.getStudentName());
        this.jTextAreaduedate.setText(bill.getDueDate());
        this.selectedTaskId = bill.getStudentId();
        this.jTextAreastatus.setText(bill.getStatus());
        this.jTextAreapaid.setText(bill.getDateFullyPaid());

        this.jLabelstatus.setVisible(true);
        this.jLabelpaid.setVisible(true);
        this.jLabelduedate.setVisible(true);
        this.jTextAreaduedate.setVisible(true);
        this.jTextAreapaid.setVisible(true);
        this.jTextAreastatus.setVisible(true);

        this.buttondelete.setEnabled(true);
        this.saveMode = "UPDATE";
        System.out.println("SaveMode:" + saveMode);
    }

    private void addBill(){
        String desc = this.jTextAreaname.getText();
        String deadline = this.jTextAreaduedate.getText();
        String status = this.jTextAreastatus.getText();
        boolean success = false;
        
        if (this.saveMode.equalsIgnoreCase("ADD")) {
            if (desc.trim().isEmpty() || deadline.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "All Fields are required", "Add Bill", JOptionPane.WARNING_MESSAGE);
                return;
            }
            controller.handleAddCostumer(desc, deadline);
            success = true;
            JOptionPane.showMessageDialog(this, "Bill Successfully added", "Add Bill", JOptionPane.INFORMATION_MESSAGE);
        } else if(this.saveMode.equalsIgnoreCase("UPDATE")){
            if (desc.trim().isEmpty() || deadline.trim().isEmpty() || status.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "All Fields are required for update", "Update Bill", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            boolean res = controller.handleUpdateBill(this.selectedTaskId, desc, deadline, status);
            if (res) {
                JOptionPane.showMessageDialog(this, "Bill Successfully Updated", "Update Bill", JOptionPane.INFORMATION_MESSAGE);
                success = true;
            } else {
                JOptionPane.showMessageDialog(this, "An error occured while updating bill", "Update Bill", JOptionPane.ERROR_MESSAGE);
            }
        }

        if (success) {
            this.jTable1.clearSelection();
            setUpDefaultDisplay();
            this.selectedTaskId = null;
            refreshBillList(); 
        }
    }

    private void deleteBill(){
        if (this.selectedTaskId != null) {
            int confirmDeletion = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this Bill? This cannot be undone.",
                    "Confirm Bill Deletion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (confirmDeletion == JOptionPane.YES_OPTION) {
                boolean success = controller.handleDeleteBill(this.selectedTaskId);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Bill Successfully Deleted", "Bill Deletion", JOptionPane.INFORMATION_MESSAGE);
                    this.jTable1.clearSelection();
                    setUpDefaultDisplay(); 
                    refreshBillList(); 
                } else { 
                    JOptionPane.showMessageDialog(this, "An error occured while deleting the bill", "Bill Deletion", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabelname = new javax.swing.JLabel();
        jLabelpaid = new javax.swing.JLabel();
        jLabelstatus = new javax.swing.JLabel();
        jLabelduedate = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaname = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextAreapaid = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextAreaduedate = new javax.swing.JTextArea();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextAreastatus = new javax.swing.JTextArea();
        buttonsave = new java.awt.Button();
        buttondelete = new java.awt.Button();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 102, 0));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Student  Bill");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 153, Short.MAX_VALUE)
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Student ID", "Name", "Date of Pay", "Due Date", "Status"
            }
        ));
        jTable1.setPreferredSize(new java.awt.Dimension(600, 200));
        jTable1.setRowSelectionAllowed(false);
        jScrollPane1.setViewportView(jTable1);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Student Info"));

        jLabelname.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelname.setText("Name");

        jLabelpaid.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelpaid.setText("Date of fully paid");

        jLabelstatus.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelstatus.setText("Status");

        jLabelduedate.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelduedate.setText("Due Date");

        jTextAreaname.setColumns(20);
        jTextAreaname.setRows(5);
        jTextAreaname.setText("elgen");
        jScrollPane2.setViewportView(jTextAreaname);

        jTextAreapaid.setColumns(20);
        jTextAreapaid.setRows(5);
        jScrollPane3.setViewportView(jTextAreapaid);

        jTextAreaduedate.setColumns(20);
        jTextAreaduedate.setRows(5);
        jTextAreaduedate.setText("20240905");
        jScrollPane4.setViewportView(jTextAreaduedate);

        jTextAreastatus.setColumns(20);
        jTextAreastatus.setRows(5);
        jScrollPane5.setViewportView(jTextAreastatus);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabelname)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 391, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabelduedate)
                        .addComponent(jLabelpaid))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(58, 58, 58)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelstatus)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabelname)))
                .addGap(56, 56, 56)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelpaid)
                    .addComponent(jLabelstatus))
                .addGap(3, 3, 3)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelduedate)
                .addGap(22, 22, 22)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        buttonsave.setLabel("SAVE");
        buttonsave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonsaveActionPerformed(evt);
            }
        });

        buttondelete.setLabel("DELETE");
        buttondelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttondeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(268, 268, 268)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 18, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 654, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(60, 60, 60)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(buttonsave, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttondelete, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(23, 23, 23))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonsave, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)
                        .addComponent(buttondelete, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)))
                .addContainerGap(53, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonsaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonsaveActionPerformed
            addBill();        // TODO add your handling code here:
    }//GEN-LAST:event_buttonsaveActionPerformed

    private void buttondeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttondeleteActionPerformed
            deleteBill();        // TODO add your handling code here:
    }//GEN-LAST:event_buttondeleteActionPerformed

    private BillController controller;
    private BillTableModel tableModel;
    private String selectedTaskId;
    private String saveMode;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Button buttondelete;
    private java.awt.Button buttonsave;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelduedate;
    private javax.swing.JLabel jLabelname;
    private javax.swing.JLabel jLabelpaid;
    private javax.swing.JLabel jLabelstatus;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextAreaduedate;
    private javax.swing.JTextArea jTextAreaname;
    private javax.swing.JTextArea jTextAreapaid;
    private javax.swing.JTextArea jTextAreastatus;
    // End of variables declaration//GEN-END:variables
}
