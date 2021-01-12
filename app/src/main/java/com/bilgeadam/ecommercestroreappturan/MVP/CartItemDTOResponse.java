package com.bilgeadam.ecommercestroreappturan.MVP;

import java.util.List;

public class CartItemDTOResponse {


    private String success;
    private String message;
    private List<Product> cartItemDTOList;
    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Product> getCartItemDTOList() {
        return cartItemDTOList;
    }

    public void setCartItemDTOList(List<Product> cartItemDTOList) {
        this.cartItemDTOList = cartItemDTOList;
    }


}
