package com.github.koshkin.loanapplication.models;

import android.util.Log;

import com.github.koshkin.loanapplication.network.AsyncTaskParser;
import com.github.koshkin.loanapplication.utils.NullChecker;
import com.koshkin.loanappmodel.loan.Loan;
import com.koshkin.loanappmodel.loan.Term;
import com.koshkin.loanappmodel.loan.TermType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by tehras on 5/16/15.
 */
public class LoanCacheObject implements AsyncTaskParser<LoanCacheObject> {
    private static LoanCacheObject sLoanList = null;

    private LoanCacheObject() {
    }

    private static boolean mReceivedData = false;

    public static boolean isReceivedData() {
        return mReceivedData;
    }

    public static void setReceivedData(boolean mReceivedData) {
        LoanCacheObject.mReceivedData = mReceivedData;
    }

    public static LoanCacheObject getInstance() {
        if (sLoanList == null)
            return new LoanCacheObject();

        return sLoanList;
    }

    private static final String JSON_PARSER_NAME = "name";
    private static final String JSON_PARSER_CURRENT_AMOUNT = "currentAmount";
    private static final String JSON_PARSER_INITIAL_AMOUNT = "initialAmount";
    private static final String JSON_PARSER_TERM = "term";
    private static final String JSON_PARSER_INTEREST_RATE = "interestRate";
    private static final String JSON_PARSER_TYPE = "type";
    private static final String JSON_PARSER_PAYMENT_AMOUNT = "paymentAmount";

    private static final String JSON_PARSER_LENGTH = "length";
    private static final String JSON_PARSER_START_DATE = "startDate";

    private static final String JSON_PARSER_RESPONSE_DATA = "responseData";
    private static final String JSON_PARSER_LOANS = "loans";

    private static final String JSON_PUT_VERSION = "version";
    private static final String JSON_PUT_TIMESTAMP = "timestamp";
    private static final String JSON_PUT_DATA = "data";
    private static final String JSON_PUT_TYPE = "type";
    private static final String JSON_PUT_LOAN = "loan";

    public static String getJson(Loan loan) throws JSONException {
        JSONObject jsonOutput = new JSONObject();

        jsonOutput.put(JSON_PUT_VERSION, 1);
        jsonOutput.put(JSON_PUT_TIMESTAMP, new Date());

        JSONObject dataJsonObject = new JSONObject();
        dataJsonObject.put(JSON_PUT_TYPE, "loan");
        dataJsonObject.put(JSON_PUT_LOAN, convertToJson(loan));

        jsonOutput.put(JSON_PUT_DATA, dataJsonObject);

        return jsonOutput.toString();
    }

    private static JSONObject convertToJson(Loan loan) throws JSONException {
        JSONObject loanObject = new JSONObject();

        loanObject.put(JSON_PARSER_NAME, loan.getName());
        loanObject.put(JSON_PARSER_CURRENT_AMOUNT, loan.getCurrentAmount());
        loanObject.put(JSON_PARSER_INITIAL_AMOUNT, loan.getInitialAmount());

        JSONObject termObject = new JSONObject();

        termObject.put(JSON_PARSER_LENGTH, loan.getTerm().getLength());
        termObject.put(JSON_PARSER_TYPE, loan.getTerm().getType());
        termObject.put(JSON_PARSER_PAYMENT_AMOUNT, loan.getTerm().getPaymentAmount());

        loanObject.put(JSON_PARSER_TERM, termObject);

        loanObject.put(JSON_PARSER_INTEREST_RATE, loan.getInterestRate());
        loanObject.put(JSON_PARSER_START_DATE, loan.getStartDate().getTime());

        return loanObject;
    }

    @Override
    public LoanCacheObject parseResponse(String response) throws JSONException {
        Log.v(getClass().getSimpleName(), "Response - " + response);

        sLoanList = getInstance();

        if (NullChecker.isNullOrEmpty(response))
            return sLoanList;

        JSONObject reader = new JSONObject(response);

        if (reader.has(JSON_PARSER_RESPONSE_DATA)) {
            JSONObject responseObject = reader.getJSONObject(JSON_PARSER_RESPONSE_DATA);
            setReceivedData(true);
            if (responseObject.has(JSON_PARSER_LOANS)) {
                JSONArray loans = responseObject.getJSONArray(JSON_PARSER_LOANS);

                for (int i = 0; i < loans.length(); i++) {
                    Loan loan = new Loan();
                    JSONObject loanObject = loans.getJSONObject(i);
                    if (loanObject.has(JSON_PARSER_NAME))
                        loan.setName(loanObject.getString(JSON_PARSER_NAME));
                    if (loanObject.has(JSON_PARSER_CURRENT_AMOUNT))
                        loan.setCurrentAmount(BigDecimal.valueOf(loanObject.getLong(JSON_PARSER_CURRENT_AMOUNT)));
                    if (loanObject.has(JSON_PARSER_INITIAL_AMOUNT))
                        loan.setInitialAmount(BigDecimal.valueOf(loanObject.getLong(JSON_PARSER_INITIAL_AMOUNT)));
                    if (loanObject.has(JSON_PARSER_INTEREST_RATE))
                        loan.setInterestRate(BigDecimal.valueOf(loanObject.getDouble(JSON_PARSER_INTEREST_RATE)));
                    if (loanObject.has(JSON_PARSER_START_DATE))
                        loan.setStartDate(new Date(loanObject.getLong(JSON_PARSER_START_DATE)));
                    if (loanObject.has(JSON_PARSER_TERM)) {
                        Term term = new Term();
                        JSONObject termObject = loanObject.getJSONObject(JSON_PARSER_TERM);
                        if (termObject.has(JSON_PARSER_LENGTH))
                            term.setLength(termObject.getInt(JSON_PARSER_LENGTH));
                        if (termObject.has(JSON_PARSER_TYPE))
                            term.setType(TermType.valueOf(termObject.getString(JSON_PARSER_TYPE)));
                        if (termObject.has(JSON_PARSER_PAYMENT_AMOUNT))
                            term.setPaymentAmount(BigDecimal.valueOf(termObject.getDouble(JSON_PARSER_PAYMENT_AMOUNT)));
                        loan.setTerm(term);
                    }
                    if (getLoans() == null)
                        mLoans = new ArrayList<>();

                    mLoans.add(loan);
                }
            }
        }

        sLoanList = this;

        return sLoanList;
    }

    public static void clear() {
        sLoanList = null;
    }

    private ArrayList<Loan> mLoans;

    public ArrayList<Loan> getLoans() {
        return mLoans;
    }
}
