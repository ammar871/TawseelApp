package com.ammar.tawseel.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ammar.tawseel.R;
import com.ammar.tawseel.databinding.ActivitySkipingBinding;
import com.ammar.tawseel.editor.ShardEditor;
import com.ammar.tawseel.ui.auth.LoginActivity;

public class SkipingActivity extends AppCompatActivity {
ShardEditor shardEditor;
    private LinearLayout dotsLayout;
    private ImageView[] dots;
    private int[] layouts;


    MyViewPagerAdapter myViewPagerAdapter;
ActivitySkipingBinding binding;
    private int dotscount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
       shardEditor = new ShardEditor(this);
        if (!shardEditor.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_skiping);



        layouts = new int[]{
                R.layout.layout_slide_one,
                R.layout.layout_slide_two,
                R.layout.latout_slide_three,
                };
        addBottomDots(0);

        myViewPagerAdapter = new MyViewPagerAdapter();
        binding.viewpager.setAdapter(myViewPagerAdapter);
       binding.viewpager.addOnPageChangeListener(viewPagerPageChangeListener);
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    binding.viewpager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });
        binding.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });
        binding.btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });

    }

    private void addBottomDots(int currentPage) {
        dots = new ImageView[layouts.length];

        Drawable colorsActive =ContextCompat.getDrawable(this, R.drawable.ic_activate);
        Drawable colorsInactive =ContextCompat.getDrawable(this, R.drawable.ic_unactivae);

        binding.layoutDocts.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);

            dots[i].setImageDrawable(colorsInactive);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(3, 0, 3, 0);

            binding.layoutDocts.addView(dots[i], params);
        }

        if (dots.length > 0)
            dots[currentPage].setImageDrawable(colorsActive);
    }

    private int getItem(int i) {
        return binding.viewpager.getCurrentItem() + i;
    }
    private void launchHomeScreen() {
        shardEditor.setFirstTimeLaunch(false);
        startActivity(new Intent(SkipingActivity.this, LoginActivity.class));
        finish();
    }


    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                binding.btnNext.setVisibility(View.GONE);
                binding.btnSkip.setVisibility(View.GONE);
                binding.btnStart.setVisibility(View.VISIBLE);
            } else {
                // still pages are left
                binding.btnNext.setText("التالى");
                binding.btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    };
        public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}