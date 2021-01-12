package com.bilgeadam.ecommercestroreappturan.Adapters;

import android.content.Context;import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bilgeadam.ecommercestroreappturan.Fragments.MyOrderedProductsDetailPage;
import com.bilgeadam.ecommercestroreappturan.MVP.Product;
import com.bilgeadam.ecommercestroreappturan.R;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by AbhiAndroid
 */
public class DetailOrderProductListAdapter extends RecyclerView.Adapter<DetailOrderedProductsListViewHolder> {
    Context context;
    List<Product> productList;

    public DetailOrderProductListAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public DetailOrderedProductsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.detail_ordered_products_list_items1, null);
        DetailOrderedProductsListViewHolder DetailOrderedProductsListViewHolder = new DetailOrderedProductsListViewHolder(context, view, productList);
        return DetailOrderedProductsListViewHolder;
    }

    @Override
    public void onBindViewHolder(final DetailOrderedProductsListViewHolder holder, final int position) {

        holder.productName1.setText(productList.get(position).getName());

       // if (!productList.get(position).getSize().equalsIgnoreCase("")) {
           // Log.d("size", productList.get(position).getSize());
           // holder.size.setText("Size: " + productList.get(position).getSize());
           // holder.size.setVisibility(View.VISIBLE);
       // } else {
          //  holder.size.setVisibility(View.GONE);
        //}
       // if (!productList.get(position).getProductColor().equalsIgnoreCase("")) {
         //   Log.d("color", productList.get(position).getProductColor());
           // holder.color.setText("Color: " + productList.get(position).getProductColor());
           // holder.color.setVisibility(View.VISIBLE);
       // } else {
           // holder.color.setVisibility(View.INVISIBLE);
        //}

       // holder.qty.setText("Adet: " + productList.get(position).getQuantity());
        holder.price.setText("Fiyat: " + MyOrderedProductsDetailPage.currency + " " + productList.get(position).getPrice());
        try {
            Picasso.with(context)
                    .load(productList.get(position).getImage())
                    .resize(Integer.parseInt(context.getResources().getString(R.string.targetProductImageWidth1)),Integer.parseInt(context.getResources().getString(R.string.targetProductImageHeight)))
                    .placeholder(R.drawable.defaultimage)
                    .into(holder.image1);
        } catch (Exception e) {
        }

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

}
