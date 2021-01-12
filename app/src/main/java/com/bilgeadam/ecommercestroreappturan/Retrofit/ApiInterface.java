package com.bilgeadam.ecommercestroreappturan.Retrofit;


import com.bilgeadam.ecommercestroreappturan.MVP.AddOrderDetailResponse;
import com.bilgeadam.ecommercestroreappturan.MVP.AddOrderResponse;
import com.bilgeadam.ecommercestroreappturan.MVP.AddToCartResponse;
import com.bilgeadam.ecommercestroreappturan.MVP.AddToWishlistResponse;
import com.bilgeadam.ecommercestroreappturan.MVP.CartItemDTOResponse;
import com.bilgeadam.ecommercestroreappturan.MVP.CategoryListResponse;
import com.bilgeadam.ecommercestroreappturan.MVP.FAQResponse;
import com.bilgeadam.ecommercestroreappturan.MVP.MyOrdersResponse;
import com.bilgeadam.ecommercestroreappturan.MVP.NewSignUpResponse;
import com.bilgeadam.ecommercestroreappturan.MVP.Product;
import com.bilgeadam.ecommercestroreappturan.MVP.RegistrationResponse;
import com.bilgeadam.ecommercestroreappturan.MVP.SignUpResponse;
import com.bilgeadam.ecommercestroreappturan.MVP.SliderResponse;
import com.bilgeadam.ecommercestroreappturan.MVP.StripeResponse;
import com.bilgeadam.ecommercestroreappturan.MVP.UserProfileResponse;
import com.bilgeadam.ecommercestroreappturan.MVP.WishlistResponse;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;

public interface ApiInterface {

    // API's endpoints
    @GET("/getAllProducts/")
    public void getAllProducts(
            Callback<List<Product>> callback);

    @GET("/getMainCategories_Products/")
    public void getCategoryList(Callback<List<CategoryListResponse>> callback);

    @GET("/getAllMansets/")
    public void getSliderList2(Callback<List<SliderResponse>> callback);

    @GET("/allfaq")
    public void getFAQ(Callback<FAQResponse> callback);

    @GET("/allterms")
    public void getTerms(Callback<FAQResponse> callback);

    @FormUrlEncoded
    @POST("/tokenSave")
    public void sendAccessToken(@Field("accesstoken") String accesstoken, Callback<RegistrationResponse> callback);

    @FormUrlEncoded
    @POST("/addFavorite")
    public void addToWishList(@Field("product_id") String product_id, @Field("user_id") String user_id,
                              Callback<AddToWishlistResponse> callback);


    @FormUrlEncoded
    @POST("/deleteFavorite")
    public void deleteToWishList(@Field("product_id") String product_id, @Field("user_id") String user_id,
                              Callback<AddToWishlistResponse> callback);



    @FormUrlEncoded
    @POST("/mobileAddCart")
    public void addToCart(@Field("product_id") String product_id, @Field("userid") String user_id,
                          @Field("productQuantity") String productQuantity, @Field("productPrice") String productPrice,
                          @Field("productName") String productName, Callback<AddToCartResponse> callback);


    @FormUrlEncoded
    @POST("/updateCart")
    public void updateCart(@Field("product_id") String product_id,
                           @Field("quantity") double quantity, Callback<AddToCartResponse> callback);



    @FormUrlEncoded
    @POST("/updateQuantityCart")
    public void updateQuantityCart(@Field("product_id") String product_id,
                           @Field("quantity") double quantity, Callback<AddToCartResponse> callback);



    @FormUrlEncoded
    @POST("/wishCheck")
    public void checkWishList(@Field("product_id") String product_id, @Field("user_id") String user_id, Callback<AddToWishlistResponse> callback);


    @FormUrlEncoded
    @POST("/getFavori_Products")
    public void getWishList(@Field("user_id") String user_id, Callback<WishlistResponse> callback);



    @FormUrlEncoded
    @POST("/getProductsDetailByProductId")
    public void getProductDetails(@Field("ProductId") String product_id, Callback<Product> callback);


    @FormUrlEncoded
    @POST("/orders")
    public void getMyOrders(@Field("user_id") String user_id, Callback<MyOrdersResponse> callback);


    @FormUrlEncoded
    @POST("/mobileAllCart")
    public void getCartList(@Field("user_id") String user_id, Callback<CartItemDTOResponse> callback);


    @FormUrlEncoded
    @POST("/userProfile")
    public void getUserProfile(@Field("user_id") String user_id, Callback<UserProfileResponse> callback);


    @FormUrlEncoded
    @POST("/updateProfile")
    public void updateProfile(@Field("user_id") String user_id,
                              @Field("name") String name,
                              @Field("surname") String surname,
                              @Field("email") String email,
                              @Field("password") String password,
                              @Field("telephone") String telephone,
                              @Field("gender") String gender,
                              Callback<UserProfileResponse> callback);





    @FormUrlEncoded
    @POST("/resentMail")
    public void resentEmail(@Field("email") String email, Callback<SignUpResponse> callback);


    @FormUrlEncoded
    @POST("/mobileLogin")
    public void login(@Field("email") String email, @Field("password") String password, Callback<SignUpResponse> callback);


    @FormUrlEncoded
    @POST("/JSON/paystripe.php")
    public void stripePayment(@Field("stripeToken") String stripeToken,
                              @Field("total") String total,
                              @Field("user_id") String user_id,
                              @Field("cart_id") String cart_id,
                              @Field("address") String address,
                              @Field("phone") String phone,
                              Callback<StripeResponse> callback);


    @FormUrlEncoded
    @POST("/addOrder")
    public void addOrder(@Field("UserId") String UserId,
                         @Field("PaymentTypeId") String PaymentTypeId,
                         @Field("Price") String Price,
                         @Field("Tax") String Tax,
                         @Field("TotalPrice") String TotalPrice,
                         Callback<AddOrderResponse> callback);


    @FormUrlEncoded
    @POST("/addOrderDetail")
    public void addOrderDetail(@Field("OrderId") String OrderId,
                               @Body List<Product> productDTOList,
                         Callback<AddOrderDetailResponse> callback);

    @FormUrlEncoded
    @POST("/addOrderDetailOne")
    public void addOrderDetailOne(@Field("OrderId") String OrderId,
                                  @Field("ProductId") String ProductId,
                                  @Field("Quantity") String Quantity,
                                  @Field("Price") String Price,
                               Callback<AddOrderDetailResponse> callback);


    @FormUrlEncoded
    @POST("/forgotPassword")
    public void forgotPassword(@Field("email") String email, Callback<SignUpResponse> callback);




    @FormUrlEncoded
    @POST("/kayit")
    public void kayit(@Field("name") String name, @Field("surname") String surname,
                      @Field("email") String email, @Field("password") String password,
                      Callback<NewSignUpResponse> callback);

    @FormUrlEncoded
    @POST("/SearchAllProducts2")
    public void aramayaGoreUrunleriGetir(@Field("sorgu") String sorgu,
                                         Callback<List<Product>> callback);

}
