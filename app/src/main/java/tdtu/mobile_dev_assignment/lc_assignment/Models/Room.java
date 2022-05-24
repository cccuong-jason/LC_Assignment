package tdtu.mobile_dev_assignment.lc_assignment.Models;

import java.util.List;

public class Room {
    private long roomId;

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getBenefits() {
        return benefits;
    }

    public void setBenefits(List<String> benefits) {
        this.benefits = benefits;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public long getAdult() {
        return adult;
    }

    public void setAdult(long adult) {
        this.adult = adult;
    }

    public long getChildren() {
        return children;
    }

    public void setChildren(long children) {
        this.children = children;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public Room() {}

    public Room(long roomId, String name, String description, List<String> benefits, List<String> images, long adult, long children, long price, String checkIn, String checkOut, long hotelId) {
        this.roomId = roomId;
        this.name = name;
        this.description = description;
        this.benefits = benefits;
        this.images = images;
        this.adult = adult;
        this.children = children;
        this.price = price;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.hotelId = hotelId;
        this.expanded = false;
    }

    private String name;
    private String description;

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    private long hotelId;
    private List<String> benefits;
    private List<String> images;
    private long adult;
    private long children;
    private long price;
    private String checkIn;
    private String checkOut;
    private boolean expanded;
}
