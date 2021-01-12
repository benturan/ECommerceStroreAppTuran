package com.bilgeadam.ecommercestroreappturan.MVP;

public class CartItemDTO{

    public int productId;
    public String productName;
    public double price;
    public Integer productQuantity;
    public double lineTotalAmount;
    public String image;

    public int getProductId() {
        return productId;
    }
    public void setProductId(int productId) {
        this.productId = productId;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public Integer getProductQuantity() {
        return productQuantity;
    }
    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }
    public double getLineTotalAmount() {
        return lineTotalAmount;
    }
    public void setLineTotalAmount(double lineTotalAmount) {
        this.lineTotalAmount = lineTotalAmount;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

}

