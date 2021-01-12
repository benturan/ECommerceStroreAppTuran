package com.bilgeadam.ecommercestroreappturan.Fragments;

import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bilgeadam.ecommercestroreappturan.Adapters.CategoryListAdapter;
import com.bilgeadam.ecommercestroreappturan.Activities.MainActivity;
import com.bilgeadam.ecommercestroreappturan.R;
import com.bilgeadam.ecommercestroreappturan.Activities.SplashScreen;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryList extends Fragment {

    View view;
    @BindView(R.id.categoryRecyclerView)
    RecyclerView categoryRecyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_category_list, container, false);
        ButterKnife.bind(this, view);
        setCategoryData();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        MainActivity.search.setVisibility(View.VISIBLE);
        MainActivity.title.setText("Ana Kategoriler");
        //Config.getCartList(getActivity(),true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MainActivity.search.setVisibility(View.GONE);
    }

    private void setCategoryData() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        categoryRecyclerView.setLayoutManager(gridLayoutManager);
        CategoryListAdapter categoryListAdapter = new CategoryListAdapter(getActivity(), SplashScreen.categoryListResponseData);
        categoryRecyclerView.setAdapter(categoryListAdapter);
    }
}
