package com.github.koshkin.loanapplication.adapters;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by tehras on 5/16/15.
 */
public class LoanListRecyclerAdapterItemDecoration extends RecyclerView.ItemDecoration {

    private int top;

    public LoanListRecyclerAdapterItemDecoration(int top) {
        this.top = top;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildPosition(view) == 0)
            outRect.top = top;
    }
}
