package com.github.koshkin.loanapplication.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.koshkin.loanapplication.R;
import com.github.koshkin.loanapplication.utils.Utils;
import com.koshkin.loanappmodel.loan.Loan;

import java.util.ArrayList;

/**
 * Created by tehras on 5/16/15.
 */
public class LoanListRecyclerAdapter extends RecyclerView.Adapter<LoanListRecyclerAdapter.LoanViewHolder> {

    ArrayList<Loan> mLoans;

    public LoanListRecyclerAdapter(ArrayList<Loan> loans) {
        mLoans = loans;
    }

    @Override
    public LoanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_adapter_loan_list_item, parent, false);

        return new LoanViewHolder(v);
    }

    @Override
    public void onBindViewHolder(LoanViewHolder holder, int position) {
        Loan loan = mLoans.get(position);

        holder.mInitialAmount.setText(Utils.getAmount(loan.getInitialAmount()));
        holder.mCurrentAmount.setText(Utils.getAmount(loan.getCurrentAmount()));
        holder.mInterestRate.setText(Utils.getPercentage(loan.getInterestRate()));
        holder.mLoanName.setText(loan.getName());
    }

    @Override
    public int getItemCount() {
        return mLoans.size();
    }

    public class LoanViewHolder extends RecyclerView.ViewHolder {

        TextView mInitialAmount, mCurrentAmount, mInterestRate, mLoanName;

        public LoanViewHolder(View itemView) {
            super(itemView);

            mInitialAmount = (TextView) itemView.findViewById(R.id.loan_item_initial_amount);
            mCurrentAmount = (TextView) itemView.findViewById(R.id.loan_item_current_amount);
            mInterestRate = (TextView) itemView.findViewById(R.id.loan_item_interest_rate);
            mLoanName = (TextView) itemView.findViewById(R.id.loan_item_name);
        }
    }
}
