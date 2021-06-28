package com.ammar.tawseel.ui.fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.ammar.tawseel.R;
import com.ammar.tawseel.databinding.ActivityDetailesBinding;
import com.ammar.tawseel.databinding.ActivityHomeBinding;
import com.ammar.tawseel.editor.ShardEditor;
import com.ammar.tawseel.netWorke.APIClient;
import com.ammar.tawseel.netWorke.APIInterFace;
import com.ammar.tawseel.pojo.response.APIResponse;
import com.ammar.tawseel.ui.home.HomeActivity;
import com.ammar.tawseel.uitllis.Cemmon;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailesActivity extends AppCompatActivity {
    ActivityDetailesBinding binding;
    ShardEditor shardEditor;
    String idBill = "";
    APIInterFace apiInterFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        shardEditor = new ShardEditor(this);
        if (shardEditor.loadData().get(ShardEditor.KEY_LANG) != "") {

            Cemmon.setLocale(this, shardEditor.loadData().get(ShardEditor.KEY_LANG));

        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detailes);
        apiInterFace = APIClient.getClient().create(APIInterFace.class);

        if (getIntent() != null) {
            idBill = getIntent().getStringExtra("idBill");
            Log.d("idBill", "onCreateView: " + idBill);
            if (!idBill.equals("")) {
                loadShowBill(idBill);
            }

        }

        binding.btnDelete.setOnClickListener(v -> {
            binding.tvCloseOprator.setVisibility(View.GONE);
            binding.progressDelete.setVisibility(View.VISIBLE);
            callDeleteApi(idBill);
        });

binding.imgBack.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        onBackPressed();
    }
});
    }

    private void callDeleteApi(String idBill) {

        Call<APIResponse.CanceleResponse> chatCall = apiInterFace.deleteBill(
                idBill, "application/json", "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                shardEditor.loadData().get(ShardEditor.KEY_LANG)
        );
        chatCall.enqueue(new Callback<APIResponse.CanceleResponse>() {
            @Override
            public void onResponse(Call<APIResponse.CanceleResponse> call, Response<APIResponse.CanceleResponse> response) {
                if (response.code() == 200) {

                    if (response.body().getStatus()) {


                        Toast.makeText(DetailesActivity.this, "تم حذف الفاتورة", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(DetailesActivity.this, HomeActivity.class));
                        finish();
                    }
                } else if (response.code() == 401) {
                    shardEditor.logOut();
                }

            }

            @Override
            public void onFailure(Call<APIResponse.CanceleResponse> call, Throwable t) {
                Log.d("eiled", "onResponse: " + t.getMessage());
            }
        });

    }

    private void loadShowBill(String idBill) {

        Call<APIResponse.ResponseShowBill> call = apiInterFace.showBill(idBill
                , "application/json",
                "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                shardEditor.loadData().get(ShardEditor.KEY_LANG));
        call.enqueue(new Callback<APIResponse.ResponseShowBill>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<APIResponse.ResponseShowBill> call,
                                   @NonNull Response<APIResponse.ResponseShowBill> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus()) {
                        binding.tvNumberFatorabar.setText(response.body().getData().getId() + "");
                        binding.tvNameFatora.setText(response.body().getData().getName() + "");
                        binding.tvDevelaryFatora.setText(response.body().getData().getIDName() + "");
                        binding.tvTvNumFatoraFatora.setText(response.body().getData().getId() + "");
                        binding.tvFromAddress.setText(response.body().getData().getFromAddress() + "");
                        binding.tvToAddress.setText(response.body().getData().getToAddress() + "");
                        binding.tvTvPricFatora.setText(response.body().getData().getPrice() + "");

                        binding.homeContant.setVisibility(View.VISIBLE);
                        binding.layoutProgress.setVisibility(View.GONE);


                    }

                }


            }

            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseShowBill> call, @NonNull Throwable t) {

            }
        });
    }
}