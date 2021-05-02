package com.ammar.tawseel.ui.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.ammar.tawseel.R;
import com.ammar.tawseel.databinding.ActivityLoginBinding;
import com.ammar.tawseel.editor.ShardEditor;
import com.ammar.tawseel.netWorke.APIClient;
import com.ammar.tawseel.netWorke.APIInterFace;
import com.ammar.tawseel.pojo.response.APIResponse;
import com.ammar.tawseel.ui.home.HomeActivity;
import com.ammar.tawseel.uitllis.Cemmon;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final int LOCATION_SETTINGS_REQUEST = 101;
    ActivityLoginBinding binding;
    APIInterFace apiInterFace;
    ShardEditor shardEditor;
    GoogleSignInClient mGoogleSignInClient;
    //for facebook
   private CallbackManager callbackManager;
    private static final int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        shardEditor = new ShardEditor(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        apiInterFace = APIClient.getClient().create(APIInterFace.class);
        enableLoc();
        printHashKey();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        callbackManager = CallbackManager.Factory.create();
        onClicksButtons();


        textSpan();
        binding.loginFace.setOnClickListener(v -> {



            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile","email"));
            loginManferFace();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (shardEditor.loadDataEnter().get(ShardEditor.IS_LOGIN_WITH)) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        } else {


        }

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void onClicksButtons() {
        binding.tvForget.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this,
                    ForgetPasswordActivity.class));
            overridePendingTransition(R.anim.enter, R.anim.exite);
        });

        binding.tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            overridePendingTransition(R.anim.enter, R.anim.exite);
        });


        binding.layoutLogin.setOnClickListener(v -> {

            if (isValueLogin()) {
                callAPILogin(Objects.requireNonNull(binding.inputLayoutName.getEditText()).getText().toString().trim(),
                        Objects.requireNonNull(binding.inputLayoutPass.getEditText()).getText().toString().trim());
            } else {

                Toast.makeText(LoginActivity.this, "من فضلك املئ البيانات ....", Toast.LENGTH_SHORT).show();

            }
        });

        binding.loginGoogle.setOnClickListener(v -> {
            binding.loginGoogle.setVisibility(View.GONE);
            binding.progressbarGoogle.setVisibility(View.VISIBLE);

            signIn();
        });


    }

    private void loginManferFace() {

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("resulte", loginResult.getAccessToken().getUserId() + "");

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        if (object != null) {

                                            String name_fb = object.optString("name");
                                            String id_fb = object.optString("id");
                                            String email_fb = object.optString("email");
                                            Log.d("facebook", "onCompleted: "+name_fb +"\n "+ email_fb +"\n "+ id_fb);
loginWithSocial(email_fb,id_fb,name_fb,"facebook");
//                                            shardUserSaved = new ShardUserSaved(name_fb, "not"
//                                                    , email_fb, "not", "not");
//                                            sharedEditor.saveData(shardUserSaved);


                                        }


                                    }
                                });

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email");
                        request.setParameters(parameters);
                        request.executeAsync();


