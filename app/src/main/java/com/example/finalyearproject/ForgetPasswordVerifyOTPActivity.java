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

public class ForgetPasswordVerifyOTPActivity extends AppCompatActivity {

    TextView tvmobileno,tvResendOTP;
    EditText etinput1,etinput2,etinput3,etinput4,etinput5,etinput6;
    AppCompatButton btnVerify;

    ProgressDialog progressDialog;

    private String strVerificationCode,strMobileNo;


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
        strMobileNo = getIntent().getStringExtra("mobileno");


        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etinput1.getText().toString().trim().isEmpty() || etinput2.getText().toString().trim().isEmpty() ||
                        etinput3.getText().toString().trim().isEmpty() || etinput4.getText().toString().trim().isEmpty() ||
                        etinput5.getText().toString().trim().isEmpty() || etinput6.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(ForgetPasswordVerifyOTPActivity.this, "Please Enter A Valid OTP", Toast.LENGTH_SHORT).show();

                }

                String otpcode = etinput1.getText().toString()+etinput2.getText().toString()+etinput3.getText().toString()+
                        etinput4.getText().toString()+etinput5.getText().toString()+etinput6.getText().toString();

                if (strVerificationCode!=null)
                {
                    progressDialog = new ProgressDialog(ForgetPasswordVerifyOTPActivity.this);
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
                                        Intent intent=new Intent(ForgetPasswordVerifyOTPActivity.this, SetUpNewPasswordActivity.class);
                                        intent.putExtra("mobile",strMobileNo);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(ForgetPasswordVerifyOTPActivity.this, "OTP verification fail", Toast.LENGTH_SHORT).show();
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
                        60, TimeUnit.SECONDS, ForgetPasswordVerifyOTPActivity.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                Toast.makeText(ForgetPasswordVerifyOTPActivity.this,"Verification Completed",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(ForgetPasswordVerifyOTPActivity.this,"Verification Failed",Toast.LENGTH_SHORT).show();

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