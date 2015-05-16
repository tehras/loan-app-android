package com.github.koshkin.loanapplication.models;

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

    private static final String JSON_PARSER_LENGTH = "length";
    private static final String JSON_PARSER_START_DATE = "startDate";

    private static final String JSON_PARSER_RESPONSE_DATA = "responseData";
    private static final String JSON_PARSER_LOANS = "loans";

    @Override
    public LoanCacheObject parseResponse(String response) throws JSONException {
        sLoanList = getInstance();

        if (NullChecker.isNullOrEmpty(response))
            return sLoanList;

        JSONObject reader = new JSONObject(response);

        if (reader.has(JSON_PARSER_RESPONSE_DATA)) {
            JSONObject responseObject = reader.getJSONObject(JSON_PARSER_RESPONSE_DATA);
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
                        loan.setInterestRate(loanObject.getDouble(JSON_PARSER_INTEREST_RATE));
                    if (loanObject.has(JSON_PARSER_START_DATE))
                        loan.setStartDate(new Date(loanObject.getLong(JSON_PARSER_START_DATE)));
                    if (loanObject.has(JSON_PARSER_TERM)) {
                        Term term = new Term();
                        JSONObject termObject = loanObject.getJSONObject(JSON_PARSER_TERM);
                        if (termObject.has(JSON_PARSER_LENGTH))
                            term.setLength(termObject.getInt(JSON_PARSER_LENGTH));
                        if (termObject.has(JSON_PARSER_TYPE))
                            term.setType(TermType.valueOf(termObject.getString(JSON_PARSER_TYPE)));
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

    public void setLoans(ArrayList<Loan> loans) {
        mLoans = loans;
    }
}
