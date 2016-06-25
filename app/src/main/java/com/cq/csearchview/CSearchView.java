package com.cq.csearchview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
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
    private long duration = 1000,alphaDuration=800;
    private AnimatorSet showAnimatorSet, hideAnimatorSet;
    private int searchDrawable, cancelDrawable;
    private int hintColor, textColor, lineColor, bgColor;
    private boolean openWtihShowSoftInput;
    private InputMethodManager imm;
    private OnSearchViewManagerListener mOnSearchViewManagerListener;
    private OnStatusChangeListener mOnStatusChangeListener;
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
            openWtihShowSoftInput=typedArray.getBoolean(R.styleable.CSearchView_csv_openShowSoftInput,true);
            searchDrawable = typedArray.getResourceId(R.styleable.CSearchView_csv_searchDrawable, R.mipmap.ic_search_36pt_3x);
            cancelDrawable = typedArray.getResourceId(R.styleable.CSearchView_csv_cancelDrawable, R.mipmap.ic_close_36pt_3x);
        } finally {
            typedArray.recycle();
        }

        imm=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        ivSearch.setImageResource(searchDrawable);
        edtSearch.setLineColor(lineColor);
        edtSearch.setTextColor(textColor);
        edtSearch.setHintTextColor(hintColor);
        setBackgroundColor(bgColor);

        initEvent();
    }

    private void initEvent() {
        ivCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (isShow) {
                    if (hideAnimatorSet == null) {
                        ObjectAnimator ivObjectAnimator = ObjectAnimator.ofFloat(ivSearch, "alpha", 0.9f, 0).setDuration(alphaDuration);
                        ObjectAnimator edtObjectAnimator = ObjectAnimator.ofFloat(edtSearch, "alpha", 0.9f, 0).setDuration(alphaDuration);
                        ObjectAnimator hideIconAnimator=ObjectAnimator.ofFloat(ivCancel,"alpha",0.9f,0).setDuration(alphaDuration/2);
                        final ObjectAnimator showAnimator= ObjectAnimator.ofFloat(ivCancel,"alpha",0,1).setDuration(alphaDuration/2);
                        hideIconAnimator.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                ivCancel.setImageResource(searchDrawable);
                                showAnimator.start();
                            }
                        });


                        hideAnimatorSet = new AnimatorSet();
                        hideAnimatorSet.playTogether(ivObjectAnimator, edtObjectAnimator,hideIconAnimator);
                        hideAnimatorSet.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                setHideSearchEnd();
                            }

                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);
                                setHideSearchStar();
                            }
                        });
                    }
                    if (hideAnimatorSet.isRunning() || showAnimatorSet.isRunning()) {
                        return;
                    }
                    hideAnimatorSet.start();

                } else {
                    if (showAnimatorSet == null) {

                        ObjectAnimator ivObjectAnimator = ObjectAnimator.ofFloat(ivSearch, "translationX", ivCancel.getLeft()-ivSearch.getLeft(), 0).setDuration(duration);
                        ObjectAnimator edtObjectAnimator = ObjectAnimator.ofFloat(edtSearch, "translationX", ivCancel.getRight()-edtSearch.getLeft(), 0).setDuration(duration);
                        ObjectAnimator showAnimator= ObjectAnimator.ofFloat(ivCancel,"alpha",0,1).setDuration(alphaDuration);

                        showAnimatorSet = new AnimatorSet();
                        showAnimatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
                        showAnimatorSet.playTogether(ivObjectAnimator, edtObjectAnimator,showAnimator);
                        showAnimatorSet.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);
                                setShowSearchStart();
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                setShowSearchEnd();
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

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(null!=mOnSearchViewManagerListener){
                    mOnSearchViewManagerListener.onTextChangeListener(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void setHideSearchStar() {
        imm.hideSoftInputFromWindow(edtSearch.getWindowToken(),0);
        edtSearch.setCursorVisible(false);
        if(null!=mOnStatusChangeListener){
            mOnStatusChangeListener.onHideStartListener();
        }

    }




    private void setHideSearchEnd() {
        edtSearch.setVisibility(INVISIBLE);
        ivSearch.setVisibility(INVISIBLE);
        edtSearch.setAlpha(1f);
        ivSearch.setAlpha(1f);
        edtSearch.setText("");
        if(null!=mOnStatusChangeListener){
            mOnStatusChangeListener.onHideEndListener();
        }
    }

    private void setShowSearchStart() {
        ivSearch.setVisibility(VISIBLE);
        edtSearch.setVisibility(VISIBLE);
        edtSearch.setCursorVisible(false);
        edtSearch.setHint("");
        ivCancel.setImageResource(cancelDrawable);
        if(null!=mOnStatusChangeListener){
            mOnStatusChangeListener.onShowStartListener();
        }
    }

    private void setShowSearchEnd() {
        edtSearch.setCursorVisible(true);
        edtSearch.requestFocus();
        edtSearch.setHint(hint);
        if(openWtihShowSoftInput){
            imm.showSoftInput(edtSearch,InputMethodManager.SHOW_FORCED);
        }
        if(null!=mOnStatusChangeListener){
            mOnStatusChangeListener.onShowEndListener();
        }
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


    public void setOnSearchViewManagerListener(OnSearchViewManagerListener onSearchViewManagerListener) {
        mOnSearchViewManagerListener = onSearchViewManagerListener;
    }

    public void setOnStatusChangeListener(OnStatusChangeListener onStatusChangeListener) {
        mOnStatusChangeListener = onStatusChangeListener;
    }

    public interface OnSearchViewManagerListener{
        void onTextChangeListener(CharSequence s);
    }

    public interface OnStatusChangeListener{
        void onShowStartListener();
        void onShowEndListener();
        void onHideStartListener();
        void onHideEndListener();
    }


    public abstract class StatusChangeListenerAdapter implements OnStatusChangeListener{
        @Override
        public void onHideEndListener() {

        }

        @Override
        public void onHideStartListener() {

        }

        @Override
        public void onShowEndListener() {

        }

        @Override
        public void onShowStartListener() {

        }
    }
}
