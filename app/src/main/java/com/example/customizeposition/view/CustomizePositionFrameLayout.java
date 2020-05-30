package com.example.customizeposition.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.leanback.widget.VerticalGridView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.customizeposition.R;
import com.example.customizeposition.Util.Util;


/**
 * @author chengxiaobo
 * @time 2020-05-23 09:35
 */
public class CustomizePositionFrameLayout extends FrameLayout {

    private VerticalGridView mRecyclerView;
    private FrameLayout mFlTip;
    private ImageView mIvTip;
    private MyAdapter mMyAdapter;
    private View mCurrentFocusView;

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
        mRecyclerView.addItemDecoration(new ItemDecoration());
        mRecyclerView.setNumColumns(4);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (mFlTip.getVisibility() == View.VISIBLE && mCurrentFocusView != null) {
                    mIvTip.setTranslationX(mCurrentFocusView.getX());
                    mIvTip.setTranslationY(mCurrentFocusView.getY());
                }
            }
        });
    }

    public void setAdapter(final MyAdapter myAdapter) {

        mMyAdapter = myAdapter;
        myAdapter.setItemListener(new MyAdapter.ItemListener() {
            @Override
            public void onItemClick(MyApplicationBean myApplicationBean, View view) {
                mFlTip.setVisibility(View.VISIBLE);
                mIvTip.setImageResource(myApplicationBean.drawableId);
                mIvTip.setTranslationX(view.getX());
                mIvTip.setTranslationY(view.getY());

            }

            @Override
            public void OnItemFocusChange(MyApplicationBean myApplicationBean, final View view, boolean hasFocus) {
                if (hasFocus) {
                    mCurrentFocusView = view;
                    mIvTip.setTranslationX(view.getX());
                    mIvTip.setTranslationY(view.getY());
                }
            }
        });


        mRecyclerView.setAdapter(myAdapter);
    }

    class ItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(Util.dip2px(getContext(), 30), Util.dip2px(getContext(), 30), Util.dip2px(getContext(), 30), Util.dip2px(getContext(), 30));
        }
    }


}


