package com.example.customizeposition.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewPropertyAnimator;
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
    private boolean isEdit = false;
    private RecyclerView.LayoutManager mLayoutManager;

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
        mIvTip.setScaleX(1.2f);
        mIvTip.setScaleY(1.2f);
        mLayoutManager = new GridLayoutManager(getContext(), 4);
        mRecyclerView.setLayoutManager(mLayoutManager);
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
                isEdit = true;

//                MyAdapter.ViewHolder viewHolder = (MyAdapter.ViewHolder) mRecyclerView.findViewHolderForAdapterPosition(15);
//                log("click: " + viewHolder);
            }

            @Override
            public void OnItemFocusChange(MyApplicationBean myApplicationBean, final View view, boolean hasFocus, int position) {
//                mLayoutManager.scrollToPosition(position);
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
        if (mRecyclerView.hasFocus() && isEdit) {
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
        log("mRecyclerView.findFocus(): " + view);
        if (view != null) {
            int oldPosition = (int) view.getTag();
            if (view instanceof ImageView) {
                View nextFocusView = view.focusSearch(direction);
                log("nextFocusView: " + nextFocusView);
                if (nextFocusView != null) {
                    int newPosition = (int) nextFocusView.getTag();
                    nextFocusView.requestFocus();
                    log("oldPosition: " + oldPosition + "  newPosition:" + newPosition);
                    changeFocus(oldPosition, newPosition);
                    return true;
                } else {
                    log("error: nextFocus null");
                }
            }
        } else {
            log("error: mRecyclerView.findFocus() null");
        }
        return false;
    }

    private void changeFocus(final int oldPosition, final int newPosition) {
        mMyAdapter.changePosition(oldPosition, newPosition);
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                MyAdapter.ViewHolder viewHolder = (MyAdapter.ViewHolder) mRecyclerView.findViewHolderForAdapterPosition(newPosition);
                if (viewHolder != null) {
                    viewHolder.imageViewIcon.requestFocus();
//                    mLayoutManager.scrollToPosition((int) viewHolder.imageViewIcon.getTag());

                    float y = viewHolder.flIvContainer.getY();
                    if (y < 0) {
                        y = 0;
                    } else if (y > mRecyclerView.getHeight() - viewHolder.flIvContainer.getHeight()) {
                        y = mRecyclerView.getHeight() - viewHolder.flIvContainer.getHeight();
                    }
                    mIvTip.animate().translationX(viewHolder.flIvContainer.getX()).translationY(y).setDuration(500).start();

                }
            }
        });
    }

    private void doAnimator(View view, float translateX, float translateY, final int oldPosition, final int newPosition) {
        ObjectAnimator translateXAnimator = ObjectAnimator.ofFloat(view, "translationX", view.getTranslationX(), translateX);
        ObjectAnimator translateYAnimator = ObjectAnimator.ofFloat(view, "translationY", view.getTranslationY(), translateY);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(translateXAnimator, translateYAnimator);
        animatorSet.setDuration(500);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mMyAdapter.changePosition(oldPosition, newPosition);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}


