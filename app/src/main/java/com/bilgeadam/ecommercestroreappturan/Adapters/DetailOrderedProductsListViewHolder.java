package com.bilgeadam.ecommercestroreappturan.Adapters;

import android.content.Context;import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bilgeadam.ecommercestroreappturan.MVP.Product;
import com.bilgeadam.ecommercestroreappturan.R;

import java.util.List;

import butterknife.ButterKnife;

public class DetailOrderedProductsListViewHolder extends RecyclerView.ViewHolder {

    ImageView image1;
    TextView productName1,size,color,qty,price;

    public DetailOrderedProductsListViewHolder(final Context context, View itemView, List<Product> productList) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        image1 = (ImageView) itemView.findViewById(R.id.productImage1);
        productName1 = (TextView) itemView.findViewById(R.id.productName1);
        size = (TextView) itemView.findViewById(R.id.size);
        color = (TextView) itemView.findViewById(R.id.color);
        qty = (TextView) itemView.findViewById(R.id.quantity);
        price = (TextView) itemView.findViewById(R.id.price);



    }
}
