package com.github.koshkin.loanapplication.fragments.loan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.github.koshkin.loanapplication.BaseFragment;
import com.github.koshkin.loanapplication.R;
import com.github.koshkin.loanapplication.models.LoanCacheObject;
import com.github.koshkin.loanapplication.network.AsyncTaskCallbackInterceptor;
import com.github.koshkin.loanapplication.network.AsyncTaskEventRunner;
import com.github.koshkin.loanapplication.network.Request;
import com.github.koshkin.loanapplication.network.Response;
import com.github.koshkin.loanapplication.utils.EditTextWatcherUtils;
import com.github.koshkin.loanapplication.utils.Utils;
import com.github.koshkin.loanapplication.view.LoanEditText;
import com.github.koshkin.loanapplication.view.LoanSpinner;
import com.koshkin.loanappmodel.loan.Loan;
import com.koshkin.loanappmodel.loan.Term;
import com.koshkin.loanappmodel.loan.TermType;

import org.json.JSONException;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.github.koshkin.loanapplication.utils.EditTextWatcherUtils.*;

/**
 * Created by tehras on 7/4/15.
 */
public class LoanCreateNewFragment extends BaseFragment implements AsyncTaskCallbackInterceptor {

    private View.OnClickListener mCreateLoanListener;

    public static LoanCreateNewFragment newInstance() {
        return new LoanCreateNewFragment();
    }

    private LoanEditText mLoanName, mCurrentAmount, mInitialAmount, mInterestRate, mPaymentStartDate, mTotalTermsLength, mPerTermAmount;
    private LoanSpinner mTermType;
    private ButtonRectangle mCreateLoan;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_loan_create_page, container, false);

        mLoanName = (LoanEditText) rootView.findViewById(R.id.loan_create_name);
        mCurrentAmount = (LoanEditText) rootView.findViewById(R.id.loan_current_amount);
        mInitialAmount = (LoanEditText) rootView.findViewById(R.id.loan_initial_amount);
        mInterestRate = (LoanEditText) rootView.findViewById(R.id.loan_interest_rate);
        mPaymentStartDate = (LoanEditText) rootView.findViewById(R.id.loan_payment_start_date);
        mTotalTermsLength = (LoanEditText) rootView.findViewById(R.id.loan_term_payment_length);
        mPerTermAmount = (LoanEditText) rootView.findViewById(R.id.loan_term_payment_amount);
        mTermType = (LoanSpinner) rootView.findViewById(R.id.loan_term_type);

        mCreateLoan = (ButtonRectangle) rootView.findViewById(R.id.loan_create_button);
        mCreateLoan.setOnClickListener(getCreateLoanListener());

        //This part makes the text format well
        setUpPaymentStartDate(mPaymentStartDate);
        setUpDollarAmount(mCurrentAmount);
        setUpDollarAmount(mInitialAmount);
        setUpDollarAmount(mPerTermAmount);
        setUpPercentageAmount(mInterestRate);

        return rootView;
    }

    public View.OnClickListener getCreateLoanListener() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Loan loan = new Loan();
                loan.setName(String.valueOf(mLoanName.getText()));
                loan.setInitialAmount(Utils.stringToDouble(String.valueOf(mInitialAmount.getText())));
                loan.setCurrentAmount(Utils.stringToDouble(String.valueOf(mCurrentAmount.getText())));
                try {
                    loan.setStartDate(new SimpleDateFormat("MM/dd/yyyy").parse(String.valueOf(mPaymentStartDate.getText())));
                } catch (ParseException e) {
                    //TODO wrong error
                }
                loan.setInterestRate(Utils.stringToDouble(String.valueOf(mInterestRate.getText())));

                Term term = new Term();
                term.setPaymentAmount(Utils.stringToDouble(String.valueOf(mPerTermAmount.getText())));
                term.setLength(Integer.parseInt(String.valueOf(mTotalTermsLength.getText())));
                term.setType(convertStringToTermType(mTermType.getSelectedItem().toString()));

                loan.setTerm(term);

                sendLoanData(loan);
                //TODO error checks
            }
        };
    }

    private TermType convertStringToTermType(String termTypeString) {
        for (TermType termType : TermType.values()) {
            if (termType.name().equalsIgnoreCase(termTypeString))
                return termType;
        }

        return null;
    }

    public void sendLoanData(Loan loan) {
        Request request = new Request();
        try {
            request.setRequestObject(LoanCacheObject.getJson(loan));
            request.addHeader("Content-Type", "application/json");
            new AsyncTaskEventRunner(LoanCreateNewFragment.this, LoanCacheObject.getInstance(), Request.ReqId.POST_NEW_LOAN).executeTask(request);
        } catch (JSONException e) {
            //TODO show some error window
        }
    }

    @Override
    public void onCallbackReceived(Response response, Request request) {
        if (response.getResponseCode() == Response.ResponseCode.SUCCESS)
            startLoanHomeFragment();
        else {
            //TODO error
        }
    }
}
