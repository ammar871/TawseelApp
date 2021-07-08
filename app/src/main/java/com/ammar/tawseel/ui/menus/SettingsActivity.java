package com.ammar.tawseel.ui.menus;

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
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ammar.tawseel.R;
import com.ammar.tawseel.databinding.ActivitySettingsBinding;
import com.ammar.tawseel.editor.ShardEditor;
import com.ammar.tawseel.netWorke.APIClient;
import com.ammar.tawseel.netWorke.APIInterFace;
import com.ammar.tawseel.pojo.response.APIResponse;
import com.ammar.tawseel.ui.ContactUsActivity;
import com.ammar.tawseel.ui.SplashActivity;
import com.ammar.tawseel.ui.home.HomeActivity;
import com.ammar.tawseel.uitllis.Cemmon;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    ActivitySettingsBinding binding;
    ShardEditor shardEditor;
    String itemSelectedSpinner;
    ArrayList<String> languages = new ArrayList<>();
    APIInterFace apiInterFace;
    CircleImageView imgProfile;
    TextView tv_nam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        shardEditor = new ShardEditor(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        if (shardEditor.loadData().get(ShardEditor.KEY_LANG) != "") {

            Cemmon.setLocale(this, shardEditor.loadData().get(ShardEditor.KEY_LANG));

        }
        apiInterFace = APIClient.getClient().create(APIInterFace.class);
        inItSpinner();
        inItView();

        loadDataProfile();
        openDraw();
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemSelectedSpinner.equals("لغة العرض")) {

                } else if ((itemSelectedSpinner.equals("اللغة العربيـة"))) {

                    shardEditor.saveLang("ar");
                    Cemmon.LONG_USER = "ar";
                } else if ((itemSelectedSpinner.equals("English"))) {
                    shardEditor.saveLang("en");
                    Cemmon.LONG_USER = "en";
                }
                startActivity(new Intent(SettingsActivity.this, SplashActivity.class));
                finish();
            }
        });
    }

    private void inItSpinner() {
        languages.add(getString(R.string.lang_display));
        languages.add("اللغة العربيـة");
        languages.add("English");
        ArrayAdapter<String> langAdapter = new ArrayAdapter<String>(SettingsActivity.this, R.layout.spinner_text, languages);
        langAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        binding.spinnerLang.setAdapter(langAdapter);
        binding.spinnerLang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemSelectedSpinner = parent.getItemAtPosition(position).toString();


            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    ProgressBar nav_progress;
    TextView tv_logout;

    @SuppressLint("SetTextI18n")
    private void inItView() {
        TextView tv_userProfil = findViewById(R.id.nav_userProfil);
        tv_logout = findViewById(R.id.nav_logout);

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
        TextView tv_home = findViewById(R.id.tv_home);
        tv_home.setOnClickListener(this);
        tv_userProfil.setOnClickListener(this);
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

                        //  Objects.requireNonNull(binding.inputLayoutLocation.getEditText()).setText(response.body().getData().getGpsAddress() + "");
                        Log.d("dataprofil", "onResponse: " + response.body().getData().getName());
                        if (response.body().getData().getAvatar() != null && !response.body().getData().getAvatar().equals("")) {

                            Cemmon.NAME_OF_USER = response.body().getData().getName();
                            tv_nam.setText(Cemmon.NAME_OF_USER + "");
                            Cemmon.IMAGE_OF_USER = response.body().getData().getAvatar() + "";
                            Log.d("iiiiiiiiiiii", "onResponse: " + Cemmon.IMAGE_OF_USER);
                            Picasso.with(SettingsActivity.this)
                                    .load(Cemmon.BASE_URL + Cemmon.IMAGE_OF_USER).placeholder(R.drawable.imagerat)
                                    .into(imgProfile);


                        }
                    } else {

                    }

                } else if (response.code() == 401) {
                    shardEditor.logOut();
                }


            }

            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseShowProfile> call, @NonNull Throwable t) {
//                Toast.makeText(EditeProfilActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @SuppressLint("WrongConstant")
    private void openDraw() {
        binding.imgToggls.setOnClickListener((View.OnClickListener) v -> {


            binding.draw.openDrawer(Gravity.START);

        });
    }

    private void isLoginWitch() {

        Call<APIResponse.LogOutResponse> call = apiInterFace.logOut("Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN));
        call.enqueue(new Callback<APIResponse.LogOutResponse>() {
            @SuppressLint("WrongConstant")
            @Override
            public void onResponse(Call<APIResponse.LogOutResponse> call, Response<APIResponse.LogOutResponse> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus()) {
                        GoogleSignInOptions gso = new GoogleSignInOptions.
                                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                                build();

                        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(SettingsActivity.this, gso);
                        googleSignInClient.signOut();

                        LoginManager.getInstance().logOut();
                        binding.draw.closeDrawer(Gravity.START);
                        shardEditor.logOut();
                    }

                } else {
                    LayoutInflater inflater = LayoutInflater.from(SettingsActivity.this);
                    View view = inflater.inflate(R.layout.dialog_logout, null);


                    AlertDialog alertDialog1 = new AlertDialog.Builder(SettingsActivity.this)
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
                nav_progress.setVisibility(View.GONE);
                tv_logout.setVisibility(View.VISIBLE);
            }
        });


    }

    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        switch (v.getId()) {
            case R.id.nav_userProfil:
                startActivity(new Intent(this, EditeProfilActivity.class));


                binding.draw.closeDrawer(Gravity.START);
                break;

            case R.id.tv_home:


                binding.draw.closeDrawer(Gravity.START);

                startActivity(new Intent(SettingsActivity.this, HomeActivity.class));

                break;
            case R.id.nav_logout:
                tv_logout.setVisibility(View.GONE);
                nav_progress.setVisibility(View.VISIBLE);
                isLoginWitch();


                break;
            case R.id.nav_order:

                startActivity(new Intent(SettingsActivity.this, RatingUsersActivity.class));
                binding.draw.closeDrawer(Gravity.START);

                break;
            case R.id.tv_rates:


                binding.draw.closeDrawer(Gravity.START);

                startActivity(new Intent(SettingsActivity.this, RatingUsersActivity.class));

                break;
            case R.id.tv_sitting:

                binding.draw.closeDrawer(Gravity.START);
                startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
                break;
            case R.id.tv_about_us:

                binding.draw.closeDrawer(Gravity.START);
                startActivity(new Intent(SettingsActivity.this, WhoUsActivity.class));
                break;
            case R.id.tv_call_us:


                binding.draw.closeDrawer(Gravity.START);
                startActivity(new Intent(SettingsActivity.this, ContactUsActivity.class));
                break;
            case R.id.tv_plociy:


                binding.draw.closeDrawer(Gravity.START);
                startActivity(new Intent(SettingsActivity.this, PrivacyPolicyActivity.class));
                break;
            case R.id.tv_shar:

                startActivity(new Intent(SettingsActivity.this, ShareAppActivity.class));
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
}