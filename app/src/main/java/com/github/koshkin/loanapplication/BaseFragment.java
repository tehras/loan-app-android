package com.github.koshkin.loanapplication;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.github.koshkin.loanapplication.fragments.home.LoanHomeFragment;
import com.github.koshkin.loanapplication.fragments.login.LoginScreenFragment;

/**
 * Extend this Base Fragment from all other fragments
 * It will have common methods to start fragments
 * <p/>
 * Created by tehras on 5/16/15.
 */
public class BaseFragment extends Fragment {

    protected void startLoginFragment() {
        addFragment(LoginScreenFragment.newInstance());
    }

    protected void startRegisterFragment() {
        //TODO startRegistrationPage

    }

    protected void startLoanHomeFragment() {
        addFragment(LoanHomeFragment.newInstance());
    }

    protected void addFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.abc_popup_enter, R.anim.abc_popup_exit)
                .add(R.id.container, fragment)
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
    }

    protected void shakeAnimation(Context context, View view) {
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake));
    }

}
