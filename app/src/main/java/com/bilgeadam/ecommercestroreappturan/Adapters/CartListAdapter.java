package com.bilgeadam.ecommercestroreappturan.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.recyclerview.widget.RecyclerView;

import com.bilgeadam.ecommercestroreappturan.Config;
import com.bilgeadam.ecommercestroreappturan.Fragments.CartProductDetail;
import com.bilgeadam.ecommercestroreappturan.Fragments.MyCartList;
import com.bilgeadam.ecommercestroreappturan.MVP.AddToCartResponse;
import com.bilgeadam.ecommercestroreappturan.MVP.Product;
import com.bilgeadam.ecommercestroreappturan.Activities.MainActivity;
import com.bilgeadam.ecommercestroreappturan.R;
import com.bilgeadam.ecommercestroreappturan.Retrofit.Api;
import com.bilgeadam.ecommercestroreappturan.util.Database;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by AbhiAndroid
 */
public class CartListAdapter extends RecyclerView.Adapter<CartListViewHolder> {
    Context context;
   // List<Product> productList;
    List<Product> productList;
    double totalAmount = 0f, amountPayable;
    public static double totalAmountPayable = 0f;
    public static double totalPrice=0f;
    public static double totalTax;
    double tax=0f;
    //public CartListAdapter(Context context, List<Product> productList) {
    public CartListAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
        CartProductDetail.productList = productList;
        for(int position =0;position<productList.size();position++){
            totalAmount = totalAmount + (productList.get(position).getPrice() *
                    productList.get(position).getQuantity());

        }
        totalPrice = totalAmount;
    }

    @Override
    public CartListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_list_items, null);
        CartListViewHolder CartListViewHolder = new CartListViewHolder(context, view, productList);
        return CartListViewHolder;
    }

    @Override
    public void onBindViewHolder(final CartListViewHolder holder, final int position) {
        if (position == productList.size() - 1) {
            holder.totalAmount.setVisibility(View.VISIBLE);
            holder.txtGurantee.setText(Html.fromHtml(context.getResources().getString(R.string.secure_payment_text)));

            holder.textViews.get(0).setText(" (" + MyCartList.productsData.size() + " Urun fiyatı)");
            holder.textViews.get(1).setText(MainActivity.currency + " " + totalAmount);
            //if (MyCartList.cartistResponseData.getShipping().length()>0) {

              //  holder.textViews.get(2).setText(MainActivity.currency + " " + MyCartList.cartistResponseData.getShipping());
              //   amountPayable = totalAmount +
                      //  Double.parseDouble(MyCartList.cartistResponseData.getShipping());
          //  }else {
                amountPayable=totalAmount;
               // holder.textViews.get(2).setText(MainActivity.currency + " 0.0");

           // }
            if (MyCartList.productsData.get(position).getTax()>0) {
                 tax = (totalAmount / 100) * MyCartList.productsData.get(position).getTax();
                holder.textViews.get(5).setText("KDV: (" + MyCartList.productsData.get(position).getTax() + "%)");
           }
            totalTax = tax;
           // tax = Double.valueOf(String.format("%.2f", tax));

           // Log.d("floatTax", tax + "");

            holder.textViews.get(3).setText(String.format("%.2f", tax));
            //holder.textViews.get(3).setText(MainActivity.currency + " " + tax);
            holder.textViews.get(4).setText(MainActivity.currency + " " + (String.format("%.2f", (amountPayable + tax))));
            totalAmountPayable = (amountPayable + tax);
           // Log.d("totalAmountPayable", totalAmountPayable);
        } else
            holder.totalAmount.setVisibility(View.GONE);

        holder.productName1.setText(productList.get(position).getName());
        holder.price1.setText(MainActivity.currency + " " + productList.get(position).getPrice());
        holder.tquantity.setText(productList.get(position).getQuantity()+"");
        try {
            Picasso.with(context)
                    .load(productList.get(position).getImage())
                    .resize(Integer.parseInt(context.getResources().getString(R.string.cartImageWidth)),Integer.parseInt(context.getResources().getString(R.string.cartImageWidth)))
                    .placeholder(R.drawable.defaultimage)
                    .into(holder.image1);
        } catch (Exception e) {
        }

       // if (!productList.get(position).getSize().equalsIgnoreCase("")) {
          //  Log.d("size", productList.get(position).getSize());
            //holder.size.setText("Size: " + productList.get(position).getSize());
            //holder.size.setVisibility(View.VISIBLE);
       // } else {
         //   holder.size.setVisibility(View.GONE);
        //}
        //if (!productList.get(position).getProductColor().equalsIgnoreCase("")) {
          //  Log.d("color", productList.get(position).getProductColor());
            //holder.color.setText("Color: " + productList.get(position).getProductColor());
           // holder.color.setVisibility(View.VISIBLE);
       // } else {
          //  holder.color.setVisibility(View.GONE);
        //}
        try {
            double discountPercentage = productList.get(position).getOldPrice() - productList.get(position).getPrice();
            Log.d("percentage", discountPercentage + "");
            discountPercentage = (discountPercentage / productList.get(position).getOldPrice()) * 100;
            if ((int) Math.round(discountPercentage) > 0)

            {
                holder.discountPercentage1.setText(((int) Math.round(discountPercentage) + "% indirim!"));
            }
            holder.actualPrice1.setText(MainActivity.currency + " " + productList.get(position).getOldPrice());
            holder.actualPrice1.setPaintFlags(holder.actualPrice1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        } catch (Exception e) {
        }
        holder.itemView.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
               // CartProductDetail.productList.clear();
               // ProductDetail.productList.addAll(productList);
                CartProductDetail productDetail = new CartProductDetail();
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                bundle.putInt("ProductID", productList.get(position).getProductID());
                productDetail.setArguments(bundle);
                ((MainActivity) context).loadFragment(productDetail, true);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                //updateCart(position, Integer.valueOf(holder.tquantity.getText().toString()));
                updateCartSQLite(String.valueOf(MyCartList.productsData.get(position).getProductID()));

            }
        });
        final PopupMenu popupMenu = new PopupMenu(context, holder.tquantity);
        popupMenu.getMenuInflater().

                inflate(R.menu.textview_popup_menu, popupMenu.getMenu());

        for (int i = 1; i <= productList.get(position).getLimit(); i++)
        {
            popupMenu.getMenu().add(i + "");
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()

        {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {


                    holder.tquantity.setText(Integer.valueOf(menuItem.getTitle().toString())+"");
                   // updateQuantityCart(position, Integer.valueOf(menuItem.getTitle().toString()));
                updateQuantityCartSQLite(String.valueOf(MyCartList.productsData.get(position).getProductID()),
                        menuItem.getTitle().toString());

                return false;
            }
        });
        holder.tquantity.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                popupMenu.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    private void updateCart( int position,  double quantity) {

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Lütfen bekleyiniz!");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Api.getClient2().updateCart(
               // productList.get(position).getProductId(),
                String.valueOf(productList.get(position).getProductID()),
                quantity,
                new Callback<AddToCartResponse>() {
                    @Override
                    public void success(AddToCartResponse addToCartResponse, Response response) {
                        progressDialog.dismiss();
                        Log.d("addToCartResponse", addToCartResponse.getSuccess() + "");
                        if (addToCartResponse.getSuccess().equalsIgnoreCase("true")) {
                            Config.getCartList(context,true);
                            ((MainActivity) context).loadFragment(new MyCartList(), false);

                         //   Config.showCustomAlertDialog(context,
                                   // "Your Cart status",
                                   // addToCartResponse.getMessage(),
                                  //  SweetAlertDialog.SUCCESS_TYPE);
                        } else {

                            Config.showCustomAlertDialog(context,
                                    "Sepetinizi durumu",
                                    addToCartResponse.getMessage(),
                                    SweetAlertDialog.NORMAL_TYPE);
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        progressDialog.dismiss();

                        Log.e("error", error.toString());
                    }
                });
    }

    private void updateCartSQLite( String position) {

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Lütfen bekleyiniz!");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Database db = new Database(context);
        db.delete(position);
        Config.sqliteCartList = db.userCartList();
        progressDialog.dismiss();

        Config.getCartListSQLite(context,true);
        ((MainActivity) context).loadFragment(new MyCartList(), false);





    }

    private void updateQuantityCart( int position,  int quantity) {

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Lütfen bekleyiniz!");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Api.getClient2().updateQuantityCart(
                // productList.get(position).getProductId(),
                String.valueOf(productList.get(position).getProductID()),
                Double.valueOf(quantity),
                new Callback<AddToCartResponse>() {
                    @Override
                    public void success(AddToCartResponse addToCartResponse, Response response) {
                        progressDialog.dismiss();
                        Log.d("addToCartResponse", addToCartResponse.getSuccess() + "");
                        if (addToCartResponse.getSuccess().equalsIgnoreCase("true")) {
                            Config.getCartList(context,true);
                            ((MainActivity) context).loadFragment(new MyCartList(), false);

                            //   Config.showCustomAlertDialog(context,
                            // "Your Cart status",
                            // addToCartResponse.getMessage(),
                            //  SweetAlertDialog.SUCCESS_TYPE);
                        } else {

                            Config.showCustomAlertDialog(context,
                                    "Sepetinizin durumu",
                                    addToCartResponse.getMessage(),
                                    SweetAlertDialog.NORMAL_TYPE);
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        progressDialog.dismiss();

                        Log.e("error", error.toString());
                    }
                });
    }


    private void updateQuantityCartSQLite( String position,  String quantity) {

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Lütfen bekleyiniz!");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Database db = new Database(context);
        db.cartProductUpdate(position,quantity);
        Config.sqliteCartList = db.userCartList();
        progressDialog.dismiss();

        Config.getCartListSQLite(context,true);
        ((MainActivity) context).loadFragment(new MyCartList(), false);

    }
}
