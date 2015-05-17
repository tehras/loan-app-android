package com.github.koshkin.loanapplication.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.view.LineChartView;
import com.db.chart.view.XController;
import com.db.chart.view.YController;
import com.db.chart.view.animation.Animation;
import com.db.chart.view.animation.easing.linear.LinearEase;
import com.db.chart.view.animation.style.DashAnimation;
import com.github.koshkin.loanapplication.BaseFragment;
import com.github.koshkin.loanapplication.R;
import com.github.koshkin.loanapplication.adapters.LoanListRecyclerAdapter;
import com.github.koshkin.loanapplication.adapters.LoanListRecyclerAdapterItemDecoration;
import com.github.koshkin.loanapplication.models.LoanCacheObject;
import com.github.koshkin.loanapplication.network.AsyncTaskCallbackInterceptor;
import com.github.koshkin.loanapplication.network.AsyncTaskEventRunner;
import com.github.koshkin.loanapplication.network.Request;
import com.github.koshkin.loanapplication.network.Response;
import com.github.koshkin.loanapplication.touch_listeners.LineEntryListener;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import java.text.DecimalFormat;

/**
 * Created by tehras on 5/16/15.
 */
public class LoanHomeFragment extends BaseFragment implements AsyncTaskCallbackInterceptor, ObservableScrollViewCallbacks, SwipeRefreshLayout.OnRefreshListener {

    private ObservableRecyclerView mRecyclerView;
    private CardView mLineChartViewHolder;
    private LoanCacheObject mLoanCacheObject;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ScrollView mScrollView;

    public static LoanHomeFragment newInstance() {
        return new LoanHomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO make a network call for data
        if (LoanCacheObject.getInstance().getLoans() == null || LoanCacheObject.getInstance().getLoans().isEmpty())
            getLoansDataTask();
        else
            mLoanCacheObject = LoanCacheObject.getInstance();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_loan_main_page, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);

        mRecyclerView = (ObservableRecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setScrollViewCallbacks(this);

        setUpRecyclerView(mRecyclerView);

        mLineChartView = (LineChartView) rootView.findViewById(R.id.home_page_chart_view);
        mLineChartViewHolder = (CardView) rootView.findViewById(R.id.card_view);

        //TODO remove this later
        updateGraphView(null);

        if (mLoanCacheObject != null)
            updateLoanListView(mLoanCacheObject);

        return rootView;
    }

    public void setUpRecyclerView(RecyclerView recyclerView) {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    public void updateLoanListView(LoanCacheObject responseObject) {
        LoanListRecyclerAdapter adapter = new LoanListRecyclerAdapter(responseObject.getLoans());

        mRecyclerView.addItemDecoration(new LoanListRecyclerAdapterItemDecoration(getHeightOffsetByChart()));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.invalidate();
    }

    private LineChartView mLineChartView;

    @Override
    public void onCallbackReceived(Response response, Request request) {
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshing(true);
        if (response.getResponseCode() == Response.ResponseCode.SUCCESS)
            if (request.getReqId() == Request.ReqId.GET_LOAN_LIST)
                if (response.getResponseObject() instanceof LoanCacheObject)
                    updateLoanListView((LoanCacheObject) response.getResponseObject());
        //TODO error scenarios

    }

    public void updateGraphView(LoanCacheObject responseObject) {
        mLineChartView.reset();

        //TODO getDataSet
        LineSet lineSet = new LineSet();
        lineSet.addPoint("0", 2300);
        lineSet.addPoint("1", 4200);
        lineSet.addPoint("2", 3100);
        lineSet.addPoint("3", 5600);
        lineSet.addPoint("4", 7400);
        lineSet.addPoint("5", 5600);
        lineSet.addPoint("6", 3200);
        lineSet.addPoint("7", 3600);
        lineSet.addPoint("8", 2700);
        lineSet.addPoint("9", 2800);

        lineSet.setDots(true)
                .setDotsColor(getActivity().getResources().getColor(R.color.line_dot_color))
                .setDotsRadius(Tools.fromDpToPx(5))
                .setDotsStrokeThickness(Tools.fromDpToPx(2))
                .setDotsStrokeColor(getActivity().getResources().getColor(R.color.line_dot_stroke_color))
                .setFill(getActivity().getResources().getColor(R.color.line_fill_color))
                .setLineColor(getActivity().getResources().getColor(R.color.line_color))
                .beginAt(0).endAt(lineSet.size())
                .setLineThickness(Tools.fromDpToPx(3))
                .setSmooth(true)
                .setDashed(true);

        mLineChartView.addData(lineSet);

        mLineChartView
                .setXAxis(false)
                .setXLabels(XController.LabelPosition.INSIDE)
                .setYAxis(false)
                .setYLabels(YController.LabelPosition.INSIDE)
                .setAxisBorderValues(2000, 8000, 1500)
                .setLabelsFormat(new DecimalFormat("#,###,###"))
                .show(getAnimation());

        mLineChartView.setOnEntryClickListener(new LineEntryListener(getActivity(), mLineChartView, lineSet));

        mLineChartView.animateSet(0, new DashAnimation());
    }

    private Animation getAnimation() {
        return new Animation()
                .setAlpha(1)
                .setDuration(1000)
                .setEasing(new LinearEase());
    }

    private int getHeightOffsetByChart() {
        return getActivity().getResources().getDimensionPixelSize(R.dimen.home_page_chart_height) + getActivity().getResources().getDimensionPixelSize(R.dimen.cardview_marginTopBottom) * 3;
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstTouch, boolean drag) {
        int heightOffsetByChart = getHeightOffsetByChart();

        int correctedScrollY = scrollY + heightOffsetByChart;
        if (scrollY + heightOffsetByChart > heightOffsetByChart)
            correctedScrollY = heightOffsetByChart;
        else if (scrollY + heightOffsetByChart < 0)
            correctedScrollY = 0;

        float alpha = 1 - ((float) correctedScrollY / (float) heightOffsetByChart);

        mLineChartViewHolder.setAlpha(alpha);
        mLineChartViewHolder.setTranslationY(-correctedScrollY);
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

    @Override
    public void onRefresh() {
        getLoansDataTask();
    }

    public void getLoansDataTask() {
        new AsyncTaskEventRunner(LoanHomeFragment.this, LoanCacheObject.getInstance(), Request.ReqId.GET_LOAN_LIST).executeTask();
    }
}
