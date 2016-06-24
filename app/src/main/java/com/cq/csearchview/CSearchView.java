package com.cq.csearchview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by cqll on 2016/6/24.
 */
public class CSearchView extends RelativeLayout {
    private ImageView ivCancel, ivSearch;
    private CEditText edtSearch;
    private boolean isShow = false;
    private String hint = "请输入相关搜索字符";
    private long duration = 1000;
    private AnimatorSet showAnimatorSet, hideAnimatorSet;
    private int searchDrawable, cancelDrawable;
    private int hintColor, textColor, lineColor, bgColor;

    public CSearchView(Context context) {
        this(context, null);
    }

    public CSearchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CSearchView);
        try {
            duration = (long) typedArray.getFloat(R.styleable.CSearchView_csv_moveDuration, 1000);
            hint = typedArray.getString(R.styleable.CSearchView_csv_hint);
            hintColor = typedArray.getColor(R.styleable.CSearchView_csv_hintColor, Color.parseColor("#BDBDBD"));
            textColor = typedArray.getColor(R.styleable.CSearchView_csv_textColor, Color.parseColor("#1D1D1D"));
            lineColor = typedArray.getColor(R.styleable.CSearchView_csv_lineColor, Color.parseColor("#AEAEAE"));
            bgColor = typedArray.getColor(R.styleable.CSearchView_csv_backgroundColor, Color.parseColor("#00000000"));

            searchDrawable = typedArray.getResourceId(R.styleable.CSearchView_csv_searchDrawable, R.mipmap.ic_search_36pt_3x);
            cancelDrawable = typedArray.getResourceId(R.styleable.CSearchView_csv_cancelDrawable, R.mipmap.ic_close_36pt_3x);
        } finally {
            typedArray.recycle();
        }


        ivSearch.setImageResource(searchDrawable);
        edtSearch.setLineColor(lineColor);
        edtSearch.setTextColor(textColor);
        edtSearch.setHintTextColor(hintColor);
        setBackgroundColor(bgColor);

        ivCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (isShow) {
                    if (hideAnimatorSet == null) {
                        ObjectAnimator ivObjectAnimator = ObjectAnimator.ofFloat(ivSearch, "alpha", 0.8f, 0).setDuration(500);
                        ObjectAnimator edtObjectAnimator = ObjectAnimator.ofFloat(edtSearch, "alpha", 0.8f, 0).setDuration(500);

                        hideAnimatorSet = new AnimatorSet();
                        hideAnimatorSet.playTogether(ivObjectAnimator, edtObjectAnimator);
                        hideAnimatorSet.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                edtSearch.setVisibility(INVISIBLE);
                                ivSearch.setVisibility(INVISIBLE);
                                edtSearch.setAlpha(1f);
                                ivSearch.setAlpha(1f);
                                edtSearch.setText("");
                            }

                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);
                                ivCancel.setImageResource(searchDrawable);
                            }
                        });
                    }
                    if (hideAnimatorSet.isRunning() || showAnimatorSet.isRunning()) {
                        return;
                    }
                    hideAnimatorSet.start();

                } else {
                    if (showAnimatorSet == null) {

                        ObjectAnimator ivObjectAnimator = ObjectAnimator.ofFloat(ivSearch, "translationX", ivCancel.getLeft(), 0).setDuration(duration);
                        ObjectAnimator edtObjectAnimator = ObjectAnimator.ofFloat(edtSearch, "translationX", ivCancel.getLeft() - (edtSearch.getLeft() - ivSearch.getRight()), 0).setDuration(duration);
                        showAnimatorSet = new AnimatorSet();
                        showAnimatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
                        showAnimatorSet.playTogether(ivObjectAnimator, edtObjectAnimator);
                        showAnimatorSet.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);
                                ivSearch.setVisibility(VISIBLE);
                                edtSearch.setVisibility(VISIBLE);
                                edtSearch.setCursorVisible(false);
                                edtSearch.setHint("");
                                ivCancel.setImageResource(cancelDrawable);
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                edtSearch.setCursorVisible(true);
                                edtSearch.requestFocus();
                                edtSearch.setHint(hint);
                            }
                        });
                    }
                    if (showAnimatorSet.isRunning()) {
                        return;
                    }
                    if (hideAnimatorSet != null && hideAnimatorSet.isRunning()) {
                        return;
                    }
                    showAnimatorSet.start();
                }
                isShow = !isShow;
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        bgColor = color;
        ivCancel.setBackgroundColor(bgColor);
    }

    public void setHint(String hint) {
        edtSearch.setHint(hint);
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setLineColor(int color) {
        edtSearch.setLineColor(color);
    }

    private void initView(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.c_searchview, this, true);
        ivCancel = (ImageView) findViewById(R.id.iv_cancel);
        ivSearch = (ImageView) findViewById(R.id.iv_search);
        edtSearch = (CEditText) findViewById(R.id.edt_search);


    }


}
