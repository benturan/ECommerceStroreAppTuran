package com.bilgeadam.ecommercestroreappturan.Adapters;

import android.content.Context;
import androidx.cardview.widget.CardView;import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bilgeadam.ecommercestroreappturan.Fragments.Home;
import com.bilgeadam.ecommercestroreappturan.Fragments.ProductsList;
import com.bilgeadam.ecommercestroreappturan.Activities.MainActivity;
import com.bilgeadam.ecommercestroreappturan.R;


/**
 * Created by AbhiAndroid
 */
public class CategoriesProductsViewHolder extends RecyclerView.ViewHolder {

    TextView catName;
    CardView cardView;
    RecyclerView productsRecyclerView;
    Button viewAll;
    LinearLayout homeCategoryProductLayout;
    RelativeLayout homeCategoryRelativeLayout;

    public CategoriesProductsViewHolder(final Context context, View itemView) {
        super(itemView);
        productsRecyclerView = (RecyclerView) itemView.findViewById(R.id.productsRecyclerView);
        catName = (TextView) itemView.findViewById(R.id.categoryName);
        viewAll = (Button) itemView.findViewById(R.id.viewAll);
        homeCategoryProductLayout = (LinearLayout) itemView.findViewById(R.id.homeCategoryProductLayout);
        homeCategoryRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.homeCategoryRelativeLayout);
        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Home.swipeRefreshLayout.isRefreshing()) {
                    ProductsList.categoryPosition = getAdapterPosition();
                    ((MainActivity) context).loadFragment(new ProductsList(), true);
                }
            }
        });
    }
}
