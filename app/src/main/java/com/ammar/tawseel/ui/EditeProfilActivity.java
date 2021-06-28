package com.ammar.tawseel.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ammar.tawseel.R;
import com.ammar.tawseel.databinding.ActivityEditeProfilBinding;
import com.ammar.tawseel.databinding.LayoutPicPicureBinding;
import com.ammar.tawseel.editor.ShardEditor;
import com.ammar.tawseel.netWorke.APIClient;
import com.ammar.tawseel.netWorke.APIInterFace;
import com.ammar.tawseel.pojo.response.APIResponse;
import com.ammar.tawseel.ui.auth.SelectLocationActivity;
import com.ammar.tawseel.ui.auth.UserProfilActivity;
import com.ammar.tawseel.ui.home.HomeActivity;
import com.ammar.tawseel.uitllis.Cemmon;
import com.ammar.tawseel.uitllis.PathVideo;
import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditeProfilActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityEditeProfilBinding binding;
    ShardEditor shardEditor;
    String latitude, longitude;
    Uri saveuri;
    LayoutPicPicureBinding layoutPicPicureBinding;
    APIInterFace apiInterFace;
    CircleImageView imgProfile;
    TextView tv_nam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        shardEditor = new ShardEditor(this);

        if (shardEditor.loadData().get(ShardEditor.KEY_LANG) != "") {

            Cemmon.setLocale(this, shardEditor.loadData().get(ShardEditor.KEY_LANG));

        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edite_profil);

        if (Cemmon.isNetworkOnline(this)) {

            if (binding.layoutLocationInternet.getVisibility() == View.VISIBLE) {
                binding.layoutLocationInternet.setVisibility(View.GONE);
            }
            binding.layoutHome.setVisibility(View.VISIBLE);


            apiInterFace = APIClient.getClient().create(APIInterFace.class);

            onClicksButtons();


            inItView();

            loadDataProfile();
            openDraw();
            binding.imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

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


    private void onClicksButtons() {
        binding.imgEdite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                binding.inputLayoutLocation.setEnabled(true);
//                binding.editTextName.setEnabled(true);
//                binding.editTextPhone.setEnabled(true);
//                binding.profileImage.setEnabled(true);

            }
        });

//        binding.imgMap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(EditeProfilActivity.this, SelectLocationActivity.class);
//                startActivityForResult(intent, 1);
//            }
//        });

        binding.profileImagee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialogBottomSheetLogin();
            }
        });
        binding.btnSave.setOnClickListener(v -> {
            binding.layoutProgress.setVisibility(View.VISIBLE);
            binding.layoutHome.setVisibility(View.GONE);
            if (latitude == null || longitude == null) {
                latitude = Cemmon.latude + "";
                longitude = Cemmon.langtude + "";

            }
            editeProfil(0.0, 0.0, "null", binding.editTextName.getText().toString()
                    , Objects.requireNonNull(binding.editTextPhone.getText()).toString(), Cemmon.BASE_URL + saveuri);
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
                        binding.layoutHome.setVisibility(View.VISIBLE);
                        binding.layoutProgress.setVisibility(View.GONE);
                        Objects.requireNonNull(binding.inputLayoutName.getEditText()).setText(response.body().getData().getName());
                        Objects.requireNonNull(binding.inputLayoutPhon.getEditText()).setText(response.body().getData().getPhone() + "");
                        //  Objects.requireNonNull(binding.inputLayoutLocation.getEditText()).setText(response.body().getData().getGpsAddress() + "");
                        Log.d("dataprofil", "onResponse: " + response.body().getData().getName());
                        if (response.body().getData().getAvatar() != null && !response.body().getData().getAvatar().equals("")) {

                            Cemmon.NAME_OF_USER = response.body().getData().getName();
                            tv_nam.setText(Cemmon.NAME_OF_USER + "");
                            Cemmon.IMAGE_OF_USER = response.body().getData().getAvatar() + "";
                            Log.d("iiiiiiiiiiii", "onResponse: " + Cemmon.IMAGE_OF_USER);
                            Picasso.with(EditeProfilActivity.this)
                                    .load(Cemmon.BASE_URL + Cemmon.IMAGE_OF_USER).placeholder(R.drawable.imagerat)
                                    .into(imgProfile);
                            Picasso.with(EditeProfilActivity.this)
                                    .load(Cemmon.BASE_URL + Cemmon.IMAGE_OF_USER).placeholder(R.drawable.imagerat)
                                    .into(binding.profileImagee);


                        }
                    } else {
                        Toast.makeText(EditeProfilActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        binding.layoutHome.setVisibility(View.VISIBLE);
                        binding.layoutProgress.setVisibility(View.GONE);
                    }

                } else if (response.code() == 401) {
                    shardEditor.logOut();
                }


            }

            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseShowProfile> call, @NonNull Throwable t) {
//                Toast.makeText(EditeProfilActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                binding.layoutHome.setVisibility(View.VISIBLE);
                binding.layoutProgress.setVisibility(View.GONE);
            }
        });
    }

    Bitmap bitmap;
    String imageString;
    String imagepath;
    MultipartBody.Part multipartBody;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

