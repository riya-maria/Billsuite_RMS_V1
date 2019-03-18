package com.technologies.nuvac.billsuiterms;

/**
 * Created by nuvac on 28/02/2018.
 */

public class salesItems {

    String InvoiceNumber;
    String TotalVAT;
    String Amount;
    String Status;
    String TableID;
    String ClientID;
    String StaffID;
    public String getInvoiceNumber() {
        return InvoiceNumber;
    }
    public void setInvoiceNumber(String invoiceNumber) {
        InvoiceNumber = invoiceNumber;
    }
    public String getTotalVAT() {
        return TotalVAT;
    }
    public void setTotalVAT(String totalVAT) {
        TotalVAT = totalVAT;
    }
    public String getAmount() {
        return Amount;
    }
    public void setAmount(String amount) {
        Amount = amount;
    }
    public String getStatus() {
        return Status;
    }
    public void setStatus(String status) {
        Status = status;
    }
    public String getTableID() {
        return TableID;
    }
    public void setTableID(String tableID) {
        TableID = tableID;
    }
    public String getClientID() {
        return ClientID;
    }
    public void setClientID(String clientID) {
        ClientID = clientID;
    }
    public String getStaffID() {
        return StaffID;
    }
    public void setStaffID(String staffID) {
        StaffID = staffID;
    }

}
