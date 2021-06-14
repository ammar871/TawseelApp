package com.ammar.tawseel.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ammar.tawseel.R;
import com.ammar.tawseel.adapters.AdapterOrdersHome;
import com.ammar.tawseel.adapters.AdapterRatinge;
import com.ammar.tawseel.databinding.ActivityRatingUsersBinding;
import com.ammar.tawseel.editor.ShardEditor;
import com.ammar.tawseel.netWorke.APIClient;
import com.ammar.tawseel.netWorke.APIInterFace;
import com.ammar.tawseel.pojo.data.DataOrder;
import com.ammar.tawseel.pojo.data.Rating;
import com.ammar.tawseel.pojo.response.APIResponse;
import com.ammar.tawseel.uitllis.Cemmon;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.Header;
import retrofit2.http.Path;

public class RatingUsersActivity extends AppCompatActivity {
    ActivityRatingUsersBinding binding;
    ShardEditor shardEditor;
    APIInterFace apiInterFace;
    AdapterRatinge adapterRatinge;
    private AlertDialog alertDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        shardEditor=new ShardEditor(this);
        if (shardEditor.loadData().get(ShardEditor.KEY_LANG)!=""){

            Cemmon.setLocale(this, shardEditor.loadData().get(ShardEditor.KEY_LANG));

        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rating_users);
        apiInterFace = APIClient.getClient().create(APIInterFace.class);


        binding.rvRatings.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        binding.rvRatings.setHasFixedSize(true);
binding.imgBack.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        onBackPressed();
    }
});
        loadRatings("1");

    }

    private void loadRatings(String page) {
        if (binding.layoutProgress.getVisibility() == View.GONE && binding.rvRatings.getVisibility() == View.VISIBLE) {
            binding.rvRatings.setVisibility(View.GONE);
            binding.layoutProgress.setVisibility(View.VISIBLE);
        }
        Call<APIResponse.ResponseRating> call = apiInterFace.getRating(page, "application/json",
                "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                "ar"
        );
        call.enqueue(new Callback<APIResponse.ResponseRating>() {
            @Override
            public void onResponse(@NonNull Call<APIResponse.ResponseRating> call,
                                   @NonNull Response<APIResponse.ResponseRating> response) {

                if (response.code() == 200) {
                    assert response.body() != null;

                    adapterRatinge = new AdapterRatinge((ArrayList<Rating>) response.body().getRatings(),
                            RatingUsersActivity.this, new AdapterRatinge.OnclickMessage() {
                        @Override
                        public void itemOnclickNewRating(Rating dataNotification) {
                            showDialogUpdateRating(dataNotification);

                        }
                    });
                    binding.rvRatings.setAdapter(adapterRatinge);
                    binding.layoutProgress.setVisibility(View.GONE);
                    binding.rvRatings.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseRating> call, @NonNull Throwable t) {

            }
        });


    }
    float getrating;
    private void showDialogUpdateRating(Rating rating) {
        View customLayout = LayoutInflater.from(this).inflate(R.layout.dilog_rating, null);


        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setView(customLayout);

        FrameLayout btnRating = customLayout.findViewById(R.id.btn_save_rating);
        TextView tv_lable = customLayout.findViewById(R.id.tv_btn_update);
        EditText ed_text = customLayout.findViewById(R.id.ed_update_raing);
        ProgressBar progressBar = customLayout.findViewById(R.id.progressbarRating);
        AppCompatRatingBar ratingBar = customLayout.findViewById(R.id.tv_rating_dialog);
        ratingBar.setRating(Float.parseFloat(rating.getStars()));

        ratingBar.setOnRatingBarChangeListener((ratingBar1, rating1, fromUser) -> getrating= ratingBar1.getRating());
        btnRating.setOnClickListener(v -> {

            progressBar.setVisibility(View.VISIBLE);
            tv_lable.setVisibility(View.GONE);

            Log.d("ratingaaa", "showDialogUpdateRating: " +
                    rating.getTo() + rating.getId() + ed_text.getText()
                    .toString() + "\n" + shardEditor.loadData().get(ShardEditor.KEY_TOKEN)
                    + "\n"+Math.round(ratingBar.getRating()));

            Call<APIResponse.ResponseUpdateRating> call = apiInterFace.upDateRating(
                    rating.getTo()
                    , ed_text.getText().toString(),
                    Math.round(ratingBar.getRating()) + "", rating.getId() + "", "application/json",
                    "Bearer" + " "+ shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                    shardEditor.loadData().get(ShardEditor.KEY_LANG)
            );

            call.enqueue(new Callback<APIResponse.ResponseUpdateRating>() {
                @Override
                public void onResponse(@NonNull Call<APIResponse.ResponseUpdateRating> call,
                                       @NonNull Response<APIResponse.ResponseUpdateRating> response) {
                    if (response.code() == 200) {

                        if (response.body().getStatus()) {
                            Toast.makeText(RatingUsersActivity.this,
                                    "تم التعديل ",
                                    Toast.LENGTH_SHORT).show();

                        } else {

                            Toast.makeText(
                                    RatingUsersActivity.this,
                                    response.body().getMessage().get(0).toString() + "",
                                    Toast.LENGTH_SHORT)
                                    .show();

                        }

                        progressBar.setVisibility(View.GONE);
                        tv_lable.setVisibility(View.VISIBLE);

                        alertDialog.dismiss();

                        loadRatings("1");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<APIResponse.ResponseUpdateRating> call, @NonNull Throwable t) {

                }
            });


        });
        builder.setCancelable(true);
        alertDialog = builder.create();
        alertDialog.show();

    }
}