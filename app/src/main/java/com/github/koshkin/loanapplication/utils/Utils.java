package com.github.koshkin.loanapplication.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by tehras on 5/16/15.
 */
public class Utils {

    public static String getAmount(BigDecimal bigDecimal) {
        return "$" + new DecimalFormat(TWO_AFTER_DECIMAL_FORMAT).format(bigDecimal);
    }
    public static String getAmount(Double duo) {
        return "$" + new DecimalFormat(TWO_AFTER_DECIMAL_FORMAT).format(duo);
    }

    public static String getPercentage(Double percentage) {
        return new DecimalFormat(TWO_AFTER_DECIMAL_PERCENTAGE_FORMATE).format(percentage) + "%";
    }

    public static String getPercentage(BigDecimal percentage) {
        return new DecimalFormat(TWO_AFTER_DECIMAL_PERCENTAGE_FORMATE).format(percentage) + "%";
    }

    public static Double stringToDouble(String string){
        string = string.replaceAll("[,$.]","");
        return Double.parseDouble(string);
    }

    public static void closeKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static boolean notNullOrEmpty(JSONObject jsonObject, String param) throws JSONException {
        return jsonObject.has(param) && jsonObject.get(param)!=null;
    }

    private static final String TWO_AFTER_DECIMAL_PERCENTAGE_FORMATE = "#,##0.00";
    private static final String TWO_AFTER_DECIMAL_FORMAT = "#,###,###,##0.00";
}
