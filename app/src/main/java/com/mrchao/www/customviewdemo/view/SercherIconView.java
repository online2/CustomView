package com.mrchao.www.customviewdemo.view;

import android.animation.Animator;
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
 * on 2017/7/4.
 * 搜索自定义View
 */

public class SercherIconView extends View implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {

    private Paint mPaint;
    private Path mPathCir,cirDst;
    private Path mBigPath, bigDst;
    private PathMeasure mPathMeasure;
    private float[] pos = new float[2];
    private float[] tan = new float[2];
    private ValueAnimator loadAnim, okAnim, startSerAnim;
    private float mAnimatedValue;
    private int State = 0;//状态有四种
    private boolean isOver = false;//标志位，表示load状态执行完，可以开始画放大镜了

    public SercherIconView(Context context) {
        super(context);
        init(context);
    }

    public SercherIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SercherIconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4);
        mPaint.setColor(context.getResources().getColor(R.color.lightskyblue));

        loadAnim = ValueAnimator.ofFloat(0, 1);
        loadAnim.setRepeatCount(0);
        loadAnim.setDuration(1500);
        loadAnim.addUpdateListener(this);
        loadAnim.addListener(this);

        okAnim = ValueAnimator.ofFloat(1, 0);
        okAnim.setDuration(1500);
        okAnim.setRepeatCount(0);
        okAnim.addUpdateListener(this);

        startSerAnim = ValueAnimator.ofFloat(0, 1);
        startSerAnim.setDuration(1500);
        startSerAnim.setRepeatCount(0);
        startSerAnim.addUpdateListener(this);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mPathMeasure = new PathMeasure();
        mBigPath = new Path();//外圆
        bigDst = new Path();
        mBigPath.addArc(new RectF(-70, -70, 70, 70), 45, 359.9f);
        mPathMeasure.setPath(mBigPath, true);
        mPathMeasure.getPosTan(0, pos, tan);//记录外圆起点坐标
        float x = pos[0];
        float y = pos[1];
        mPathCir = new Path();
        mPathCir.addArc(new RectF(-35, -35, 35, 35), 45, 359.9f); //放大镜
        mPathCir.lineTo(x, y);
        cirDst = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(getWidth() / 2, getHeight() / 2);
        if (State == 0) {
            canvas.drawPath(mPathCir, mPaint);
        }
        if (State == 1) {//开始搜索 放大镜消失然后转圈
            cirDst.reset();
            mPathMeasure.nextContour();
            mPathMeasure.setPath(mPathCir, false);
            mPathMeasure.getSegment(mPathMeasure.getLength() * mAnimatedValue, mPathMeasure.getLength(), cirDst, true);
            canvas.drawPath(cirDst, mPaint);
        }
        if (State == 2) {//搜索中
            bigDst.reset();
            mPathMeasure.nextContour();
            mPathMeasure.setPath(mBigPath, false);
            float stopD = mPathMeasure.getLength() * mAnimatedValue;
            float start = (float) (stopD - (0.5 - Math.abs(0.5 - mAnimatedValue)) * 200f);
            mPathMeasure.getSegment(start, stopD, bigDst, true);
            canvas.drawPath(bigDst, mPaint);
        }
        if (State == 3) {//搜索成功 画出放大镜
            cirDst.reset();
            mPathMeasure.nextContour();
            mPathMeasure.setPath(mPathCir, false);
            mPathMeasure.getSegment(mPathMeasure.getLength() * mAnimatedValue, mPathMeasure.getLength(), cirDst, true);
            canvas.drawPath(cirDst, mPaint);
        }
    }

    /**
     * 开始搜索
     */
    public void startSer() {
        isOver = false;
        State = 1;
        startSerAnim.start();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                endSer();
            }
        }, 6000);
    }

    /**
     * 搜索结束
     */
    public void endSer() {
        isOver = true;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        if (startSerAnim.equals(animation)) {
            mAnimatedValue = (float) animation.getAnimatedValue();
            if (mAnimatedValue == 1) {
                State = 2;
                loadAnim.start();
            }
            invalidate();
        } else if (loadAnim.equals(animation)) {
            mAnimatedValue = (float) animation.getAnimatedValue();
            invalidate();
        } else if (okAnim.equals(animation)) {
            mAnimatedValue = (float) animation.getAnimatedValue();
            invalidate();
        }
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (!isOver) {
            startSerAnim.start();
        } else {
            State = 3;
            okAnim.start();
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