//            case 1:
//                if (resultCode == RESULT_OK) {
//                    String tv_location = data.getStringExtra("location");
//                    latitude = data.getStringExtra("lantuid");
//                    longitude = data.getStringExtra("lang");
//                    Log.d("eeeeeeeee", "onActivityResult: "+longitude+latitude);
//                    binding.editTextLocation.setText(tv_location);
//
//                }
//                break;
            case 0:
                if (resultCode == RESULT_OK) {


                    File file = new File(Objects.requireNonNull(PathVideo.getPath(this, data.getData())));
                    RequestBody requestFile = RequestBody.create((MediaType.parse("multipart/form-data")),
                            file);
                    multipartBody = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);

                    Log.d("mmmmmmmmmm", "onActivityResult: " + multipartBody + "\n" + file);
                    try {
                        //getting image from gallery
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                        //Setting image to ImageView
                        binding.profileImagee.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //converting image to base64 string


//                    imageString =getRealPathFromUri(filePath);


                }

                break;


            case 2:
                if (resultCode == RESULT_OK) {

                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    Uri uri = getImageUri(photo);
                    File file = new File(Objects.requireNonNull(PathVideo.getPath(this, uri)));
                    RequestBody requestFile = RequestBody.create((MediaType.parse("multipart/form-data")),
                            file);
                    multipartBody = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);

                    try {
                        //getting image from gallery


                        //Setting image to ImageView
                        binding.profileImagee.setImageBitmap(photo);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //converting image to base64 string


                    imageString = getRealPathFromURI(uri);

                    Log.d("aaaaaaaaaaaaa", "onActivityResult: " + imageString);
                }

                break;

        }
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
                    startActivity(new Intent(EditeProfilActivity.this, HomeActivity.class));
                    finish();
                }

            }
        }.start();

    }

    private Uri getImageUri(Bitmap photo) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), photo, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private void editeProfil(double latitude, double longitude, String addresss, String name, String phone, String saveuri) {
        RequestBody usernameBody = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody userphoneBody = RequestBody.create(MediaType.parse("text/plain"), phone);


        Call<APIResponse.ResponseProfile> call = apiInterFace.editProfile(
                latitude
                , longitude, addresss, usernameBody, userphoneBody, multipartBody, "application/json", "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN));


        call.enqueue(new Callback<APIResponse.ResponseProfile>() {
            @Override
            public void onResponse(@NonNull Call<APIResponse.ResponseProfile> call,
                                   @NonNull Response<APIResponse.ResponseProfile> response) {

                if (response.code() == 200) {


                    if (response.body().getStatus()) {
                        binding.layoutHome.setVisibility(View.VISIBLE);
                        binding.layoutProgress.setVisibility(View.GONE);

                        Log.d("avatar", "onResponse: " + response.body().getData().getName());
                        showDialogScuccess(R.layout.dialog_success, 1);

                    } else {
                        binding.layoutHome.setVisibility(View.VISIBLE);
                        binding.layoutProgress.setVisibility(View.GONE);
                        Toast.makeText(EditeProfilActivity.this, "" + response.body().getMessage().get(0), Toast.LENGTH_SHORT).show();

                        showDialogScuccess(R.layout.dialog_success, 0);
                    }


                }
            }

            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseProfile> call, @NonNull Throwable t) {
                binding.layoutHome.setVisibility(View.VISIBLE);
                binding.layoutProgress.setVisibility(View.GONE);
                showDialogScuccess(R.layout.dialog_success, 0);
            }
        });


    }

    private void getDialogBottomSheetLogin() {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        layoutPicPicureBinding = DataBindingUtil.inflate(
                LayoutInflater.from(this)
                , R.layout.layout_pic_picure, findViewById(R.id.CountenerPick), false
        );
        bottomSheetDialog.setContentView(layoutPicPicureBinding.getRoot());

        //-----optional section-------//
        FrameLayout frameLayout = bottomSheetDialog.findViewById(
                com.google.android.material.R.id.design_bottom_sheet
        );
        if (frameLayout != null) {
            BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
            bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        }
        //-----optional section-------//
        bottomSheetDialog.show();

        layoutPicPicureBinding.layoutPickGallry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPictureFromGalary();
                bottomSheetDialog.dismiss();
            }
        });
        layoutPicPicureBinding.layoutPickCanera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPictureFromCamera();
                bottomSheetDialog.dismiss();
            }
        });

        layoutPicPicureBinding.imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

    }

    private void getPictureFromCamera() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, 2);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }

    }

    @SuppressLint("WrongConstant")
    private void openDraw() {
        binding.toogles.setOnClickListener((View.OnClickListener) v -> {


            binding.draw.openDrawer(Gravity.START);

        });
    }

    private void getPictureFromGalary() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 0);
    }

    private String getStringImage(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        final String temp = Base64.encodeToString(b, Base64.DEFAULT);
        Log.i("My_data_image", "" + temp);
        return temp;
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

    private void isLoginWitch() {
        GoogleSignInOptions gso = new GoogleSignInOptions.
                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
        googleSignInClient.signOut();

        LoginManager.getInstance().logOut();

        shardEditor.logOut();

    }

    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View v) {
        Fragment fragment = null;
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

                startActivity(new Intent(EditeProfilActivity.this, OrdersActivity.class));
                binding.draw.closeDrawer(Gravity.START);

                break;
            case R.id.tv_rates:


                binding.draw.closeDrawer(Gravity.START);

                startActivity(new Intent(EditeProfilActivity.this, RatingUsersActivity.class));

                break;
            case R.id.tv_sitting:

                binding.draw.closeDrawer(Gravity.START);
                startActivity(new Intent(EditeProfilActivity.this, SettingsActivity.class));
                break;
            case R.id.tv_about_us:

                binding.draw.closeDrawer(Gravity.START);
                startActivity(new Intent(EditeProfilActivity.this, WhoUsActivity.class));
                break;
            case R.id.tv_call_us:


                binding.draw.closeDrawer(Gravity.START);
                startActivity(new Intent(EditeProfilActivity.this, ContactUsActivity.class));
                break;
            case R.id.tv_plociy:


                binding.draw.closeDrawer(Gravity.START);
                startActivity(new Intent(EditeProfilActivity.this, PrivacyPolicyActivity.class));
                break;
            case R.id.tv_shar:

                startActivity(new Intent(EditeProfilActivity.this, ShareAppActivity.class));
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
}