package com.github.koshkin.loanapplication.models;

import com.github.koshkin.ResponseHolder;
import com.github.koshkin.loanapplication.BuildConfig;
import com.koshkin.loanappmodel.loan.TermType;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.powermock.api.mockito.PowerMockito.spy;

/**
 * Created by tehras on 5/16/15.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, emulateSdk = 21)
public class LoanCacheObjectUnitTest {

    @Test
    public void parsingTest() {

        LoanCacheObject loanCacheObject = spy(LoanCacheObject.getInstance());

        //RESPONSE
        try {
            loanCacheObject.parseResponse(ResponseHolder.GET_LOAN_LIST_RESPONSE);
        } catch (JSONException e) {
            assertNull(e);
        }

        assertNotNull(loanCacheObject);
        assertNotNull(loanCacheObject.getLoans());
        assertEquals(loanCacheObject.getLoans().size(), 1);

        assertEquals(BigDecimal.valueOf(5000), loanCacheObject.getLoans().get(0).getCurrentAmount());
        assertEquals(BigDecimal.valueOf(10000), loanCacheObject.getLoans().get(0).getInitialAmount());
        assertEquals((Double) 6.9, loanCacheObject.getLoans().get(0).getInterestRate());
        assertEquals("Sex Chane Loan", loanCacheObject.getLoans().get(0).getName());
        assertEquals((Integer) 69, loanCacheObject.getLoans().get(0).getTerm().getLength());
        assertEquals(TermType.MONTHS, loanCacheObject.getLoans().get(0).getTerm().getType());
        assertNotNull(loanCacheObject.getLoans().get(0).getStartDate());
    }

    @Test
    public void parsingNegativeTest() {
        LoanCacheObject loanCacheObject = spy(LoanCacheObject.getInstance());

        //RESPONSE
        try {
            loanCacheObject.parseResponse("");
        } catch (JSONException e) {
            assertNull(e);
        }

        assertNotNull(loanCacheObject);
        assertNull(loanCacheObject.getLoans());

        try {
            loanCacheObject.parseResponse("{\"status\":true,\"httpCode\":200,\"responseId\":null,\"resourceUri\":null,\"user\":{\"name\":\"Taras Cockskin\"},\"requestData\":null,\"responseData\":{}}");
        } catch (JSONException e) {
            assertNull(e);
        }

        assertNotNull(loanCacheObject);
        assertNull(loanCacheObject.getLoans());
    }


}
