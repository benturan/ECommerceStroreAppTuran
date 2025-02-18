package com.bilgeadam.ecommercestroreappturan.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bilgeadam.ecommercestroreappturan.Config;
import com.bilgeadam.ecommercestroreappturan.Fragments.MyWishList;
import com.bilgeadam.ecommercestroreappturan.Fragments.ProductDetail;
import com.bilgeadam.ecommercestroreappturan.MVP.AddToWishlistResponse;
import com.bilgeadam.ecommercestroreappturan.MVP.Product;
import com.bilgeadam.ecommercestroreappturan.Activities.MainActivity;
import com.bilgeadam.ecommercestroreappturan.R;
import com.bilgeadam.ecommercestroreappturan.Retrofit.Api;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by AbhiAndroid
 */
public class WishListAdapter extends RecyclerView.Adapter<HomeProductsViewHolder> {
    Context context;
    List<Product> productList;

    public WishListAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public HomeProductsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.wish_list_items, null);
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
                    //.load(productList.get(position).getImages().get(0))
                    .load(productList.get(position).getImage())
                    .resize(Integer.parseInt(context.getResources().getString(R.string.targetProductImageWidth1)),Integer.parseInt(context.getResources().getString(R.string.targetProductImageHeight)))
                    .placeholder(R.drawable.defaultimage)
                    .into(holder.image1);
        } catch (Exception e) {
        }
        try {
            double discountPercentage = productList.get(position).getOldPrice() - productList.get(position).getPrice();
            Log.d("percentage", discountPercentage + "");
            discountPercentage = (discountPercentage / productList.get(position).getPrice()) * 100;
            if ((int) Math.round(discountPercentage) > 0) {
                holder.discountPercentage1.setText(((int) Math.round(discountPercentage) + "% Off"));
            }
            holder.actualPrice1.setText(MainActivity.currency + " " + productList.get(position).getPrice());
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
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteToWishList(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    private void addToWishList(final int position) {
        final SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Yükleniyor...");
        pDialog.setCancelable(false);
        pDialog.show();
        Api.getClient2().addToWishList(String.valueOf(productList.get(position).getProductID()),
                MainActivity.userId,
                new Callback<AddToWishlistResponse>() {
                    @Override
                    public void success(AddToWishlistResponse addToWishlistResponse, Response response) {
                        pDialog.dismiss();

                        Log.d("addToWishListResponse", addToWishlistResponse.getSuccess() + "");
                        if (addToWishlistResponse.getSuccess().equalsIgnoreCase("true")) {
                            ((MainActivity) context).loadFragment(new MyWishList(), false);
                            Config.showCustomAlertDialog(context,
                                    "Your wishlist status",
                                    addToWishlistResponse.getMessage(),
                                    SweetAlertDialog.SUCCESS_TYPE);
                        }else {
                            Config.showCustomAlertDialog(context,
                                    "Your wishlist status",
                                    addToWishlistResponse.getMessage(),
                                    SweetAlertDialog.NORMAL_TYPE);
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        pDialog.dismiss();

                        Log.e("error", error.toString());
                    }
                });
    }

    private void deleteToWishList(final int position) {
        final SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Yükleniyor...");
        pDialog.setCancelable(false);
        pDialog.show();
        Api.getClient2().deleteToWishList(String.valueOf(productList.get(position).getProductID()),
                MainActivity.userId,
                new Callback<AddToWishlistResponse>() {
                    @Override
                    public void success(AddToWishlistResponse addToWishlistResponse, Response response) {
                        pDialog.dismiss();

                        Log.d("addToWishListResponse", addToWishlistResponse.getSuccess() + "");
                        if (addToWishlistResponse.getSuccess().equalsIgnoreCase("true")) {
                            ((MainActivity) context).loadFragment(new MyWishList(), false);

                        }else {

                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        pDialog.dismiss();

                        Log.e("error", error.toString());
                    }
                });
    }


}
