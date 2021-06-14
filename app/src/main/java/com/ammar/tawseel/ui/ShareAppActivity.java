package com.ammar.tawseel.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.ammar.tawseel.BuildConfig;
import com.ammar.tawseel.R;
import com.ammar.tawseel.databinding.ActivityShareAppBinding;
import com.ammar.tawseel.editor.ShardEditor;
import com.ammar.tawseel.uitllis.Cemmon;

public class ShareAppActivity extends AppCompatActivity {
    ActivityShareAppBinding binding;
    ShardEditor shardEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shardEditor=new ShardEditor(this);
        if (shardEditor.loadData().get(ShardEditor.KEY_LANG)!=""){

            Cemmon.setLocale(this, shardEditor.loadData().get(ShardEditor.KEY_LANG));

        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_share_app);


        onClickesButtonsShar();

    }

    private void onClickesButtonsShar() {

        binding.sharWhats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WhatesSend("com.whatsapp");
            }
        });
        binding.sharInstgram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WhatesSend("com.instagram.android");
            }
        });
        binding.sharTweter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WhatesSend("com.twitter.android");
            }
        });
        binding.sharSnabshit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = "https://play.google.com/store/apps/details?id=" +
                        BuildConfig.APPLICATION_ID;

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied", link);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(ShareAppActivity.this, "Copied", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    void shareinWhatsapp(String application) {
//        Intent intent = this.getPackageManager().getLaunchIntentForPackage(application);
//        if (intent != null) {
//            // The application exists
//            Intent shareIntent = new Intent();
//            shareIntent.setAction(Intent.ACTION_SEND);
//            shareIntent.setPackage(application);
//
//            shareIntent.putExtra(android.content.Intent.EXTRA_TITLE, "title");
//            shareIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id="+
//                            BuildConfig.APPLICATION_ID );
//            // Start the specific social application
//            startActivity(shareIntent);
//        } else {
//            // The application does not exist
//            // Open GooglePlay or use the default system picker
//        }
//    }

    private void WhatesSend(String link) {
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage(link);
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" +
                BuildConfig.APPLICATION_ID);
        try {
            startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "not Found App", Toast.LENGTH_SHORT).show();
        }
    }

}