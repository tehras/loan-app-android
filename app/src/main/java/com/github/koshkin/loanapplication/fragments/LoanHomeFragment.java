package com.github.koshkin.loanapplication.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.koshkin.loanapplication.BaseFragment;
import com.github.koshkin.loanapplication.R;
import com.github.koshkin.loanapplication.adapters.LoanListRecyclerAdapter;
import com.github.koshkin.loanapplication.models.LoanCacheObject;
import com.github.koshkin.loanapplication.network.AsyncTaskCallbackInterceptor;
import com.github.koshkin.loanapplication.network.AsyncTaskEventRunner;
import com.github.koshkin.loanapplication.network.Request;
import com.github.koshkin.loanapplication.network.Response;

/**
 * Created by tehras on 5/16/15.
 */
public class LoanHomeFragment extends BaseFragment implements AsyncTaskCallbackInterceptor {

    private RecyclerView mRecyclerView;

    public static LoanHomeFragment newInstance() {
        LoanHomeFragment fragment = new LoanHomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO make a network call for data
        new AsyncTaskEventRunner(LoanHomeFragment.this, LoanCacheObject.getInstance(), Request.ReqId.GET_LOAN_LIST).executeTask();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_loan_main_page, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        setUpRecyclerView(mRecyclerView);

        return rootView;
    }

    public void setUpRecyclerView(RecyclerView recyclerView) {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    public void updateLoanListView(LoanCacheObject responseObject) {
        //TODO create adapter
        mRecyclerView.setAdapter(new LoanListRecyclerAdapter(responseObject.getLoans()));
        mRecyclerView.invalidate();
    }

    public void updateGraphView(LoanCacheObject responseObject) {
        //TODO create adapter
    }

    @Override
    public void onCallbackReceived(Response response, Request request) {
        if (response.getResponseCode() == Response.ResponseCode.SUCCESS)
            if (request.getReqId() == Request.ReqId.GET_LOAN_LIST)
                if (response.getResponseObject() instanceof LoanCacheObject)
                    updateLoanListView((LoanCacheObject) response.getResponseObject());
                else if (request.getReqId() == Request.ReqId.GET_HOME_GRAPH) {
                    updateGraphView((LoanCacheObject) response.getResponseObject());
                }

        //TODO error scenarios

    }
}
