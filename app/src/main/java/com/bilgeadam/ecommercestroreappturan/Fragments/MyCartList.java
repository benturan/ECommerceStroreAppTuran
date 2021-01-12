package com.bilgeadam.ecommercestroreappturan.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.bilgeadam.ecommercestroreappturan.Activities.AccountVerification;
import com.bilgeadam.ecommercestroreappturan.Adapters.CartListAdapter;
import com.bilgeadam.ecommercestroreappturan.Config;
import com.bilgeadam.ecommercestroreappturan.Activities.Login;
import com.bilgeadam.ecommercestroreappturan.MVP.CartItemDTOResponse;
import com.bilgeadam.ecommercestroreappturan.MVP.Product;
import com.bilgeadam.ecommercestroreappturan.Activities.MainActivity;
import com.bilgeadam.ecommercestroreappturan.R;
import com.bilgeadam.ecommercestroreappturan.Activities.SignUp;
import com.bilgeadam.ecommercestroreappturan.util.Database;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class MyCartList extends Fragment {

    View view;

    @BindView(R.id.categoryRecyclerView)
    RecyclerView productsRecyclerView;
    public static int categoryPosition = 0;
    public static List<Product> productsData = new ArrayList<>();
    public static CartItemDTOResponse cartistResponseData;
    @BindView(R.id.proceedToPayment)
    Button proceedToPayment;
    public static Context context;
    @BindView(R.id.emptyCartLayout)
    LinearLayout emptyCartLayout;
    @BindView(R.id.loginLayout)
    LinearLayout loginLayout;
    @BindView(R.id.continueShopping)
    Button continueShopping;

    @BindView(R.id.verifyEmailLayout)
    LinearLayout verifyEmailLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cart_list, container, false);
        ButterKnife.bind(this, view);
        context = getActivity();
        MainActivity.title.setText("Sepetim");
        proceedToPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).loadFragment(new ChoosePaymentMethod(), true);

            }
        });
        if (!MainActivity.userId.equalsIgnoreCase("")) {
           // getCartList();
            getCartListSQLite();
        } else {
            proceedToPayment.setVisibility(View.GONE);
            loginLayout.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @OnClick({R.id.continueShopping, R.id.loginNow, R.id.txtSignUp, R.id.verfiyNow})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.continueShopping:
                Config.moveTo(getActivity(), MainActivity.class);
                getActivity().finish();
                break;
            case R.id.loginNow:
                Config.moveTo(getActivity(), Login.class);
                break;
            case R.id.txtSignUp:
                Config.moveTo(getActivity(), SignUp.class);
                break;

            case R.id.verfiyNow:
                Config.moveTo(getActivity(), AccountVerification.class);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MainActivity.cart.setVisibility(View.VISIBLE);
    }

    public void getCartListSQLite() {
        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
         //cartistResponseData = cartItemDTOResponse;
         pDialog.dismiss();
         productsData = new ArrayList<>();
        Database db = new Database(getActivity());
        Config.sqliteCartList = db.userCartList();
        for(int i=0;i<Config.sqliteCartList.size();i++){
            Product product = new Product();
            product.setProductID(Integer.parseInt(Config.sqliteCartList.get(i).get("ProductId")));
            product.setName(Config.sqliteCartList.get(i).get("ProductName"));
            product.setImage(Config.sqliteCartList.get(i).get("ProductImage"));
            product.setTax(Integer.valueOf(Config.sqliteCartList.get(i).get("Tax")));
            product.setQuantity(Integer.valueOf(Config.sqliteCartList.get(i).get("Quantity")));
            product.setLimit(Integer.valueOf(Config.sqliteCartList.get(i).get("Limits")));
            product.setStock(Integer.valueOf(Config.sqliteCartList.get(i).get("Stock")));
            product.setPrice(Double.valueOf(Config.sqliteCartList.get(i).get("Price")));
            product.setOldPrice(Double.valueOf(Config.sqliteCartList.get(i).get("OldPrice")));
            productsData.add(product);
        }
               // productsData.clear();
                //productsData = cartItemDTOResponse.getCartItemDTOList();
                //if (cartItemDTOResponse.getSuccess().equalsIgnoreCase("false")) {
                   // verifyEmailLayout.setVisibility(View.VISIBLE);
                    //proceedToPayment.setVisibility(View.GONE);
               // } else {
                    try {

                       // cartItemDTOResponse.getCartItemDTOList().size();
                        setProductsData();
                    } catch (Exception e) {
                        proceedToPayment.setVisibility(View.GONE);
                        emptyCartLayout.setVisibility(View.VISIBLE);
                    }
               // }
    }



    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        MainActivity.cart.setVisibility(View.GONE);
        //Config.getCartList(getActivity(), false);
        Config.getCartListSQLite(getActivity(), true);
    }

    private void setProductsData() {
        CartListAdapter wishListAdapter;
        GridLayoutManager gridLayoutManager;
        gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        productsRecyclerView.setLayoutManager(gridLayoutManager);
        wishListAdapter = new CartListAdapter(getActivity(), productsData);
        productsRecyclerView.setAdapter(wishListAdapter);

    }
}
