package com.bilgeadam.ecommercestroreappturan.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bilgeadam.ecommercestroreappturan.MVP.Product;
import com.bilgeadam.ecommercestroreappturan.Activities.MainActivity;
import com.bilgeadam.ecommercestroreappturan.R;
import com.squareup.picasso.Picasso;
import com.bilgeadam.ecommercestroreappturan.Fragments.ProductDetail;

import java.util.List;


/**
 * Created by AbhiAndroid
 */
public class SearchProductListAdapter extends RecyclerView.Adapter<HomeProductsViewHolder> {
    Context context;
    List<Product> productList;

    public SearchProductListAdapter(Context context, List<Product> productList) {
        this.context = context;
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


        holder.cardView.setVisibility(View.GONE);
        holder.cardView1.setVisibility(View.VISIBLE);
        holder.productName1.setText(productList.get(position).getName());
        holder.price1.setText(MainActivity.currency + " " + productList.get(position).getPrice());
        try {
            Picasso.with(context)
                    .load(productList.get(position).getImage())
                    .placeholder(R.drawable.defaultimage)
                    .resize(Integer.parseInt(context.getResources().getString(R.string.targetProductImageWidth1)),Integer.parseInt(context.getResources().getString(R.string.targetProductImageHeight)))
                    .into(holder.image1);
        } catch (Exception e) {
        }
        try {

            double discountPercentage = productList.get(position).getOldPrice() - productList.get(position).getPrice();
           // Log.d("percentage", discountPercentage + "");
            discountPercentage = (discountPercentage / productList.get(position).getPrice()) * 100;
            if ((int) Math.round(discountPercentage) > 0) {
                holder.discountPercentage1.setText(((int) Math.round(discountPercentage) + "% indirim"));
            }
            holder.actualPrice1.setText(MainActivity.currency + " " + productList.get(position).getOldPrice());
            holder.actualPrice1.setPaintFlags(holder.actualPrice1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        }catch (Exception e){}
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductDetail.productList.clear();
                ProductDetail.productList.addAll(productList);
                ProductDetail productDetail = new ProductDetail();
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                productDetail.setArguments(bundle);
                ((MainActivity) context).loadFragment(productDetail, true);
            }
        });


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

}
