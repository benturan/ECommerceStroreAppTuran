package com.bilgeadam.ecommercestroreappturan.Fragments;


import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.bilgeadam.ecommercestroreappturan.Activities.AccountVerification;
import com.bilgeadam.ecommercestroreappturan.Common;
import com.bilgeadam.ecommercestroreappturan.Config;
import com.bilgeadam.ecommercestroreappturan.Activities.Login;
import com.bilgeadam.ecommercestroreappturan.MVP.UserProfileResponse;
import com.bilgeadam.ecommercestroreappturan.Activities.MainActivity;
import com.bilgeadam.ecommercestroreappturan.R;
import com.bilgeadam.ecommercestroreappturan.Retrofit.Api;
import com.bilgeadam.ecommercestroreappturan.Activities.SignUp;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MyProfile extends Fragment {

    View view;
    @BindViews({R.id.etAd, R.id.etSoyad,R.id.etEmail, R.id.etPassword, R.id.etTelefon,})
    List<EditText> editTexts;
    UserProfileResponse userProfileResponseData;
    @BindView(R.id.submitBtn)
    Button submitBtn;
    @BindViews({R.id.male, R.id.female})
    List<CircleImageView> circleImageViews;
    String gender = "";
    @BindView(R.id.profileLayout)
    LinearLayout profileLayout;
    @BindView(R.id.loginLayout)
    LinearLayout loginLayout;
    @BindView(R.id.logout)
    Button logout;

    @BindView(R.id.verifyEmailLayout)
    LinearLayout verifyEmailLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        ButterKnife.bind(this, view);
        MainActivity.title.setText("Profilim");

        if (!MainActivity.userId.equalsIgnoreCase("")) {
            getUserProfileData();
        } else {
            profileLayout.setVisibility(View.INVISIBLE);
            loginLayout.setVisibility(View.VISIBLE);
        }
        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(view);

            }
        });
        return view;
    }
    protected void hideKeyboard(View view)
    {
        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    private void setUserProfileData() {
//        logout.setText("Logout ( "+userProfileResponseData.get);
        editTexts.get(0).setText(userProfileResponseData.getName());
        editTexts.get(1).setText(userProfileResponseData.getSurname());
        editTexts.get(2).setText(userProfileResponseData.getEmail());
        editTexts.get(3).setText(userProfileResponseData.getPassword());
        editTexts.get(4).setText(userProfileResponseData.getTelephone());
        try {
            if (userProfileResponseData.getGender().equalsIgnoreCase("Kadın")) {
                circleImageViews.get(0).setImageResource(R.drawable.male_unselect);
                circleImageViews.get(1).setImageResource(R.drawable.female_select);
                gender = "kadın";
            } else if (userProfileResponseData.getGender().equalsIgnoreCase("Erkek")) {

                circleImageViews.get(0).setImageResource(R.drawable.male_select);
                circleImageViews.get(1).setImageResource(R.drawable.female_unselect);
                gender = "erkek";
            }
        } catch (Exception e) {

        }
    }

    @OnClick({R.id.male, R.id.female, R.id.submitBtn, R.id.logout, R.id.loginNow, R.id.txtSignUp, R.id.verfiyNow})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.male:
                circleImageViews.get(0).setImageResource(R.drawable.male_select);
                circleImageViews.get(1).setImageResource(R.drawable.female_unselect);
                gender = "erkek";
                break;
            case R.id.female:
                circleImageViews.get(0).setImageResource(R.drawable.male_unselect);
                circleImageViews.get(1).setImageResource(R.drawable.female_select);
                gender = "kadın";
                break;
            case R.id.submitBtn:
                if (gender.equalsIgnoreCase("")) {
                    Config.showCustomAlertDialog(getActivity(), "Profilinizi güncellemek için lütfen cinsiyetinizi seçin", "",
                            SweetAlertDialog.ERROR_TYPE);
                } else if (validate(editTexts.get(0))
                        && validate(editTexts.get(1))
                        && validate(editTexts.get(2))
                        && validate(editTexts.get(3))
                        && validate(editTexts.get(4)))
                    updateProfile();
                break;
            case R.id.logout:
                logout();
                break;
            case R.id.loginNow:
                Config.moveTo(getActivity(), Login.class);
                break;
            case R.id.txtSignUp:
                Config.moveTo(getActivity(), SignUp.class);
                break;

            case R.id.verfiyNow:
                Config.moveTo(getActivity(), AccountVerification.class);
                break;
        }
    }

    private void logout() {

        final SweetAlertDialog alertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
        alertDialog.setTitleText("Çıkış yapmak istediğinizden emin misiniz?");
        alertDialog.setCancelText("Vazgeç");
        alertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                alertDialog.dismissWithAnimation();
            }
        });
        alertDialog.show();
        Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
        btn.setBackground(getResources().getDrawable(R.drawable.custom_dialog_button));
        btn.setText("Çıkış Yap");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.saveUserData(getActivity(), "email", "");
                Common.saveUserData(getActivity(), "userId", "");
                Config.moveTo(getActivity(), Login.class);
                getActivity().finishAffinity();

            }
        });
    }

    private boolean validate(EditText editText) {
        if (editText.getText().toString().trim().length() > 0) {
            return true;
        }
        editText.setError("Lütfen tüm alanları doldurunuz!");
        editText.requestFocus();
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
     //   Config.getCartList(getActivity(), true);

    }


    public void getUserProfileData() {
        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Yükleniyor..");
        pDialog.setCancelable(false);
        pDialog.show();
        Api.getClient2().getUserProfile(
                MainActivity.userId, new Callback<UserProfileResponse>() {
                    @Override
                    public void success(UserProfileResponse userProfileResponse, Response response) {
                        userProfileResponseData = userProfileResponse;
                        pDialog.dismiss();
                        if (userProfileResponse.getMessage().equalsIgnoreCase("false")) {
                            profileLayout.setVisibility(View.INVISIBLE);
                            verifyEmailLayout.setVisibility(View.VISIBLE);
                        } else
                            setUserProfileData();


                    }

                    @Override
                    public void failure(RetrofitError error) {
                        pDialog.dismiss();

                    }
                });
    }

    public void updateProfile() {
        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Güncelleniyor...");
        pDialog.setCancelable(false);

        pDialog.show();
        Api.getClient2().updateProfile(
                MainActivity.userId,
                editTexts.get(0).getText().toString().trim(),
                editTexts.get(1).getText().toString().trim(),
                editTexts.get(2).getText().toString().trim(),
                editTexts.get(3).getText().toString().trim(),
                editTexts.get(4).getText().toString().trim(),
                gender,
                new Callback<UserProfileResponse>() {
                    @Override
                    public void success(UserProfileResponse userProfileResponse, Response response) {
                        pDialog.dismiss();
                        if (userProfileResponse.getMessage().equalsIgnoreCase("true")) {
                            Config.showCustomAlertDialog(getActivity(),
                                    "Profil Durumu",
                                    "Profil güncellendi",
                                    SweetAlertDialog.SUCCESS_TYPE);
                        } else {
                            Toast.makeText(getActivity(), "Sorun oluştu!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        pDialog.dismiss();

                    }
                });
    }

}
