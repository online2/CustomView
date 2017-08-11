package com.mrchao.www.customviewdemo.chartview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnticipateInterpolator;

import com.mrchao.www.customviewdemo.R;
import com.mrchao.www.customviewdemo.chartview.bean.TwoPieViewBean;
import com.mrchao.www.customviewdemo.util.MathUtil;

import java.util.ArrayList;
import java.util.Random;

/**
 * 两个圆弧的Pie
 * author:Linfc on 2017/8/10.
 */

public class TwoPieView extends View {

    private int overRadius, innerRadius, inner2Radius;

    private int startAngle = 0;
    private int overSweep = 0;
    private int overSweep2 = 0;
    private int sweepAngle1 = 0;
    private int sweepAngle2 = 0;

    private Path overCirPath, innerCirPath;
    private RectF overRectF, innerRectF, inner2RectF;
    private Paint mPaint;
    //外圆环标记物坐标
    private float[] overPoint = new float[2];
    private float[] overTan = new float[2];
    //内圆环标记物坐标
    private float[] innerPoint = new float[2];
    private float[] innerTan = new float[2];
    private PathMeasure pathMeasure;
    private int mStrokeWidth;//圆环宽度
    private boolean isStart = false;

    private ArrayList<TwoPieViewBean> data = new ArrayList<>();
    private float mAnimatedFraction;


    public TwoPieView(Context context) {
        this(context, null);
    }

    public TwoPieView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TwoPieView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mStrokeWidth = MathUtil.dp2px(2);
        overCirPath = new Path();
        overRectF = new RectF();
        innerCirPath = new Path();
        innerRectF = new RectF();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        inner2RectF = new RectF();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        overRadius = w / 4;
        innerRadius = overRadius - mStrokeWidth;
        inner2Radius = innerRadius - mStrokeWidth;
        overRectF.set(-overRadius, -overRadius, overRadius, overRadius);
        innerRectF.set(-innerRadius, -innerRadius, innerRadius, innerRadius);
        inner2RectF.set(-inner2Radius, -inner2Radius, inner2Radius, inner2Radius);
        pathMeasure = new PathMeasure();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(getWidth() / 2, getHeight() / 2);//坐标移到view中心
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_red));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        overCirPath.addArc(overRectF, 0, 360);
        canvas.drawPath(overCirPath, mPaint);
        if (isStart) {
            //画外层圆环
//            mPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_red));
//            mPaint.setStyle(Paint.Style.STROKE);
//            mPaint.setStrokeWidth(mStrokeWidth);
//            overCirPath.reset();
//            overCirPath.addArc(overRectF, 0, 360);
//            pathMeasure.nextContour();
//            pathMeasure.setPath(overCirPath, true);
//            float stopD = mAnimatedFraction * pathMeasure.getLength();
//            pathMeasure.getSegment(0, stopD, dst, true);
//            canvas.drawPath(dst, mPaint);

            drawOverCiclr(canvas);
            drawInnerCiclr(canvas);
        }
    }

    /**
     * 画外圆环
     *
     * @param canvas
     */
    private void drawOverCiclr(Canvas canvas) {
        //画实心大圆
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawArc(overRectF, startAngle, overSweep, true, mPaint);

        //画大圆标记
        String tltle1 = data.get(0).getTitle();
        String value1 = data.get(0).getValue();

        mPaint.setStyle(Paint.Style.STROKE);
        overCirPath.reset();
        overCirPath.addArc(overRectF, startAngle, overSweep);
        pathMeasure.nextContour();
        pathMeasure.setPath(overCirPath, false);
        pathMeasure.getPosTan(pathMeasure.getLength() / 2, overPoint, overTan);
        float[] overLinePoint = MathUtil.getCoordinatePoint(overRadius + MathUtil.dp2px(10),
                (float) MathUtil.getTanAngle(overPoint), 0, 0);
        float v = 0;
        float v1 = 0;
        int textWidth1 = 0;
        if (tltle1.length() > value1.length()) {
            textWidth1 = MathUtil.getTextWidth(mPaint, tltle1);
        } else {
            textWidth1 = MathUtil.getTextWidth(mPaint, value1);
        }
        if (overLinePoint[0] > 0) {
            v = overLinePoint[0] + textWidth1;
            v1 = overLinePoint[0] - 1;
        } else {
            v = overLinePoint[0] - textWidth1;
            v1 = overLinePoint[0] + 1;
        }

        canvas.drawLine(overPoint[0], overPoint[1], overLinePoint[0], overLinePoint[1], mPaint);
        canvas.drawLine(v1, overLinePoint[1], v, overLinePoint[1], mPaint);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(1);
        mPaint.setTextSize(MathUtil.sp2px(8));
        //在第1,2象限花在线下方，在3,4画在线上方
        float x = 0;
        float y = 0;
        float y1 = 0;
        if (overLinePoint[0] > 0) {
            x = overLinePoint[0];
        } else {
            x = v;
        }
        if (overLinePoint[1] > 0) {
            y = overLinePoint[1] + MathUtil.getTextHeight(mPaint, tltle1) + MathUtil.dp2px(2);
            y1 = overLinePoint[1] + MathUtil.getTextHeight(mPaint, value1) * 2 + MathUtil.dp2px(2);
        } else {
            y = overLinePoint[1] - MathUtil.getTextHeight(mPaint, tltle1) * 2;
            y1 = overLinePoint[1] - MathUtil.getTextHeight(mPaint, value1) + MathUtil.dp2px(4);
        }
        canvas.drawText(tltle1, x, y, mPaint);
        canvas.drawText(value1, x, y1, mPaint);
    }

    /**
     * 画内圆环
     *
     * @param canvas
     */
    private void drawInnerCiclr(Canvas canvas) {
        //画内圆
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_green));
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawArc(inner2RectF, overSweep + startAngle, overSweep2, true, mPaint);
        //画白边
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_white));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        canvas.drawArc(innerRectF, overSweep + startAngle, overSweep2, true, mPaint);

        //画内圆数据
        innerCirPath.reset();
        innerCirPath.addArc(inner2RectF, overSweep + startAngle, overSweep2);
        pathMeasure.nextContour();
        pathMeasure.setPath(innerCirPath, false);
        pathMeasure.getPosTan(pathMeasure.getLength() / 2, innerPoint, innerTan);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(MathUtil.sp2px(1));
        mPaint.setTextSize(MathUtil.sp2px(9));
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_white));

