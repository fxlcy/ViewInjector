# ViewInjector
### 简介</br>
-----------
通过重写LayoutInflaterFactory实现视图和事件的注入,性能不下于findViewById（）,且不用生成多余的代码</br>
### 用法</br>
#### 注入View和OnClick事件
```java
public class MainActivity extends AppCompatActivity {
    @InjectView(R.id.btn_1)
    private Button mBtn1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //注意要在super.onCreate()之前调用
        ViewInjector.getDelegate(this).addInjectTarget(this/*可以是任何包含@InjectView字段的的Object对象*/
                , this/*可以是任何包含@Event方法的Object对象，如何和前一个参数一样，可以省略*/);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }

    /**
     * 注意防止不要被混淆
     * 默认只能注入onClick事件
     * */
    @Event({R.id.btn_1, R.id.btn_2})
    void onClick(View view) {

    }
}
```
#### 自定义要注入的事件</br>
##### 　①定义InjectEvent接口实现</br>
```java
//实现InjectEvent接口
public class InjectOnContextClickListener implements InjectEvent<AdapterView/*要注入的视图类型*/, AdapterView.OnItemSelectedListener
        /*要注入的事件接口类型*/> {
    @Override
    public void setEvent(Object target, AdapterView view, AdapterView.OnItemSelectedListener method) {
        view.setOnItemSelectedListener(method);//设置事件的实现
    }
}
```
##### 　②使用</br>
```java
    @Event(value = {R.id.btn_1, R.id.btn_2},injectEvent = InjectOnItemSelectedListener.class,eventType = AdapterView.OnItemSelectedListener.class)
    void onItemSelected(View view) {

    }
```
### 通过gradle集成</br>
在模块的build.grale中添加
```groovy
dependencies {
    compile 'cn.fxlcy.library:viewinjector:0.1.0'
}
```
