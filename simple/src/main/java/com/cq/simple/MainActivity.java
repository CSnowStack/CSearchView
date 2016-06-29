package com.cq.simple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.cq.csearchview.CSearchView;
import com.cq.csearchview.StatusChangeListenerAdapter;

public class MainActivity extends AppCompatActivity {
    private CSearchView mCsv;
    private TextView mTxtTitle;
    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mCsv=(CSearchView)findViewById(R.id.csv);
        mTxtTitle=(TextView) findViewById(R.id.txtTitle);
        mToolbar=(Toolbar)findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCsv.setOnStatusChangeListener(new StatusChangeListenerAdapter() {
            @Override
            public void onShowStartListener() {
                super.onShowStartListener();
                mTxtTitle.setVisibility(View.GONE);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }

            @Override
            public void onHideEndListener() {
                super.onHideEndListener();
                mTxtTitle.setVisibility(View.VISIBLE);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        });
    }


}
