package com.ammar.tawseel.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ammar.tawseel.R;
import com.ammar.tawseel.databinding.ActivityShowImageChatBinding;
import com.facebook.drawee.backends.pipeline.Fresco;

import me.relex.photodraweeview.OnPhotoTapListener;
import me.relex.photodraweeview.OnViewTapListener;

public class ShowImageChatActivity extends AppCompatActivity {
    String getImage;
    ActivityShowImageChatBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_image_chat);

        if (getIntent() != null) {
            getImage = getIntent().getStringExtra("image");

            fullScreen();
        }
    }

    private void fullScreen() {
        binding.photoDraweeView.setPhotoUri(Uri.parse(getImage));
        binding.photoDraweeView.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override public void onPhotoTap(View view, float x, float y) {
//                Toast.makeText(view.getContext(), "onPhotoTap :  x =  " + x + ";" + " y = " + y,
//                        Toast.LENGTH_SHORT).show();
            }
        });
        binding.photoDraweeView.setOnViewTapListener(new OnViewTapListener() {
            @Override public void onViewTap(View view, float x, float y) {
              //  Toast.makeText(view.getContext(), "onViewTap", Toast.LENGTH_SHORT).show();
            }
        });

        binding.photoDraweeView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override public boolean onLongClick(View v) {
                Toast.makeText(v.getContext(), "onLongClick", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
}