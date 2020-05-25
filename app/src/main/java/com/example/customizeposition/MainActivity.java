package com.example.customizeposition;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;

import com.example.customizeposition.view.CustomizePositionFrameLayout;
import com.example.customizeposition.view.MyAdapter;
import com.example.customizeposition.view.MyApplicationBean;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public CustomizePositionFrameLayout mCustomizePositionFrameLayout;
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCustomizePositionFrameLayout = findViewById(R.id.customizePositionFrameLayout);
        List<MyApplicationBean> list = new ArrayList<MyApplicationBean>();
        for (int i = 0; i <= 21; i++) {
            MyApplicationBean myApplicationBean = new MyApplicationBean();
            int resourceId = this.getResources().getIdentifier("d_" + i, "drawable", getPackageName());
            myApplicationBean.drawableId = resourceId;
            list.add(myApplicationBean);
        }
        mCustomizePositionFrameLayout.setAdapter(new MyAdapter(list));

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mCustomizePositionFrameLayout.onKeyDown(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
