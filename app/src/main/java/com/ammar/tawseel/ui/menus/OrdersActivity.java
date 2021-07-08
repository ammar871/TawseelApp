package com.ammar.tawseel.ui.menus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ammar.tawseel.R;
import com.ammar.tawseel.adapters.AdapterFinshedOrders;
import com.ammar.tawseel.adapters.AdapterOrdersHome;
import com.ammar.tawseel.databinding.ActivityOrdersBinding;
import com.ammar.tawseel.editor.ShardEditor;
import com.ammar.tawseel.netWorke.APIClient;
import com.ammar.tawseel.netWorke.APIInterFace;
import com.ammar.tawseel.pojo.data.DataFinshOrder;
import com.ammar.tawseel.pojo.data.DataOrder;
import com.ammar.tawseel.pojo.response.APIResponse;
import com.ammar.tawseel.ui.ContactUsActivity;
import com.ammar.tawseel.ui.home.HomeActivity;
import com.ammar.tawseel.uitllis.Cemmon;
import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

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
    ArrayList<DataOrder>listCurrent=new ArrayList<>();
    ArrayList<DataFinshOrder>listfinshed=new ArrayList<>();
    int page = 1;
    int pageFinshe = 1;
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


        binding.rvOrdersCurrent.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        binding.rvOrdersCurrent.setHasFixedSize(true);

        binding.rvOrdersFinshed.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        binding.rvOrdersFinshed.setHasFixedSize(true);

        loadCurrentOrders(1);
        openDraw();
      inItView();
      loadDataProfile();

        binding.layoutCanceledOrder.setOnClickListener(v -> {
            binding.layoutCanceledOrder.setBackground(getResources().getDrawable(R.drawable.draw_selected_tab));
            binding.layoutCurrentOrder.setBackground(getResources().getDrawable(R.drawable.draw_text_tab));
            binding.scrolCurrent.setVisibility(View.GONE);
            binding.nestscrollfinshed.setVisibility(View.VISIBLE);
            binding.layoutProgress.setVisibility(View.VISIBLE);
            loadFinshidOrders(1);
        });
        binding.layoutCurrentOrder.setOnClickListener(v -> {
            binding.layoutCanceledOrder.setBackground(getResources().getDrawable(R.drawable.draw_text_tab));
            binding.layoutCurrentOrder.setBackground(getResources().getDrawable(R.drawable.draw_selected_tab));
            binding.scrolCurrent.setVisibility(View.VISIBLE);
            binding.nestscrollfinshed.setVisibility(View.GONE);
            binding.layoutProgress.setVisibility(View.VISIBLE);
            loadCurrentOrders(1);
        });


        binding.scrolCurrent.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()){

                    if (listCurrent.size()>0){
                        binding.proBarPag.setVisibility(View.VISIBLE);
                        page++;
//

                        loadCurrentOrdersPagenaintion(page);
                    }



                }
            }
        });
        binding.nestscrollfinshed.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()){
                    if (listfinshed.size()>0){
                        binding.proBarPagFinshid.setVisibility(View.VISIBLE);
                        pageFinshe++;
                        loadFinshedOrderPage(pageFinshe);

                    }



                }
            }
        });
    }

    private void loadFinshedOrderPage(int pageFinshe) {

        Call<APIResponse.ResponseFinshedOrder>call=apiInterFace.finishedOrders(pageFinshe+"",
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
                        listfinshed.addAll(response.body().getData());
                        adapterFinshedOrders = new AdapterFinshedOrders(OrdersActivity.this, listfinshed);
                        binding.rvOrdersFinshed.setAdapter(adapterFinshedOrders);
                        binding.proBarPagFinshid.setVisibility(View.GONE);

                    }

                }

            }

            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseFinshedOrder> call, @NonNull Throwable t) {

            }
        });

    }

    private void loadCurrentOrdersPagenaintion(int page) {
        Call<APIResponse.ResponseCurrentOrder> call=apiInterFace.currentOrder( page+"","application/json", "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                shardEditor.loadData().get(ShardEditor.KEY_LANG)
        );
        call.enqueue(new Callback<APIResponse.ResponseCurrentOrder>() {
            @Override
            public void onResponse(@NonNull Call<APIResponse.ResponseCurrentOrder> call,
                                   @NonNull Response<APIResponse.ResponseCurrentOrder> response) {

                if (response.code()==200){
                    assert response.body() != null;
                    if (response.body().getStauts()){
                        listCurrent.addAll(response.body().getData());
                        adapterOrdersHome = new AdapterOrdersHome(OrdersActivity.this,listCurrent);
                        binding.rvOrdersCurrent.setAdapter(adapterOrdersHome);
                        binding.proBarPag.setVisibility(View.GONE);

                    }

                }

            }

            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseCurrentOrder> call, @NonNull Throwable t) {

            }
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
    private void loadCurrentOrders(int page) {
        Call<APIResponse.ResponseCurrentOrder> call=apiInterFace.currentOrder(page+"" ,"application/json", "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                shardEditor.loadData().get(ShardEditor.KEY_LANG)
        );
        call.enqueue(new Callback<APIResponse.ResponseCurrentOrder>() {
            @Override
            public void onResponse(@NonNull Call<APIResponse.ResponseCurrentOrder> call,
                                   @NonNull Response<APIResponse.ResponseCurrentOrder> response) {

                if (response.code()==200){
                    assert response.body() != null;
                    if (response.body().getStauts()){
                        listCurrent= (ArrayList<DataOrder>) response.body().getData();
                        adapterOrdersHome = new AdapterOrdersHome(OrdersActivity.this, listCurrent);
                        binding.rvOrdersCurrent.setAdapter(adapterOrdersHome);
                        binding.layoutProgress.setVisibility(View.GONE);
                        binding.layoutHome.setVisibility(View.VISIBLE);
                    }

                }else if (response.code() == 401) {
                    shardEditor.logOut();
                }


            }

            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseCurrentOrder> call, @NonNull Throwable t) {

            }
        });


    }
    private void loadFinshidOrders(int page) {
        Call<APIResponse.ResponseFinshedOrder>call=apiInterFace.finishedOrders(page+"",
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
                        listfinshed= (ArrayList<DataFinshOrder>) response.body().getData();
                        adapterFinshedOrders = new AdapterFinshedOrders(OrdersActivity.this, listfinshed);
                        binding.rvOrdersFinshed.setAdapter(adapterFinshedOrders);
                        binding.layoutProgress.setVisibility(View.GONE);
                        binding.layoutHome.setVisibility(View.VISIBLE);
                    }

                }else if (response.code() == 401) {
                    shardEditor.logOut();
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
            case R.id.tv_home:


                binding.draw.closeDrawer(Gravity.START);

                startActivity(new Intent(OrdersActivity.this, HomeActivity.class));

                break;

            case R.id.nav_logout:
                nav_progress.setVisibility(View.VISIBLE);
                tv_logout.setVisibility(View.GONE);
                isLoginWitch();


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

                        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(OrdersActivity.this, gso);
                        googleSignInClient.signOut();

                        LoginManager.getInstance().logOut();
                        binding.draw.closeDrawer(Gravity.START);
                        shardEditor.logOut();
                    }

                }else {
                    LayoutInflater inflater = LayoutInflater.from(OrdersActivity.this);
                    View view = inflater.inflate(R.layout.dialog_logout, null);





                    AlertDialog alertDialog1 = new AlertDialog.Builder(OrdersActivity.this)
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
}