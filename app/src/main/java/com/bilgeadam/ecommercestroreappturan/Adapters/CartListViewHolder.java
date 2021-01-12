package com.bilgeadam.ecommercestroreappturan.Adapters;

import android.content.Context;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bilgeadam.ecommercestroreappturan.MVP.Product;
import com.bilgeadam.ecommercestroreappturan.R;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class CartListViewHolder extends RecyclerView.ViewHolder {

    ImageView image1;
    ImageView delete;
    TextView productName1, price1, actualPrice1, discountPercentage1, tquantity, size, color, txtGurantee;
    CardView cardView1;
    @BindView(R.id.totalAmount)
    LinearLayout totalAmount;
    @BindViews({R.id.txtPrice, R.id.price, R.id.delivery,  R.id.tax,  R.id.amountPayable,  R.id.txtTax})
    List<TextView> textViews;

    public CartListViewHolder(final Context context, View itemView, List<Product> productList) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        image1 = (ImageView) itemView.findViewById(R.id.productImage1);
        delete = (ImageView) itemView.findViewById(R.id.delete);
        productName1 = (TextView) itemView.findViewById(R.id.productName1);
        size = (TextView) itemView.findViewById(R.id.size);
        color = (TextView) itemView.findViewById(R.id.color);
        price1 = (TextView) itemView.findViewById(R.id.price1);
        tquantity = (TextView) itemView.findViewById(R.id.tquantity);
        txtGurantee = (TextView) itemView.findViewById(R.id.txtGurantee);
        actualPrice1 = (TextView) itemView.findViewById(R.id.actualPrice1);
        discountPercentage1 = (TextView) itemView.findViewById(R.id.discountPercentage1);
        cardView1 = (CardView) itemView.findViewById(R.id.cardView1);


    }
}
