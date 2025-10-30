package ui;

import controller.BillController;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import model.BillManager;
import java.util.logging.Logger;
import java.util.logging.Level;


public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Error setting System Look and Feel", ex);
        }

        
        BillManager billManager = new BillManager();
        billManager.loadBills();

        BillController controller = new BillController(billManager);
        
        java.awt.EventQueue.invokeLater(() -> {
            MainForm view = new MainForm(controller); 
            
            controller.setView(view);                                  
            view.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    billManager.saveBills();
                    System.exit(0);
                }
            });
            view.setVisible(true);
        });
        
        
        
        
    }
      
}
