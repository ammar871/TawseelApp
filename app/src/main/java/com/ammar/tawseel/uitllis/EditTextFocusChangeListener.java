package com.ammar.tawseel.uitllis;

import android.view.View;
import android.widget.ScrollView;

import androidx.core.widget.NestedScrollView;

public class EditTextFocusChangeListener implements View.OnFocusChangeListener {

    private NestedScrollView scrollView;

    public EditTextFocusChangeListener(NestedScrollView scrollView) {
        this.scrollView = scrollView;
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if(hasFocus) {
            int left = view.getLeft();
            int top = view.getTop();
            int bottom = view.getBottom();
            int keyboardHeight = scrollView.getHeight() / 3;

            // if the bottom of edit text is greater than scroll view height divide by 3,
            // it means that the keyboard is visible
            if (bottom > keyboardHeight)  {
                // increase scroll view with padding
                scrollView.setPadding(0, 0, 0, keyboardHeight);
                // scroll to the edit text position
                scrollView.scrollTo(left, top);
            }
        }
    }
}