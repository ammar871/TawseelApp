package com.ammar.tawseel.ui.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.ammar.tawseel.R;
import com.ammar.tawseel.databinding.ActivityForgetPasswordBinding;
import com.ammar.tawseel.databinding.ActivityForgetPasswordBindingImpl;

public class ForgetPasswordActivity extends AppCompatActivity {
ActivityForgetPasswordBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_forget_password);
        textSpan();
    }


    private void textSpan() {
        String text = "عند قيامك بتسجيل جديد، فانك توافق على كافة شروط سياسة الخصوصية و الاستخدام";
        SpannableString ss = new SpannableString(text);
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Toast.makeText(ForgetPasswordActivity.this, "One", Toast.LENGTH_SHORT).show();
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