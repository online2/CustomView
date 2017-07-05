package com.mrchao.www.customviewdemo.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.mrchao.www.customviewdemo.R;


/**
 * Created by Linfc
 * on 2017/7/3.
 * 仿华为的LodeView
 */

public class HuaWeiLoadView extends View {
    private int radius = 10;
    private Path mSamllPath;
    private PathMeasure mPathMeasure;
    private Path mBigPath;
    private Paint mSmallCircle;
    private Paint mBigCircle;
    private float[] pos = new float[2];
    private float[] tan = new float[2];
    private float x = 0;
    private float y = 0;

    public HuaWeiLoadView(Context context) {
        super(context);
        init(context);
    }


    public HuaWeiLoadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HuaWeiLoadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mSmallCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSmallCircle.setColor(context.getResources().getColor(R.color.springgreen));
        mSmallCircle.setStyle(Paint.Style.FILL);
        mSmallCircle.setStrokeWidth(4);

        mBigCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBigCircle.setColor(context.getResources().getColor(R.color.lightskyblue));
        mBigCircle.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mSamllPath = new Path();
        mSamllPath.addArc(new RectF(radius, radius, w  - radius, h  - radius), 0, 359);
        mBigPath = new Path();
        mBigPath.addArc(new RectF(radius, radius, w  - radius, h  - radius), 0, 360);
        mPathMeasure = new PathMeasure(mBigPath, false);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(mBigPath, mBigCircle);
        if (x == 0 && y == 0) {
            startAnim();
        } else {
            canvas.drawCircle(x, y, radius, mSmallCircle);
        }

    }

    private void startAnim() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        animator.setDuration(3000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float mAnimatedValue = (float) animation.getAnimatedValue();
                mPathMeasure.getPosTan(mAnimatedValue, pos, tan);
                x = pos[0];
                y = pos[1];
                invalidate();
            }
        });
        animator.start();
    }
}
