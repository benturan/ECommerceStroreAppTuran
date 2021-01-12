package com.bilgeadam.ecommercestroreappturan.Activities;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bilgeadam.ecommercestroreappturan.Config;
import com.bilgeadam.ecommercestroreappturan.MVP.SignUpResponse;
import com.bilgeadam.ecommercestroreappturan.R;
import com.bilgeadam.ecommercestroreappturan.Retrofit.Api;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AccountVerification extends AppCompatActivity {

    @BindView(R.id.resendEmail)
    Button resendEmail;
    @BindView(R.id.email)
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_verification);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.resendEmail, R.id.signUp,R.id.login, R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.resendEmail:
                if (Config.validateEmail(email,AccountVerification.this))
                resendEmail();
                break;
            case R.id.signUp:
                Config.moveTo(AccountVerification.this, SignUp.class);
                break;
            case R.id.login:
                Config.moveTo(AccountVerification.this, Login.class);
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    private void resendEmail() {
        final SweetAlertDialog pDialog = new SweetAlertDialog(AccountVerification.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Yükleniyor...");
        pDialog.setCancelable(false);
        pDialog.show();
        // sending gcm token to server
        Api.getClient2().resentEmail(email.getText().toString().trim(),
                new Callback<SignUpResponse>() {
                    @Override
                    public void success(SignUpResponse signUpResponse, Response response) {
                        pDialog.dismiss();
                        Log.d("resendEmailResponse", signUpResponse.getSuccess() + "");
                        Toast.makeText(AccountVerification.this, "Aktivasyon için mail gönderilmiştir!", Toast.LENGTH_SHORT).show();
                        if (signUpResponse.getSuccess().equalsIgnoreCase("true")) {
                            finish();
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
