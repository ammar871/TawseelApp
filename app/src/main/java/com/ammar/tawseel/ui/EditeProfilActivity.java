package com.ammar.tawseel.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
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
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditeProfilActivity extends AppCompatActivity {
    ActivityEditeProfilBinding binding;
    ShardEditor shardEditor;
    String latitude, longitude;
    Uri saveuri;
    LayoutPicPicureBinding layoutPicPicureBinding;
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edite_profil);

        if (Cemmon.isNetworkOnline(this)) {

            if (binding.layoutLocationInternet.getVisibility() == View.VISIBLE) {
                binding.layoutLocationInternet.setVisibility(View.GONE);
            }
            binding.layoutHome.setVisibility(View.VISIBLE);


            apiInterFace = APIClient.getClient().create(APIInterFace.class);
            onClicksButtons();

            loadDataProfile();
            binding.imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

        }
        else {
            binding.layoutLocationInternet.setVisibility(View.VISIBLE);
            binding.layoutHome.setVisibility(View.GONE);
        }

        binding.btnDissmes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent intent = new Intent(Intent.ACTION_MAIN, null);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.wifi.WifiSettings");
                    intent.setComponent(cn);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }catch (ActivityNotFoundException ignored){
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

        binding.imgMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditeProfilActivity.this, SelectLocationActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        binding.profileImage.setOnClickListener(new View.OnClickListener() {
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
            editeProfil(Double.parseDouble(latitude),Double.parseDouble(longitude), Objects.requireNonNull(binding.editTextLocation.getText()).toString(), binding.editTextName.getText().toString()
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
                        Objects.requireNonNull(binding.inputLayoutLocation.getEditText()).setText(response.body().getData().getGpsAddress() + "");
                        Log.d("dataprofil", "onResponse: " + response.body().getData().getName());
                        if (response.body().getData().getAvatar() != null && !response.body().getData().getAvatar().equals("")) {
                            Glide.with(EditeProfilActivity.this)
                                    .load(Cemmon.BASE_URL + response.body().getData().getAvatar())
                                    .into(binding.profileImage);

                        }
                    } else {
                        Toast.makeText(EditeProfilActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        binding.layoutHome.setVisibility(View.VISIBLE);
                        binding.layoutProgress.setVisibility(View.GONE);
                    }

                } else {
                    Toast.makeText(EditeProfilActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    binding.layoutHome.setVisibility(View.VISIBLE);
                    binding.layoutProgress.setVisibility(View.GONE);
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

            case 1:
                if (resultCode == RESULT_OK) {
                    String tv_location = data.getStringExtra("location");
                    latitude = data.getStringExtra("lantuid");
                    longitude = data.getStringExtra("lang");
                    Log.d("eeeeeeeee", "onActivityResult: "+longitude+latitude);
                    binding.editTextLocation.setText(tv_location);

                }
                break;
            case 0:
                if (resultCode == RESULT_OK) {


                    File file = new File(Objects.requireNonNull(PathVideo.getPath(this, data.getData())));
                    RequestBody requestFile = RequestBody.create((MediaType.parse("multipart/form-data")),
                            file);
                     multipartBody = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);

                    Log.d("mmmmmmmmmm", "onActivityResult: "+multipartBody  +  "\n"+ file);
                    try {
                        //getting image from gallery
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                        //Setting image to ImageView
                        binding.profileImage.setImageBitmap(bitmap);
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
                        binding.profileImage.setImageBitmap(photo);
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

    public String getRealPathFromURI(Uri contentUri)
    {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(),contentUri,proj,null,null,null);
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

}