package com.example.fotnews;


import android.app.Activity;
import android.os.Build;
import android.view.Window;
import android.view.WindowInsets;
import androidx.core.content.ContextCompat;

import com.google.android.material.appbar.MaterialToolbar;

public class UiHelper {

    public static void setStatusBarandNavigationBarColor(Activity activity, int statusbar_color, int navigationbar_color) {
        Window window = activity.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(activity, statusbar_color));
        window.setNavigationBarColor(ContextCompat.getColor(activity, navigationbar_color));
    }

    public static void setStatusBarPadding(Activity activity, MaterialToolbar toolbar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsets insets = activity.getWindowManager().getCurrentWindowMetrics().getWindowInsets();
            int topInset = insets.getInsets(WindowInsets.Type.statusBars()).top;
            toolbar.setPadding(0, topInset, 0, 0);
        } else {
            int statusBarHeight = 0;
            int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
            }
            toolbar.setPadding(0, statusBarHeight, 0, 0);
        }

    }


}
