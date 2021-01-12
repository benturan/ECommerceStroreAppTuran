package com.bilgeadam.ecommercestroreappturan.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.bilgeadam.ecommercestroreappturan.Adapters.SearchProductListAdapter;
import com.bilgeadam.ecommercestroreappturan.MVP.Product;
import com.bilgeadam.ecommercestroreappturan.Activities.MainActivity;
import com.bilgeadam.ecommercestroreappturan.R;
import com.bilgeadam.ecommercestroreappturan.Retrofit.Api;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class SearchProducts extends Fragment {

    @BindView(R.id.searchProductsRecyclerView)
    RecyclerView searchProductsRecyclerView;
    @BindView(R.id.searchEditText)
    EditText searchEditText;
    List<Product> productList;

    @BindView(R.id.defaultMessage)
    TextView defaultMessage;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_search_products, container, false);
        ButterKnife.bind(this, view);
       // defaultMessage.setText("Urun Ara");
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("text", editable.toString());
                searchProducts(editable.toString());
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        MainActivity.title.setText("Search");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void searchProducts(String s) {
        productList = new ArrayList<>();
        if (s.length() > 0) {

            Api.getClient2().aramayaGoreUrunleriGetir(s,
                    new Callback<List<Product>>() {
                        @Override
                        public void success(List<Product> responseProductList, Response response) {
                            //pDialog.dismiss();

                            productList = responseProductList;

                            setProductsData();


                        }

                        @Override
                        public void failure(RetrofitError error) {
                            String json =  new String(((TypedByteArray)error.getResponse().getBody()).getBytes());
                            Log.v("failure", json.toString());


                            Log.e("error", error.toString());
                        }
                    });
        }


    }




    private void setProductsData() {
        SearchProductListAdapter productListAdapter;
        GridLayoutManager gridLayoutManager;
        gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        searchProductsRecyclerView.setLayoutManager(gridLayoutManager);
        productListAdapter = new SearchProductListAdapter(getActivity(), productList);
        searchProductsRecyclerView.setAdapter(productListAdapter);

    }
}
