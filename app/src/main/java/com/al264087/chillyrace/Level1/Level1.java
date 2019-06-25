package com.al264087.chillyrace.Level1;

import android.util.DisplayMetrics;

import com.al264087.chillyrace.FrameWork.GameActivity;
import com.al264087.chillyrace.FrameWork.IGameController;

public class Level1 extends GameActivity {
    @Override
    protected IGameController buildGameController() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        return new Level1Controller(this, width, height);
    }
}