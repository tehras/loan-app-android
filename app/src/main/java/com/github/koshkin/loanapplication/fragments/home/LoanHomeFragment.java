package com.github.koshkin.loanapplication.fragments.home;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.koshkin.loanapplication.BaseFragment;
import com.github.koshkin.loanapplication.R;
import com.github.koshkin.loanapplication.adapters.LoanListRecyclerAdapter;
import com.github.koshkin.loanapplication.adapters.LoanListRecyclerAdapterItemDecoration;
import com.github.koshkin.loanapplication.models.LoanCacheObject;
import com.github.koshkin.loanapplication.network.AsyncTaskCallbackInterceptor;
import com.github.koshkin.loanapplication.network.AsyncTaskEventRunner;
import com.github.koshkin.loanapplication.network.Request;
import com.github.koshkin.loanapplication.network.Response;
import com.github.koshkin.loanapplication.utils.Utils;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.koshkin.loanappmodel.loan.Loan;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by tehras on 5/16/15.
 */
public class LoanHomeFragment extends BaseFragment implements AsyncTaskCallbackInterceptor, ObservableScrollViewCallbacks, SwipeRefreshLayout.OnRefreshListener {

    private ObservableRecyclerView mRecyclerView;
    private FrameLayout mLineChartViewHolder;
    private LoanCacheObject mLoanCacheObject;
    private FloatingActionButton mAddNewLoan;
    private TextView mCurrentAmount, mPreviousAmount, mPreviousAmountLabel;
    private RelativeLayout mHeaderContainer;


    public static LoanHomeFragment newInstance() {
        return new LoanHomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_loan_main_page, container, false);

        Utils.closeKeyboard(getActivity());

        if (LoanCacheObject.getInstance().getLoans() == null || LoanCacheObject.getInstance().getLoans().isEmpty() || !LoanCacheObject.getInstance().isReceivedData())
            getLoansDataTask();
        else
            mLoanCacheObject = LoanCacheObject.getInstance();

        mRecyclerView = (ObservableRecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setScrollViewCallbacks(this);

        setUpRecyclerView(mRecyclerView);

        mLineChartView = (LineChart) rootView.findViewById(R.id.home_page_chart_view);
        mLineChartViewHolder = (FrameLayout) rootView.findViewById(R.id.card_view);


        //TODO remove this later
        updateGraphView(null);

        if (mLoanCacheObject != null)
            updateLoanListView(mLoanCacheObject);

        mAddNewLoan = (FloatingActionButton) rootView.findViewById(R.id.add_loan_floating_action_button);
        mAddNewLoan.setOnClickListener(getAddNewLoanButtonListener());

        mHeaderContainer = (RelativeLayout) rootView.findViewById(R.id.home_page_header_container);
        mCurrentAmount = (TextView) rootView.findViewById(R.id.home_page_header_current_amount);
        mPreviousAmount = (TextView) rootView.findViewById(R.id.home_page_header_previous_amount);
        mPreviousAmountLabel = (TextView) rootView.findViewById(R.id.home_page_header_previous_label);

        return rootView;
    }

