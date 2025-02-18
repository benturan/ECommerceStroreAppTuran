package com.bilgeadam.ecommercestroreappturan.Fragments;


import android.app.Activity;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bilgeadam.ecommercestroreappturan.Adapters.DotsAdapter;
import com.bilgeadam.ecommercestroreappturan.Adapters.HomeCategoryAdapter;
import com.bilgeadam.ecommercestroreappturan.Adapters.HomeCategoryProductsAdapter;
import com.bilgeadam.ecommercestroreappturan.Adapters.MyPagerAdapter;
import com.bilgeadam.ecommercestroreappturan.DetectConnection;
import com.bilgeadam.ecommercestroreappturan.MVP.CategoryListResponse;
import com.bilgeadam.ecommercestroreappturan.MVP.Product;
import com.bilgeadam.ecommercestroreappturan.Activities.MainActivity;
import com.bilgeadam.ecommercestroreappturan.MVP.SliderResponse;
import com.bilgeadam.ecommercestroreappturan.R;
import com.bilgeadam.ecommercestroreappturan.Retrofit.Api;
import com.bilgeadam.ecommercestroreappturan.Activities.SplashScreen;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Home extends Fragment {

    View view;
    private static ViewPager mPager;
    @BindView(R.id.categoryRecyclerView)
    RecyclerView categoryRecyclerView;
    @BindView(R.id.categoryProductRecyclerView)
    RecyclerView categoryProductRecyclerView;
    public static RecyclerView dotsRecyclerView;
    public static DotsAdapter dotsAdapter;
    public static Activity activity;
    public static NestedScrollView nestedScrollView;
    private String TAG = "testing";
    @BindString(R.string.app_name)
    String app_name;
    @BindView(R.id.sliderLayout)
    LinearLayout sliderLayout;
    public static SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        MainActivity.title.setText(app_name);
        activity = (Activity) view.getContext();
        dotsRecyclerView = (RecyclerView) view.findViewById(R.id.dotsRecyclerView);
        nestedScrollView = (NestedScrollView) view.findViewById(R.id.nestedScrollView);
        setCategoryData();
        setCategoryProductsData();
        try {
            if (SplashScreen.sliderResponsesData.size() > 0) {
                setDots(0);
                init();
            }else
                sliderLayout.setVisibility(View.GONE);

        } catch (Exception e) {
            sliderLayout.setVisibility(View.GONE);
        }
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.simpleSwipeRefreshLayout);

        // implement setOnRefreshListener event on SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (DetectConnection.checkInternetConnection(getActivity())) {
                    MainActivity.searchLayout.setVisibility(View.GONE);
                    //Config.getCartList(getActivity(), true);
                    getSliderList();
                } else {
                    Toast.makeText(getActivity(), "Internet Not Available", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

         return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("onStart", "called");
        MainActivity.cart.setVisibility(View.VISIBLE);
       // Config.getCartList(getActivity(), true);
        ((MainActivity) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_UNLOCKED);
        MainActivity.drawerLayout.closeDrawers();
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (MainActivity.searchLayout.getVisibility() == View.VISIBLE) {
                    if (scrollY > oldScrollY) {
                        Log.i(TAG, "Scroll DOWN");
                        hideToolbar();
                    }
                    if (scrollY < oldScrollY) {
                        Log.i(TAG, "Scroll UP");
                        showToolbar();
                    }

                    if (scrollY == 0) {
                        Log.i(TAG, "TOP SCROLL");
                        showToolbar();

                    }
                    if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                        Log.i(TAG, "BOTTOM SCROLL");
                        hideToolbar();
                    }
                } else
                    nestedScrollView.setNestedScrollingEnabled(false);
            }
        });

    }

    public void showToolbar() {
        MainActivity.toolbarContainer.clearAnimation();
        MainActivity.toolbarContainer
                .animate()
                .translationY(0)
                .start();

    }

    private void hideToolbar() {
        MainActivity.toolbarContainer.clearAnimation();
        MainActivity.toolbarContainer
                .animate()
                .translationY(-MainActivity.toolbar.getBottom())
                .alpha(1.0f)
                .start();
    }

    private void setCategoryData() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        categoryRecyclerView.setLayoutManager(gridLayoutManager);
        Log.d("categorySize", SplashScreen.categoryListResponseData.size() + "");
        if (SplashScreen.categoryListResponseData.size() < 4) {
            HomeCategoryAdapter homeCategoryAdapter = new HomeCategoryAdapter(getActivity(), SplashScreen.categoryListResponseData, SplashScreen.categoryListResponseData.size());
            categoryRecyclerView.setAdapter(homeCategoryAdapter);
        } else {
            HomeCategoryAdapter homeCategoryAdapter = new HomeCategoryAdapter(getActivity(), SplashScreen.categoryListResponseData, 4);
            categoryRecyclerView.setAdapter(homeCategoryAdapter);

        }
    }

    public void setDots(int selectedPos) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        dotsRecyclerView.setLayoutManager(linearLayoutManager);
        dotsAdapter = new DotsAdapter(activity, SplashScreen.sliderResponsesData.size(), selectedPos);
        dotsRecyclerView.setAdapter(dotsAdapter);

    }

    private void setCategoryProductsData() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        categoryProductRecyclerView.setLayoutManager(gridLayoutManager);
       HomeCategoryProductsAdapter homeCategoryAdapter = new HomeCategoryProductsAdapter(getActivity(),
               SplashScreen.categoryListResponseData);
        categoryProductRecyclerView.setAdapter(homeCategoryAdapter);

    }

    private void init() {
        mPager = (ViewPager) view.findViewById(R.id.pager);
        MyPagerAdapter mAdapter = new MyPagerAdapter(getChildFragmentManager(),
                SplashScreen.sliderResponsesData);
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(mPager.getChildCount() * MyPagerAdapter.LOOPS_COUNT / 2, false); // set current item in the adapter to middle
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                position = position % SplashScreen.sliderResponsesData.size();
                Log.d("onPageSelected", position + "");
                setDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    public void getCategoryList() {
        // getting category list news data
        Api.getClient2().getCategoryList(new Callback<List<CategoryListResponse>>() {
            @Override
            public void success(List<CategoryListResponse> categoryListResponses, Response response) {
                SplashScreen.categoryListResponseData.clear();
                SplashScreen.categoryListResponseData.addAll(categoryListResponses);
                setCategoryData();
                setCategoryProductsData();
                swipeRefreshLayout.setRefreshing(false);
                MainActivity.searchLayout.setVisibility(View.VISIBLE);

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("error", error.toString());
                swipeRefreshLayout.setRefreshing(false);
                MainActivity.searchLayout.setVisibility(View.VISIBLE);

            }
        });
    }

    public void getSliderList() {
        // getting slider list data
        Api.getClient2().getSliderList2(new Callback<List<SliderResponse>>() {
            @Override
            public void success(List<SliderResponse> sliderResponses, Response response) {

                try {
                    SplashScreen.sliderResponsesData = new ArrayList<>();
                    SplashScreen.sliderResponsesData.addAll(sliderResponses);
                    sliderLayout.setVisibility(View.VISIBLE);
                    setDots(0);
                    init();
                } catch (Exception e) {
                    sliderLayout.setVisibility(View.GONE);
                }
              //  getAllProducts();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("error", error.toString());
                swipeRefreshLayout.setRefreshing(false);
                MainActivity.searchLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    public void getAllProducts() {

        // getting news list data
        Api.getClient().getAllProducts(new Callback<List<Product>>() {
            @Override
            public void success(List<Product> allProducts, Response response) {
                Log.d("allProductsDataHome", allProducts.get(0).getProductName());
                SplashScreen.allProductsData.clear();
                SplashScreen.allProductsData.addAll(allProducts);
                getCategoryList();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("error", error.toString());
                swipeRefreshLayout.setRefreshing(false);
                MainActivity.searchLayout.setVisibility(View.VISIBLE);
            }
        });
    }
}
