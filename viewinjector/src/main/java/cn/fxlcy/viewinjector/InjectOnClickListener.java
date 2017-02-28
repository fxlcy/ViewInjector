package cn.fxlcy.viewinjector;

import android.view.View;

/**
 * Created by fxlcy on 2016/12/8.
 */

public class InjectOnClickListener implements InjectEvent<View, View.OnClickListener> {
    @Override
    public void setEvent(final Object target, View view, View.OnClickListener listener) {
        view.setOnClickListener(listener);
    }
}