//                        getUserProfile(loginResult.getAccessToken());


                        new Handler().postDelayed(new Runnable() {
                            public void run() {

                            }
                        }, 1000);
                    }

                    @Override
                    public void onCancel() {
                        Log.d("resulte", "cancel" + "");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d("resulte", "error" + exception + "");
                    }
                });
    }


    private boolean isSignedIn() {

        return GoogleSignIn.getLastSignedInAccount(this) != null;

    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        return accessToken != null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

        handleSignInResult(result);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                String personName = acct.getDisplayName();
                String personemail = acct.getEmail();
                String personId = acct.getId();

//                shardEditor.saveToken(personToken);
            loginWithSocial(personemail,personId,personName,"google");

            }


        } else {
            binding.loginGoogle.setVisibility(View.VISIBLE);
            binding.progressbarGoogle.setVisibility(View.GONE);

        }
    }

    private void loginWithSocial(String personemail, String personId, String personName, String typ) {

        Call<APIResponse.ResponseLogin> call = apiInterFace.registerSocialGoogle(typ,
                personemail, personId,personName,"android", Cemmon.FIREBASE_TOKEN);

        call.enqueue(new Callback<APIResponse.ResponseLogin>() {
            @Override
            public void onResponse(@NonNull Call<APIResponse.ResponseLogin> call,
                                   @NonNull Response<APIResponse.ResponseLogin> response) {
                binding.loginGoogle.setVisibility(View.VISIBLE);
                binding.progressbarGoogle.setVisibility(View.GONE);

                if (response.code() == 200) {

                    assert response.body() != null;
                    if (response.body().getStatus()) {
                        shardEditor.saveDataLoginWith(true);
                        shardEditor.saveToken(response.body().getToken());

                        assert response.body() != null;
//                        Log.d("tokengoogle", "onResponse: "+response.body().getToken());
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();


                    } else {
                        Toast.makeText(LoginActivity.this,response.body().getMessage().get(0), Toast.LENGTH_SHORT).show();

                    }

                }

            }


            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseLogin> call, @NonNull Throwable t) {
                binding.loginGoogle.setVisibility(View.VISIBLE);
                binding.progressbarGoogle.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void textSpan() {
        String text = "عند قيامك بتسجيل جديد، فانك توافق على كافة شروط سياسة الخصوصية و الاستخدام";
        SpannableString ss = new SpannableString(text);
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Toast.makeText(LoginActivity.this, "One", Toast.LENGTH_SHORT).show();
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

    public void callAPILogin(String username, String password) {

        binding.progressbar.setVisibility(View.VISIBLE);
        binding.imgLogin.setVisibility(View.GONE);

        Call<APIResponse.ResponseLogin> call = apiInterFace.loginAPI(
                username, password,"android", Cemmon.FIREBASE_TOKEN);

        call.enqueue(new Callback<APIResponse.ResponseLogin>() {
            @Override
            public void onResponse(@NonNull Call<APIResponse.ResponseLogin> call,
                                   @NonNull Response<APIResponse.ResponseLogin> response) {


                if (response.code() == 200) {

                    assert response.body() != null;
                    if (response.body().getStatus()) {
                        shardEditor.saveDataLoginWith(true);
                        shardEditor.saveToken(response.body().getToken());
//                        shardEditor.saveData(response.body().getUser());
                        assert response.body() != null;
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();

                        binding.progressbar.setVisibility(View.GONE);
                        binding.imgLogin.setVisibility(View.VISIBLE);

                    } else {
                        Toast.makeText(LoginActivity.this,response.body().getMessage().get(0), Toast.LENGTH_SHORT).show();
                        binding.progressbar.setVisibility(View.GONE);
                        binding.imgLogin.setVisibility(View.VISIBLE);
                    }

                }

            }


            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseLogin> call, @NonNull Throwable t) {
                binding.progressbar.setVisibility(View.GONE);
                binding.imgLogin.setVisibility(View.VISIBLE);
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    ///==============================Valdiate================
    private boolean vaildatEmail(String email) {


        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        return email.matches(emailPattern);
    }

    private static boolean isValidPhoneNumber(String phoneNumber) {
        return !TextUtils.isEmpty(phoneNumber) && android.util.Patterns.PHONE.matcher(phoneNumber).matches();
    }

    private boolean isValueLogin() {

        if (binding.inputLayoutName.getEditText().getText().toString().isEmpty()
                && binding.inputLayoutName.getEditText().getText().toString().equals("")
        ) {

            binding.inputLayoutName.getEditText().setError("أدخــل اسمك ");
            return false;

        } else if (binding.inputLayoutPass.getEditText().getText().toString().isEmpty()
                && binding.inputLayoutPass.getEditText().getText().toString().equals("")) {
            binding.inputLayoutPass.getEditText().setError("أدخــل الرقم السرى ");
            return false;

        } else {

            return true;
        }

    }



    private void printHashKey() {

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.ammar.tawseel",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    private void enableLoc() {



        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);


        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result =
                LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {


            @Override
            public void onComplete(Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);


                    // All location settings are satisfied. The client can initialize location
                    // requests here.

                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.
                            try {
                                // Cast to a resolvable exception.
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                resolvable.startResolutionForResult(
                                        LoginActivity.this,
                                        LOCATION_SETTINGS_REQUEST);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            } catch (ClassCastException e) {
                                // Ignore, should be an impossible error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            }
        });

    }
}