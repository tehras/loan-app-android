package com.github.koshkin.loanapplication.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.koshkin.loanapplication.R;

import java.util.List;

/**
 * Created by tehras on 7/4/15.
 */
public class LoanSpinner extends Spinner {
    public LoanSpinner(Context context) {
        super(context);
        setCustomColors(context);
    }

    public LoanSpinner(Context context, int mode) {
        super(context, mode);
        setCustomColors(context);
    }

    public LoanSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomColors(context);
    }

    public LoanSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCustomColors(context);
    }

    public LoanSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
        setCustomColors(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LoanSpinner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, int mode) {
        super(context, attrs, defStyleAttr, defStyleRes, mode);
        setCustomColors(context);
    }

    private void setCustomColors(Context context) {
        Drawable background = this.getBackground();
        background.setColorFilter(context.getResources().getColor(R.color.edit_text_underline_filter), PorterDuff.Mode.SRC_ATOP);
        this.setBackground(background);

        setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView selectedText = (TextView) parent.getChildAt(0);
                if (selectedText != null) {
                    selectedText.setTextColor(Color.WHITE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
