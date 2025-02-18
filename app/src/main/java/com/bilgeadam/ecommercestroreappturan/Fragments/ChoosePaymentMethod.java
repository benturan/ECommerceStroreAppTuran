package com.bilgeadam.ecommercestroreappturan.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.bilgeadam.ecommercestroreappturan.Activities.Login;
import com.bilgeadam.ecommercestroreappturan.Config;
import com.bilgeadam.ecommercestroreappturan.MVP.UserProfileResponse;
import com.bilgeadam.ecommercestroreappturan.Activities.MainActivity;
import com.bilgeadam.ecommercestroreappturan.PaymentIntegrationMethods.PayPalActivityPayment;
import com.bilgeadam.ecommercestroreappturan.R;
import com.bilgeadam.ecommercestroreappturan.Retrofit.Api;
import com.bilgeadam.ecommercestroreappturan.PaymentIntegrationMethods.StripePaymentIntegration;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ChoosePaymentMethod extends Fragment {

    View view;
    @BindView(R.id.addNewAddressLayout)
    LinearLayout addNewAddressLayout;
    @BindView(R.id.addressCheckBox)
    CheckBox addressCheckBox;
    @BindView(R.id.addNewAddress)
    TextView addNewAddress;
    @BindView(R.id.fillAddress)
    TextView fillAddress;
    @BindView(R.id.paymentMethodsGroup)
    RadioGroup paymentMethodsGroup;
    @BindView(R.id.makePayment)
    Button makePayment;
    String paymentMethod;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.choosePaymentLayout)
    LinearLayout choosePaymentLayout;
    @BindViews({R.id.fullNameEdt, R.id.mobEditText, R.id.cityEditText, R.id.areaEditText, R.id.buildingEditText, R.id.pincodeEditText, R.id.stateEditText, R.id.landmarkEditText,})
    List<EditText> editTexts;
    public static String address, mobileNo,userEmail;
    Intent intent;
    public static String paymentTypeId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        int layout = R.layout.fragment_choose_payment_method;
        view = inflater.inflate(layout, container, false);
        ButterKnife.bind(this, view);
        MainActivity.title.setText("Ödeme yöntemini seçin");
        MainActivity.cart.setVisibility(View.GONE);
        MainActivity.cartCount.setVisibility(View.GONE);
        getUserProfileData();
        addressCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    addNewAddressLayout.setVisibility(View.GONE);
                    addNewAddress.setText("Yeni adres ekle!");

                }
            }
        });
        choosePaymentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(view);

            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MainActivity.cart.setVisibility(View.VISIBLE);
        MainActivity.cartCount.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.addNewAddress, R.id.makePayment, R.id.fillAddress})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addNewAddress:
                addNewAddressLayout.setVisibility(View.VISIBLE);
                addressCheckBox.setChecked(false);
                addNewAddress.setText("Bu adresi kullan!");
                break;
            case R.id.makePayment:
                if (!addressCheckBox.isChecked()) {
                    if (addNewAddressLayout.getVisibility() == View.VISIBLE) {
                        if (validate(editTexts.get(0))
                                && validate(editTexts.get(1))
                                && validate(editTexts.get(2))
                                && validate(editTexts.get(3))
                                && validate(editTexts.get(4))
                                && validate(editTexts.get(5))
                                && validate(editTexts.get(6))) {
                            String s = "";
                            if (editTexts.get(6).getText().toString().trim().length() > 0) {
                                s = ", " + editTexts.get(6).getText().toString().trim();
                            }
                            address = editTexts.get(0).getText().toString().trim()
                                    + ", "
                                    + editTexts.get(4).getText().toString().trim()
                                    + s
                                    + ", " + editTexts.get(3).getText().toString().trim()
                                    + ", " + editTexts.get(2).getText().toString().trim()
                                    + ", " + editTexts.get(6).getText().toString().trim()
                                    + ", " + editTexts.get(5).getText().toString().trim()
                                    + "\n" + editTexts.get(1).getText().toString().trim();
                            mobileNo = editTexts.get(1).getText().toString().trim();
                            moveNext();
                            if(MainActivity.userId.equalsIgnoreCase(""))
                            {
                                Config.showLoginCustomAlertDialog(getActivity(),
                                  "Devam etmek için Giriş Yapmalısınız!",
                                 "Siparişi tamamlamak için Giriş yapmalısınız!",
                                 SweetAlertDialog.WARNING_TYPE);
                            }
                        }
                    } else {
                        Config.showCustomAlertDialog(getActivity(),
                                "Lütfen ödeme yapmak için kayıtlı adresinizi seçin veya yeni bir adres ekleyin!",
                                "",
                                SweetAlertDialog.ERROR_TYPE);
                    }
                } else {
                    moveNext();
                }

                break;
            case R.id.fillAddress:
                ((MainActivity) getActivity()).loadFragment(new MyProfile(), true);
                break;
        }

    }
    protected void hideKeyboard(View view)
    {
        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    private void moveNext() {
        switch (paymentMethodsGroup.getCheckedRadioButtonId()) {
            case R.id.paypal:
                paymentMethod = "paypal";
                paymentTypeId = "1";
                intent = new Intent(getActivity(), PayPalActivityPayment.class);
                startActivity(intent);
                break;
            case R.id.cod:
                paymentMethod = "cod";
                paymentTypeId = "3";
                if (MainActivity.userId.equalsIgnoreCase("")) {

                    Config.showLoginCustomAlertDialog(getActivity(),
                            "Giriş Yap!",
                            "Siparişinizi tamamlamak için lütfen giriş yapın!",
                            SweetAlertDialog.WARNING_TYPE);

                    Config.moveTo(getActivity(), Login.class);

                }
                else {
                    Config.addOrder(getActivity(),
                            "COD",
                            "COD");
                }


                break;
            case R.id.stripe:
                paymentMethod = "stripe";
                paymentTypeId = "2";
                intent = new Intent(getActivity(), StripePaymentIntegration.class);
                startActivity(intent);
                break;
            default:
                paymentMethod = "";
                Config.showCustomAlertDialog(getActivity(),
                        "Ödeme Tipi",
                        "Ödeme yapmak için ödeme yönteminizi seçin",
                        SweetAlertDialog.NORMAL_TYPE);
                break;


        }

        Log.d("paymentMethod", paymentMethod);
    }

    private boolean validate(EditText editText) {
        if (editText.getText().toString().trim().length() > 0) {
            return true;
        }
        editText.setError("Lütfen tüm alanları doldurunuz!");
        editText.requestFocus();
        return false;
    }

    public void getUserProfileData() {
        progressBar.setVisibility(View.VISIBLE);
        Api.getClient2().getUserProfile(
                MainActivity.userId, new Callback<UserProfileResponse>() {
                    @Override
                    public void success(UserProfileResponse userProfileResponse, Response response) {
                        progressBar.setVisibility(View.GONE);
                        userEmail=userProfileResponse.getEmail();
                        String s = "";
                        //if (!userProfileResponse.getLandmark().equalsIgnoreCase("")) {
                           // s = ", " + userProfileResponse.getLandmark();
                      //  }
                       // if (userProfileResponse.getFlat().equalsIgnoreCase("")) {
                          //  addressCheckBox.setChecked(false);
                          //  addressCheckBox.setVisibility(View.GONE);
                          //  fillAddress.setVisibility(View.VISIBLE);
                       // } else {
                            address = userProfileResponse.getName()
                                    + ", "
                                   // + userProfileResponse.getFlat()
                                    + s;
                                   // + ", " + userProfileResponse.getLocality()
                                   // + ", " + userProfileResponse.getCity()
                                    //+ ", " + userProfileResponse.getState()
                                    //+ ", " + userProfileResponse.getPincode()
                                  //  + "\n" + userProfileResponse.getMobile();
                            addressCheckBox.setText(address);
                            mobileNo = userProfileResponse.getTelephone();
                       // }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        progressBar.setVisibility(View.GONE);

                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }
}
