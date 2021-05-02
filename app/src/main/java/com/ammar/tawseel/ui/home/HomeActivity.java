package com.ammar.tawseel.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.ammar.tawseel.R;
import com.ammar.tawseel.databinding.ActivityHomeBinding;
import com.ammar.tawseel.editor.ShardEditor;
import com.ammar.tawseel.ui.EditeProfilActivity;
import com.ammar.tawseel.ui.auth.LoginActivity;
import com.ammar.tawseel.ui.fragments.HomeFragment;
import com.ammar.tawseel.ui.fragments.MenusFragment;
import com.ammar.tawseel.ui.fragments.MessagesFragment;
import com.ammar.tawseel.ui.fragments.NotifecationFragment;
import com.ammar.tawseel.ui.fragments.TawseelMapsFragment;
import com.ammar.tawseel.uitllis.OnclicksInActivities;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener
        , View.OnClickListener {
    private static final int LOCATION_SETTINGS_REQUEST = 101;
    ActivityHomeBinding binding;
    ShardEditor sharedEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sharedEditor = new ShardEditor(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        if (sharedEditor.loadData().get(ShardEditor.KEY_TOKEN) != null
                && !sharedEditor.loadData().get(ShardEditor.KEY_TOKEN).equals("")) {

            Log.d("tokenaa", "onCreate: " + sharedEditor.loadData().get(ShardEditor.KEY_TOKEN));
        }

        navigationBottom();
        loadFragment(new TawseelMapsFragment());


        inItView();
        //enableLoc();
    }

    private void inItView() {
        TextView tv_userProfil = findViewById(R.id.nav_userProfil);
        TextView tv_logout = findViewById(R.id.nav_logout);


        TextView tv_rates = findViewById(R.id.tv_rates);
        TextView tv_sitting = findViewById(R.id.tv_sitting);
        TextView tv_about_us = findViewById(R.id.tv_about_us);
        TextView tv_call_us = findViewById(R.id.tv_call_us);
        TextView tv_plociy = findViewById(R.id.tv_plociy);
        TextView tv_shar = findViewById(R.id.tv_shar);
        TextView tv_rate = findViewById(R.id.tv_rate);

        tv_userProfil.setOnClickListener(this);
        tv_logout.setOnClickListener(this);

        tv_rates.setOnClickListener(this);
        tv_sitting.setOnClickListener(this);
        tv_about_us.setOnClickListener(this);
        tv_call_us.setOnClickListener(this);
        tv_plociy.setOnClickListener(this);
        tv_shar.setOnClickListener(this);
        tv_rate.setOnClickListener(this);


    }

    private void navigationBottom() {
        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.getMenu().getItem(2).setChecked(true);
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(this);
        binding.imgTawseel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new TawseelMapsFragment());
                binding.bottomNavigationView.getMenu().getItem(2).setChecked(true);
            }
        });
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.notify_nav:

                fragment = new NotifecationFragment();

                break;
            case R.id.nav_home:

                fragment = new HomeFragment();


                break;

            case R.id.nav_message:
                fragment = new MessagesFragment();


                break;
            case R.id.nav_menu:

                fragment = new MenusFragment();
//                binding.tvBar.setText("حســابي");

                break;
            default:

        }
        return loadFragment(fragment);
    }


    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.layout_view, fragment)
                    .commit();
            return true;
        }
        return false;
    }


    @SuppressLint({"RtlHardcoded", "NonConstantResourceId"})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_userProfil:
               startActivity(new Intent(this, EditeProfilActivity.class));


                binding.draw.closeDrawer(Gravity.LEFT);
                break;


            case R.id.nav_logout:
                isLoginWitch();

                binding.draw.closeDrawer(Gravity.LEFT);
                break;
            case R.id.tv_rates:


                binding.draw.closeDrawer(Gravity.LEFT);
                break;
            case R.id.tv_sitting:


                binding.draw.closeDrawer(Gravity.LEFT);
                break;
            case R.id.tv_about_us:

                binding.draw.closeDrawer(Gravity.LEFT);
                break;
            case R.id.tv_call_us:


                binding.draw.closeDrawer(Gravity.LEFT);
                break;
            case R.id.tv_plociy:


                binding.draw.closeDrawer(Gravity.LEFT);
                break;
            case R.id.tv_shar:


                binding.draw.closeDrawer(Gravity.LEFT);
                break;
            case R.id.tv_rate:


                binding.draw.closeDrawer(Gravity.LEFT);
                break;

        }
    }

    private void isLoginWitch() {
        GoogleSignInOptions gso = new GoogleSignInOptions.
                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
        googleSignInClient.signOut();

        LoginManager.getInstance().logOut();

        sharedEditor.logOut();

    }


//    private void enableLoc() {
//
//
//        LocationRequest locationRequest = LocationRequest.create();
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setInterval(30 * 1000);
//        locationRequest.setFastestInterval(5 * 1000);
//
//
//        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
//                .addLocationRequest(locationRequest);
//
//        builder.setAlwaysShow(true);
//
//        Task<LocationSettingsResponse> result =
//                LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());
//
//        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
//
//
//            @Override
//            public void onComplete(Task<LocationSettingsResponse> task) {
//                try {
//                    LocationSettingsResponse response = task.getResult(ApiException.class);
//
//
//                    // All location settings are satisfied. The client can initialize location
//                    // requests here.
//
//                } catch (ApiException exception) {
//                    switch (exception.getStatusCode()) {
//                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                            // Location settings are not satisfied. But could be fixed by showing the
//                            // user a dialog.
//                            try {
//                                // Cast to a resolvable exception.
//                                ResolvableApiException resolvable = (ResolvableApiException) exception;
//                                // Show the dialog by calling startResolutionForResult(),
//                                // and check the result in onActivityResult().
//                                resolvable.startResolutionForResult(
//                                        HomeActivity.this,
//                                        LOCATION_SETTINGS_REQUEST);
//                            } catch (IntentSender.SendIntentException e) {
//                                // Ignore the error.
//                            } catch (ClassCastException e) {
//                                // Ignore, should be an impossible error.
//                            }
//                            break;
//                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                            // Location settings are not satisfied. However, we have no way to fix the
//                            // settings so we won't show the dialog.
//                            break;
//                    }
//                }
//            }
//        });
//
//    }
}