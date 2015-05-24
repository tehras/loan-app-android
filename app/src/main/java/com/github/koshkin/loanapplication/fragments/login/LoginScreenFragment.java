package com.github.koshkin.loanapplication.fragments.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gc.materialdesign.views.ButtonFlat;
import com.github.koshkin.loanapplication.BaseFragment;
import com.github.koshkin.loanapplication.R;
import com.github.koshkin.loanapplication.models.LoginAuthentication;
import com.github.koshkin.loanapplication.network.AsyncTaskCallbackInterceptor;
import com.github.koshkin.loanapplication.network.AsyncTaskEventRunner;
import com.github.koshkin.loanapplication.network.Request;
import com.github.koshkin.loanapplication.network.Response;
import com.github.koshkin.loanapplication.view.LoanEditText;

/**
 * This will contain the login screen
 * <p/>
 * Created by tehras on 5/23/15.
 */
public class LoginScreenFragment extends BaseFragment implements AsyncTaskCallbackInterceptor {

    private static final int USERNAME_MIN_LENGTH = 4;
    private static final int PASSWORD_MIN_LENGTH = 6;

    public static LoginScreenFragment newInstance() {
        return new LoginScreenFragment();
    }

    private LoanEditText mUsernameEditText, mPasswordEditText;
    private ButtonFlat mLoginButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_page, container, false);

        mUsernameEditText = (LoanEditText) view.findViewById(R.id.login_username_edit_text);
        mPasswordEditText = (LoanEditText) view.findViewById(R.id.login_password_edit_text);
        mLoginButton = (ButtonFlat) view.findViewById(R.id.login_button);

        mPasswordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mPasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());

        mLoginButton.setOnClickListener(getLoginButtonOnClickListener());

        return view;
    }

    public View.OnClickListener getLoginButtonOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInvalid = false;
                if (mPasswordEditText.getText().toString().length() <= PASSWORD_MIN_LENGTH) {
                    shakeAnimation(getActivity(), mPasswordEditText);
                    isInvalid = true;
                }
                if (mUsernameEditText.getText().toString().length() <= USERNAME_MIN_LENGTH) {
                    shakeAnimation(getActivity(), mUsernameEditText);
                    isInvalid = true;
                }

                if (!isInvalid) {
                    AsyncTaskEventRunner asyncTaskEventRunner = new AsyncTaskEventRunner(LoginScreenFragment.this, new LoginAuthentication(), Request.ReqId.POST_LOGIN);
                    asyncTaskEventRunner.executeTask(mUsernameEditText.getText().toString(), mPasswordEditText.getText().toString());
                }
            }
        };
    }

    @Override
    public void onCallbackReceived(Response response, Request request) {
        //TODO correct Logic
        startLoanHomeFragment();
    }
}
