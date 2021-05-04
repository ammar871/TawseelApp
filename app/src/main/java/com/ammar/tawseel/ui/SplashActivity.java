package com.ammar.tawseel.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.ammar.tawseel.R;
import com.ammar.tawseel.databinding.ActivitySplashBinding;
import com.ammar.tawseel.uitllis.Cemmon;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private static final int LOCATION_PER_REQUEST = 9999;
    private static final int PERMISSION_ID = 999;
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION

                    }, LOCATION_PER_REQUEST);


        }else {
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



    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PER_REQUEST:

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
           break;
            default:
                requestPermissions();
                break;
        }
    }

    private void getTokenFCM() {

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            if (!TextUtils.isEmpty(token)) {

                Cemmon.FIREBASE_TOKEN = token;

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

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PER_REQUEST);
    }

}