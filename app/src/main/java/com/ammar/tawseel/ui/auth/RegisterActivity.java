package com.ammar.tawseel.ui.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.ammar.tawseel.R;
import com.ammar.tawseel.databinding.ActivityRegisterBinding;
import com.ammar.tawseel.editor.ShardEditor;
import com.ammar.tawseel.netWorke.APIClient;
import com.ammar.tawseel.netWorke.APIInterFace;
import com.ammar.tawseel.pojo.data.DataUser;
import com.ammar.tawseel.pojo.response.APIResponse;
import com.ammar.tawseel.ui.EditeProfilActivity;
import com.ammar.tawseel.ui.PrivacyPolicyActivity;
import com.ammar.tawseel.ui.PrivacyPolicyActivityTwo;
import com.ammar.tawseel.ui.home.HomeActivity;
import com.ammar.tawseel.uitllis.Cemmon;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;
    APIInterFace apiInterFace;
    ShardEditor shardEditor;
    String visbilty = "hide";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        shardEditor = new ShardEditor(this);
        if (shardEditor.loadData().get(ShardEditor.KEY_LANG) != "") {

            Cemmon.setLocale(this, shardEditor.loadData().get(ShardEditor.KEY_LANG));

        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);

        if (Cemmon.isNetworkOnline(this)) {

            if (binding.layoutLocationInternet.getVisibility() == View.VISIBLE) {
                binding.layoutLocationInternet.setVisibility(View.GONE);
            }

            binding.layoutHome.setVisibility(View.VISIBLE);

            visibaltyPass();

            apiInterFace = APIClient.getClient().create(APIInterFace.class);

            binding.layoutRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isValue()) {
                        Cemmon.hideKeyboard(RegisterActivity.this);
                        callRegisterApi(binding.inputLayoutName.getEditText().getText().toString()
                                , binding.inputLayoutPass.getEditText().getText().toString(),
                                binding.inputLayoutEmail.getEditText().getText().toString());


                    }


                }
            });


            binding.tvLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                }
            });
            textSpan();


        } else {
            binding.layoutLocationInternet.setVisibility(View.VISIBLE);
            binding.layoutHome.setVisibility(View.GONE);
        }


        binding.btnDissmes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_MAIN, null);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.wifi.WifiSettings");
                    intent.setComponent(cn);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (ActivityNotFoundException ignored) {
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                }
            }
        });

    }

    private void visibaltyPass() {
        binding.editTextPass.setTransformationMethod(new PasswordTransformationMethod());
        if (visbilty.equals("hide")) {
            binding.showHideBtn.setImageResource(R.drawable.ic_baseline_visibility_off_24);
            binding.editTextPass.setTransformationMethod(new PasswordTransformationMethod());


        } else if (visbilty.equals("show")) {
            binding.showHideBtn.setImageResource(R.drawable.ic_baseline_visibility_24);
            binding.editTextPass.setTransformationMethod(null);
        }
        binding.showHideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (visbilty.equals("hide")) {
                    binding.showHideBtn.setImageResource(R.drawable.ic_baseline_visibility_24);
                    binding.editTextPass.setTransformationMethod(null);


                    visbilty = "show";

                } else if (visbilty.equals("show")) {
                    binding.showHideBtn.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                    binding.editTextPass.setTransformationMethod(new PasswordTransformationMethod());
                    visbilty = "hide";
                }
            }
        });
    }

    private void callRegisterApi(String name, String pass, String email) {

        binding.progressbar.setVisibility(View.VISIBLE);
        binding.imgRegister.setVisibility(View.GONE);

        Call<APIResponse.ResponseRegister> call = apiInterFace.registerAPI(
                shardEditor.loadData().get(ShardEditor.KEY_LANG),
                name, pass, email, "android", Cemmon.FIREBASE_TOKEN);

        call.enqueue(new Callback<APIResponse.ResponseRegister>() {
            @Override
            public void onResponse(@NonNull Call<APIResponse.ResponseRegister> call,
                                   @NonNull Response<APIResponse.ResponseRegister> response) {


                if (response.code() == 200) {

                    assert response.body() != null;
                    if (response.body().getStatus()) {
                        shardEditor.saveDataLoginWith(true);


                        shardEditor.saveToken(response.body().getToken());
                        Cemmon.NAME_OF_USER = response.body().getData().getUsername();


                        assert response.body() != null;
                        binding.progressbar.setVisibility(View.GONE);
                        binding.imgRegister.setVisibility(View.VISIBLE);
                        startActivity(new Intent(RegisterActivity.this, UserProfilActivity.class));
                        finish();



                    } else {
                        Toast.makeText(RegisterActivity.this, response.body().getMessage().get(0).toString(), Toast.LENGTH_SHORT).show();
                        binding.progressbar.setVisibility(View.GONE);
                        binding.imgRegister.setVisibility(View.VISIBLE);
                    }

                }

            }


            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseRegister> call, @NonNull Throwable t) {
                binding.progressbar.setVisibility(View.GONE);
                binding.imgRegister.setVisibility(View.VISIBLE);

            }
        });

    }

    ///============================== Valdiate ================
    private boolean vaildatEmail(String email) {


        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        return email.matches(emailPattern);
    }

    private boolean isValue() {

        if (binding.inputLayoutName.getEditText().getText().toString().isEmpty()
                && binding.inputLayoutName.getEditText().getText().toString().equals("")
        ) {

            binding.inputLayoutName.getEditText().setError(getString(R.string.error_name));
            return false;

        } else if (binding.inputLayoutPass.getEditText().getText().toString().isEmpty()
                && binding.inputLayoutPass.getEditText().getText().toString().equals("")) {
            binding.inputLayoutPass.getEditText().setError(getString(R.string.error_pass));
            return false;

        } else if (binding.inputLayoutEmail.getEditText().getText().toString().isEmpty()
                && binding.inputLayoutEmail.getEditText().getText().toString().equals("") &&
                vaildatEmail(binding.inputLayoutEmail.getEditText().getText().toString())) {
            binding.inputLayoutPass.getEditText().setError(getString(R.string.error_email));
            return false;

        } else {

            return true;
        }

    }


    private void textSpan() {
        String text = getString(R.string.proxy);
        SpannableString ss = new SpannableString(text);
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(new Intent(RegisterActivity.this, PrivacyPolicyActivityTwo.class));
                //  Toast.makeText(RegisterActivity.this, "One", Toast.LENGTH_SHORT).show();
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
}