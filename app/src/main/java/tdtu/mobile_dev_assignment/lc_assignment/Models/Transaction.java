package tdtu.mobile_dev_assignment.lc_assignment.Models;

public class Transaction {
    private String transactionId;

    public Transaction(String transactionId, long totalPrice, String paymentType, String status, long roomId, String bookedDate, long promotion) {
        this.transactionId = transactionId;
        this.totalPrice = totalPrice;
        this.paymentType = paymentType;
        this.status = status;
        this.roomId = roomId;
        this.bookedDate = bookedDate;
        this.promotion = promotion;
    }

    private long totalPrice;
    private String paymentType;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public String getBookedDate() {
        return bookedDate;
    }

    public void setBookedDate(String bookedDate) {
        this.bookedDate = bookedDate;
    }

    public long getPromotion() {
        return promotion;
    }

    public void setPromotion(long promotion) {
        this.promotion = promotion;
    }

    private String status;
    private long roomId;
    private String bookedDate;
    private long promotion;
}
