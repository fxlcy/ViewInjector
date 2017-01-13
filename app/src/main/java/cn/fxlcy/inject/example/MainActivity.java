package cn.fxlcy.inject.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import cn.fxlcy.viewinjector.ViewInjector;
import cn.fxlcy.viewinjector.annotation.Event;
import cn.fxlcy.viewinjector.annotation.InjectView;

public class MainActivity extends AppCompatActivity {
    @InjectView(R.id.tv_test)
    private TextView mTvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ViewInjector.getDelegate(this)// 必须在onCreate之前执行
                .addInjectTarget(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }


    //TODO 注意混淆时排除用使用Event注解方法
    @Event(R.id.btn_test)
    void onClick(View view) {
        mTvTest.setText("hello world!");
    }
}
