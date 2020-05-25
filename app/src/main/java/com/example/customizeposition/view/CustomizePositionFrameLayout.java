package com.example.customizeposition.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.customizeposition.R;
import com.example.customizeposition.Util.Util;


/**
 * @author chengxiaobo
 * @time 2020-05-23 09:35
 */
public class CustomizePositionFrameLayout extends FrameLayout {

    private RecyclerView mRecyclerView;
    private FrameLayout mFlTip;
    private ImageView mIvTip;
    private MyAdapter mMyAdapter;

    private final String TAG = "CustomizePositionLayout";

    public CustomizePositionFrameLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public CustomizePositionFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomizePositionFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        LayoutInflater.from(this.getContext()).inflate(R.layout.view_customize_position, this);
        mRecyclerView = findViewById(R.id.recyclerView);
        mFlTip = findViewById(R.id.fl_tip);
        mIvTip = findViewById(R.id.iv_tip);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mRecyclerView.addItemDecoration(new ItemDecoration());

    }

    public void setAdapter(final MyAdapter myAdapter) {
        mMyAdapter = myAdapter;
        mRecyclerView.setAdapter(myAdapter);
        myAdapter.setItemListener(new MyAdapter.ItemListener() {
            @Override
            public void onItemClick(MyApplicationBean myApplicationBean, View view) {
                mFlTip.setVisibility(View.VISIBLE);
                mIvTip.setImageResource(myApplicationBean.drawableId);
                mIvTip.setTranslationX(view.getX());
                mIvTip.setTranslationY(view.getY());
                mIvTip.setScaleX(1.2f);
                mIvTip.setScaleY(1.2f);
            }

            @Override
            public void OnItemFocusChange(MyApplicationBean myApplicationBean, final View view, boolean hasFocus) {
            }
        });
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                MyAdapter.ViewHolder viewHolder = (MyAdapter.ViewHolder) mRecyclerView.findViewHolderForAdapterPosition(0);
                if (viewHolder != null) {
                    viewHolder.imageViewIcon.requestFocus();
                }
            }
        });
    }

    class ItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(Util.dip2px(getContext(), 30), Util.dip2px(getContext(), 30), Util.dip2px(getContext(), 30), Util.dip2px(getContext(), 30));
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mRecyclerView.hasFocus()) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    return dealKeyEvent(FOCUS_LEFT);
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    return dealKeyEvent(FOCUS_RIGHT);
                case KeyEvent.KEYCODE_DPAD_UP:
                    return dealKeyEvent(FOCUS_UP);
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    return dealKeyEvent(FOCUS_DOWN);
            }
        }
        return false;
    }

    private void log(String message) {
        Log.e(TAG, message);
    }

    private boolean dealKeyEvent(int direction) {
        View view = mRecyclerView.findFocus();
        if (view != null) {
            int oldPosition = (int) view.getTag();
            if (view instanceof ImageView) {
                View nextFocusView = view.focusSearch(direction);
                if (nextFocusView instanceof ImageView) {
                    int newPosition = (int) nextFocusView.getTag();
                    nextFocusView.requestFocus();
                    changeFocus(oldPosition, newPosition);
                    return true;
                }

            }
        }
        return false;
    }

    private void changeFocus(int oldPosition, final int newPosition) {
        mMyAdapter.changePosition(oldPosition, newPosition);
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                MyAdapter.ViewHolder viewHolder = (MyAdapter.ViewHolder) mRecyclerView.findViewHolderForAdapterPosition(newPosition);
                if (viewHolder != null) {
                    viewHolder.imageViewIcon.requestFocus();
                    mIvTip.setTranslationX(viewHolder.flIvContainer.getX());
                    float y = viewHolder.flIvContainer.getY();
                    if (y < 0) {
                        y = 0;
                    } else if (y > mRecyclerView.getHeight() - viewHolder.flIvContainer.getHeight()) {
                        y = mRecyclerView.getHeight() - viewHolder.flIvContainer.getHeight();
                    }
                    mIvTip.setTranslationY(y);
                    mIvTip.setScaleX(1.2f);
                    mIvTip.setScaleY(1.2f);
                }
            }
        });
    }
}


