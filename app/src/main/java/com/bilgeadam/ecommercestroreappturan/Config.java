package com.bilgeadam.ecommercestroreappturan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

import com.bilgeadam.ecommercestroreappturan.Activities.Login;
import com.bilgeadam.ecommercestroreappturan.Activities.MainActivity;
import com.bilgeadam.ecommercestroreappturan.Adapters.CartListAdapter;
import com.bilgeadam.ecommercestroreappturan.MVP.AddOrderDetailResponse;
import com.bilgeadam.ecommercestroreappturan.MVP.AddOrderResponse;
import com.bilgeadam.ecommercestroreappturan.MVP.CartItemDTOResponse;
import com.bilgeadam.ecommercestroreappturan.PaymentIntegrationMethods.OrderConfirmed;
import com.bilgeadam.ecommercestroreappturan.Activities.SignUp;
import com.bilgeadam.ecommercestroreappturan.Fragments.ChoosePaymentMethod;
import com.bilgeadam.ecommercestroreappturan.Fragments.MyCartList;
import com.bilgeadam.ecommercestroreappturan.Retrofit.Api;
import com.bilgeadam.ecommercestroreappturan.util.Database;

import java.util.ArrayList;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Config {
    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PAYPAL_CLIENT_ID = "your_paypal_id";
    // id to handle the notification in the notification tray
    public static final String SHARED_PREF = "ah_firebase";
    public static ArrayList<HashMap<String, String>> sqliteCartList;

    public static void moveTo(Context context, Class targetClass) {
        Intent intent = new Intent(context, targetClass);
        context.startActivity(intent);
    }
    public static boolean validateEmail(EditText editText,Context context) {
        String email = editText.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            editText.setError(context.getString(R.string.err_msg_email));
            editText.requestFocus();
            return false;
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public static void showCustomAlertDialog(Context context, String title, String msg,int type) {
        SweetAlertDialog alertDialog = new SweetAlertDialog(context, type);
        alertDialog.setTitleText(title);

        if (msg.length() > 0)
            alertDialog.setContentText(msg);
        alertDialog.show();
        Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
    }

    public static void showLoginCustomAlertDialog(final Context context, String title, String msg, int type) {
        SweetAlertDialog alertDialog = new SweetAlertDialog(context, type);
        alertDialog.setTitleText(title);
        alertDialog.setCancelText("Giriş Yap");
        alertDialog.setCancelClickListener( new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                Config.moveTo(context, Login.class);

            }
        });
        alertDialog.setConfirmText("Kayıt Ol");
        alertDialog.setConfirmClickListener( new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                Config.moveTo(context, SignUp.class);

            }
        });
        if (msg.length() > 0)
            alertDialog.setContentText(msg);
        alertDialog.show();
        Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        Button btn1 = (Button) alertDialog.findViewById(R.id.cancel_button);
        btn1.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));

    }

    public static void getCartList(final Context context, final boolean b) {
        if (b)
            MainActivity.progressBar.setVisibility(View.VISIBLE);
        MainActivity.cartCount.setVisibility(View.GONE);

        //Api.getClient2().getCartList(MainActivity.userId, new Callback<CartistResponse>() {
        Api.getClient2().getCartList(MainActivity.userId, new Callback<CartItemDTOResponse>() {
            @Override
            public void success(CartItemDTOResponse cartItemDTOResponse, Response response) {
                MainActivity.progressBar.setVisibility(View.GONE);
                try {
                    if (cartItemDTOResponse.getCartItemDTOList().size() <= 0) {
                        MainActivity.cartCount.setVisibility(View.GONE);
                    } else {
                        MainActivity.cartCount.setText(cartItemDTOResponse.getCartItemDTOList().size() + "");
                        if (!b) {
                            Log.d("equals", "equals");
                            MainActivity.cartCount.setVisibility(View.GONE);

                        } else {
                            MainActivity.cartCount.setVisibility(View.VISIBLE);

                        }
                    }
                } catch (Exception e) {
                    MainActivity.cartCount.setVisibility(View.GONE);

                }

            }

            @Override
            public void failure(RetrofitError error) {
                MainActivity.progressBar.setVisibility(View.GONE);
            }
        });
    }

    public static void getCartListSQLite(final Context context, final boolean b) {
        if (b)


            // urada db de cart listesi çekilecek
            MainActivity.progressBar.setVisibility(View.GONE);
        Database db = new Database(context);
        sqliteCartList  = db.userCartList();
                try {
                    if (sqliteCartList.size() <= 0) {
                        MainActivity.cartCount.setVisibility(View.GONE);
                    } else {

                        MainActivity.cartCount.setText(sqliteCartList.size() + "");
                        if (!b) {
                            Log.d("equals", "equals");
                            MainActivity.cartCount.setVisibility(View.GONE);

                        } else {
                            MainActivity.cartCount.setVisibility(View.VISIBLE);

                        }
                    }
                } catch (Exception e) {
                    MainActivity.cartCount.setVisibility(View.GONE);

                }

    }

    public static void addOrder(final Context context, String transactionId, String paymentMode) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Sipariş kaydediliyor...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Api.getClient2().addOrder(MainActivity.userId,
                ChoosePaymentMethod.paymentTypeId,
                String.valueOf(CartListAdapter.totalPrice),
                String.valueOf(CartListAdapter.totalTax),
                String.valueOf(CartListAdapter.totalAmountPayable),
                new Callback<AddOrderResponse>() {
                    @Override
                    public void success(AddOrderResponse addOrderDetail, Response response) {


                        Database db = new Database(context);
                        for (int i = 0; i <  MyCartList.productsData.size(); i++) {

                            addOrderDetailOne(context,String.valueOf(addOrderDetail.getOrderId()),
                                    String.valueOf(MyCartList.productsData.get(i).getProductID()),
                                    String.valueOf(MyCartList.productsData.get(i).getQuantity()),
                                            String.valueOf(MyCartList.productsData.get(i).getPrice()));

                        }

                        for (int i = 0; i <  MyCartList.productsData.size(); i++) {
                            db.delete(String.valueOf(MyCartList.productsData.get(i).getProductID()));
                        }

                        Config.getCartListSQLite(context,true);

                        Intent intent = new Intent(context, OrderConfirmed.class);
                        context.startActivity(intent);
                        ((Activity) context).finishAffinity();


                        progressDialog.dismiss();

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        progressDialog.dismiss();
                        ((Activity) context).finish();
                    }
                });
    }

    public static void addOrderDetail(final Context context,int OrderId) {
        Api.getClient2().addOrderDetail(String.valueOf(OrderId),
                MyCartList.productsData,
                new Callback<AddOrderDetailResponse>() {
                    @Override
                    public void success(AddOrderDetailResponse addOrderDetailResponse, Response response) {

                        Database db = new Database(context);
                        for (int i = 0; i <  MyCartList.productsData.size(); i++) {
                            db.delete(String.valueOf(MyCartList.productsData.get(i).getProductID()));
                        }

                        Config.getCartListSQLite(context,true);

                        Intent intent = new Intent(context, OrderConfirmed.class);
                        context.startActivity(intent);
                        ((Activity) context).finishAffinity();
                    }

                    @Override
                    public void failure(RetrofitError error) {

                        ((Activity) context).finish();
                    }
                });
    }

    public static void addOrderDetailOne(final Context context,String OrderId,String ProductId, String Quantity, String Price) {
        Api.getClient2().addOrderDetailOne(OrderId,
                ProductId,
                Quantity,
                Price,
                new Callback<AddOrderDetailResponse>() {
                    @Override
                    public void success(AddOrderDetailResponse addOrderDetailResponse, Response response) {


                    }

                    @Override
                    public void failure(RetrofitError error) {

                        ((Activity) context).finish();
                    }
                });
    }
}