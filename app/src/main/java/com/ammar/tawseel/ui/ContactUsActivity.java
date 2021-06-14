package com.ammar.tawseel.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ammar.tawseel.R;
import com.ammar.tawseel.databinding.ActivityContactUsBinding;
import com.ammar.tawseel.databinding.ActivityWhoUsBinding;
import com.ammar.tawseel.editor.ShardEditor;
import com.ammar.tawseel.netWorke.APIClient;
import com.ammar.tawseel.netWorke.APIInterFace;
import com.ammar.tawseel.pojo.response.APIResponse;
import com.ammar.tawseel.uitllis.Cemmon;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUsActivity extends AppCompatActivity {
    ShardEditor shardEditor;
    APIInterFace apiInterFace;
    ActivityContactUsBinding binding;
    String numberWhatsResponse;

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

                    Toast.makeText(ContactUsActivity.this, "Please,,input your details ", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ContactUsActivity.this, "تم ارسال رسالتك ", Toast.LENGTH_SHORT).show();


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
        String contact = "+2"+numberWhatsResponse;
        String url = "https://api.whatsapp.com/send?phone=" + contact;
        try {
            PackageManager pm = getPackageManager();
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(ContactUsActivity.this, "ليس لديه واتس ", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}