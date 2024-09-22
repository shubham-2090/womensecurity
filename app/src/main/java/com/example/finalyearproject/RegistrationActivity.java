package com.example.finalyearproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalyearproject.common.Urls;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;

public class RegistrationActivity extends AppCompatActivity {

    EditText etname,etmobileno,etEmailId,etUsername,etPassword;
    Button btnRegister;

    SharedPreferences preferences; //temporary data store
    SharedPreferences.Editor editor;//put or edit data

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        setTitle("Registration Activity");

        preferences = PreferenceManager.getDefaultSharedPreferences(RegistrationActivity.this);
        editor = preferences.edit();


        etname = findViewById(R.id.etRegistername);
        etmobileno = findViewById(R.id.etRegistermobileno);
        etEmailId = findViewById(R.id.etregisterEmailID);
        etUsername = findViewById(R.id.etRegisterusername);
        etPassword = findViewById(R.id.etRegisterpassword);
        btnRegister = findViewById(R.id.btnRegisterRegister);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etname.getText().toString().isEmpty())
                {
                    etname.setError("please enter your name");
                } else if (etmobileno.getText().toString().isEmpty()) {
                    etmobileno.setError("please enter your mobile no");
                } else if (etmobileno.getText().toString().length() != 10) {
                    etmobileno.setError("please enter 10 digit mobile no");
                } else if (etEmailId.getText().toString().isEmpty()) {
                    etEmailId.setError("please enter your email id");
                } else if (!etEmailId.getText().toString().contains("@")||
                        !etEmailId.getText().toString().contains(".com")) {
                    etEmailId.setError("please enter valid email id");
                } else if (etUsername.getText().toString().isEmpty()) {
                    etUsername.setError("please enter your username");
                } else if (etUsername.getText().toString().length() < 8) {
                    etUsername.setError("username must be greater than 8 characters");
                } else if (!etUsername.getText().toString().matches(".*[A-Z].*")) {
                    etUsername.setError("please enter atleast 1 uppercase letter");
                } else if (!etUsername.getText().toString().matches(".*[a-z].*")) {
                    etUsername.setError("please enter atleast 1 lowercas letter");
                } else if (!etUsername.getText().toString().matches(".*[0-9].*")) {
                    etUsername.setError("please enter atleast 1 Number");
                } else if (!etUsername.getText().toString().matches(".*[@,#,%,&,!].*")) {
                    etUsername.setError("please enter atleast 1 special symbol");
                }else if (etPassword.getText().toString().isEmpty()) {
                    etPassword.setError("please enter your password");
                } else if (etPassword.getText().toString().length() < 8) {
                    etPassword.setError("password must be greater than 8 characters");
                } else if (!etPassword.getText().toString().matches(".*[A-Z].*")) {
                    etPassword.setError("please enter atleast 1 uppercase letter");
                } else if (!etPassword.getText().toString().matches(".*[a-z].*")) {
                    etPassword.setError("please enter atleast 1 lowercas letter");
                } else if (!etPassword.getText().toString().matches(".*[0-9].*")) {
                    etPassword.setError("please enter atleast 1 Number");
                } else if (!etPassword.getText().toString().matches(".*[@,#,%,&,!].*")) {
                    etPassword.setError("please enter atleast 1 special symbol");
                }
                else
                {
                   progressDialog = new ProgressDialog(RegistrationActivity.this);
                   progressDialog.setTitle("Please wait...!");
                   progressDialog.setMessage("Registration is in progress");
                  // progressDialog.setCanceledOnTouchOutside(true);
                   progressDialog.show();

                   PhoneAuthProvider.getInstance().verifyPhoneNumber(
                           "+91" + etmobileno.getText().toString(),
                           60, TimeUnit.SECONDS, RegistrationActivity.this,
                           new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                               @Override
                               public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                   progressDialog.dismiss();
                                   Toast.makeText(RegistrationActivity.this,"Verification Completed",Toast.LENGTH_SHORT).show();
                               }

                               @Override
                               public void onVerificationFailed(@NonNull FirebaseException e) {
                                   progressDialog.dismiss();
                                   Toast.makeText(RegistrationActivity.this,"Verification Failed",Toast.LENGTH_SHORT).show();

                               }

                               @Override
                               public void onCodeSent(@NonNull String verificationCode, @NonNull
                               PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                   Intent intent = new Intent(RegistrationActivity.this, VerifyOTPActivity.class);
                                   intent.putExtra("verificationcode",verificationCode);
                                   intent.putExtra("name",etname.getText().toString());
                                   intent.putExtra("mobileno",etmobileno.getText().toString());
                                   intent.putExtra("email",etEmailId.getText().toString());
                                   intent.putExtra("username",etUsername.getText().toString());
                                   intent.putExtra("password",etPassword.getText().toString());
                                   startActivity(intent);
                               }
                           }
                   );


 //                  userRegisterDetails();
                }

            }
        });


    }

    private void userRegisterDetails() {
// client and server communication 
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();//put the data
        params.put("name",etname.getText().toString());
        params.put("mobileno",etmobileno.getText().toString());
        params.put("emailid",etEmailId.getText().toString());
        params.put("username",etUsername.getText().toString());
        params.put("password",etPassword.getText().toString());

        client.post(Urls.RegisterUserWebService,params,new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        try {
                            String status = response.getString("success");
                            if (status.equals("1"))
                            {

                                Toast.makeText(RegistrationActivity.this, "Registration Successfully Done", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                startActivity(intent);
                                progressDialog.dismiss();
                            }
                            else
                            {

                                Toast.makeText(RegistrationActivity.this, "Already Data Present", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Toast.makeText(RegistrationActivity.this, "server error", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }

        );
    }
}