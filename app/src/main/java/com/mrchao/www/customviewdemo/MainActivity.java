package com.mrchao.www.customviewdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mrchao.www.customviewdemo.chartview.HalfMakerPieView;
import com.mrchao.www.customviewdemo.chartview.TwoPieView;
import com.mrchao.www.customviewdemo.chartview.bean.TwoPieViewBean;
import com.mrchao.www.customviewdemo.view.AiLoadView;
import com.mrchao.www.customviewdemo.view.HuaWeiLoadView;
import com.mrchao.www.customviewdemo.view.SercherIconView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

    @BindView(R.id.aiLoad)
    AiLoadView mAiLoad;
    @BindView(R.id.huaweiLoad)
    HuaWeiLoadView mHuaweiLoad;
    @BindView(R.id.serCherLoad)
    SercherIconView mSerCherLoad;
    @BindView(R.id.alSucc)
    Button mAlSucc;
    @BindView(R.id.alFauil)
    Button mAlFauil;
    @BindView(R.id.dashboard_view_6)
    HalfMakerPieView mDashboardView6;
    @BindView(R.id.dashboard_view_7)
    TwoPieView mDashboardView7;

    private ArrayList<TwoPieViewBean> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        data = new ArrayList<>();
        TwoPieViewBean data1 = new TwoPieViewBean();
        data1.setTitle("总目标");
        data1.setValue("158.1(亿元)");
        data1.setPencet(80);
        TwoPieViewBean data2 = new TwoPieViewBean();
        data2.setTitle("完成:35.16%");
        data2.setValue("55.5(亿元)");
        data2.setPencet(20);
        data.add(data1);
        data.add(data2);
        mDashboardView7.setDataInfo(data);
    }

    @OnClick({R.id.aiLoad, R.id.alSucc, R.id.alFauil, R.id.huaweiLoad, R.id.serCherLoad,
            R.id.dashboard_view_6, R.id.dashboard_view_7})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.alSucc:
                mAiLoad.setState(1);
                break;
            case R.id.alFauil:
                mAiLoad.setState(-1);
                break;
            case R.id.huaweiLoad:
                break;
            case R.id.serCherLoad:
                mSerCherLoad.startSer();
                break;
            case R.id.dashboard_view_6:
                mDashboardView6.setAngleValue(40, 50, 90);
                break;
            case R.id.dashboard_view_7:
                mDashboardView7.start();
                break;
        }
    }

}