    public void setUpRecyclerView(RecyclerView recyclerView) {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    public void updateLoanListView(LoanCacheObject responseObject) {
        final LoanListRecyclerAdapter adapter = new LoanListRecyclerAdapter(responseObject.getLoans());

        mCurrentAmount.setText(getCurrentTotalAmount(responseObject));
        mPreviousAmount.setText(getInitialTotalAmount(responseObject));
        mPreviousAmountLabel.setText("Initial Amount -");

        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.addItemDecoration(new LoanListRecyclerAdapterItemDecoration(getHeightOffsetByChart()));
                mRecyclerView.setAdapter(adapter);
                mRecyclerView.invalidate();
            }
        });

    }

    private String getCurrentTotalAmount(LoanCacheObject loanCacheObject) {
        if (loanCacheObject == null || loanCacheObject.getLoans() == null || loanCacheObject.getLoans().isEmpty())
            return "N/A";

        double totalAmount = 0d;
        for (Loan loan : loanCacheObject.getLoans()) {
            totalAmount = totalAmount + loan.getCurrentAmount();
        }

        return String.valueOf(totalAmount);
    }

    private String getInitialTotalAmount(LoanCacheObject loanCacheObject) {
        if (loanCacheObject == null || loanCacheObject.getLoans() == null || loanCacheObject.getLoans().isEmpty())
            return "N/A";

        double initialAmount = 0d;
        for (Loan loan : loanCacheObject.getLoans()) {
            initialAmount = initialAmount + loan.getInitialAmount();
        }

        return String.valueOf(initialAmount);
    }

    private LineChart mLineChartView;

    public static String GET_LOAN_LIST_RESPONSE = "{\"status\":true,\"httpCode\":200,\"responseId\":null,\"resourceUri\":null,\"user\":{\"name\":\"Taras Cockskin\"},\"requestData\":null,\"responseData\":{\"loans\":[{\"name\":\"Sex Chane Loan\",\"currentAmount\":5000,\"initialAmount\":10000,\"term\":{\"length\":69,\"type\":\"MONTHS\"},\"interestRate\":6.9,\"startDate\":-61519748106204}]}}";

    @Override
    public void onCallbackReceived(Response response, Request request) {
        if (response.getResponseCode() == Response.ResponseCode.SUCCESS) {
            if (request.getReqId() == Request.ReqId.GET_LOAN_LIST)
                if (response.getResponseObject() instanceof LoanCacheObject)
                    updateLoanListView((LoanCacheObject) response.getResponseObject());
        } else {
            LoanCacheObject loanCacheObject = LoanCacheObject.getInstance();
            try {
                loanCacheObject.parseResponse(GET_LOAN_LIST_RESPONSE);
                loanCacheObject.setReceivedData(false);
                updateLoanListView(loanCacheObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateGraphView(LoanCacheObject responseObject) {
        //TODO populate chart

        //SetBackgroundColor
        Paint paint = new Paint();
        paint.setColor(Color.TRANSPARENT);
        mLineChartView.setPaint(paint, Chart.PAINT_GRID_BACKGROUND);
        mLineChartView.setBackgroundColor(Color.TRANSPARENT);
        mLineChartView.setBackground(getResources().getDrawable(android.R.color.transparent));
        mLineChartViewHolder.setBackground(getResources().getDrawable(android.R.color.transparent));


        //Touches
        mLineChartView.setTouchEnabled(true); //Allows for touches
        mLineChartView.setDragEnabled(false); //We don't want dragging
        mLineChartView.setScaleEnabled(false); //We don't want scaling
        mLineChartView.setPinchZoom(false); //We don't want pinch zoom
        mLineChartView.setHighlightEnabled(true); //YES! This is what makes it beautiful

        //Legend
        Legend legend = mLineChartView.getLegend();
        legend.setEnabled(false);

        mLineChartView.setDescription("");

        //X-Axis
        XAxis xAxis = mLineChartView.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(8f);
        xAxis.setTextColor(getActivity().getResources().getColor(R.color.tertiary));
        xAxis.setDrawAxisLine(false); //xAxis is ugly
        xAxis.setDrawGridLines(false); //keep the chart clean
        xAxis.setAvoidFirstLastClipping(true);

        //Y-Axis
        YAxis leftAxis = mLineChartView.getAxisLeft();
        YAxis rightAxis = mLineChartView.getAxisRight();
        leftAxis.setTextSize(8f);
        leftAxis.setTextColor(getActivity().getResources().getColor(R.color.tertiary));
        leftAxis.setStartAtZero(false);
        leftAxis.setEnabled(true);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawGridLines(false);
        rightAxis.setEnabled(false);

        //Set Data
        mLineChartView.setData(getLineData());
        mLineChartView.invalidate();
    }

    private LineData getLineData() {
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            entries.add(new Entry((float) (100f + (Math.random() * 50f)), i));
            xVals.add("XVal - " + i);
        }

        LineDataSet lineDataSet = new LineDataSet(entries, "Loan 1");
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        //LineDataSet settings
        lineDataSet.setDrawCubic(true);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setLineWidth(3f);
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawVerticalHighlightIndicator(true);
        lineDataSet.setHighlightLineWidth(1.2f);
        lineDataSet.setHighLightColor(getResources().getColor(R.color.tertiary));

        //setup
        LineData lineData = new LineData(xVals, lineDataSet);
        lineData.setHighlightEnabled(true);
        lineData.addDataSet(lineDataSet);

        return lineData;
    }

    private int getHeightOffsetByChart() {
        int height = mHeaderContainer.getHeight();
        return (int) (getActivity().getResources().getDimensionPixelSize(R.dimen.home_page_chart_height) * 1.1 + getActivity().getResources().getDimensionPixelSize(R.dimen.cardview_marginTopBottom) * 3 + height);
    }

    private int mPreviousScrollY = 0;

    @Override
    public void onScrollChanged(int scrollY, boolean firstTouch, boolean drag) {
        int heightOffsetByChart = getHeightOffsetByChart();

        int correctedScrollY = scrollY + heightOffsetByChart;
        if (scrollY + heightOffsetByChart > heightOffsetByChart)
            correctedScrollY = heightOffsetByChart;
        else if (scrollY + heightOffsetByChart < 0) {
            correctedScrollY = 0;
            if (scrollY + heightOffsetByChart < -50)
                onRefresh();
        }

        float alpha = 1 - ((float) correctedScrollY / (float) heightOffsetByChart);

        mLineChartViewHolder.setAlpha(alpha);
        mLineChartViewHolder.setTranslationY(-correctedScrollY);
        mHeaderContainer.setTranslationY(-correctedScrollY);

        //Logic for the FAB button
        int difference = correctedScrollY - mPreviousScrollY;

        if (difference > 0) {
            mAddNewLoan.setTranslationY(mPreviousScrollY);
        } else {
            Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.abc_slide_in_bottom);
            mAddNewLoan.setTranslationY(0);
            mAddNewLoan.startAnimation(animation);
        }

        mPreviousScrollY = correctedScrollY;
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

    public View.OnClickListener getAddNewLoanButtonListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoanCreateFragment();
            }
        };
    }
}
