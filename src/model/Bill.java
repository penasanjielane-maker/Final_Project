package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Bill implements Serializable {

    private String studentId;
    private String studentName;
    private String dateFullyPaid; 
    private String dueDate;
    private String status; // Present, Absent, Excuse
    private static final long serialVersionUID = 1L;
    
    public Bill() {
        this.status = "Paid"; 
        setDateFullyPaid();
        
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getDateFullyPaid() {
        return dateFullyPaid;
    }

    public void setDateFullyPaid() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");    
    LocalDate now = LocalDate.now();
    this.dateFullyPaid = now.format(formatter);
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Bill{" + "studentId=" + studentId + ", studentName=" + studentName + ", dateFullyPaid=" + dateFullyPaid + ", dueDate=" + dueDate + ", status=" + status + '}';
    }

   
}