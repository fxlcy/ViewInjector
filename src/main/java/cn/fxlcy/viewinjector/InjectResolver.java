package cn.fxlcy.viewinjector;

import android.view.View;

import java.lang.reflect.Field;

/**
 * Created by fxlcy on 2016/12/8.
 */

class InjectResolver {
    private Field mField;
    private Object mTarget;

    InjectResolver(Field field, Object target) {
        mField = field;
        mTarget = target;
    }

    Object getTarget() {
        return mTarget;
    }

    void inject(View view) {
        try {
            mField.set(mTarget, view);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
