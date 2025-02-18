package com.bilgeadam.ecommercestroreappturan.Adapters;

import android.content.Context;
import android.os.Bundle;import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bilgeadam.ecommercestroreappturan.Fragments.Home;
import com.bilgeadam.ecommercestroreappturan.Fragments.ProductDetail;
import com.bilgeadam.ecommercestroreappturan.MVP.Product;
import com.bilgeadam.ecommercestroreappturan.Activities.MainActivity;
import com.bilgeadam.ecommercestroreappturan.R;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by AbhiAndroid
 */
public class HomeProductsAdapter extends RecyclerView.Adapter<HomeProductsViewHolder> {
    Context context;
    List<Product> productList;
    int i, parentPosition;

    public HomeProductsAdapter(Context context, List<Product> productList, int i, int position) {
        this.context = context;
        this.i = i;
        this.parentPosition = position;
        this.productList = productList;
    }

    @Override
    public HomeProductsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_products_list_items, null);
        HomeProductsViewHolder homeProductsViewHolder = new HomeProductsViewHolder(context, view, productList);
        return homeProductsViewHolder;
    }

    @Override
    public void onBindViewHolder(final HomeProductsViewHolder holder, final int position) {

        //if (this.parentPosition % 2 == 0) {
            holder.cardView.setVisibility(View.VISIBLE);
            holder.cardView1.setVisibility(View.GONE);
       // }

        //else {
           // holder.cardView.setVisibility(View.GONE);
           // holder.cardView1.setVisibility(View.VISIBLE);
        //}
        holder.productName.setText(productList.get(position).getName());
        //holder.price.setText(MainActivity.currency + " " + productList.get(position).getSellprice());
        holder.price.setText(MainActivity.currency + " " + productList.get(position).getPrice());

        try {
            Picasso.with(context)
                    .load(productList.get(position).getImage())
                   // .load(productList.get(position).getProductMedias().get(0).getMedia_url())
                   // .load(productList.get(position).getImages().get(0))
                    //.load(productList.get(position).getImage())
                    .placeholder(R.drawable.defaultimage)
                    .resize(Integer.parseInt(context.getResources().getString(R.string.targetProductImageWidth)),Integer.parseInt(context.getResources().getString(R.string.targetProductImageHeight)))
                    .into(holder.image);
        } catch (Exception e) {
            Log.d("exception", e.toString());
        }
        holder.productName1.setText(productList.get(position).getName());
        holder.price1.setText(MainActivity.currency + " " + productList.get(position).getPrice());
        try {
            Picasso.with(context)
                    .load(productList.get(position).getProductMedias().get(0).getMedia_url())
                    //.load(productList.get(position).getImage())
                    .resize(Integer.parseInt(context.getResources().getString(R.string.targetProductImageWidth1)),Integer.parseInt(context.getResources().getString(R.string.targetProductImageHeight)))
                    .placeholder(R.drawable.defaultimage)
                    .into(holder.image1);
        } catch (Exception e) {
            Log.d("exception", e.toString());
        }
        try {
           // double discountPercentage = Integer.parseInt(productList.get(position).getMrpprice()) - Integer.parseInt(productList.get(position).getSellprice());
           // Log.d("percentage", discountPercentage + "");
           // discountPercentage = (discountPercentage / Integer.parseInt(productList.get(position).getMrpprice())) * 100;
           // if ((int) Math.round(discountPercentage) > 0) {
              //  holder.discountPercentage.setText(((int) Math.round(discountPercentage) + "% Off"));
              //  holder.discountPercentage1.setText(((int) Math.round(discountPercentage) + "% Off"));
          //  }
            //Log.d("mrptextsize", productList.get(position).getMrpprice().length() + "");
           // holder.actualPrice.setText(MainActivity.currency + " " + productList.get(position).getMrpprice());
           // holder.actualPrice.setPaintFlags(holder.actualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
           // holder.actualPrice1.setPaintFlags(holder.actualPrice1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
          //  holder.actualPrice1.setText(MainActivity.currency + " " + productList.get(position).getMrpprice());
        } catch (Exception e) {

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Home.swipeRefreshLayout.isRefreshing()) {
                    ProductDetail.productList.clear();
                    ProductDetail.productList.addAll(productList);
                    ProductDetail productDetail = new ProductDetail();
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", position);
                    productDetail.setArguments(bundle);
                    ((MainActivity) context).loadFragment(productDetail, true);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return i;
    }

}
