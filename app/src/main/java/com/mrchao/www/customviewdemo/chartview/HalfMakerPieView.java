package com.mrchao.www.customviewdemo.chartview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.BounceInterpolator;

import com.mrchao.www.customviewdemo.R;
import com.mrchao.www.customviewdemo.util.MathUtil;

import java.util.Random;

/**
 * author:Linfc on 2017/8/10.
 */

public class HalfMakerPieView extends View {

    private int mRadius; // 扇形半径
    private int mStartAngle = 180; // 起始角度
    private int mSweepAngle = 180; // 绘制角度
    private int mMin = 0; // 最小值
    private int mMax = 100; // 最大值
    private int mSection = 10; // 值域（mMax-mMin）等分份数
    private int mPortion = 5; // 一个mSection等分份数
    private int mRealTimeValue1 = mMin; // 实时读数
    private int mStrokeWidth; // 画笔宽度
    private int mLength1; // 长刻度长度
    private int mPLRadius; // 指针长半径
    private int mPSRadius; // 指针短半径

    private Paint mPaint;
    private RectF mRectFArc;//外层圆环区域
    private RectF mTextRectFArc;//文字圆环区域
    private int textRadius;
    private Path mPath;
    private String[] mTexts;


    private float mCenterX = 0, mCenterY = 0; // 圆心坐标
    private Path pathBitCir;//最外层圆环Path
    private PathMeasure pathMeasureBitCir;
    private float[] posStrat = new float[2];//最外层圆环起点坐标
    private float[] posEnd = new float[2];
    private float[] tanStrat = new float[2];//最外层圆环终点坐标
    private float[] tanEnd = new float[2];

    private RectF mackerRectF; //外层标记物区域
    private int makerRadius;

    private int mSweepAngle1, mSweepAngle2, mSweepAngle3;
    private RectF centRectF;


    public HalfMakerPieView(Context context) {
        this(context, null);
    }

    public HalfMakerPieView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HalfMakerPieView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        mStrokeWidth = MathUtil.dp2px(2);
        mLength1 = MathUtil.dp2px(8);

        mPSRadius = MathUtil.dp2px(10);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mRectFArc = new RectF();
        mTextRectFArc = new RectF();
        mPath = new Path();

        mTexts = new String[mSection + 1]; // 需要显示mSection + 1个刻度读数
        for (int i = 0; i < mTexts.length; i++) {
            int n = (mMax - mMin) / mSection;
            if (i == 0 || i == mTexts.length - 1 || i == mTexts.length / 2) {//只显示三个数值
                mTexts[i] = String.valueOf(mMin + i * n);
            } else {
                mTexts[i] = String.valueOf("");
            }
        }

        pathBitCir = new Path();
        pathMeasureBitCir = new PathMeasure();

        mackerRectF = new RectF();
        centRectF = new RectF(-MathUtil.dp2px(14), -MathUtil.dp2px(14), MathUtil.dp2px(14), MathUtil.dp2px(14));

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRadius = w / 3 - mStrokeWidth;

        textRadius = mRadius + MathUtil.dp2px(5);
        mPLRadius = mRadius - mLength1 * 2;
        makerRadius = mRadius + MathUtil.dp2px(27);

        mPaint.setTextSize(MathUtil.sp2px(16));

        mRectFArc.set(-mRadius, -mRadius, mRadius, mRadius);

        mTextRectFArc.set(-textRadius, -textRadius, textRadius,  textRadius);

        pathBitCir.addArc(mRectFArc, mStartAngle - 10, mSweepAngle + 20);
        pathMeasureBitCir.setPath(pathBitCir, false);
        pathMeasureBitCir.getPosTan(0, posStrat, tanStrat);
        pathMeasureBitCir.getPosTan(pathMeasureBitCir.getLength(), posEnd, tanEnd);

