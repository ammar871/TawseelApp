package com.ammar.tawseel.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.ammar.tawseel.R;
import com.ammar.tawseel.databinding.ActivityPrivacyPolicyBinding;
import com.ammar.tawseel.databinding.ActivityWhoUsBinding;
import com.ammar.tawseel.editor.ShardEditor;
import com.ammar.tawseel.netWorke.APIClient;
import com.ammar.tawseel.netWorke.APIInterFace;
import com.ammar.tawseel.pojo.response.APIResponse;
import com.ammar.tawseel.uitllis.Cemmon;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrivacyPolicyActivity extends AppCompatActivity {
    ShardEditor shardEditor;
    APIInterFace apiInterFace;
    ActivityPrivacyPolicyBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        shardEditor = new ShardEditor(this);
        if (shardEditor.loadData().get(ShardEditor.KEY_LANG) != "") {

            Cemmon.setLocale(this, shardEditor.loadData().get(ShardEditor.KEY_LANG));

        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_privacy_policy);
        apiInterFace = APIClient.getClient().create(APIInterFace.class);

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        loadData();

    }

    private void loadData() {

        Call<APIResponse.PoeloyProxyResponse> call = apiInterFace.poeloyProxyResponse("application/json",
                "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                shardEditor.loadData().get(ShardEditor.KEY_LANG));
        call.enqueue(new Callback<APIResponse.PoeloyProxyResponse>() {
            @Override
            public void onResponse(Call<APIResponse.PoeloyProxyResponse> call, Response<APIResponse.PoeloyProxyResponse> response) {
                if (response.code() == 200) {
                    binding.layoutHome.setVisibility(View.VISIBLE);
                    binding.layoutProgress.setVisibility(View.GONE);
                    if (response.body().getData().size() > 0) {

                        for (int i = 0; i < response.body().getData().size(); i++) {
                            binding.tvPloicy.append(response.body().getData().get(i));
                            binding.tvPloicy.append("\n");
                        }
                    }
                } else {
                    binding.layoutHome.setVisibility(View.VISIBLE);
                    binding.layoutProgress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<APIResponse.PoeloyProxyResponse> call, Throwable t) {
                binding.layoutHome.setVisibility(View.VISIBLE);
                binding.layoutProgress.setVisibility(View.GONE);
            }
        });
    }
}