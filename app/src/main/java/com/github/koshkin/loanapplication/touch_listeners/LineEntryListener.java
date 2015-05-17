package com.github.koshkin.loanapplication.touch_listeners;

import android.animation.TimeInterpolator;
import android.app.Activity;
import android.graphics.Rect;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.db.chart.Tools;
import com.db.chart.listener.OnEntryClickListener;
import com.db.chart.model.LineSet;
import com.db.chart.view.LineChartView;
import com.github.koshkin.loanapplication.R;

import java.text.DecimalFormat;

/**
 * Created by tehras on 5/16/15.
 */
public class LineEntryListener implements OnEntryClickListener {

    public Activity mContext;
    public LineChartView mLineChartView;
    public LineSet mLineSet;

    private final TimeInterpolator enterInterpolator = new DecelerateInterpolator(1.5f);
    private final TimeInterpolator exitInterpolator = new AccelerateInterpolator();

    public LineEntryListener(Activity context, LineChartView lineChartView, LineSet lineSet) {
        mContext = context;
        mLineChartView = lineChartView;
        mLineSet = lineSet;
    }

    @Override
    public void onClick(int setIndex, int entryIndex, Rect rect) {

        if (mLineTooltip == null)
            showLineTooltip(setIndex, entryIndex, rect);
        else
            dismissLineTooltip(setIndex, entryIndex, rect);
    }

    TextView mLineTooltip;

    private void showLineTooltip(int setIndex, int entryIndex, Rect rect) {

        mLineTooltip = (TextView) mContext.getLayoutInflater().inflate(R.layout.view_circular_tooltip, null);
        mLineTooltip.setText("$" + new DecimalFormat("#,###,###").format((int) mLineSet.getEntries().get(entryIndex).getValue()));

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) Tools.fromDpToPx(35), (int) Tools.fromDpToPx(35));
        layoutParams.leftMargin = rect.centerX() - layoutParams.width / 2;
        layoutParams.topMargin = rect.centerY() - layoutParams.height / 2;
        mLineTooltip.setLayoutParams(layoutParams);

        mLineTooltip.setPivotX(layoutParams.width / 2);
        mLineTooltip.setPivotY(layoutParams.height / 2);
        mLineTooltip.setAlpha(0);
        mLineTooltip.setScaleX(0);
        mLineTooltip.setScaleY(0);
        mLineTooltip.animate()
                .setDuration(150)
                .alpha(1)
                .scaleX(1).scaleY(1)
                .rotation(360)
                .setInterpolator(enterInterpolator);

        mLineChartView.showTooltip(mLineTooltip);
    }

    private void dismissLineTooltip(final int setIndex, final int entryIndex, final Rect rect) {

        mLineTooltip.animate()
                .setDuration(100)
                .scaleX(0).scaleY(0)
                .alpha(0)
                .setInterpolator(exitInterpolator).withEndAction(new Runnable() {
            @Override
            public void run() {
                mLineChartView.removeView(mLineTooltip);
                mLineTooltip = null;
                if (entryIndex != -1)
                    showLineTooltip(setIndex, entryIndex, rect);
            }
        });
    }
}
