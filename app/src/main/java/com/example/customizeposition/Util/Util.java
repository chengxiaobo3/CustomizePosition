package com.example.customizeposition.Util;

import android.content.Context;

/**
 * @author chengxiaobo
 * @time 2020-05-23 23:26
 */
public class Util {

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
