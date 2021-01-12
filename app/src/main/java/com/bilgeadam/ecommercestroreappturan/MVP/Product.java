
package com.bilgeadam.ecommercestroreappturan.MVP;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Product {

    private int productID;
    private String name;
    private String categoryname;
    private double price;
    private String image;
    private String imgSrc;
    private int sales;
    private int categoryid;
    private String success;
    private String productId;
    private String iteam_id;

    private int limit;
    private String orderstatus;
    private String productName;
    private double oldPrice;
    private String sellprice;
    private String size;
    private String status;
    private String color;
    private String currency;
    private int stock;
    private int tax;
    private int quantity;
    private String description;
    private List<String> images = null;
    private List<ProductMedias> productMedias;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public List<ProductMedias> getProductMedias() {
        return productMedias;
    }

    public void setProductMedias(List<ProductMedias> productMedias) {
        this.productMedias = productMedias;
    }

    public String getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }

    public int getTax() {
        return tax;
    }

    public void setTax(int tax) {
        this.tax = tax;
    }


    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public double getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(double oldPrice) {
        this.oldPrice = oldPrice;
    }


    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getIteam_id() {
        return iteam_id;
    }

    public void setItemId(String iteam_id) {
        this.iteam_id = iteam_id;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductColor() {
        return color;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }



    public String getSellprice() {
        return sellprice;
    }

    public void setSellprice(String sellprice) {
        this.sellprice = sellprice;
    }


    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status)

    {
        this.status = status;
    }


    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }


    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }


    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }


    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}