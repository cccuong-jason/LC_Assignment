package tdtu.mobile_dev_assignment.lc_assignment.Models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Hotel {
    private String activity;

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDirections() {
        return directions;
    }

    public Hotel(String activity, String address, String content, String directions, String email, Map<String, Double> geo, long id, String name, String phone, long price, String title, String type, String url, double rate, List<String> facilities, String country, String city, List<String> images, List<Room> room) {
        this.activity = activity;
        this.address = address;
        this.content = content;
        this.directions = directions;
        this.email = email;
        this.geo = geo;
        this.room = room;
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.price = price;
        this.title = title;
        this.type = type;
        this.url = url;
        this.rate = rate;
        this.facilities = facilities;
        this.country = country;
        this.city = city;
        this.images = images;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, Double> getGeo() {
        return geo;
    }

    public void setGeo(Map<String, Double> geo) {
        this.geo = geo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String address;
    private String content;
    private String directions;

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public List<String> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<String> facilities) {
        this.facilities = facilities;
    }

    private Hotel() {
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    private String email;
    private Map<String, Double> geo;
    private long id;
    private String name;
    private String phone;

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    private long price;
    private String title;
    private String type;
    private String url;

    private double rate;

    private List<String> facilities;

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    private String country;

    public List<Room> getRoom() {
        return room;
    }

    public void setRooms(List<Room> room) {
        this.room = room;
    }

    private String city;
    private String categories;
    private List<String> images;
    private List<Room> room;
}