        mackerRectF.set(-makerRadius, -makerRadius, makerRadius, makerRadius);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(getWidth() / 2, getHeight() / 2);//坐标移到view中心

//        /**
//         * 画底部色块和标注
//         */
//        mPaint.setTextSize(sp2px(12));
//        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_black));
//        for (int i = 0; i < bottomText.size(); i++) {
//            int textWidth = getTextWidth(bottomText.get(i));
//            int textHeigth = getTextHeight(bottomText.get(i));
//            canvas.drawText(bottomText.get(i), -mRadius + mRadius * i - textWidth / 2, mRadius / 2, mPaint);
//            float left = -mRadius + mRadius * i - textWidth;
//            float top = mRadius / 2 - textHeigth;
//            float bottom = mRadius / 2 + textHeigth / 2;
//            float right = left + textHeigth;
//            bottomRectF.set(left, top, right, bottom);
//            canvas.drawRect(bottomRectF, mPaint);
//
//        }


        /**
         * 画圆弧
         */
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        canvas.drawPath(pathBitCir, mPaint);
        canvas.drawLine(posStrat[0], posStrat[1], mCenterX, mCenterY, mPaint);
        canvas.drawLine(posEnd[0], posEnd[1], mCenterX, mCenterY, mPaint);

        /**
         * 画颜色区域
         */
        int mStartAngle1 = mStartAngle - 10;
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_yellow));
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawArc(mRectFArc, mStartAngle1, mSweepAngle1, true, mPaint);

        int mStartAngle2 = mStartAngle1 + mSweepAngle1;
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_green));
        canvas.drawArc(mRectFArc, mStartAngle2, mSweepAngle2, true, mPaint);

        int mStartAngle3 = mStartAngle2 + mSweepAngle2;
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_red));
        canvas.drawArc(mRectFArc, mStartAngle3, mSweepAngle3, true, mPaint);
        /**
         * 画刻度相关信息
         */
        drawPointInfo(canvas);


        /**
         * 画指针
         */
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        float θ = mStartAngle + mSweepAngle * (mRealTimeValue1 - mMin) / (mMax - mMin); // 指针与水平线夹角
        int d = MathUtil.dp2px(5); // 指针由两个等腰三角形构成，d为共底边长的一半
        mPath.reset();
        float[] p1 = MathUtil.getCoordinatePoint(d, θ - 90, 0, 0);
        mPath.moveTo(p1[0], p1[1]);
        float[] p2 = MathUtil.getCoordinatePoint(mPLRadius, θ, 0, 0);
        mPath.lineTo(p2[0], p2[1]);
        float[] p3 = MathUtil.getCoordinatePoint(d, θ + 90, 0, 0);
        mPath.lineTo(p3[0], p3[1]);
        float[] p4 = MathUtil.getCoordinatePoint(mPSRadius, θ - 180, 0, 0);
        mPath.lineTo(p4[0], p4[1]);
        mPath.close();
        canvas.drawPath(mPath, mPaint);


        /**
         * 画指针上的圆圈和文字
         */
        canvas.save();
        mPath.reset();
        float[] point = MathUtil.getCoordinatePoint(mRadius + MathUtil.dp2px(5), θ, 0, 0);//顶点坐标
        float[] pointCenter = MathUtil.getCoordinatePoint(mRadius + MathUtil.dp2px(30), θ, 0, 0);//圆心坐标
        //画圆
        canvas.translate(pointCenter[0], pointCenter[1]);
        mPaint.setStrokeWidth(MathUtil.dp2px(4));
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(centRectF, 0, 360, false, mPaint);
        canvas.restore();
        //画角
        mPaint.setStyle(Paint.Style.FILL);
        double tanAngle = MathUtil.getTanAngle(new float[]{MathUtil.dp2px(30) + mRadius, MathUtil.dp2px(7) + MathUtil.dp2px(10)});
        int sqrtRadius = (int) Math.sqrt(Math.pow(MathUtil.dp2px(30) + mRadius, 2) + Math.pow(MathUtil.dp2px(7), 2));
        float[] leftPoint = MathUtil.getCoordinatePoint(sqrtRadius, (float) (θ - tanAngle), 0, 0);
        float[] RightPoint = MathUtil.getCoordinatePoint(sqrtRadius, (float) (θ + tanAngle), 0, 0);

        mPath.reset();
        mPath.moveTo(point[0], point[1]);
        mPath.lineTo(leftPoint[0], leftPoint[1]);
        mPath.quadTo(point[0], point[1], RightPoint[0], RightPoint[1]);
        mPath.close();
        canvas.drawPath(mPath, mPaint);


        mPaint.setTextSize(MathUtil.sp2px(9));
        mPaint.setStrokeWidth(MathUtil.sp2px(1));
        mPaint.setStyle(Paint.Style.STROKE);
        mPath.reset();
        // 粗略把文字的宽度视为圆心角2*θ对应的弧长，利用弧长公式得到θ，下面用于修正角度
        float θ2 = (float) (180 * MathUtil.getTextWidth(mPaint, getRealTimeValue() + "%") / 2 /
                (Math.PI * mRadius + MathUtil.dp2px(20)));
        mPath.reset();

        if (θ == 360) {//防止超过180度是文字发生偏移
            mPath.addArc(mackerRectF, θ - θ2, mSweepAngle);
        } else {
            mPath.addArc(mackerRectF, θ - θ2, θ);
        }
        canvas.drawTextOnPath(getRealTimeValue() + "%", mPath, 0, 0, mPaint);
        /**
         * 画指针围绕的镂空圆心
         */
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(mCenterX, mCenterY, MathUtil.dp2px(2), mPaint);


    }

    private void drawPointInfo(Canvas canvas) {

        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        float x0 = -mRadius;
        float y0 = 0;
        float x1 = -mRadius + mLength1;
        float y1 = 0;
        canvas.save();
        canvas.drawLine(x0, y0, x1, y1, mPaint);
        float angle = mSweepAngle * 1f / mSection;
        for (int i = 0; i < mSection; i++) {
            canvas.rotate(angle, mCenterX, mCenterY);
            canvas.drawLine(x0, y0, x1, y1, mPaint);
        }
        canvas.restore();

        /**
         * 画短刻度
         * 同样采用canvas的旋转原理
         */
        canvas.save();
        mPaint.setStrokeWidth(1);
        float x2 = (float) -mRadius + mLength1 / 2;
        float y2 = (float) 0;
        canvas.drawLine(x0, y0, x2, y2, mPaint);
        angle = mSweepAngle * 1f / (mSection * mPortion);
        for (int i = 1; i < mSection * mPortion; i++) {
            canvas.rotate(angle, mCenterX, mCenterY);
            if (i % mPortion == 0) { // 避免与长刻度画重合
                continue;
            }
            canvas.drawLine(x0, y0, x2, y2, mPaint);
        }
        canvas.restore();

        /**
         * 画长刻度读数
         * 添加一个圆弧path，文字沿着path绘制
         */
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_black));
        mPaint.setTextSize(MathUtil.sp2px(10));
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < mTexts.length; i++) {
            // 粗略把文字的宽度视为圆心角2*θ对应的弧长，利用弧长公式得到θ，下面用于修正角度
            float θ = (float) (180 * MathUtil.getTextWidth(mPaint, mTexts[i]) / 2 / (Math.PI * (mRadius - MathUtil.getTextHeight(mPaint, mTexts[i]))));
            mPath.reset();
            mPath.addArc(mTextRectFArc,
                    mStartAngle + i * (mSweepAngle / mSection) - θ, // 正起始角度减去θ使文字居中对准长刻度
                    mSweepAngle
            );
            canvas.drawTextOnPath(mTexts[i], mPath, 0, 0, mPaint);
        }
    }


    public int getRealTimeValue() {
        return mRealTimeValue1;
    }


    public int setRealTimeValue(final int realTimeValue) {
        if (mRealTimeValue1 == realTimeValue || realTimeValue < mMin || realTimeValue > mMax) {
            return 0;
        }
        return realTimeValue;
    }


    /**
     * 设置每个色块角度
     */
    public void setAngleValue(final int sweepAngle1, final int sweepAngle2, final int sweepAngle3) {
        final int i = setRealTimeValue((new Random().nextInt(100)));

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new BounceInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mSweepAngle1 = (int) (sweepAngle1 * valueAnimator.getAnimatedFraction());
                mSweepAngle2 = (int) (sweepAngle2 * valueAnimator.getAnimatedFraction());
                mSweepAngle3 = (int) ((sweepAngle3 + 20) * valueAnimator.getAnimatedFraction());
                mRealTimeValue1 = (int) (i * valueAnimator.getAnimatedFraction());
                postInvalidate();
            }
        });
        valueAnimator.start();
    }


}
