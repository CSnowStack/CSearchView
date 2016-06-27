package com.cq.csearchview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by cqll on 2016/6/24.
 */
public class CEditText extends EditText{
    private Paint mPaint;
    private int lineColor;
    private Context mContext;
    public CEditText(Context context) {
        super(context);
    }

    public CEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        mPaint=new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        lineColor= Color.parseColor("#1E1E1E");
    }

    public CEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(lineColor);
        mPaint.setStrokeWidth(DensityUtil.dip2px(mContext,1));

        canvas.drawLine(0,this.getHeight()-DensityUtil.dip2px(mContext,2),  this.getWidth()-1, this.getHeight()-DensityUtil.dip2px(mContext,2), mPaint);
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }
}
