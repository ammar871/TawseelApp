package com.ammar.tawseel.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ammar.tawseel.R;
import com.ammar.tawseel.databinding.ActivityConfirmBinding;
import com.ammar.tawseel.editor.ShardEditor;
import com.ammar.tawseel.netWorke.APIClient;
import com.ammar.tawseel.netWorke.APIInterFace;
import com.ammar.tawseel.pojo.response.APIResponse;
import com.ammar.tawseel.ui.home.HomeActivity;
import com.ammar.tawseel.uitllis.Cemmon;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmActivity extends AppCompatActivity {
    ActivityConfirmBinding binding;
    ShardEditor shardEditor;
    APIInterFace apiInterFace;
    String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shardEditor = new ShardEditor(this);
        if (shardEditor.loadData().get(ShardEditor.KEY_LANG) != "") {

            Cemmon.setLocale(this, shardEditor.loadData().get(ShardEditor.KEY_LANG));

        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_confirm);
        apiInterFace = APIClient.getClient().create(APIInterFace.class);
        if (getIntent() != null) {
            orderId = getIntent().getStringExtra("idOrder");

            Log.d("ooooooooooooooo", "onCreate: " + orderId);
        }
        binding.btnPickUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.progressbarRating.setVisibility(View.VISIBLE);
                binding.doneOrder.setVisibility(View.GONE);
                confirmOrderApi(orderId);
            }
        });
        binding.btnUnpickUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.progressbarRatingcancel.setVisibility(View.VISIBLE);
                binding.tvCancelOrder.setVisibility(View.GONE);
                cancleOrderApi(orderId);
            }
        });

    }

    private void cancleOrderApi(String orderId) {

        Call<APIResponse.CanceleResponse> call = apiInterFace.cancelOrder(orderId,
                "application/json",
                "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                shardEditor.loadData().get(ShardEditor.KEY_LANG));
        call.enqueue(new Callback<APIResponse.CanceleResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<APIResponse.CanceleResponse> call,
                                   @NonNull Response<APIResponse.CanceleResponse> response) {
                if (response.code() == 200) {
                    binding.tvCancelOrder.setVisibility(View.VISIBLE);
                    binding.progressbarRatingcancel.setVisibility(View.GONE);
                    if (response.body().getStatus()) {
                        startActivity(new Intent(ConfirmActivity.this, HomeActivity.class));
                        finish();




                    }else {
                        Toast.makeText(ConfirmActivity.this, ""+response.body().getMessage().get(0), Toast.LENGTH_SHORT).show();

                    }
                } else {
                    binding.tvCancelOrder.setVisibility(View.VISIBLE);
                    binding.progressbarRatingcancel.setVisibility(View.GONE);
                    Log.d("eeeeeeeeeeeeee", "onResponse: "+response.body().getMessage().get(0));
                }

            }


            @Override
            public void onFailure(@NonNull Call<APIResponse.CanceleResponse> call, @NonNull Throwable t) {

                binding.tvCancelOrder.setVisibility(View.VISIBLE);
                binding.progressbarRatingcancel.setVisibility(View.GONE);
                Log.d("eeeeeeeeeeeeee", "onResponse: "+t.getMessage());
            }
        });
    }

    private void confirmOrderApi(String orderId) {

        Call<APIResponse.ConfirmResponse> call = apiInterFace.confirmOrder(orderId,
                "application/json",
                "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                shardEditor.loadData().get(ShardEditor.KEY_LANG));
        call.enqueue(new Callback<APIResponse.ConfirmResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<APIResponse.ConfirmResponse> call,
                                   @NonNull Response<APIResponse.ConfirmResponse> response) {
                if (response.code() == 200) {
                    binding.doneOrder.setVisibility(View.VISIBLE);
                    binding.progressbarRating.setVisibility(View.GONE);
                    if (response.body().getStatus()) {
                        showDilaogRateDriver(response.body().getData().getDriverId()+"");




                    }else {
                        Toast.makeText(ConfirmActivity.this, ""+response.body().getMessage().get(0), Toast.LENGTH_SHORT).show();

                    }
                } else {
                    binding.doneOrder.setVisibility(View.VISIBLE);
                    binding.progressbarRating.setVisibility(View.GONE);
                    Log.d("eeeeeeeeeeeeee", "onResponse: "+response.body().getMessage().get(0));
                }

            }


            @Override
            public void onFailure(@NonNull Call<APIResponse.ConfirmResponse> call, @NonNull Throwable t) {

                binding.doneOrder.setVisibility(View.VISIBLE);
                binding.progressbarRating.setVisibility(View.GONE);
                Log.d("eeeeeeeeeeeeee", "onResponse: "+t.getMessage());
            }
        });

    }
    private AlertDialog alertDialog = null;
    float getrating;
    private void showDilaogRateDriver(String driverid) {

        View customLayout = LayoutInflater.from(this).inflate(R.layout.add_rate_dailog, null);


        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setView(customLayout);

        FrameLayout btnRating = customLayout.findViewById(R.id.btn_save_rating);
        TextView tv_lable = customLayout.findViewById(R.id.tv_btn_update);
        EditText ed_text = customLayout.findViewById(R.id.ed_update_raing);
        ProgressBar progressBar = customLayout.findViewById(R.id.progressbarRating);
        AppCompatRatingBar ratingBar = customLayout.findViewById(R.id.tv_rating_dialog);


        ratingBar.setOnRatingBarChangeListener((ratingBar1, rating1, fromUser) -> getrating= ratingBar1.getRating());
        btnRating.setOnClickListener(v -> {

            progressBar.setVisibility(View.VISIBLE);
            tv_lable.setVisibility(View.GONE);


            Call<APIResponse.AddRateResponse> call = apiInterFace.addRating(
                    driverid
                    , ed_text.getText().toString(),
                    Math.round(ratingBar.getRating()) + "",
                    "application/json",
                    "Bearer" + " "+ shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                    shardEditor.loadData().get(ShardEditor.KEY_LANG)
            );

            call.enqueue(new Callback<APIResponse.AddRateResponse>() {
                @Override
                public void onResponse(@NonNull Call<APIResponse.AddRateResponse> call,
                                       @NonNull Response<APIResponse.AddRateResponse> response) {
                    if (response.code() == 200) {

                        if (response.body().getStatus()) {
                            Toast.makeText(ConfirmActivity.this,
                                    "تم التقييم ",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ConfirmActivity.this, HomeActivity.class));
                            finish();
                            alertDialog.dismiss();
                        } else {

                            Toast.makeText(
                                        ConfirmActivity.this,
                                    response.body().getMessage().get(0).toString() + "",
                                    Toast.LENGTH_SHORT)
                                    .show();

                        }

                        progressBar.setVisibility(View.GONE);
                        tv_lable.setVisibility(View.VISIBLE);



//                        loadRatings("1");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<APIResponse.AddRateResponse> call, @NonNull Throwable t) {
                    alertDialog.dismiss();
                }
            });


        });
        builder.setCancelable(true);
        alertDialog = builder.create();
        alertDialog.show();
    }
}