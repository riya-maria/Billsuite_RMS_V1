package com.technologies.nuvac.billsuiterms;

/**
 * Created by company ismi on 4/27/2018.
 */

public class vatItems {
    String ID;
    String Branch;
    String FromDate;
    String ToDate;
    String RegNo;
    String Status;
    public String getBranch() {
        return Branch;
    }
    public void setBranch(String branch) {
        Branch = branch;
    }
    public String getFromDate() {
        return FromDate;
    }
    public void setFromDate(String fromDate) {
        FromDate = fromDate;
    }
    public String getToDate() {
        return ToDate;
    }
    public void setToDate(String toDate) {
        ToDate = toDate;
    }
    public String getRegNo() {
        return RegNo;
    }
    public void setRegNo(String regNo) {
        RegNo = regNo;
    }
    public String getID() {
        return ID;
    }
    public void setID(String ID) {
        this.ID = ID;
    }
    public String getStatus() {
        return Status;
    }
    public void setStatus(String status) {
        Status = status;
    }
}
