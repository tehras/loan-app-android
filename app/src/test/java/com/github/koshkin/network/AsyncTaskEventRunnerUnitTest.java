package com.github.koshkin.network;

import android.support.v4.app.Fragment;

import com.github.koshkin.loanapplication.BuildConfig;
import com.github.koshkin.loanapplication.LoanActivity;
import com.github.koshkin.loanapplication.fragments.home.LoanHomeFragment;
import com.github.koshkin.loanapplication.models.LoanCacheObject;
import com.github.koshkin.loanapplication.network.AsyncTaskCallbackInterceptor;
import com.github.koshkin.loanapplication.network.AsyncTaskEventRunner;
import com.github.koshkin.loanapplication.network.Request;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import java.util.List;

import static org.powermock.api.mockito.PowerMockito.spy;

/**
 * Created by tehras on 5/16/15.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, emulateSdk = 21)
public class AsyncTaskEventRunnerUnitTest {

    LoanActivity mLoanActivity;
    Fragment mFragment;

    @Before
    public void setUp() {
        ActivityController controller = Robolectric.buildActivity(LoanActivity.class).create().start().resume().visible();
        mLoanActivity = spy((LoanActivity) controller.get());

        //Gets the fragment
        List<Fragment> fragments = mLoanActivity.getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof LoanHomeFragment) {
                mFragment = spy(fragment);
                break;
            }
        }
    }

    @Test
    public void getLoansServiceCallTest() {
        LoanCacheObject loanCacheObject = LoanCacheObject.getInstance();

        AsyncTaskEventRunner asyncTaskEventRunner = spy(new AsyncTaskEventRunner((AsyncTaskCallbackInterceptor) mFragment, loanCacheObject, Request.ReqId.GET_LOAN_LIST));
        asyncTaskEventRunner.executeTask();

        Robolectric.flushForegroundScheduler();
        Robolectric.flushBackgroundScheduler();

        Assert.assertNotNull(loanCacheObject.getLoans());
    }
}
