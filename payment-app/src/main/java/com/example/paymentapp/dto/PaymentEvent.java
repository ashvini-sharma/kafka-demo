package com.example.paymentapp.dto;

public class PaymentEvent {

    private Integer customerId;
    private Integer orderId;
    private Integer price;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "PaymentEvent{" +
                "customerId=" + customerId +
                ", orderId=" + orderId +
                ", price=" + price +
                '}';
    }
}