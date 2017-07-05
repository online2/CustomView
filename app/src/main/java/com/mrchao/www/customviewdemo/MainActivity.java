package com.mrchao.www.customviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mrchao.www.customviewdemo.view.AiLoadView;
import com.mrchao.www.customviewdemo.view.HuaWeiLoadView;
import com.mrchao.www.customviewdemo.view.SercherIconView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.aiLoad, R.id.alSucc, R.id.alFauil, R.id.huaweiLoad, R.id.serCherLoad})
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
        }
    }
}
