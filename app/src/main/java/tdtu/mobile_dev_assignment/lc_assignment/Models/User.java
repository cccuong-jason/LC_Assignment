package tdtu.mobile_dev_assignment.lc_assignment.Models;

import android.provider.ContactsContract;
import android.util.Log;

import com.google.firebase.database.Exclude;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {

    private String name;
    private String phone;
    private String avatar;
    private String email;
    private String user_id;

    public String getBirthdate() {
        return birthdate;
    }

    public User() {}

    public User(String name, String email, String user_id) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            this.createdAt = dtf.format(now).toString();
            this.updatedAt = dtf.format(now).toString();
        }
        this.name = name;
        this.email = email;
        this.user_id = user_id;
        this.avatar = "https://firebasestorage.googleapis.com/v0/b/lc-musicsheet.appspot.com/o/Multiavatar-2955f534745d5b067b.png?alt=media&token=5f863e36-4bda-44a1-a226-20f7854771eb";
        this.phone = "";
        this.birthdate = "";
        this.address = "";
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    private String birthdate;
    private String address;
    private String createdAt;
    private String updatedAt;
    private Map<String, Object> bookedHotel = new HashMap<String, Object>();
    private Map<String, Object> bookmarkedList = new HashMap<String, Object>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Map<String, Object> getbookedHotel() {
        return bookedHotel;
    }

    public void setbookedHotel(Map<String, Object> bookedHotel) {
        this.bookedHotel = bookedHotel;
    }

    public Map<String, Object> getBookmarkedList() {
        return bookmarkedList;
    }

    public void setBookmarkedList(Map<String, Object> bookmarkedList) {
        this.bookmarkedList = bookmarkedList;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

//    public Map<String, Object> toMap() {
//        HashMap<String, Object> result = new HashMap<>();
//        Log.i("bookedHotel", getbookedHotel().toString());
//        for (String bookedItem : this.bookedHotel) {
//            result.put(bookedItem, true);
//        }
//
//        return result;
//    }
//
//    public void addbookedHotel(String bookedId) {
//        if (!this.bookedHotel.contains(bookedId)) {
//            this.bookedHotel.add(bookedId);
//        }
//    }
}