//        float[] innerCentPoint = MathAngleUtil.getCoordinatePoint(innerRadius / 2,
//                (float) MathAngleUtil.getTanAngle(innerPoint), 0, 0);
        String tltle2 = data.get(1).getTitle();
        String value2 = data.get(1).getValue();
        int textWidth = 0;
        if (tltle2.length() > value2.length()) {
            textWidth = MathUtil.getTextWidth(mPaint, tltle2);
        } else {
            textWidth = MathUtil.getTextWidth(mPaint, value2);
        }
//        innerCentPoint[0] = innerCentPoint[0] - textWidth / 2;
//        canvas.drawText(tltle2, innerCentPoint[0], innerCentPoint[1] + MathAngleUtil.getTextHeight(mPaint, tltle2) - MathAngleUtil.dp2px(4), mPaint);
//        canvas.drawText(value2, innerCentPoint[0], innerCentPoint[1] + MathAngleUtil.getTextHeight(mPaint, value2) * 2, mPaint);

        float[] start = MathUtil.getCoordinatePoint((innerRadius - textWidth) / 2, (float) MathUtil.getTanAngle(innerPoint), 0, 0);
        float[] end = MathUtil.getCoordinatePoint((innerRadius + textWidth) / 2, (float) MathUtil.getTanAngle(innerPoint), 0, 0);
        innerCirPath.reset();
        if (start[0]>0){
            innerCirPath.moveTo(start[0], start[1]);
            innerCirPath.lineTo(end[0], end[1]);
        }else {
            innerCirPath.moveTo(end[0], end[1]);
            innerCirPath.lineTo(start[0], start[1]);
        }

        canvas.drawTextOnPath(tltle2,innerCirPath,0,0,mPaint);

    }


    /**
     * 设置每个色块角度
     */
    public void start() {
        isStart = true;
        startAngle = new Random().nextInt(360);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new AnticipateInterpolator());//先后弹在向前
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAnimatedFraction = valueAnimator.getAnimatedFraction();
                overSweep = (int) (sweepAngle1 * mAnimatedFraction);
                overSweep2 = (int) (sweepAngle2 * mAnimatedFraction);
                postInvalidate();
            }
        });
        valueAnimator.start();
    }

    /**
     * 设置数据
     *
     * @param data
     */
    public void setDataInfo(ArrayList<TwoPieViewBean> data) {
        this.data.clear();
        this.data.addAll(data);
        sweepAngle1 = (int) (data.get(0).getPencet() * 3.6);
        sweepAngle2 = (int) (data.get(1).getPencet() * 3.6);
    }
}
