package com.ammar.tawseel.ui.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
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

import java.io.File;
import java.io.IOException;
import java.util.Objects;

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

        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_profil);

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

    private void editeProfil(String latitude, String longitude, String addresss, String name, String phone, Uri saveuri) {
        String imageUri;
        if (saveuri != null) {
            imageUri = getPath(saveuri);
        } else {
            imageUri = "";
        }
        Call<APIResponse.ResponseProfile> call = apiInterFace.editProfile(
                latitude
                , longitude, addresss, name, phone, imageUri, "application/json", "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN));


        call.enqueue(new Callback<APIResponse.ResponseProfile>() {
            @Override
            public void onResponse(@NonNull Call<APIResponse.ResponseProfile> call,
                                   @NonNull Response<APIResponse.ResponseProfile> response) {

                if (response.code() == 200) {
                    binding.tvSave.setVisibility(View.VISIBLE);
                    binding.progressbar.setVisibility(View.GONE);

                    if (response.body().getStatus()) {
                        Toast.makeText(UserProfilActivity.this, "تم التعديل بنجاح ", Toast.LENGTH_SHORT).show();
                        Log.d("avatar", "onResponse: " + response.body().getData().getName());
                        startActivity(new Intent(UserProfilActivity.this, HomeActivity.class));
                        finish();

                    } else {

                        Toast.makeText(UserProfilActivity.this, "" + response.body().getMessage().get(0), Toast.LENGTH_SHORT).show();
                    }


                }
            }

            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseProfile> call, @NonNull Throwable t) {
                binding.tvSave.setVisibility(View.VISIBLE);
                binding.progressbar.setVisibility(View.GONE);
                // Toast.makeText(UserProfilActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


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
                "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN), "ar", "application/json");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {

                    saveuri = data.getData();

                    binding.profileImage.setImageURI(saveuri);

                    Uri selectedImageUri = data.getData();
                    Log.d("images", "onActivityResult: " + saveuri);


                }

                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    String tv_location = data.getStringExtra("location");
                    latitude = data.getStringExtra("lantuid");
                    longitude = data.getStringExtra("lang");
                    binding.editTextLocation.setText(tv_location);

                }
                break;
        }


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

