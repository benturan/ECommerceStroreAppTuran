package com.bilgeadam.ecommercestroreappturan.Fragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;

import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bilgeadam.ecommercestroreappturan.Activities.AccountVerification;
import com.bilgeadam.ecommercestroreappturan.Activities.Login;
import com.bilgeadam.ecommercestroreappturan.Activities.MainActivity;
import com.bilgeadam.ecommercestroreappturan.Adapters.ColorListAdapter;
import com.bilgeadam.ecommercestroreappturan.Adapters.DetailPageSliderPagerAdapter;
import com.bilgeadam.ecommercestroreappturan.Adapters.DotsAdapter;
import com.bilgeadam.ecommercestroreappturan.Adapters.MyPagerAdapter;
import com.bilgeadam.ecommercestroreappturan.Adapters.SizeListAdapter;
import com.bilgeadam.ecommercestroreappturan.Config;
import com.bilgeadam.ecommercestroreappturan.MVP.AddToWishlistResponse;
import com.bilgeadam.ecommercestroreappturan.MVP.Product;
import com.bilgeadam.ecommercestroreappturan.MVP.ProductMedias;
import com.bilgeadam.ecommercestroreappturan.R;
import com.bilgeadam.ecommercestroreappturan.Retrofit.Api;
import com.bilgeadam.ecommercestroreappturan.util.Database;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class CartProductDetail extends Fragment {

    View view;
    private static ViewPager mPager;
    int position;
    int ProductID;
    @BindView(R.id.dotsRecyclerView)
    RecyclerView dotsRecyclerView;
    @BindView(R.id.sizeRecyclerView)
    RecyclerView sizeRecyclerView;
    @BindView(R.id.colorRecyclerView)
    RecyclerView colorRecyclerView;
    public static DotsAdapter dotsAdapter;
    Activity activity;
    ArrayList<String> sliderImages = new ArrayList<>();
    ArrayList<String>   images = new ArrayList<>();
    public static Product productResponse = new Product();

    @BindViews({R.id.productName, R.id.price, R.id.actualPrice, R.id.discountPercentage, R.id.etquantity, R.id.status})
    List<TextView> textViews;
    public static List<Product> productList = new ArrayList<>();
    ArrayList<String> sizeList = new ArrayList<>();
    ArrayList<String> colorList = new ArrayList<>();
    @BindView(R.id.productDescWebView)
    WebView productDescWebView;
    TextView addToWishList;
    @BindView(R.id.sizeCardView)
    CardView sizeCardView;
    @BindView(R.id.colorCardView)
    CardView colorCardView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.progressBar1)
    ProgressBar progressBar1;
    public static Button addToCart;
    public static double productQuantity;
    @BindView(R.id.noImageAdded)
    ImageView noImageAdded;
    @BindView(R.id.etquantity)
    EditText etquantity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        ButterKnife.bind(this, view);
        activity = (Activity) view.getContext();
        Bundle bundle = getArguments();
        position = bundle.getInt("position");
        ProductID = bundle.getInt("ProductID");
        addToCart = (Button) view.findViewById(R.id.addToCart);
        addToWishList = (TextView) view.findViewById(R.id.addToWishList);
        //getProductDetails();
        setData();
        checkWishList();
        return view;
    }

    @OnClick({R.id.addToWishListLayout, R.id.addToWishList, R.id.addToCart})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addToWishList:
            case R.id.addToWishListLayout:
                if (!MainActivity.userId.equalsIgnoreCase("")) {

                    addToWishList();
                } else {

                    Config.showLoginCustomAlertDialog(getActivity(),
                            "Devam etmek için Giriş Yapmalısınız!",
                            "Favori listenize urun eklemek için lütfen giriş yapın!",
                            SweetAlertDialog.WARNING_TYPE);


                    Config.moveTo(getActivity(), Login.class);

                }
                break;
            case R.id.addToCart:
                if (!MainActivity.userId.equalsIgnoreCase("")) {
                    // if (addToCart.getText().toString().trim().equalsIgnoreCase("Sepete Ekle")) {

                    // if (SizeListAdapter.pos == -1 && sizeCardView.getVisibility() == View.VISIBLE) {
                    // Config.showCustomAlertDialog(getActivity(), "Select Size:", "Please select your size to add this item in your cart.", SweetAlertDialog.ERROR_TYPE);
                    // } else if (ColorListAdapter.pos == -1 && colorCardView.getVisibility() == View.VISIBLE) {
                    // Config.showCustomAlertDialog(getActivity(), "Select Color:", "Please select your color to add this item in your cart.", SweetAlertDialog.ERROR_TYPE);
                    // } else {
                    // addToCart();
                    addToCartSQLite();
                    // }

                    //}
                    if (addToCart.getText().toString().trim().equalsIgnoreCase("Out of Stock")) {
                        Config.showCustomAlertDialog(getActivity(),
                                "Stokta Yok",
                                "Bu urun stokta yoktur.",
                                SweetAlertDialog.ERROR_TYPE);

                    } else {
                        ((MainActivity) getActivity()).loadFragment(new MyCartList(), true);
                    }
                }
                else
                {
                    Config.showLoginCustomAlertDialog(getActivity(),
                            "Giriş Yap!",
                            "Sepetinize ürün eklemek için lütfen giriş yapın!",
                            SweetAlertDialog.WARNING_TYPE);



                }
                break;
        }

    }

    private void addToWishList() {
        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        Api.getClient2().addToWishList(String.valueOf(productResponse.getProductID()),
                MainActivity.userId,
                new Callback<AddToWishlistResponse>() {
                    @Override
                    public void success(AddToWishlistResponse addToWishlistResponse, Response response) {
                        pDialog.dismiss();
                        Log.d("addToWishListResponse", addToWishlistResponse.getSuccess() + "");
                        if (addToWishlistResponse.getSuccess().equalsIgnoreCase("true")) {
                            //  Config.showCustomAlertDialog(getActivity(),
                            //  addToWishlistResponse.getMessage(),
                            // "",
                            // SweetAlertDialog.SUCCESS_TYPE);

                            addToWishList.setVisibility(View.VISIBLE);
                            if (addToWishlistResponse.getSuccess().equalsIgnoreCase("true")) {
                                addToWishList.setCompoundDrawablesWithIntrinsicBounds(R.drawable.favorited_icon, 0, 0, 0);
                            } else
                                addToWishList.setCompoundDrawablesWithIntrinsicBounds(R.drawable.unfavorite_icon, 0, 0, 0);


                            // checkWishList();
                        } else {
                            final SweetAlertDialog alertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
                            alertDialog.setTitleText(addToWishlistResponse.getMessage());
                            alertDialog.setConfirmText("Verify Now");
                            alertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    alertDialog.dismissWithAnimation();
                                    Config.moveTo(getActivity(), AccountVerification.class);

                                }
                            });
                            alertDialog.show();
                            Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
                            btn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        pDialog.dismiss();

                        Log.e("error", error.toString());
                    }
                });
    }





    private void addToCartSQLite() {

        int[] dizi = {0,1,2};
    
        String size = "", color = "";
        try {
            if (SizeListAdapter.pos != -1) {
                size = sizeList.get(SizeListAdapter.pos);
            }
        } catch (Exception e) {
        }
        try {

            if (ColorListAdapter.pos != -1) {
                color = colorList.get(ColorListAdapter.pos);
            }
        } catch (Exception e) {
        }
        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Ekleniyor");
        pDialog.setCancelable(false);
        pDialog.show();

        Database db = new Database(getActivity());
        db.addCartProduct(MainActivity.userId, String.valueOf(productResponse.getProductID()),
                String.valueOf(productResponse.getName()),
                String.valueOf(productResponse.getImage()),
                String.valueOf(productResponse.getTax()),
                etquantity.getText().toString(),
                String.valueOf(productResponse.getLimit()),
                String.valueOf(productResponse.getStock()),
                textViews.get(1).getText().toString().replace("₺",""),
                String.valueOf(productResponse.getOldPrice()));
        db.close();
        pDialog.dismiss();
        addToCart.setText("Sepete Git");
       // Config.getCartList(getActivity(), true);
        Config.getCartListSQLite(getActivity(), true);

    }
    private void checkWishList() {
        progressBar.setVisibility(View.VISIBLE);
        addToWishList.setVisibility(View.GONE);
        Api.getClient2().checkWishList(String.valueOf(productList.get(position).getProductID()),
                MainActivity.userId,
                new Callback<AddToWishlistResponse>() {
                    @Override
                    public void success(AddToWishlistResponse addToWishlistResponse, Response response) {

                        progressBar.setVisibility(View.GONE);
                        addToWishList.setVisibility(View.VISIBLE);
                        Log.d("addToWishListResponse", addToWishlistResponse.getSuccess() + "");
                        if (addToWishlistResponse.getSuccess().equalsIgnoreCase("true")) {
                            addToWishList.setCompoundDrawablesWithIntrinsicBounds(R.drawable.favorited_icon, 0, 0, 0);
                        } else
                            addToWishList.setCompoundDrawablesWithIntrinsicBounds(R.drawable.unfavorite_icon, 0, 0, 0);


                    }

                    @Override
                    public void failure(RetrofitError error) {
                        progressBar.setVisibility(View.GONE);
                        addToWishList.setVisibility(View.VISIBLE);
                        Log.e("error", error.toString());
                    }
                });
    }



    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        MainActivity.search.setVisibility(View.VISIBLE);
        MainActivity.cart.setVisibility(View.VISIBLE);
        MainActivity.title.setText("");
        //Config.getCartList(getActivity(), true);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MainActivity.search.setVisibility(View.GONE);
    }

    public void setSizeListData() {
        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        flowLayoutManager.setAutoMeasureEnabled(true);
        sizeRecyclerView.setLayoutManager(flowLayoutManager);
        SizeListAdapter topListAdapter = new SizeListAdapter(getActivity(), sizeList);
        sizeRecyclerView.setAdapter(topListAdapter);
    }

    public void setColorListData() {
        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        flowLayoutManager.setAutoMeasureEnabled(true);
        colorRecyclerView.setLayoutManager(flowLayoutManager);
        ColorListAdapter topListAdapter = new ColorListAdapter(getActivity(), colorList);
        colorRecyclerView.setAdapter(topListAdapter);
    }

    private void setData() {
        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        Api.getClient2().getProductDetails(String.valueOf(ProductID), new Callback<Product>() {
            @Override
            public void success(Product Presponse, Response response) {

                productResponse = Presponse;
                sliderImages = new ArrayList<>();
                //for ile dön
                for (ProductMedias str : productResponse.getProductMedias()) {
                    images.add(str.getMedia_url());
                }


                try {
                    sliderImages.addAll(images);
                    if (sliderImages.size() > 0) {
                        init();
                        noImageAdded.setVisibility(View.GONE);
                    } else {
                        noImageAdded.setVisibility(View.VISIBLE);
                    }
                }catch (Exception e)
                {
                    noImageAdded.setVisibility(View.VISIBLE);
                }
                setDots(0);
                productDescWebView.loadDataWithBaseURL(null, productResponse.getDescription(), "text/html", "utf-8", null);
                textViews.get(0).setText(productResponse.getName());
                textViews.get(1).setText(MainActivity.currency + " " + productResponse.getPrice());
                textViews.get(1).setText(MainActivity.currency + " " + productResponse.getPrice());
                try {
                    //  double discountPercentage = Integer.parseInt(productList.get(position).getMrpprice()) - Integer.parseInt(productList.get(position).getSellprice());
                    //  Log.d("percentage", discountPercentage + "");
                    // discountPercentage = (discountPercentage / Integer.parseInt(productList.get(position).getMrpprice())) * 100;
                    // if ((int) Math.round(discountPercentage) > 0) {
                    //  textViews.get(3).setText(((int) Math.round(discountPercentage) + "% Off"));
                    // }
                    //textViews.get(2).setText(MainActivity.currency + " " + productList.get(position).getMrpprice());
                    //   textViews.get(2).setPaintFlags(textViews.get(2).getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } catch (Exception e) {
                }
                // String[] sizeArray = productList.get(position).getSize().split(",");
                // String[] colorArray = productList.get(position).getProductColor().split(",");
                //sizeList = new ArrayList<>(Arrays.asList(sizeArray));
                //colorList = new ArrayList<>(Arrays.asList(colorArray));
                // Log.d("sizeList", productList.get(position).getSize() + "");
                // if (productList.get(position).getSize().length() > 0) {
                //  setSizeListData();
                // } else {
                //  sizeCardView.setVisibility(View.GONE);
                //}
                //if (productList.get(position).getProductColor().length() > 0) {
                //    setColorListData();
                // } else {
                //  colorCardView.setVisibility(View.GONE);
                // }

                progressBar1.setVisibility(View.GONE);
                // addToWishList.setVisibility(View.VISIBLE);
                addToCart.setVisibility(View.VISIBLE);
                if (productResponse.getStock() < 1) {
                    textViews.get(4).setText("Stokta Yok");
                    addToCart.setBackgroundColor(Color.parseColor("#80148cbf"));
                    addToCart.setText("Stokta Yok");
                } else if (productResponse.getQuantity()> 0 && productResponse.getStock() < 10) {
                    textViews.get(4).setText("Acele edin, sadece" + (int)productResponse.getStock() + " adet kaldı!");
                } else textViews.get(4).setVisibility(View.GONE);
                textViews.get(5).setText(productResponse.getStatus());
                pDialog.dismiss();

            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("errorInCartList", error.toString());

                pDialog.dismiss();

            }
        });


        //productList.addAll(SplashScreen.categoryListResponseData.get(position).getProducts());
        //Log.d("productId", productList.get(position).getProductId());


    }

    private void init() {
        mPager = (ViewPager) view.findViewById(R.id.pager);
        DetailPageSliderPagerAdapter mAdapter = new DetailPageSliderPagerAdapter(getChildFragmentManager(), sliderImages);
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(mPager.getChildCount() * MyPagerAdapter.LOOPS_COUNT / 2, false); // set current item in the adapter to middle
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                position = position % sliderImages.size();
                Log.d("onPageSelected", position + "");
                setDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setDots(int selectedPos) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        dotsRecyclerView.setLayoutManager(linearLayoutManager);
        dotsAdapter = new DotsAdapter(activity, sliderImages.size(), selectedPos);
        dotsRecyclerView.setAdapter(dotsAdapter);

    }
}

