package com.github.koshkin.loanapplication.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by tehras on 5/16/15.
 */
public class Utils {

    public static String getAmount(BigDecimal bigDecimal) {
        return "$" + new DecimalFormat(TWO_AFTER_DECIMAL_FORMAT).format(bigDecimal);
    }

    public static String getPercentage(Double percentage) {
        return new DecimalFormat(TWO_AFTER_DECIMAL_PERCENTAGE_FORMATE).format(percentage) + "%";
    }

    private static final String TWO_AFTER_DECIMAL_PERCENTAGE_FORMATE = "#,##0.00";
    private static final String TWO_AFTER_DECIMAL_FORMAT = "#,###,###,##0.00";
}
