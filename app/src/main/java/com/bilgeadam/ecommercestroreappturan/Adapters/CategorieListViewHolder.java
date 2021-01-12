package com.bilgeadam.ecommercestroreappturan.Adapters;

import android.content.Context;
import androidx.cardview.widget.CardView;import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bilgeadam.ecommercestroreappturan.Fragments.ProductsList;
import com.bilgeadam.ecommercestroreappturan.Activities.MainActivity;
import com.bilgeadam.ecommercestroreappturan.R;


/**
 * Created by AbhiAndroid
 */
public class CategorieListViewHolder extends RecyclerView.ViewHolder {

    ImageView image;
    TextView catName;
    CardView cardView;

    public CategorieListViewHolder(final Context context, View itemView) {
        super(itemView);
        image = (ImageView) itemView.findViewById(R.id.categoryIcon);
        catName = (TextView) itemView.findViewById(R.id.categoryName);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductsList.categoryPosition = getAdapterPosition();
                ((MainActivity) context).loadFragment(new ProductsList(), true);
            }
        });
    }
}
