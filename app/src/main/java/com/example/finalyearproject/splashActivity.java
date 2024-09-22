package com.example.finalyearproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class splashActivity extends AppCompatActivity {

    ImageView ivlogo;
    TextView tvTitle;
    Handler handler;
    Animation animtranslate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ivlogo=findViewById(R.id.ivsplashlogo);
        tvTitle=findViewById(R.id.tvsplashTitle);

        animtranslate = AnimationUtils.loadAnimation(splashActivity.this,R.anim.toptpbottomtranslate);
        ivlogo.startAnimation(animtranslate);



        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(splashActivity.this, LoginActivity.class);
                startActivity(i);

            }
        },4000);

    }
}