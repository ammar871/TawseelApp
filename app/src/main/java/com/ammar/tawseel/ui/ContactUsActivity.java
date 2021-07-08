package com.ammar.tawseel.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ammar.tawseel.R;
import com.ammar.tawseel.databinding.ActivityContactUsBinding;
import com.ammar.tawseel.editor.ShardEditor;
import com.ammar.tawseel.netWorke.APIClient;
import com.ammar.tawseel.netWorke.APIInterFace;
import com.ammar.tawseel.pojo.response.APIResponse;
import com.ammar.tawseel.ui.home.HomeActivity;
import com.ammar.tawseel.ui.menus.EditeProfilActivity;
import com.ammar.tawseel.ui.menus.PrivacyPolicyActivity;
import com.ammar.tawseel.ui.menus.RatingUsersActivity;
import com.ammar.tawseel.ui.menus.SettingsActivity;
import com.ammar.tawseel.ui.menus.ShareAppActivity;
import com.ammar.tawseel.ui.menus.WhoUsActivity;
import com.ammar.tawseel.uitllis.Cemmon;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUsActivity extends AppCompatActivity implements View.OnClickListener{
    ShardEditor shardEditor;
    APIInterFace apiInterFace;
    ActivityContactUsBinding binding;
    String numberWhatsResponse;
    CircleImageView imgProfile;
    TextView tv_nam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        shardEditor = new ShardEditor(this);
        if (shardEditor.loadData().get(ShardEditor.KEY_LANG) != "") {

            Cemmon.setLocale(this, shardEditor.loadData().get(ShardEditor.KEY_LANG));

        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_us);
        apiInterFace = APIClient.getClient().create(APIInterFace.class);

        getNumberWhats();
        inItView();

        loadDataProfile();
        openDraw();
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.editTextEmail.getText().toString().isEmpty() ||
                        binding.editMessage.getText().toString().isEmpty()
                        ||
                        binding.editTextName.getText().toString().isEmpty()) {

                    Toast.makeText(ContactUsActivity.this,getString(R.string.please_fill_info), Toast.LENGTH_SHORT).show();
                } else {
                    binding.tvButton.setVisibility(View.GONE);
                    binding.progress.setVisibility(View.VISIBLE);
                    sendMessage();
                }

            }
        });

        binding.tvContectwhats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WhatesSend();
            }
        });

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    @SuppressLint("SetTextI18n")
    private void inItView() {
        TextView tv_userProfil = findViewById(R.id.nav_userProfil);
        TextView tv_logout = findViewById(R.id.nav_logout);
        TextView tv_home = findViewById(R.id.tv_home);
        tv_home.setOnClickListener(this);
        imgProfile = findViewById(R.id.profile_image);
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
                            Picasso.with(ContactUsActivity.this)
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
        binding.imgTogless.setOnClickListener((View.OnClickListener) v -> {


            binding.draw.openDrawer(Gravity.START);

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

                startActivity(new Intent(ContactUsActivity.this, HomeActivity.class));

                break;

            case R.id.nav_logout:
                isLoginWitch();

                binding.draw.closeDrawer(Gravity.START);
                break;
            case R.id.nav_order:

                startActivity(new Intent(ContactUsActivity.this, RatingUsersActivity.class));
                binding.draw.closeDrawer(Gravity.START);

                break;
            case R.id.tv_rates:


                binding.draw.closeDrawer(Gravity.START);

                startActivity(new Intent(ContactUsActivity.this, RatingUsersActivity.class));

                break;
            case R.id.tv_sitting:

                binding.draw.closeDrawer(Gravity.START);
                startActivity(new Intent(ContactUsActivity.this, SettingsActivity.class));
                break;
            case R.id.tv_about_us:

                binding.draw.closeDrawer(Gravity.START);
                startActivity(new Intent(ContactUsActivity.this, WhoUsActivity.class));
                break;
            case R.id.tv_call_us:


                binding.draw.closeDrawer(Gravity.START);
                startActivity(new Intent(ContactUsActivity.this, ContactUsActivity.class));
                break;
            case R.id.tv_plociy:


                binding.draw.closeDrawer(Gravity.START);
                startActivity(new Intent(ContactUsActivity.this, PrivacyPolicyActivity.class));
                break;
            case R.id.tv_shar:

                startActivity(new Intent(ContactUsActivity.this, ShareAppActivity.class));
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
        GoogleSignInOptions gso = new GoogleSignInOptions.
                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
        googleSignInClient.signOut();

        LoginManager.getInstance().logOut();

        shardEditor.logOut();

    }
    private void getNumberWhats() {


        Call<APIResponse.NumberWhatsResponse> call = apiInterFace.numberWhatsResponse("application/json",
                "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                shardEditor.loadData().get(ShardEditor.KEY_LANG));
        call.enqueue(new Callback<APIResponse.NumberWhatsResponse>() {
            @Override
            public void onResponse(Call<APIResponse.NumberWhatsResponse> call, Response<APIResponse.NumberWhatsResponse> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus()) {
                        numberWhatsResponse = response.body().getData();
                        Log.d("mmmmmmmmmmmmmmm", "onResponse: "+numberWhatsResponse);
                    }

                }else if (response.code() == 401) {
                    shardEditor.logOut();
                }

            }

            @Override
            public void onFailure(Call<APIResponse.NumberWhatsResponse> call, Throwable t) {

            }
        });
    }

    private void sendMessage() {

        Call<APIResponse.ContactResponse> call = apiInterFace.sendContactus(binding.editTextEmail.getText().toString(),
                binding.editMessage.getText().toString()
                , binding.editTextName.getText().toString(),
                "user", "application/json",
                "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                shardEditor.loadData().get(ShardEditor.KEY_LANG));

        call.enqueue(new Callback<APIResponse.ContactResponse>() {
            @Override
            public void onResponse(Call<APIResponse.ContactResponse> call, Response<APIResponse.ContactResponse> response) {
                if (response.code() == 200) {
                    binding.tvButton.setVisibility(View.VISIBLE);
                    binding.progress.setVisibility(View.GONE);
                    if (response.body().getStatus()) {
                        binding.editMessage.setText("");
                        Toast.makeText(ContactUsActivity.this, getString(R.string.send_message), Toast.LENGTH_SHORT).show();


                    } else {

                    }
                } else {
                    binding.tvButton.setVisibility(View.VISIBLE);
                    binding.progress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<APIResponse.ContactResponse> call, Throwable t) {
                binding.tvButton.setVisibility(View.VISIBLE);
                binding.progress.setVisibility(View.GONE);
            }
        });


    }

    private void WhatesSend() {
        String contact =numberWhatsResponse;
        String url = "https://api.whatsapp.com/send?phone="+ contact;
        try {
            PackageManager pm = getPackageManager();
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(ContactUsActivity.this, getString(R.string.not_whats), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}