package com.example.paymentapp.dto;

public class OrderEvent {

    private Integer customerId;
    private String product;
    private Integer orderId;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "OrderEvent{" +
                "customerId=" + customerId +
                ", product='" + product + '\'' +
                ", orderId=" + orderId +
                '}';
    }
}