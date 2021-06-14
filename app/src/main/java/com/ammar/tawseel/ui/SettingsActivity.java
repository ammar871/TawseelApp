package com.ammar.tawseel.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.ammar.tawseel.R;
import com.ammar.tawseel.databinding.ActivitySettingsBinding;
import com.ammar.tawseel.editor.ShardEditor;
import com.ammar.tawseel.uitllis.Cemmon;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {
    ActivitySettingsBinding binding;
    ShardEditor shardEditor;
    String itemSelectedSpinner;
    ArrayList<String> languages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        shardEditor = new ShardEditor(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
if (shardEditor.loadData().get(ShardEditor.KEY_LANG)!=""){

    Cemmon.setLocale(this, shardEditor.loadData().get(ShardEditor.KEY_LANG));

}

        inItSpinner();
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             onBackPressed();
            }
        });
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemSelectedSpinner.equals("لغة العرض")){

                }else if ((itemSelectedSpinner.equals("اللغة العربيـة"))){
                    shardEditor.saveLang("ar");
                }else if ((itemSelectedSpinner.equals("English"))){
                    shardEditor.saveLang("eng");
                }
                startActivity(new Intent(SettingsActivity.this,SplashActivity.class));
                finish();
            }
        });
    }

    private void inItSpinner() {
        languages.add("لغة العرض");
        languages.add("اللغة العربيـة");
        languages.add("English");
        ArrayAdapter<String> langAdapter = new ArrayAdapter<String>(SettingsActivity.this, R.layout.spinner_text, languages);
        langAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        binding.spinnerLang.setAdapter(langAdapter);
        binding.spinnerLang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemSelectedSpinner = parent.getItemAtPosition(position).toString();


            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}