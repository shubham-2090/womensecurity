package com.example.finalyearproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.finalyearproject.common.NetworkChangeListener;
import com.example.finalyearproject.common.Urls;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {
    
    ImageView ivlogo;
    TextView tvtitle,tvforgetpassword,tvnewuser;
    EditText etusername,etpassword;
    CheckBox cbshowhidepassword;
    Button btnlogin;
    
    ProgressDialog progressDialog;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    GoogleSignInOptions googleSignInOptions; //show multiple gmail options
    GoogleSignInClient googleSignInClient; //selected gmail options stored
    AppCompatButton btnSignInWithGoogle;

    SharedPreferences preferences;   //temporary data store
    SharedPreferences.Editor editor;  //data put in shared preferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       setTitle("Login Activity");

       preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
       editor = preferences.edit();
       
       ivlogo=findViewById(R.id.ivloginlogo);
       tvtitle=findViewById(R.id.tvlogintitle);
       tvnewuser=findViewById(R.id.tvloginnewuser);
       etusername=findViewById(R.id.etloginusername);
       etpassword=findViewById(R.id.etloginpassword);
       tvforgetpassword=findViewById(R.id.tvLoginForgetpassword);
       cbshowhidepassword=findViewById(R.id.cbloginshowhidepassword);
       btnlogin=findViewById(R.id.btnLoginlogin);
       btnSignInWithGoogle=findViewById(R.id.acbtnLoginsigninwithgoogle);

       googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
       googleSignInClient = GoogleSignIn.getClient(LoginActivity.this,googleSignInOptions);

       btnSignInWithGoogle.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               signIn();
           }
       });

        cbshowhidepassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    etpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }

            }

        });
       
       btnlogin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (etusername.getText().toString().isEmpty())
               {
                   etusername.setError("please enter your username");
               }  else if (!etusername.getText().toString().matches(".*[A-Z].*")) {
                   etusername.setError("please enter atleast 1 uppercase letter");
               } else if (!etusername.getText().toString().matches(".*[a-z].*")) {
                   etusername.setError("please enter atleast 1 lowercas letter");
               } else if (etusername.getText().toString().length()<8)
               {
                   etusername.setError("please enter username greater than 8 letters");
               } else if (!etpassword.getText().toString().matches(".*[0-9].*")) {
                   etpassword.setError("please enter atleast 1 Number");
               } else if (!etpassword.getText().toString().matches(".*[@,#,%,&,!].*")) {
                   etpassword.setError("please enter atleast 1 special symbol");
               }else if (etpassword.getText().toString().isEmpty())
               {
                   etpassword.setError("please enter your password");
               } else if (etpassword.getText().toString().length()<8)
               {
                   etpassword.setError("please enter password greater than 8 letters");
               }else
               {
                   progressDialog=new ProgressDialog(LoginActivity.this);
                   progressDialog.setTitle("Please wait");
                   progressDialog.setMessage("Login under process");
                   progressDialog.show();
                   userLogin();
               }
           }
       });

       tvforgetpassword.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(LoginActivity.this, ConfirmRegisterMobileNoActivity.class);
               startActivity(intent);
           }
       });

       tvnewuser.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(LoginActivity.this, RegistrationActivity.class);
               startActivity(intent);
           }
       });
    }

    private void signIn() {
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent,999);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);
                Intent intent = new Intent(LoginActivity.this, MyProfileActivity.class);
                startActivity(intent);
                finish();

            } catch (ApiException e) {
                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,intentFilter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkChangeListener);
    }

    private void userLogin() {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("username",etusername.getText().toString());
        params.put("password",etpassword.getText().toString());


        client.post(Urls.loginUserWebService,params,new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        try {
                            String status = response.getString("success");
                            if (status.equals("1"))
                            {

                              Toast.makeText(LoginActivity.this, "Login Done Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                editor.putString("username",etusername.getText().toString()).commit();
                                startActivity(intent);
                                progressDialog.dismiss();
                                finish();

                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this, "invalid username or password", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "server error", Toast.LENGTH_SHORT).show();
                    }
                }

        );
    }
}