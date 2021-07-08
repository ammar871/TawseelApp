package com.ammar.tawseel.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ammar.tawseel.R;
import com.ammar.tawseel.databinding.ActivityHomeBinding;
import com.ammar.tawseel.editor.ShardEditor;
import com.ammar.tawseel.netWorke.APIClient;
import com.ammar.tawseel.netWorke.APIInterFace;
import com.ammar.tawseel.pojo.response.APIResponse;
import com.ammar.tawseel.ui.ContactUsActivity;
import com.ammar.tawseel.ui.menus.EditeProfilActivity;
import com.ammar.tawseel.ui.menus.OrdersActivity;
import com.ammar.tawseel.ui.menus.PrivacyPolicyActivity;
import com.ammar.tawseel.ui.menus.RatingUsersActivity;
import com.ammar.tawseel.ui.menus.SettingsActivity;
import com.ammar.tawseel.ui.menus.ShareAppActivity;
import com.ammar.tawseel.ui.menus.WhoUsActivity;
import com.ammar.tawseel.ui.fragments.MenusFragment;
import com.ammar.tawseel.ui.fragments.MessagesFragment;
import com.ammar.tawseel.ui.fragments.NotifecationFragment;
import com.ammar.tawseel.ui.fragments.TawseelMapsFragment;
import com.ammar.tawseel.uitllis.Cemmon;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import com.google.android.material.bottomnavigation.BottomNavigationView;



