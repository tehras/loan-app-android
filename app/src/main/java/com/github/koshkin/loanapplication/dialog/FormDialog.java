package com.github.koshkin.loanapplication.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.koshkin.loanapplication.R;

/**
 * Created by tehras on 5/23/15.
 */
public class FormDialog extends DialogFragment {

    private FormDialogViewInterface mFormDialogViewInterface;

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    public static FormDialog newInstance() {
        return new FormDialog();
    }

    public FormDialog() {
        //TODO set default interface
        mFormDialogViewInterface = new FormDialogViewDefaultImplementation();
    }

    public void setFormDialogViewInterface(FormDialogViewInterface formDialogViewInterface) {
        mFormDialogViewInterface = formDialogViewInterface;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_form, container, false);

        LinearLayout bodyContainerView = (LinearLayout) view.findViewById(R.id.dialog_form_container);
        bodyContainerView.addView(mFormDialogViewInterface.bodyContainerView());

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }


}
