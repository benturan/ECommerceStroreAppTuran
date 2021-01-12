package com.bilgeadam.ecommercestroreappturan.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bilgeadam.ecommercestroreappturan.Common;
import com.bilgeadam.ecommercestroreappturan.Config;
import com.bilgeadam.ecommercestroreappturan.MVP.SignUpResponse;
import com.bilgeadam.ecommercestroreappturan.Retrofit.Api;
import com.bilgeadam.ecommercestroreappturan.R;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Login extends AppCompatActivity {


    @BindViews({R.id.email, R.id.password})
    List<EditText> editTexts;
    @BindView(R.id.loginLinearLayout)
    LinearLayout loginLinearLayout;
    @BindView(R.id.appIcon)
    ImageView appIcon;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        loginLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(view);

            }
        });

    }
    protected void hideKeyboard(View view)
    {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    @OnClick({R.id.txtSignUp, R.id.txtForgotPassword, R.id.skipLoginLayout, R.id.signIn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtSignUp:
                Config.moveTo(Login.this, SignUp.class);
                break;
            case R.id.txtForgotPassword:
                Config.moveTo(Login.this, ForgotPassword.class);
                break;
            case R.id.skipLoginLayout:
                Intent intent = new Intent(Login.this, MainActivity.class);
                intent.putExtra("from", "skip");
                startActivity(intent);
                finishAffinity();
                break;

            case R.id.signIn:
                if (Config.validateEmail(editTexts.get(0),Login.this) && validatePassword(editTexts.get(1))) {
                    login();
                }
                break;
        }
    }

    private boolean validatePassword(EditText editText) {
        if (editText.getText().toString().trim().length() > 5) {
            return true;
        } else if (editText.getText().toString().trim().length() > 0) {
            editText.setError("Şifreniz en az 6 karakterli olmalıdır!");
            editText.requestFocus();
            return false;
        }
        editText.setError("Tüm alanları doldurunuz!");
        editText.requestFocus();
        return false;
    }

    private void login() {
        final SweetAlertDialog pDialog = new SweetAlertDialog(Login.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Yükleniyor...");
        pDialog.setCancelable(false);
        pDialog.show();
        // sending gcm token to server
        Api.getClient2().login(editTexts.get(0).getText().toString().trim(),
                editTexts.get(1).getText().toString().trim(),
                new Callback<SignUpResponse>() {
                    @Override
                    public void success(SignUpResponse signUpResponse, Response response) {
                        pDialog.dismiss();
                        Log.d("signUpResponse", signUpResponse.getUserid() + "");

                        if (signUpResponse.getSuccess().equalsIgnoreCase("true")) {
                            Common.saveUserData(Login.this, "email", editTexts.get(1).getText().toString());
                            Common.saveUserData(Login.this, "userId", signUpResponse.getUserid() + "");
                            Config.moveTo(Login.this, SplashScreen.class);
                            finishAffinity();
                        } else if (signUpResponse.getSuccess().equalsIgnoreCase("notactive")) {
                            Toast.makeText(Login.this, "Aktivasyon için mailinizi onaylamalısınız!", Toast.LENGTH_SHORT).show();
                            Config.moveTo(Login.this, AccountVerification.class);
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
