package com.bilgeadam.ecommercestroreappturan.Retrofit;

import retrofit.RestAdapter;

/**
 * Created by AbhiAndroid
 */
public class Api {

    public static ApiInterface getClient() {

        // change your base URL
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint("http://www.abhidemo.com/demo/ecom") //Set the Root URL
                .build(); //Finally building the adapter

        //Creating object for our interface
        ApiInterface api = adapter.create(ApiInterface.class);
        return api;
    }

    public static ApiInterface getClient2() {

        // change your base URL
        RestAdapter adapter2 = new RestAdapter.Builder()
             //   .setEndpoint("https://opendart-ecommerce2.herokuapp.com") //Set the Root URL
          .setEndpoint("https://springbooteticaret.herokuapp.com")

               // .setEndpoint("http://89.19.22.18:8514")
                .build(); //Finally building the adapter
        //http://52.117.236.126:8113/allindex
        //Creating object for our interface
        ApiInterface api2 = adapter2.create(ApiInterface.class);
        return api2;
    }
}
