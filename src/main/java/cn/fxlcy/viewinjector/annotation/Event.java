package cn.fxlcy.viewinjector.annotation;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.fxlcy.viewinjector.InjectEvent;
import cn.fxlcy.viewinjector.InjectOnClickListener;

/**
 * Created by fxlcy on 2016/12/8.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Event {
    /**
     * 注入事件的view的id數組
     */
    int[] value();

    Class<? extends InjectEvent> injectEvent() default InjectOnClickListener.class;

    Class eventType() default View.OnClickListener.class;
}
