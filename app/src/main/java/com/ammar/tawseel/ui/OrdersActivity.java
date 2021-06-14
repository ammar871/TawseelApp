package com.ammar.tawseel.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.ammar.tawseel.R;
import com.ammar.tawseel.adapters.AdapterFinshedOrders;
import com.ammar.tawseel.adapters.AdapterOrdersHome;
import com.ammar.tawseel.databinding.ActivityOrdersBinding;
import com.ammar.tawseel.databinding.FragmentHomeBinding;
import com.ammar.tawseel.editor.ShardEditor;
import com.ammar.tawseel.netWorke.APIClient;
import com.ammar.tawseel.netWorke.APIInterFace;
import com.ammar.tawseel.pojo.data.DataFinshOrder;
import com.ammar.tawseel.pojo.data.DataOrder;
import com.ammar.tawseel.pojo.response.APIResponse;
import com.ammar.tawseel.ui.fragments.TawseelMapsFragment;
import com.ammar.tawseel.ui.home.HomeActivity;
import com.ammar.tawseel.uitllis.Cemmon;
import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersActivity extends AppCompatActivity implements  View.OnClickListener {
    AdapterOrdersHome adapterOrdersHome;
    AdapterFinshedOrders adapterFinshedOrders;
    ActivityOrdersBinding binding;
    APIInterFace apiInterFace;
    CircleImageView imgProfile;
    TextView tv_nam;
    ShardEditor shardEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        shardEditor=new ShardEditor(this);
        if (shardEditor.loadData().get(ShardEditor.KEY_LANG)!=""){

            Cemmon.setLocale(this, shardEditor.loadData().get(ShardEditor.KEY_LANG));

        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_orders);

        apiInterFace = APIClient.getClient().create(APIInterFace.class);


        binding.rvOrders.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        binding.rvOrders.setHasFixedSize(true);

        loadCurrentOrders();
        openDraw();
      inItView();
      loadDataProfile();

        binding.layoutFoundOrder.setOnClickListener(v -> {
            binding.layoutFoundOrder.setBackground(getResources().getDrawable(R.drawable.draw_selected_tab));
            binding.layoutNotFoundOrder.setBackground(getResources().getDrawable(R.drawable.draw_text_tab));
            binding.rvOrders.setVisibility(View.GONE);
            binding.layoutProgress.setVisibility(View.VISIBLE);
            loadFinshidOrders();
        });
        binding.layoutNotFoundOrder.setOnClickListener(v -> {
            binding.layoutFoundOrder.setBackground(getResources().getDrawable(R.drawable.draw_text_tab));
            binding.layoutNotFoundOrder.setBackground(getResources().getDrawable(R.drawable.draw_selected_tab));
            binding.rvOrders.setVisibility(View.GONE);
            binding.layoutProgress.setVisibility(View.VISIBLE);
            loadCurrentOrders();
        });
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
                            Glide.with(OrdersActivity.this)
                                    .load(Cemmon.BASE_URL + response.body().getData().getAvatar()).placeholder(
                                    R.drawable.imagerat)
                                    .into(imgProfile);
                            Cemmon.NAME_OF_USER = response.body().getData().getName();
                            tv_nam.setText(Cemmon.NAME_OF_USER + "");
                            Cemmon.IMAGE_OF_USER = response.body().getData().getAvatar() + "";
                            Log.d("iiiiiiiiiiii", "onResponse: " + Cemmon.IMAGE_OF_USER);

                        }
                    } else {
                        Toast.makeText(OrdersActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(OrdersActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseShowProfile> call, @NonNull Throwable t) {


            }
        });
    }
    private void loadCurrentOrders() {
        Call<APIResponse.ResponseCurrentOrder> call=apiInterFace.currentOrder( "application/json", "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                shardEditor.loadData().get(ShardEditor.KEY_LANG)
        );
        call.enqueue(new Callback<APIResponse.ResponseCurrentOrder>() {
            @Override
            public void onResponse(@NonNull Call<APIResponse.ResponseCurrentOrder> call,
                                   @NonNull Response<APIResponse.ResponseCurrentOrder> response) {

                if (response.code()==200){
                    assert response.body() != null;
                    if (response.body().getStauts()){
                        adapterOrdersHome = new AdapterOrdersHome(OrdersActivity.this, (ArrayList<DataOrder>) response.body().getData());
                        binding.rvOrders.setAdapter(adapterOrdersHome);
                        binding.layoutProgress.setVisibility(View.GONE);
                        binding.rvOrders.setVisibility(View.VISIBLE);
                    }

                }

            }

            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseCurrentOrder> call, @NonNull Throwable t) {

            }
        });


    }
    private void loadFinshidOrders() {
        Call<APIResponse.ResponseFinshedOrder>call=apiInterFace.finishedOrders(
                "application/json", "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                shardEditor.loadData().get(ShardEditor.KEY_LANG)
        );
        call.enqueue(new Callback<APIResponse.ResponseFinshedOrder>() {
            @Override
            public void onResponse(@NonNull Call<APIResponse.ResponseFinshedOrder> call,
                                   @NonNull Response<APIResponse.ResponseFinshedOrder> response) {

                if (response.code()==200){
                    assert response.body() != null;
                    if (response.body().getStauts()){
                        adapterFinshedOrders = new AdapterFinshedOrders(OrdersActivity.this, (ArrayList<DataFinshOrder>) response.body().getData());
                        binding.rvOrders.setAdapter(adapterFinshedOrders);
                        binding.layoutProgress.setVisibility(View.GONE);
                        binding.rvOrders.setVisibility(View.VISIBLE);
                    }

                }

            }

            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseFinshedOrder> call, @NonNull Throwable t) {

            }
        });


    }
    @SuppressLint("WrongConstant")
    private void openDraw() {
        binding.toogles.setOnClickListener((View.OnClickListener) v -> {


           binding.draw.openDrawer(Gravity.START);

        });
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View v) {
        Fragment fragment=null;
        switch (v.getId()) {
            case R.id.nav_userProfil:
                startActivity(new Intent(this, EditeProfilActivity.class));


                binding.draw.closeDrawer(Gravity.START);
                break;


            case R.id.nav_logout:
                isLoginWitch();

                binding.draw.closeDrawer(Gravity.START);
                break;
            case R.id.nav_order:


                binding.draw.closeDrawer(Gravity.START);

                break;
            case R.id.tv_rates:


                binding.draw.closeDrawer(Gravity.START);

                startActivity(new Intent(OrdersActivity.this, RatingUsersActivity.class));

                break;
            case R.id.tv_sitting:

                binding.draw.closeDrawer(Gravity.START);
                startActivity(new Intent(OrdersActivity.this, SettingsActivity.class));
                break;
            case R.id.tv_about_us:

                binding.draw.closeDrawer(Gravity.START);
                startActivity(new Intent(OrdersActivity.this, WhoUsActivity.class));
                break;
            case R.id.tv_call_us:


                binding.draw.closeDrawer(Gravity.START);
                startActivity(new Intent(OrdersActivity.this, ContactUsActivity.class));
                break;
            case R.id.tv_plociy:


                binding.draw.closeDrawer(Gravity.START);
                startActivity(new Intent(OrdersActivity.this, PrivacyPolicyActivity.class));
                break;
            case R.id.tv_shar:

                startActivity(new Intent(OrdersActivity.this, ShareAppActivity.class));
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
    @SuppressLint("SetTextI18n")
    private void inItView() {
        TextView tv_userProfil = findViewById(R.id.nav_userProfil);
        TextView tv_logout = findViewById(R.id.nav_logout);

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
}