package com.ammar.tawseel.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
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
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Objects;

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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edite_profil);
        apiInterFace = APIClient.getClient().create(APIInterFace.class);
        onClicksButtons();

        loadDataProfile();

    }

    private void onClicksButtons() {
        binding.imgEdite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.inputLayoutLocation.setEnabled(true);
                binding.editTextName.setEnabled(true);
                binding.editTextPhone.setEnabled(true);
                binding.profileImage.setEnabled(true);

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
            editeProfil(latitude, longitude, Objects.requireNonNull(binding.editTextLocation.getText()).toString(), binding.editTextName.getText().toString()
                    , Objects.requireNonNull(binding.editTextPhone.getText()).toString(), saveuri);
        });
    }

    private void loadDataProfile() {

        Call<APIResponse.ResponseShowProfile> call = apiInterFace.showProfile(
                "application/json",
                "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                "ar");
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
                                    .load(response.body().getData().getAvatar())
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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

                    saveuri = data.getData();

                    binding.profileImage.setImageURI(saveuri);

                    Uri selectedImageUri = data.getData();
                    Log.d("images", "onActivityResult: " + saveuri);


                }

                break;

        }
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


                    if (response.body().getStatus()) {
                        binding.layoutHome.setVisibility(View.VISIBLE);
                        binding.layoutProgress.setVisibility(View.GONE);
                        Toast.makeText(EditeProfilActivity.this, "تم التعديل بنجاح ", Toast.LENGTH_SHORT).show();
                        Log.d("avatar", "onResponse: " + response.body().getData().getName());
                        startActivity(new Intent(EditeProfilActivity.this, HomeActivity.class));
                        finish();

                    } else {
                        binding.layoutHome.setVisibility(View.VISIBLE);
                        binding.layoutProgress.setVisibility(View.GONE);
                        Toast.makeText(EditeProfilActivity.this, "" + response.body().getMessage().get(0), Toast.LENGTH_SHORT).show();
                    }


                }
            }

            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseProfile> call, @NonNull Throwable t) {
                binding.layoutHome.setVisibility(View.VISIBLE);
                binding.layoutProgress.setVisibility(View.GONE);
                // Toast.makeText(UserProfilActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


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

}