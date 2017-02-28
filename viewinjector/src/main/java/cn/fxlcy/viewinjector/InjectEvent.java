package cn.fxlcy.viewinjector;

import android.view.View;

/**
 * Created by fxlcy on 2016/12/8.
 */

public interface InjectEvent<V extends View, E> {
    void setEvent(Object target, V view, E method);
}
