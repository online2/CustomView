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
 * 模仿支付宝支付LoadView
 */

public class AiLoadView extends View implements ValueAnimator.AnimatorUpdateListener {

    private Paint mPaint, mPaintError;
    private Path mPathLoad, mOkPath, mErrorRightPath, mErrorLeftPath;
    private Path dst, dstOk, dstRightLine, dstLeftLine;
    private PathMeasure mPathMeasure;
    /**
     * 0加载中 1加载成功 -1 加载失败
     */
    private int State = 0;
    /**
     * 不同状态下对应的不同动画
     */
    private ValueAnimator mAnimLoad, mAnimStateChange, mAnimRightLine, mAnimLeftLine;
    /**
     * 几个状态的下对应的动画值
     */
    private float loadValue, circleValue, rightLineValue, leftLineValue;
    /**
     * 状态结束后的回调
     */
    private StartEndListener mListener;

    private float StrokeWidth = 4;

    public AiLoadView(Context context) {
        super(context);
        init(context);
    }

    public AiLoadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AiLoadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(context.getResources().getColor(R.color.deepskyblue));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(StrokeWidth);
        mPaintError = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintError.setColor(context.getResources().getColor(R.color.darksalmon));
        mPaintError.setStyle(Paint.Style.STROKE);
        mPaintError.setStrokeWidth(StrokeWidth);

        mAnimLoad = ValueAnimator.ofFloat(0, 1);
        mAnimLoad.setDuration(1000);
        mAnimLoad.setRepeatCount(ValueAnimator.INFINITE);
        mAnimLoad.addUpdateListener(this);
        mAnimLoad.start();

        mAnimStateChange = ValueAnimator.ofFloat(0, 1);
        mAnimStateChange.setDuration(800);
        mAnimStateChange.setRepeatCount(0);
        mAnimStateChange.addUpdateListener(this);

        mAnimRightLine = ValueAnimator.ofFloat(0, 1);
        mAnimRightLine.setDuration(600);
        mAnimRightLine.setRepeatCount(0);
        mAnimRightLine.addUpdateListener(this);

        mAnimLeftLine = ValueAnimator.ofFloat(0, 1);
        mAnimLeftLine.setDuration(600);
        mAnimLeftLine.setRepeatCount(0);
        mAnimLeftLine.addUpdateListener(this);

        mPathLoad = new Path();
        mOkPath = new Path();
        mErrorRightPath = new Path();
        mErrorLeftPath = new Path();
        dst = new Path();
        dstOk = new Path();
        dstRightLine = new Path();
        dstLeftLine = new Path();

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mPathLoad.addArc(new RectF(StrokeWidth, StrokeWidth, w - StrokeWidth, h - StrokeWidth), 0, 360);
        mPathMeasure = new PathMeasure(mPathLoad, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        dst.reset();
        if (State == 0) {
            float stopD = mPathMeasure.getLength() * loadValue;//0-len
            float startD = (float) (stopD - ((0.5 - Math.abs(loadValue - 0.5)) * mPathMeasure.getLength()));
            mPathMeasure.getSegment(startD, stopD, dst, true);
            canvas.drawPath(dst, mPaint);
        }
        if (State == 1) {
            mPathMeasure.nextContour();
            mPathMeasure.setPath(mPathLoad, true);
            mPathMeasure.getSegment(0, circleValue * mPathMeasure.getLength(), dst, true);
            canvas.drawPath(dst, mPaint);
            if (circleValue == 1) {//表示圆画好了可以画打勾
                dstOk.reset();
                mOkPath.moveTo(getWidth() / 3, getWidth() / 2);
                mOkPath.lineTo(getWidth() / 2, getWidth() / 5 * 3);
                mOkPath.lineTo(getWidth() / 3 * 2, getWidth() / 5 * 2);
                mPathMeasure.nextContour();
                mPathMeasure.setPath(mOkPath, false);
                mPathMeasure.getSegment(0, rightLineValue * mPathMeasure.getLength(), dstOk, true);
                canvas.drawPath(dstOk, mPaint);
                endAllAnim(rightLineValue);
            }
        }
        if (State == -1) {
            mPathMeasure.nextContour();
            mPathMeasure.setPath(mPathLoad, true);
            mPathMeasure.getSegment(0, circleValue * mPathMeasure.getLength(), dst, true);
            canvas.drawPath(dst, mPaint);
            if (circleValue == 1) {//表示圆画好了可以画右边那条线
                dstRightLine.reset();
                dstRightLine.lineTo(0, 0);
                mErrorRightPath.moveTo(getWidth() / 3, getWidth() / 3);
                mErrorRightPath.lineTo(getWidth() / 3 * 2, getWidth() / 3 * 2);
                mPathMeasure.nextContour();
                mPathMeasure.setPath(mErrorRightPath, false);
                mPathMeasure.getSegment(0, rightLineValue * mPathMeasure.getLength(), dstRightLine, true);
                canvas.drawPath(dstRightLine, mPaintError);
            }
            if (rightLineValue == 1) {//表示右边那条线可以画左边那条线
                dstLeftLine.reset();
                mErrorLeftPath.moveTo(getWidth() / 3 * 2, getWidth() / 3);
                mErrorLeftPath.lineTo(getWidth() / 3, getWidth() / 3 * 2);
                mPathMeasure.nextContour();
                mPathMeasure.setPath(mErrorLeftPath, false);
                mPathMeasure.getSegment(0, leftLineValue * mPathMeasure.getLength(), dstLeftLine, true);
                canvas.drawPath(dstLeftLine, mPaintError);
                endAllAnim(leftLineValue);
            }
        }
    }

    private void endAllAnim(float value) {
        if (value == 1) {
            if (mListener != null) {
                mListener.endListener(State);
            }
            leftLineValue = 0;
        }
    }

    public void setState(int state) {
        this.State = state;
        mAnimLoad.cancel();
        mAnimStateChange.start();
        invalidate();
    }


    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        if (animation.equals(mAnimLoad)) {
            loadValue = (float) animation.getAnimatedValue();
            invalidate();
        } else if (animation.equals(mAnimStateChange)) {
            circleValue = (float) animation.getAnimatedValue();
            if (circleValue == 1) {
                mAnimRightLine.start();
            }
            invalidate();
        } else if (animation.equals(mAnimRightLine)) {
            rightLineValue = (float) animation.getAnimatedValue();
            if (rightLineValue == 1 && State == -1) {
                mAnimLeftLine.start();
            }
            invalidate();
        } else if (animation.equals(mAnimLeftLine)) {
            leftLineValue = (float) animation.getAnimatedValue();
            invalidate();
        }
    }

    public void setListener(StartEndListener listener) {
        mListener = listener;
    }

    public interface StartEndListener {
        void endListener(int state);
    }

}
