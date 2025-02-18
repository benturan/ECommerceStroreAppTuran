package com.bilgeadam.ecommercestroreappturan.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;


import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bilgeadam.ecommercestroreappturan.Common;
import com.bilgeadam.ecommercestroreappturan.Config;
import com.bilgeadam.ecommercestroreappturan.Fragments.AppInfo;
import com.bilgeadam.ecommercestroreappturan.Fragments.FAQ;
import com.bilgeadam.ecommercestroreappturan.Fragments.Home;
import com.bilgeadam.ecommercestroreappturan.Fragments.MyCartList;
import com.bilgeadam.ecommercestroreappturan.Fragments.MyOrders;
import com.bilgeadam.ecommercestroreappturan.Fragments.MyProfile;
import com.bilgeadam.ecommercestroreappturan.Fragments.MyWishList;
import com.bilgeadam.ecommercestroreappturan.Fragments.SearchProducts;
import com.bilgeadam.ecommercestroreappturan.R;
import com.google.android.material.navigation.NavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    String from;
    public static LinearLayout toolbarContainer;
    public static View toolbar, searchLayout;
    @BindView(R.id.searchView)
    SearchView searchView;
    boolean doubleBackToExitPressedOnce = false;
    public static ImageView menu, back, cart, search;
    public static DrawerLayout drawerLayout;
    public static String currency, userId;
    @BindView(R.id.navigationView)
    NavigationView navigationView;
    public static TextView title, cartCount;
    int count;
    public static ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // navigationView = findViewById(R.id.navigationView);
        ButterKnife.bind(this);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initViews();
        getCurrency();
        getUserId();
        Config.getCartListSQLite(getApplicationContext(),true);
        Intent intent = getIntent();
        try {
            from = intent.getStringExtra("from");
           // if (from.equalsIgnoreCase("signUp")) {
               // Config.showCustomAlertDialog(MainActivity.this,
                       // "Verification email sent successfully.",
                       // "Please check your inbox and confirm your email address. The email may take upto 5 minutes to reach your inbox\n\nIf you didn't receive email from us, make sure to check your spam folder.",
                       // SweetAlertDialog.WARNING_TYPE);
          //  }
        } catch (Exception e) {
            Log.e("errorOccur", "Error");
        }
        // customized searchView
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText searchEditText = (EditText) searchView.findViewById(id);
        searchEditText.setTextSize(12);
        searchLayout = findViewById(R.id.searchLayout);
        toolbarContainer = (LinearLayout) findViewById(R.id.toolbar_container);
        toolbar = findViewById(R.id.toolbar);
        loadFragment(new Home(), false);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.myWishList:
                        loadFragment(new MyWishList(), true);

                        break;
                    case R.id.myCart:
                        loadFragment(new MyCartList(), true);
                        break;
                    case R.id.myOrders:
                        loadFragment(new MyOrders(), true);
                        break;
                    case R.id.myProfile:
                        loadFragment(new MyProfile(), true);
                        break;
                    case R.id.faq:
                        loadFragment(new FAQ(), true);
                        break;
                    case R.id.appInfo:
                        loadFragment(new AppInfo(), true);
                        break;
                    case R.id.share:
                        shareApp();
                        break;
                    case R.id.rateApp:
                        // perform click on Rate Item
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                        }
                        break;
                    case R.id.call:
                        call();
                        break;
                }
                return false;
            }
        });
       // displayFirebaseRegId(); // display firebase id

       // if (getIntent().getBooleanExtra("isFromNotification", false)) {
           // ProductDetail.productList.clear();
           // ProductDetail.productList.addAll(SplashScreen.imagesList1);
           // ProductDetail productDetail = new ProductDetail();
           // Bundle bundle = new Bundle();
           // bundle.putInt("position", 0);
           // productDetail.setArguments(bundle);
            //loadFragment(productDetail, true);
       // }

    }

    private void call() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + getResources().getString(R.string.contactNo)));
        startActivity(intent);
    }



    public void shareApp() {
        // share app with your friends
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/*");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Try this " + getResources().getString(R.string.app_name) + " App: https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
        startActivity(Intent.createChooser(shareIntent, "Share Using"));
    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
        Log.e("FCM", "Firebase reg id: " + regId);
        if (!TextUtils.isEmpty(regId)) {
        } else
            Log.d("Firebase", "Firebase Reg Id is not received yet!");
    }

    private void getUserId() {
        if (Common.getSavedUserData(MainActivity.this, "userId").equalsIgnoreCase("")) {
            userId = "";
        } else {
            userId = Common.getSavedUserData(MainActivity.this, "userId");
            Log.d("userId", userId);
        }

    }

    private void getCurrency() {
        try {
            //if (SplashScreen.allProductsData.get(0).getCurrency().equalsIgnoreCase("TRY"))
                currency = "₺";
            //else
               // currency = "$";
        } catch (Exception e) {
        }
    }

    private void initViews() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        title = (TextView) findViewById(R.id.title);
        cartCount = (TextView) findViewById(R.id.cartCount);
        menu = (ImageView) findViewById(R.id.menu);
        cart = (ImageView) findViewById(R.id.cart);
        back = (ImageView) findViewById(R.id.back);
        search = (ImageView) findViewById(R.id.search);
    }

    @Override
    public void onBackPressed() {
        // double press to exit
        if (menu.getVisibility() == View.VISIBLE) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
        } else {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back once more to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }

    public void showToolbar() {
        toolbarContainer.clearAnimation();
        toolbarContainer
                .animate()
                .translationY(0)
                .start();

    }

    public void lockUnlockDrawer(int lockMode) {
        drawerLayout.setDrawerLockMode(lockMode);
        if (lockMode == DrawerLayout.LOCK_MODE_LOCKED_CLOSED) {
            menu.setVisibility(View.GONE);
            back.setVisibility(View.VISIBLE);
            searchLayout.setVisibility(View.GONE);
            showToolbar();

        } else {
            menu.setVisibility(View.VISIBLE);
            back.setVisibility(View.GONE);
            searchLayout.setVisibility(View.VISIBLE);

        }

    }

    @OnClick({R.id.menu, R.id.back, R.id.cart, R.id.cartCount, R.id.searchTextView, R.id.search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu:
                if (!Home.swipeRefreshLayout.isRefreshing())
                    if (!MainActivity.drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    MainActivity.drawerLayout.openDrawer(Gravity.LEFT);
                }
                break;
            case R.id.back:
                if (!Home.swipeRefreshLayout.isRefreshing())
                    removeCurrentFragmentAndMoveBack();
                break;
            case R.id.searchTextView:
            case R.id.search:
                loadFragment(new SearchProducts(), true);
                break;
            case R.id.cart:
            case R.id.cartCount:
                if (!Home.swipeRefreshLayout.isRefreshing())
                    loadFragment(new MyCartList(), true);

                break;

        }
    }

    public void removeCurrentFragmentAndMoveBack() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        /*FragmentTransaction trans = fragmentManager.beginTransaction();
        trans.remove(fragment);
        trans.commit();*/
        fragmentManager.popBackStack();
    }

    public void loadFragment(Fragment fragment, Boolean bool) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        if (bool) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }


}
