package com.github.koshkin.loanapplication.fragments.login;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gc.materialdesign.views.ButtonFlat;
import com.github.koshkin.loanapplication.BaseFragment;
import com.github.koshkin.loanapplication.LoanActivity;
import com.github.koshkin.loanapplication.R;

/**
 * This is the home page that the user
 * will land on if he is not logged
 * in
 * <p/>
 * Created by tehras on 5/23/15.
 */
public class LoginHomeScreenFragment extends BaseFragment {

    public static LoginHomeScreenFragment newInstance() {
        return new LoginHomeScreenFragment();
    }

    private ButtonFlat mLoginButton, mRegisterButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pre_login_page, container, false);

        mLoginButton = (ButtonFlat) view.findViewById(R.id.login_button);
        mRegisterButton = (ButtonFlat) view.findViewById(R.id.register_button);

        mLoginButton.setOnClickListener(getLoginClickListener());
        mRegisterButton.setOnClickListener(getRegisterClickListener());

        return view;
    }

    /**
     * LoginClickListener will
     * start Login Fragment
     * on click
     *
     * @return
     */
    private View.OnClickListener getLoginClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoginFragment();
            }
        };
    }

    /**
     * RegisterClickListener will
     * start Register Fragment
     * on click
     *
     * @return
     */
    private View.OnClickListener getRegisterClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegisterFragment();
            }
        };
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Activity activity = getActivity();
        if(activity instanceof LoanActivity)
            ((LoanActivity) activity).disableDrawer();
    }
}
