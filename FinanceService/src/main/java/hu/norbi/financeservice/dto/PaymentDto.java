package hu.norbi.financeservice.dto;

import lombok.Data;


public class PaymentDto {

    private int studentId;
    private int amount;

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
