package cn.fxlcy.inject.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import cn.fxlcy.viewinjector.ViewInjector;
import cn.fxlcy.viewinjector.annotation.Event;
import cn.fxlcy.viewinjector.annotation.InjectView;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();

    @InjectView(R.id.tv_test)
    private TextView mTvTest;
    @InjectView(R.id.tv_test1)
    private TextView mTvTest1;
    @InjectView(R.id.tv_test2)
    private TextView mTvTest2;
    @InjectView(R.id.tv_test3)
    private TextView mTvTest3;
    @InjectView(R.id.tv_test4)
    private TextView mTvTest4;
    @InjectView(R.id.tv_test5)
    private TextView mTvTest5;
    @InjectView(R.id.tv_test6)
    private TextView mTvTest6;
    @InjectView(R.id.tv_test7)
    private TextView mTvTest7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"start:" + String.valueOf(System.currentTimeMillis()));

        ViewInjector.getDelegate(this)// 必须在onCreate之前执行
                .addInjectTarget(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

//        initView();

        Log.i(TAG,"end:" + String.valueOf(System.currentTimeMillis()));
    }

    private void initView() {
        mTvTest = (TextView) findViewById(R.id.tv_test);
        mTvTest1 = (TextView) findViewById(R.id.tv_test1);
        mTvTest2 = (TextView) findViewById(R.id.tv_test2);
        mTvTest3 = (TextView) findViewById(R.id.tv_test3);
        mTvTest4 = (TextView) findViewById(R.id.tv_test4);
        mTvTest5 = (TextView) findViewById(R.id.tv_test5);
        mTvTest6 = (TextView) findViewById(R.id.tv_test6);
        mTvTest7 = (TextView) findViewById(R.id.tv_test7);
    }


    //TODO 注意混淆时排除用使用Event注解方法
//    @Event(R.id.btn_test)
//    void onClick(View view) {
//        mTvTest.setText("hello world!");
//    }
}
