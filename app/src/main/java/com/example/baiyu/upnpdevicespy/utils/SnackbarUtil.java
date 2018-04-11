package com.example.baiyu.upnpdevicespy.utils;

import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;

/**
 * 文件名：SnackbarUtil
 * 描述：SnackbarUtil
 * 作者：白煜
 * 时间：2018/4/11 0011
 * 版权：
 */

public class SnackbarUtil {

    public static void snackShort(View view, String message) {
        snackShort(view, message, null, null);
    }

    public static void snackShort(View view, String message, String action, View.OnClickListener listener) {
        final Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        if (!TextUtils.isEmpty(action) && listener == null) {
            snackbar.setAction(action, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
        } else if ((!TextUtils.isEmpty(action) && listener != null)) {
            snackbar.setAction(action, listener);
        }
        snackbar.show();
    }

    //snackbar 只能显示一个按钮（官方建议） 想显示两个要自定义
//    public static void snackShort(View view, String message, String action1, View.OnClickListener action1listener
//            , String action2, View.OnClickListener action2listener) {
//        final Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
//        if (!TextUtils.isEmpty(action1) && action1listener == null) {
//            snackbar.setAction(action1, new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    snackbar.dismiss();
//                }
//            });
//        } else if ((!TextUtils.isEmpty(action1) && action1listener != null)) {
//            snackbar.setAction(action1, action1listener);
//        }
//
//        if (!TextUtils.isEmpty(action2) && action2listener == null) {
//            snackbar.setAction(action2, new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    snackbar.dismiss();
//                }
//            });
//        } else if (!TextUtils.isEmpty(action2) && action2listener != null) {
//            snackbar.setAction(action2, action2listener);
//        }
//        snackbar.show();
//    }

    public static void snackLong(View view, String message) {
        snackLong(view, message, null, null);
    }

    public static void snackLong(View view, String message, String action, View.OnClickListener listener) {
        final Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        if (!TextUtils.isEmpty(action) && listener == null) {
            snackbar.setAction(action, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
        } else if (!TextUtils.isEmpty(action) && listener != null) {
            snackbar.setAction(action, listener);
        }
        snackbar.show();
    }


}
