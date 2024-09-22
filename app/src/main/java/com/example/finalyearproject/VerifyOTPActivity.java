package com.example.finalyearproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalyearproject.common.Urls;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;

public class VerifyOTPActivity extends AppCompatActivity {

    TextView tvmobileno,tvResendOTP;
    EditText etinput1,etinput2,etinput3,etinput4,etinput5,etinput6;
    AppCompatButton btnVerify;

    ProgressDialog progressDialog;

    private String strVerificationCode, strName, strMobileNo, strEmailId, strUsername, strPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otpactivity);

        tvmobileno = findViewById(R.id.tvverifyotpmobileno);
        tvResendOTP = findViewById(R.id.tvVerifyOTPResendOTP);
        etinput1 = findViewById(R.id.etverifyotpinputcode1);
        etinput2 = findViewById(R.id.etverifyotpinputcode2);
        etinput3 = findViewById(R.id.etverifyotpinputcode3);
        etinput4 = findViewById(R.id.etverifyotpinputcode4);
        etinput5 = findViewById(R.id.etverifyotpinputcode5);
        etinput6 = findViewById(R.id.etverifyotpinputcode6);
        btnVerify = findViewById(R.id.btnVerifyOTPverify);

        strVerificationCode = getIntent().getStringExtra("verificationcode");
        strName = getIntent().getStringExtra("name");
        strMobileNo = getIntent().getStringExtra("mobileno");
        strEmailId = getIntent().getStringExtra("email");
        strUsername = getIntent().getStringExtra("username");
        strPassword = getIntent().getStringExtra("password");

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etinput1.getText().toString().trim().isEmpty() || etinput2.getText().toString().trim().isEmpty() ||
                etinput3.getText().toString().trim().isEmpty() || etinput4.getText().toString().trim().isEmpty() ||
                etinput5.getText().toString().trim().isEmpty() || etinput6.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(VerifyOTPActivity.this, "Please Enter A Valid OTP", Toast.LENGTH_SHORT).show();

                }

                String otpcode = etinput1.getText().toString()+etinput2.getText().toString()+etinput3.getText().toString()+
                                 etinput4.getText().toString()+etinput5.getText().toString()+etinput6.getText().toString();

                 if (strVerificationCode!=null)
                 {
                     progressDialog = new ProgressDialog(VerifyOTPActivity.this);
                     progressDialog.setTitle("Verifying OTP");
                     progressDialog.setMessage("Please Wait...");
                     progressDialog.setCanceledOnTouchOutside(false);
                     progressDialog.show();

                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                             strVerificationCode,
                             otpcode);

                     FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                             .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                 @Override
                                 public void onComplete(@NonNull Task<AuthResult> task) {

                                     if (task.isSuccessful())
                                     {
                                         progressDialog.dismiss();
                                         userRegisterDetails();
                                     }
                                     else
                                     {
                                         progressDialog.dismiss();
                                         Toast.makeText(VerifyOTPActivity.this, "OTP verification fail", Toast.LENGTH_SHORT).show();
                                     }

                                 }
                             });
                 }

            }
        });

        tvResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + strMobileNo,
                        60, TimeUnit.SECONDS, VerifyOTPActivity.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                Toast.makeText(VerifyOTPActivity.this,"Verification Completed",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(VerifyOTPActivity.this,"Verification Failed",Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onCodeSent(@NonNull String newverificationCode, @NonNull
                            PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                                strVerificationCode = newverificationCode;
                            }
                        }
                );
            }
        });



        setupInputOTP();

    }

    private void userRegisterDetails() {
// client and server communication
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();//put the data
        params.put("name", strName);
        params.put("mobileno",strMobileNo);
        params.put("emailid",strEmailId);
        params.put("username",strUsername);
        params.put("password",strPassword);

        client.post(Urls.RegisterUserWebService,params,new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        try {
                            String status = response.getString("success");
                            if (status.equals("1"))
                            {

                                Toast.makeText(VerifyOTPActivity.this, "Registration Successfully Done", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(VerifyOTPActivity.this, LoginActivity.class);
                                startActivity(intent);
                                progressDialog.dismiss();
                            }
                            else
                            {

                                Toast.makeText(VerifyOTPActivity.this, "Already Data Present", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Toast.makeText(VerifyOTPActivity.this, "server error", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }

        );
    }

    private void setupInputOTP() {

        etinput1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty())
                {
                    etinput2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etinput2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty())
                {
                    etinput3.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        etinput3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().isEmpty())
                {
                    etinput4.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etinput4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().isEmpty())
                {
                    etinput5.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etinput5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().isEmpty())
                {
                    etinput6.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




    }
}