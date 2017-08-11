package com.mrchao.www.customviewdemo.util;

import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.TypedValue;

/**
 * author:Linfc on 2017/8/10.
 */

public class MathUtil {

    /**
     * 根据点坐标返回正切值
     *
     * @param point x y 值
     * @return 角度
     */
    public static double getTanAngle(float[] point) {
        double overTan = 0;
        if ((point[0] > 0 && point[1] > 0)) {//第一象限

            overTan = Math.atan(point[1] / point[0]) * (180 / Math.PI);
        } else if (point[0] < 0 && point[1] > 0) {//第二象限
            overTan = 180 - Math.abs(Math.atan(point[1] / point[0]) * (180 / Math.PI));
        } else if (point[0] < 0 && point[1] < 0) {//第三象限
            overTan = Math.atan(point[1] / point[0]) * (180 / Math.PI) + 180;
        } else {
            overTan = 360 - Math.abs(Math.atan(point[1] / point[0]) * (180 / Math.PI));
        }
        return overTan;
    }


    /**
     * 根据半径和角度
     *
     * @param radius 半径
     * @param angle  角度
     * @return 返回对应的点坐标
     */
    public static float[] getCoordinatePoint(int radius, float angle, int mCenterX, int mCenterY) {
        float[] point = new float[2];

        double arcAngle = Math.toRadians(angle); //将角度转换为弧度
        if (angle < 90) {
            point[0] = (float) (mCenterX + Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY + Math.sin(arcAngle) * radius);
        } else if (angle == 90) {
            point[0] = mCenterX;
            point[1] = mCenterY + radius;
        } else if (angle > 90 && angle < 180) {
            arcAngle = Math.PI * (180 - angle) / 180.0;
            point[0] = (float) (mCenterX - Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY + Math.sin(arcAngle) * radius);
        } else if (angle == 180) {
            point[0] = mCenterX - radius;
            point[1] = mCenterY;
        } else if (angle > 180 && angle < 270) {
            arcAngle = Math.PI * (angle - 180) / 180.0;
            point[0] = (float) (mCenterX - Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY - Math.sin(arcAngle) * radius);
        } else if (angle == 270) {
            point[0] = mCenterX;
            point[1] = mCenterY - radius;
        } else {
            arcAngle = Math.PI * (360 - angle) / 180.0;
            point[0] = (float) (mCenterX + Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY - Math.sin(arcAngle) * radius);
        }

        return point;
    }


    public static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }

    public static int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                Resources.getSystem().getDisplayMetrics());
    }

    public static int getTextWidth(Paint mPaint, String text) {
        //获取文本的高度
        Rect bounds = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.width();
    }

    public static int getTextHeight(Paint mPaint, String text) {
        //获取文本的高度
        Rect bounds = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.height();
    }

}
