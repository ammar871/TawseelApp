package com.ammar.tawseel.ui.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.ammar.tawseel.R;
import com.ammar.tawseel.databinding.ActivityForgetPasswordBinding;
import com.ammar.tawseel.databinding.ActivityForgetPasswordBindingImpl;
import com.ammar.tawseel.editor.ShardEditor;
import com.ammar.tawseel.netWorke.APIClient;
import com.ammar.tawseel.netWorke.APIInterFace;
import com.ammar.tawseel.pojo.response.APIResponse;
import com.ammar.tawseel.ui.PrivacyPolicyActivity;
import com.ammar.tawseel.ui.PrivacyPolicyActivityTwo;
import com.ammar.tawseel.ui.home.HomeActivity;
import com.ammar.tawseel.uitllis.Cemmon;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends AppCompatActivity {
    ActivityForgetPasswordBinding binding;
    ShardEditor shardEditor;
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forget_password);
        apiInterFace = APIClient.getClient().create(APIInterFace.class);

        if (shardEditor.loadData().get(ShardEditor.KEY_LANG) != "") {

            Cemmon.setLocale(this, shardEditor.loadData().get(ShardEditor.KEY_LANG));

        }
        binding.layoutLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValueLogin()){
                    callAPILogin(binding.editTextName.getText().toString().trim(),binding.editTextPass.getText().toString().trim());
                }
            }
        });
        textSpan();
    }

    private void showDialogScuccess(int dialog_success, int statuts) {


        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(dialog_success, null);


        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        new CountDownTimer(3000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onFinish() {
                // TODO Auto-generated method stub
                alertDialog.dismiss();
                if (statuts == 1) {
                    startActivity(new Intent(ForgetPasswordActivity.this, HomeActivity.class));
                    finish();
                }

            }
        }.start();

    }
    private void textSpan() {
        String text = getString(R.string.proxy);
        SpannableString ss = new SpannableString(text);
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(new Intent(ForgetPasswordActivity.this, PrivacyPolicyActivityTwo.class));
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.text_color));
                ds.setUnderlineText(true);
            }
        };

        ss.setSpan(clickableSpan1, 42, 74, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        binding.tvPoxy.setText(ss);
        binding.tvPoxy.setMovementMethod(LinkMovementMethod.getInstance());
    }
    public void callAPILogin(String username, String Email) {

        binding.progressbar.setVisibility(View.VISIBLE);
        binding.imgLogin.setVisibility(View.GONE);

        Call<APIResponse.RestPassResponse> call = apiInterFace.forgetPass(
                username, Email);

        call.enqueue(new Callback<APIResponse.RestPassResponse>() {
            @Override
            public void onResponse(@NonNull Call<APIResponse.RestPassResponse> call,
                                   @NonNull Response<APIResponse.RestPassResponse> response) {


                if (response.code() == 200) {

                    if (response.body().getStatus()) {
                        binding.progressbar.setVisibility(View.GONE);
                        binding.imgLogin.setVisibility(View.VISIBLE);
                        Toast.makeText(ForgetPasswordActivity.this, getString(R.string.send_to_email), Toast.LENGTH_SHORT).show();
                        new CountDownTimer(3000, 1000) {

                            @Override
                            public void onTick(long millisUntilFinished) {
                                // TODO Auto-generated method stub

                            }

                            @Override
                            public void onFinish() {
                                // TODO Auto-generated method stub

                                    startActivity(new Intent(ForgetPasswordActivity.this, LoginActivity.class));
                                    finish();
                                }


                        }.start();

                    } else {
                        Toast.makeText(ForgetPasswordActivity.this, response.body().getMessage().get(0) + "", Toast.LENGTH_SHORT).show();
                        binding.progressbar.setVisibility(View.GONE);
                        binding.imgLogin.setVisibility(View.VISIBLE);
                    }



                }

            }


            @Override
            public void onFailure(@NonNull Call<APIResponse.RestPassResponse> call, @NonNull Throwable t) {
                binding.progressbar.setVisibility(View.GONE);
                binding.imgLogin.setVisibility(View.VISIBLE);

            }
        });

    }
    private boolean isValueLogin() {

        if (binding.inputLayoutName.getEditText().getText().toString().isEmpty()
                && binding.inputLayoutName.getEditText().getText().toString().equals("")
        ) {

            binding.inputLayoutName.getEditText().setError(getString(R.string.error_name));
            return false;

        } else if (binding.inputLayoutPass.getEditText().getText().toString().isEmpty()
                && binding.inputLayoutPass.getEditText().getText().toString().equals("")) {
            binding.inputLayoutPass.getEditText().setError(getString(R.string.error_pass));
            return false;

        } else {

            return true;
        }

    }
}