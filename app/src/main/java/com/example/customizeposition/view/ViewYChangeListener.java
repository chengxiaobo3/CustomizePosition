package com.example.customizeposition.view;

import android.os.Handler;
import android.view.View;

public class ViewYChangeListener {

    private View mView;
    private final long INTERVAL = 60;
    private ViewYCallBack mViewYCallBack;
    private float y;

    public ViewYChangeListener(View mView, ViewYCallBack mViewYCallBack) {
        this.mView = mView;
        this.mViewYCallBack = mViewYCallBack;
        mHandler = new Handler();
    }

    private Handler mHandler;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (y == mView.getY()) {
                mViewYCallBack.onFinish();
            } else {
                mViewYCallBack.onYChange(mView.getY());
                mHandler.postDelayed(runnable, INTERVAL);
                y = mView.getY();
            }
        }
    };

    public void start() {

        if (mView == null) {
            mViewYCallBack.onFinish();
            return;
        }
        y = mView.getY();
        mHandler.postDelayed(runnable, INTERVAL);
    }

    public interface ViewYCallBack {
        void onFinish();

        void onYChange(float y);
    }


}

