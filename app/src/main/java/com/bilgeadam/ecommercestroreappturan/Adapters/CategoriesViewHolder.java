package com.bilgeadam.ecommercestroreappturan.Adapters;

import android.content.Context;
import androidx.cardview.widget.CardView;import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bilgeadam.ecommercestroreappturan.Fragments.CategoryList;
import com.bilgeadam.ecommercestroreappturan.Fragments.Home;
import com.bilgeadam.ecommercestroreappturan.Fragments.ProductsList;
import com.bilgeadam.ecommercestroreappturan.Activities.MainActivity;
import com.bilgeadam.ecommercestroreappturan.R;


/**
 * Created by AbhiAndroid
 */
public class CategoriesViewHolder extends RecyclerView.ViewHolder {

    ImageView image;
    TextView catName;
    CardView cardView;

    public CategoriesViewHolder(final Context context, View itemView) {
        super(itemView);
        image = (ImageView) itemView.findViewById(R.id.categoryIcon);
        catName = (TextView) itemView.findViewById(R.id.categoryName);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Home.swipeRefreshLayout.isRefreshing())
                    if (getAdapterPosition() == 3) {
                        ((MainActivity) context).loadFragment(new CategoryList(), true);
                    } else {
                        ProductsList.categoryPosition = getAdapterPosition();
                        ((MainActivity) context).loadFragment(new ProductsList(), true);
                    }
            }
        });
    }
}
