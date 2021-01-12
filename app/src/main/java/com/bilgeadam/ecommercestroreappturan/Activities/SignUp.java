package com.bilgeadam.ecommercestroreappturan.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.bilgeadam.ecommercestroreappturan.Common;
import com.bilgeadam.ecommercestroreappturan.Config;
import com.bilgeadam.ecommercestroreappturan.MVP.NewSignUpResponse;
import com.bilgeadam.ecommercestroreappturan.Retrofit.Api;
import com.bilgeadam.ecommercestroreappturan.R;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class SignUp extends AppCompatActivity {

    @BindViews({R.id.username, R.id.surname, R.id.email, R.id.password})
    List<EditText> editTexts;



    @BindView(R.id.loginLinearLayout)
    LinearLayout loginLinearLayout;

    EditText name;
    EditText surname;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        ButterKnife.bind(this);
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
    @OnClick({R.id.txtSignIn, R.id.signUp, R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtSignIn:
                Config.moveTo(SignUp.this, Login.class);
                finishAffinity();
                break;
            case R.id.back:
                Config.moveTo(SignUp.this, Login.class);
                finishAffinity();
                break;
            case R.id.signUp:
                if (validate(editTexts.get(0)) && Config.validateEmail(editTexts.get(2),
                        SignUp.this) && validatePassword(editTexts.get(2))
                        && validatePassword(editTexts.get(3))) {
                  //  if (editTexts.get(2).getText().toString().trim().equals(editTexts.get(3).getText().toString().trim())) {
                       // signUp();
                        newSignUp();
                  //  } else {
                       // Toast.makeText(SignUp.this, "Password and Confirm Password should be same", Toast.LENGTH_SHORT).show();
                   // }

                }
                break;
        }
    }

    private boolean validate(EditText editText) {
        if (editText.getText().toString().trim().length() > 0) {
            return true;
        }
        editText.setError("Lütfen tüm alanları doldurunuz");
        editText.requestFocus();
        return false;
    }

    private boolean validatePassword(EditText editText) {
        if (editText.getText().toString().trim().length() > 5) {
            return true;
        } else if (editText.getText().toString().trim().length() > 0) {
            editText.setError("Şifreniz en az 6 karakterli olmalıdır!");
            editText.requestFocus();
            return false;
        }
        editText.setError("Lütfen tüm alanları doldurunuz!");
        editText.requestFocus();
        return false;
    }




    private void newSignUp() {
       // final SweetAlertDialog pDialog = new SweetAlertDialog(SignUp.this, SweetAlertDialog.PROGRESS_TYPE);
       // pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
       // pDialog.setTitleText("Gönderiliyor...");
       // pDialog.setCancelable(false);
       // pDialog.show();
        // sending gcm token to server
        Api.getClient2().kayit(editTexts.get(0).getText().toString().trim(),
                editTexts.get(1).getText().toString().trim(),
                editTexts.get(2).getText().toString().trim(),
                editTexts.get(3).getText().toString().trim(),
                new Callback<NewSignUpResponse>() {
                    @Override
                    public void success(NewSignUpResponse newSignUpResponse, Response response) {
                        //pDialog.dismiss();
//                        Log.d("signUpResponse", signUpResponse.getMessage());

                        if (newSignUpResponse.getUserid()>0) {

                            Common.saveUserData(SignUp.this, "email", editTexts.get(2).getText().toString());
                            Common.saveUserData(SignUp.this, "userId", newSignUpResponse.getUserid() + "");
                            Intent intent = new Intent(SignUp.this, MainActivity.class);
                            intent.putExtra("from", "signUp");
                            startActivity(intent);
                            finishAffinity();
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        String json =  new String(((TypedByteArray)error.getResponse().getBody()).getBytes());
                        Log.v("failure", json.toString());
                       // pDialog.dismiss();

                        Log.e("error", error.toString());
                    }
                });
    }



}
