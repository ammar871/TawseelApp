package com.ammar.tawseel.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import com.ammar.tawseel.R;
import com.ammar.tawseel.databinding.ActivitySplashBinding;
import com.ammar.tawseel.uitllis.Cemmon;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    Timer waitTimer;
    private static final int WAIT_TIME = 3000;

    ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        getTokenFCM();

        waitTimer = new Timer();

        waitTimer.schedule(new TimerTask() {

                               @SuppressLint({"ResourceAsColor", "UseCompatLoadingForDrawables"})
                               @Override
                               public void run() {

                                   startActivity(new Intent(SplashActivity.this,
                                           SkipingActivity.class));
                                   finish();
                               }
                           },
                WAIT_TIME);
    }

    private void getTokenFCM() {

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            if (!TextUtils.isEmpty(token)) {
                Log.d("tokenFir", "retrieve token successful : " + token);
                Cemmon.FIREBASE_TOKEN =token;

            } else {
                Log.w("tokenFirnull", "token should not be null...");
            }
        }).addOnFailureListener(e -> {
            //handle e
        }).addOnCanceledListener(() -> {
            //handle cancel
        }).addOnCompleteListener(task ->
                Log.v("tokenFire", "This is the token : " + task.getResult())

        );
    }
}