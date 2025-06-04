package com.example.fotnews;

import android.view.View;
import android.widget.LinearLayout;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

public class RunnableHelper {

    public static Runnable progressbar_start_loader(LinearLayout progressBar) {
        return () -> progressBar.setVisibility(View.VISIBLE);
    }

    public static Runnable progressbar_stop_loader(LinearLayout progressBar) {
        return () -> progressBar.setVisibility(View.GONE);
    }

    public static Runnable close_edit_box_loader(LinearLayout alert_background, CoordinatorLayout alert_edit_profile) {
        return () -> {
            alert_background.setVisibility(View.GONE);
            alert_edit_profile.setVisibility(View.GONE);
        };
    }

}
