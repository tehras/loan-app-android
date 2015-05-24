package com.github.koshkin.loanapplication.view;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.EditText;

import com.github.koshkin.loanapplication.R;

/**
 * Created by tehras on 5/23/15.
 */
public class LoanEditText extends EditText {
    public LoanEditText(Context context) {
        super(context);

        this.setHintTextColor(context.getResources().getColor(R.color.edit_text_hint_color));
        this.getBackground().setColorFilter(context.getResources().getColor(R.color.edit_text_underline_filter), PorterDuff.Mode.SRC_ATOP);

    }

    public LoanEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setHintTextColor(context.getResources().getColor(R.color.edit_text_hint_color));
        this.getBackground().setColorFilter(context.getResources().getColor(R.color.edit_text_underline_filter), PorterDuff.Mode.SRC_ATOP);
    }

    public LoanEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setHintTextColor(context.getResources().getColor(R.color.edit_text_hint_color));
        this.getBackground().setColorFilter(context.getResources().getColor(R.color.edit_text_underline_filter), PorterDuff.Mode.SRC_ATOP);
    }
}
