package com.bilgeadam.ecommercestroreappturan.PaymentIntegrationMethods;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.bilgeadam.ecommercestroreappturan.Adapters.CartListAdapter;
import com.bilgeadam.ecommercestroreappturan.Config;
import com.bilgeadam.ecommercestroreappturan.R;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class PayPalActivityPayment extends AppCompatActivity {

    static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pal_payment);
        ButterKnife.bind(this);

        final Intent intent = new Intent(PayPalActivityPayment.this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(CartListAdapter.totalAmountPayable), "TRY", getResources().getString(R.string.app_name), PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent1 = new Intent(PayPalActivityPayment.this, PaymentActivity.class);
        intent1.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent1.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent1, 9999);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 9999) {

            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        JSONObject jsonObject = new JSONObject(paymentDetails);
                        Log.d("response", jsonObject.getJSONObject("response") + "");
                        if (jsonObject.getJSONObject("response").getString("state").equalsIgnoreCase("approved")) {
                            Config.addOrder(PayPalActivityPayment.this,
                                    jsonObject.getJSONObject("response").getString("id"),
                                    "PayPal Payment-Gateway");
                        } else {
                            new SweetAlertDialog(PayPalActivityPayment.this)
                                    .setTitleText("Payment Failed")
                                    .setContentText("Error While Processing Payment, Please Try Again.")
                                    .show();
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                new SweetAlertDialog(PayPalActivityPayment.this)
                        .setTitleText("Payment")
                        .setContentText("Payment Canceled.")
                        .show();
                finish();
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {

                new SweetAlertDialog(PayPalActivityPayment.this)
                        .setTitleText("Payment")
                        .setContentText("Payment Invalid.")
                        .show();
                finish();
            }
        }
    }
}


