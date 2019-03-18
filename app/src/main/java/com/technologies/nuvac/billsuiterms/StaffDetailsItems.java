package com.technologies.nuvac.billsuiterms;

import android.graphics.Bitmap;

/**
 * Created by isthiishq on 14-02-2018.
 */

class StaffDetailsItems {
    String ID;
    String Name;
    String Gender;
    String Address;
    String PhoneNumber;
    String Designation;
    Bitmap Image;
    String FirmID;

    public String getID(){
        return ID;
    }
    public void setID(String id){
        this.ID=id;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }
    public String getGender() {
        return Gender;
    }
    public void setGender(String gender) {
        Gender = gender;
    }
    public String getAddress() {
        return Address;
    }
    public void setAddress(String address) {
        Address = address;
    }
    public String getPhoneNumber() {
        return PhoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }
    public String getDesignation() {
        return Designation;
    }
    public void setDesignation(String designation) {
        Designation = designation;
    }
    public Bitmap getImage() {
        return Image;
    }
    public void setImage(Bitmap image) {
        Image = image;
    }
}
