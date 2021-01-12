package com.bilgeadam.ecommercestroreappturan.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bilgeadam.ecommercestroreappturan.DetectConnection;
import com.bilgeadam.ecommercestroreappturan.MVP.CategoryListResponse;
import com.bilgeadam.ecommercestroreappturan.MVP.Product;
import com.bilgeadam.ecommercestroreappturan.MVP.SliderListResponse;
import com.bilgeadam.ecommercestroreappturan.MVP.SliderResponse;
import com.bilgeadam.ecommercestroreappturan.R;
import com.bilgeadam.ecommercestroreappturan.Retrofit.Api;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SplashScreen extends Activity {

    public static List<CategoryListResponse> categoryListResponseData;
    public static List<SliderListResponse> sliderListResponsesData;
    public static List<SliderResponse> sliderResponsesData;
    public static List<Product> allProductsData;
    public static List<Product> imagesList1;
    String id = "";
    @BindView(R.id.errorText)
    TextView errorText;
    @BindView(R.id.internetNotAvailable)
    LinearLayout internetNotAvailable;
    @BindView(R.id.splashImage)
    ImageView splashImage;
    SharedPreferences sharedPreference, sharedPreferencesCache;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sharedPreferencesCache = getSharedPreferences("cacheExist", 0);

        // check data from FCM
        try {
            Intent intent = getIntent();
            id = intent.getStringExtra("id");
            Log.d("notification Data", id);
        } catch (Exception e) {
            Log.d("error notification data", e.toString());
        }
        sharedPreference = getSharedPreferences("localData", 0);
        editor = sharedPreference.edit();
        // Check the internet and get response from API's
        if (DetectConnection.checkInternetConnection(getApplicationContext())) {
            getCategoryList();
        } else {
            errorText.setText("İnternet bağlantınızı kontrol ediniz!");
            internetNotAvailable.setVisibility(View.VISIBLE);
            splashImage.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.tryAgain)
    public void onClick() {
        if (DetectConnection.checkInternetConnection(getApplicationContext())) {
            internetNotAvailable.setVisibility(View.GONE);
            splashImage.setVisibility(View.VISIBLE);
            getCategoryList();
        } else {
            errorText.setText("İnternet bağlantınızı kontrol ediniz!");
            internetNotAvailable.setVisibility(View.VISIBLE);
            splashImage.setVisibility(View.GONE);
        }
    }

    public void getCategoryList() {
        // getting category list news data
        Api.getClient2().getCategoryList(new Callback<List<CategoryListResponse>>() {
            @Override
            public void success(List<CategoryListResponse> categoryListResponses, Response response) {
                try {
                    categoryListResponseData = categoryListResponses;
                    Log.d("categoryData", categoryListResponses.get(0).getCategory_name());
                    Gson gson = new Gson();
                    String json = gson.toJson(categoryListResponseData);
                    editor.putString("categoryList", json);
                    editor.commit();
                    getSliderList();
                } catch (Exception e) {
                    errorText.setText("No Category Added In This Store!");
                    internetNotAvailable.setVisibility(View.VISIBLE);
                    splashImage.setVisibility(View.GONE);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("error", error.toString());
                errorText.setText("İnternet bağlantınızı kontrol ediniz!");
                internetNotAvailable.setVisibility(View.VISIBLE);
                splashImage.setVisibility(View.GONE);
            }
        });
    }

    public void getSliderList() {
        // getting slider list data
        Api.getClient2().getSliderList2(new Callback<List<SliderResponse>>() {
            @Override
            public void success(List<SliderResponse> sliderResponses, Response response) {
                sliderResponsesData = sliderResponses;
               // getAllProducts();
                moveNext();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("error", error.toString());
                errorText.setText("İnternet bağlantınızı kontrol ediniz!");
                internetNotAvailable.setVisibility(View.VISIBLE);
                splashImage.setVisibility(View.GONE);
            }
        });
    }

    public void getAllProducts() {
        // getting news list data
        Api.getClient2().getAllProducts(new Callback<List<Product>>() {
            @Override
            public void success(List<Product> allProducts, Response response) {
                try {
                    allProductsData = allProducts;
                    Log.d("allProductsData", allProducts.get(0).getName());
                    Gson gson = new Gson();
                    String json = gson.toJson(allProductsData);
                    editor.putString("newslist", json);
                    editor.commit();
                    moveNext();
                } catch (Exception e) {
                    errorText.setText("No Product Added In This Store!");
                    internetNotAvailable.setVisibility(View.VISIBLE);
                    splashImage.setVisibility(View.GONE);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("error", error.toString());
                errorText.setText("İnternet bağlantınızı kontrol ediniz!");
                internetNotAvailable.setVisibility(View.VISIBLE);
                splashImage.setVisibility(View.GONE);
            }
        });
    }

    private void moveNext() {
// redirect to next page after getting data from server

        boolean isFromNotification = true;
        try {
            imagesList1 = new ArrayList<>();
            //if (id.length() > 0) {
                for (int j = 0; j < allProductsData.size(); j++) {
                    //if (allProductsData.get(j).getProductId().trim().equalsIgnoreCase(id)) {
                    if (allProductsData.get(j).getProductID()>0) {
                        imagesList1.add(allProductsData.get(j));
                    }
                }

                //isFromNotification = true;
           // } else {
              //  isFromNotification = false;
            //}
        } catch (Exception e) {
            Log.d("error notification data", e.toString());
            isFromNotification = false;
        }
        //if (Common.getSavedUserData(SplashScreen.this, "email").equalsIgnoreCase("")&&!isFromNotification) {
            //Config.moveTo(SplashScreen.this, Login.class);
            //finishAffinity();
      //  } else {
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            intent.putExtra("isFromNotification", isFromNotification);
            startActivity(intent);
            finishAffinity();
      //  }

    }

}
