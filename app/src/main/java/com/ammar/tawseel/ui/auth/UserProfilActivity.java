package com.ammar.tawseel.ui.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.loader.content.CursorLoader;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ammar.tawseel.R;
import com.ammar.tawseel.databinding.ActivityUserProfilBinding;
import com.ammar.tawseel.databinding.LayoutPicPicureBinding;
import com.ammar.tawseel.editor.ShardEditor;
import com.ammar.tawseel.netWorke.APIClient;
import com.ammar.tawseel.netWorke.APIInterFace;
import com.ammar.tawseel.pojo.response.APIResponse;
import com.ammar.tawseel.ui.home.HomeActivity;
import com.ammar.tawseel.uitllis.Cemmon;
import com.ammar.tawseel.uitllis.PathVideo;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.Header;

public class UserProfilActivity extends AppCompatActivity {
    private static final int LOCATION_SETTINGS_REQUEST = 101;
    ActivityUserProfilBinding binding;
    APIInterFace apiInterFace;
    ShardEditor shardEditor;
    String latitude, longitude;
    Uri saveuri;
    LayoutPicPicureBinding layoutPicPicureBinding;
    BottomSheetDialog bottomSheetDialog;
    String imageUri;
    static boolean turrnOfGPS = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        shardEditor = new ShardEditor(this);
        if (shardEditor.loadData().get(ShardEditor.KEY_LANG) != "") {

            Cemmon.setLocale(this, shardEditor.loadData().get(ShardEditor.KEY_LANG));

        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_profil);
        if (Cemmon.isNetworkOnline(this)) {

            if (binding.layoutLocationInternet.getVisibility() == View.VISIBLE) {
                binding.layoutLocationInternet.setVisibility(View.GONE);
            }
            binding.layoutHome.setVisibility(View.VISIBLE);


            apiInterFace = APIClient.getClient().create(APIInterFace.class);

            shardEditor = new ShardEditor(this);

            getProfile();
            enableLoc();
            binding.profileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDialogBottomSheetLogin();
                }
            });


            binding.btnMap.setOnClickListener(v -> {

                Intent intent = new Intent(UserProfilActivity.this, SelectLocationActivity.class);
                startActivityForResult(intent, 1);

            });


            binding.btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    binding.tvSave.setVisibility(View.GONE);
                    binding.progressbar.setVisibility(View.VISIBLE);
                    if (isValue()) {


                        editeProfil(latitude, longitude, Objects.requireNonNull(binding.editTextLocation.getText()).toString(), binding.editTextName.getText().toString()
                                , Objects.requireNonNull(binding.editTextPhone.getText()).toString(), saveuri);

                    } else {
                        binding.tvSave.setVisibility(View.VISIBLE);
                        binding.progressbar.setVisibility(View.GONE);

                    }
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

    MultipartBody.Part multipartBody;

    private void editeProfil(String latitude, String longitude, String addresss, String name, String phone, Uri saveuri) {
        RequestBody usernameBody = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody userphoneBody = RequestBody.create(MediaType.parse("text/plain"), phone);


        Call<APIResponse.ResponseProfile> call = apiInterFace.editProfile(
                Double.parseDouble(latitude)
                , Double.parseDouble(longitude), addresss, usernameBody, userphoneBody, multipartBody,
                "application/json", "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN));


        call.enqueue(new Callback<APIResponse.ResponseProfile>() {
            @Override
            public void onResponse(@NonNull Call<APIResponse.ResponseProfile> call,
                                   @NonNull Response<APIResponse.ResponseProfile> response) {

                if (response.code() == 200) {
                    binding.tvSave.setVisibility(View.VISIBLE);
                    binding.progressbar.setVisibility(View.GONE);

                    if (response.body().getStatus()) {

                        showDialogScuccess(R.layout.dialog_success, 1);
                        Log.d("avatar", "onResponse: " + response.body().getData().getName());


                    } else {
                        showDialogScuccess(R.layout.dialog_wrong, 0);
                    }


                }
            }

            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseProfile> call, @NonNull Throwable t) {
                binding.tvSave.setVisibility(View.VISIBLE);
                binding.progressbar.setVisibility(View.GONE);
                showDialogScuccess(R.layout.dialog_wrong, 0);

            }
        });


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
                    startActivity(new Intent(UserProfilActivity.this, HomeActivity.class));
                    finish();
                }

            }
        }.start();

    }





    private boolean isValue() {
        if (binding.editTextLocation.getText().toString().isEmpty() && binding.editTextLocation.getText().toString().equals("")) {
            binding.editTextLocation.setError("Select your location");
            return false;

        } else if (binding.editTextName.getText().toString().isEmpty() && binding.editTextName.getText().toString().equals("")) {
            binding.editTextName.setError("Select your name");
            return false;

        } else if (binding.editTextPhone.getText().toString().isEmpty() && binding.editTextPhone.getText().toString().equals("")) {
            binding.editTextPhone.setError("Select your name");
            return false;

        } else {

            return true;
        }


    }

    private void getProfile() {

        Call<APIResponse.ResponseProfile> call = apiInterFace.getProfile(
                "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN), shardEditor.loadData().get(ShardEditor.KEY_LANG), "application/json");
        call.enqueue(new Callback<APIResponse.ResponseProfile>() {
            @Override
            public void onResponse(@NonNull Call<APIResponse.ResponseProfile> call,
                                   @NonNull Response<APIResponse.ResponseProfile> response) {

                if (response.code() == 200) {

                    if (response.body().getStatus()) {

                        binding.editTextName.setText(response.body().getData().getName());
                        if (response.body().getData().getPhone() != null) {
                            binding.editTextPhone.setText(response.body().getData().getPhone());
                        } else {

                        }


                        if (response.body().getData().getAvatar() != null) {

                            Picasso.with(UserProfilActivity.this)
                                    .load(response.body().getData().getAvatar())
                                    .into(binding.profileImage);
                        }


                    } else {
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseProfile> call, @NonNull Throwable t) {

                Toast.makeText(UserProfilActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    String selectedImagePath;
    Bitmap bitmap;
    String imageString;
    String imagepath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case 1:
                if (resultCode == RESULT_OK) {
                    String tv_location = data.getStringExtra("location");
                    latitude = data.getStringExtra("lantuid");
                    longitude = data.getStringExtra("lang");
                    binding.editTextLocation.setText(tv_location);

                }
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

    public String getPath(Uri uri) {
        // just some safety built in
        if (uri == null) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        // this is our fallback here
        return uri.getPath();
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
            startActivityForResult(takePictureIntent, 0);
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

                    turrnOfGPS = true;
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
                                        UserProfilActivity.this,
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