import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener
        , View.OnClickListener {

    ActivityHomeBinding binding;
    ShardEditor shardEditor;
    APIInterFace apiInterFace;
    CircleImageView imgProfile;
    TextView tv_nam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        shardEditor = new ShardEditor(this);
        if (shardEditor.loadData().get(ShardEditor.KEY_LANG) != "") {

            Cemmon.setLocale(this, shardEditor.loadData().get(ShardEditor.KEY_LANG));

        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        apiInterFace = APIClient.getClient().create(APIInterFace.class);
        if (shardEditor.loadData().get(ShardEditor.KEY_TOKEN) != null
                && !shardEditor.loadData().get(ShardEditor.KEY_TOKEN).equals("")) {

            Log.d("tokennn", "onCreate: " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN));

            Cemmon.USER_TOKEN =shardEditor.loadData().get(ShardEditor.KEY_TOKEN);
        }

        navigationBottom();
        loadFragment(new TawseelMapsFragment());


        inItView();
        //enableLoc();
        loadDataProfile();
    }

    private void loadDataProfile() {

        Call<APIResponse.ResponseShowProfile> call = apiInterFace.showProfile(
                "application/json",
                "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                shardEditor.loadData().get(ShardEditor.KEY_LANG));
        call.enqueue(new Callback<APIResponse.ResponseShowProfile>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<APIResponse.ResponseShowProfile> call, @NonNull Response<APIResponse.ResponseShowProfile> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus()) {


                        Log.d("dataprofil", "onResponse: " + response.body().getData().getName());
                        if (response.body().getData().getAvatar() != null &&
                                !response.body().getData().getAvatar().equals("")) {
                            Glide.with(HomeActivity.this)
                                    .load(Cemmon.BASE_URL + response.body().getData().getAvatar()).placeholder(
                                    R.drawable.imagerat)
                                    .into(imgProfile);
                            Cemmon.NAME_OF_USER = response.body().getData().getName();
                            tv_nam.setText(Cemmon.NAME_OF_USER + "");
                            Cemmon.IMAGE_OF_USER = response.body().getData().getAvatar() + "";
                            Log.d("iiiiiiiiiiii", "onResponse: " + Cemmon.IMAGE_OF_USER);

                        }
                    } else {
                        Toast.makeText(HomeActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseShowProfile> call, @NonNull Throwable t) {


            }
        });
    }
    TextView tv_logout;
    ProgressBar nav_progress;
    @SuppressLint("SetTextI18n")
    private void inItView() {
        TextView tv_userProfil = findViewById(R.id.nav_userProfil);
         tv_logout = findViewById(R.id.nav_logout);
        TextView tv_home = findViewById(R.id.tv_home);

        imgProfile = findViewById(R.id.profile_image);
        nav_progress = findViewById(R.id.nav_logout_pro);
        TextView tv_rates = findViewById(R.id.tv_rates);
        TextView tv_order = findViewById(R.id.nav_order);
        TextView tv_sitting = findViewById(R.id.tv_sitting);
        TextView tv_about_us = findViewById(R.id.tv_about_us);
        TextView tv_call_us = findViewById(R.id.tv_call_us);
        TextView tv_plociy = findViewById(R.id.tv_plociy);
        TextView tv_shar = findViewById(R.id.tv_shar);
        TextView tv_rate = findViewById(R.id.tv_rate);
        tv_nam = findViewById(R.id.tv_name_user);

        tv_userProfil.setOnClickListener(this);
        tv_home.setOnClickListener(this);
        tv_logout.setOnClickListener(this);
        tv_order.setOnClickListener(this);
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
    //    binding.bottomNavigationView.getMenu().getItem(2).setChecked(true);
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

                fragment = new TawseelMapsFragment();


                break;

            case R.id.nav_message:
                fragment = new MessagesFragment();


                break;
            case R.id.nav_menu:

                fragment = new MenusFragment();
//                binding.tvBar.setText("حســابي");

                break;


//                binding.tvBar.setText("حســابي");


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


    @SuppressLint({"RtlHardcoded", "NonConstantResourceId", "WrongConstant"})
    @Override
    public void onClick(View v) {
        Fragment fragment=null;
        switch (v.getId()) {
            case R.id.nav_userProfil:
                startActivity(new Intent(this, EditeProfilActivity.class));


                binding.draw.closeDrawer(Gravity.START);
                break;


            case R.id.nav_logout:
                tv_logout.setVisibility(View.GONE);
              nav_progress.setVisibility(View.VISIBLE);
                isLoginWitch();


                break;
            case R.id.nav_order:


                binding.draw.closeDrawer(Gravity.START);
               startActivity(new Intent(this, OrdersActivity.class));
                break;
            case R.id.tv_rates:


                binding.draw.closeDrawer(Gravity.START);

                startActivity(new Intent(HomeActivity.this, RatingUsersActivity.class));

                break;
            case R.id.tv_home:


                binding.draw.closeDrawer(Gravity.START);

                startActivity(new Intent(HomeActivity.this, HomeActivity.class));

                break;
            case R.id.tv_sitting:

                binding.draw.closeDrawer(Gravity.START);
                startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
                break;
            case R.id.tv_about_us:

                binding.draw.closeDrawer(Gravity.START);
                startActivity(new Intent(HomeActivity.this, WhoUsActivity.class));
                break;
            case R.id.tv_call_us:


                binding.draw.closeDrawer(Gravity.START);
                startActivity(new Intent(HomeActivity.this, ContactUsActivity.class));
                break;
            case R.id.tv_plociy:


                binding.draw.closeDrawer(Gravity.START);
                startActivity(new Intent(HomeActivity.this, PrivacyPolicyActivity.class));
                break;
            case R.id.tv_shar:

                startActivity(new Intent(HomeActivity.this, ShareAppActivity.class));
                binding.draw.closeDrawer(Gravity.START);
                break;
            case R.id.tv_rate:

                Uri uri = Uri.parse("market://details?id=" + "com.ammar.tawseel");
                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(myAppLinkToMarket);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
                }
                binding.draw.closeDrawer(Gravity.START);
                break;

        }
    }

    private void isLoginWitch() {

        Call<APIResponse.LogOutResponse> call=apiInterFace.logOut("Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN));
        call.enqueue(new Callback<APIResponse.LogOutResponse>() {
            @SuppressLint("WrongConstant")
            @Override
            public void onResponse(Call<APIResponse.LogOutResponse> call, Response<APIResponse.LogOutResponse> response) {
                if (response.code()==200){
                    if (response.body().getStatus()){
                        GoogleSignInOptions gso = new GoogleSignInOptions.
                                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                                build();

                        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(HomeActivity.this, gso);
                        googleSignInClient.signOut();

                        LoginManager.getInstance().logOut();
                        binding.draw.closeDrawer(Gravity.START);
                        shardEditor.logOut();
                    }

                }else {
                    LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
                    View view = inflater.inflate(R.layout.dialog_logout, null);





                    AlertDialog alertDialog1 = new AlertDialog.Builder(HomeActivity.this)
                            .setView(view)
                            .create();
                    alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    alertDialog1.show();

                    new CountDownTimer(3000, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void onFinish() {
                            shardEditor.logOut();

                            alertDialog1.dismiss();
                        }
                    }.start();


                }
            }

            @Override
            public void onFailure(Call<APIResponse.LogOutResponse> call, Throwable t) {
                tv_logout.setVisibility(View.VISIBLE);
                nav_progress.setVisibility(View.GONE);
            }
        });


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


   /* private void getLanguge() {

        if (sharedEditor.loadData().get(SharedEditor.KEY_LANG).equals("")) {
            if (Locale.getDefault().getLanguage().equals("ar")) {
                sharedEditor.saveLang(Locale.getDefault().getLanguage());
                Cemmon.setLocale(this, sharedEditor.loadData().get(SharedEditor.KEY_LANG));
            } else if (Locale.getDefault().getLanguage().equals("eng")) {
                sharedEditor.saveLang(Locale.getDefault().getLanguage().replace("g", ""));
                Cemmon.setLocale(this, sharedEditor.loadData().get(SharedEditor.KEY_LANG) + "g");
            } else if (Locale.getDefault().getLanguage().equals("pt")) {
                sharedEditor.saveLang(Locale.getDefault().getLanguage());
                Cemmon.setLocale(this, sharedEditor.loadData().get(SharedEditor.KEY_LANG));
            } else {
                sharedEditor.saveLang("en");
                Cemmon.setLocale(this, sharedEditor.loadData().get(SharedEditor.KEY_LANG));
            }

        } else {

            Cemmon.setLocale(this, sharedEditor.loadData().get(SharedEditor.KEY_LANG));
        }

        Log.d("codelang", sharedEditor.loadData().get(SharedEditor.KEY_LANG));
    }*/