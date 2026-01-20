package com.example.dastetaawun;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dastetaawun.utils.FirebaseAuthHelper;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private static final int SPLASH_DURATION = 3000;

    private FirebaseAuthHelper authHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Log.d(TAG, "Splash Screen Started");

        authHelper = new FirebaseAuthHelper(this);

        new Handler().postDelayed(this::checkUserAuthentication, SPLASH_DURATION);
    }

    private void checkUserAuthentication() {

        FirebaseUser currentUser = authHelper.getCurrentUser();
        Intent intent;

        if (currentUser != null) {
            Log.d(TAG, "User logged in → MainActivity");
            intent = new Intent(SplashActivity.this, MainActivity.class);
        } else {
            Log.d(TAG, "User not logged in → LoginActivity");
            intent = new Intent(SplashActivity.this, LoginActivity.class);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}
