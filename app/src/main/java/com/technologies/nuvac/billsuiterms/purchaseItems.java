package com.technologies.nuvac.billsuiterms;

/**
 * Created by nuvac on 20/02/2018.
 */

public class purchaseItems {
    String InvoiceNum;
    String Amount;
    String PaymentMode;
    String Remark;
    String DocumentName;
    String BrandID;
    public String getInvoiceNum() {
        return InvoiceNum;
    }
    public void setInvoiceNum(String invoiceNum) {
        InvoiceNum = invoiceNum;
    }
    public String getAmount() {
        return Amount;
    }
    public void setAmount(String amount) {
        Amount = amount;
    }
    public String getPaymentMode() {
        return PaymentMode;
    }
    public void setPaymentMode(String paymentMode) {
        PaymentMode = paymentMode;
    }
    public String getRemark() {
        return Remark;
    }
    public void setRemark(String remark) {
        Remark = remark;
    }
    public String getDocumentName() {
        return DocumentName;
    }
    public void setDocumentName(String documentName) {
        DocumentName = documentName;
    }
    public String getBrandID() {
        return BrandID;
    }
    public void setBrandID(String brandID) {
        BrandID = brandID;
    }

}
