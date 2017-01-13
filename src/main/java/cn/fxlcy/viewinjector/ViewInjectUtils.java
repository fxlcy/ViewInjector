package cn.fxlcy.viewinjector;

import android.content.Context;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.view.LayoutInflater;


/**
 * Created by fxlcy on 2016/12/8.
 */

public class ViewInjectUtils {
    private ViewInjectUtils() {
    }

    public static void addInjectTarget(Context context, Object target, Object eventTarget) {
        getBaseLayoutInflaterFactory(context).addInjectTarget(target, eventTarget);
    }

    public static void addInjectTarget(Context context, Object target) {
        addInjectTarget(context, target, null);
    }

    public static void removeInjectTarget(Context context, Object target) {
        getBaseLayoutInflaterFactory(context).removeInjectTarget(target);
    }

    public static void clearInjectTarget(Context context) {
        getBaseLayoutInflaterFactory(context).clearInjectTarget();
    }

    public static void setCurrentInjectTarget(Context context, Object target) {
        getBaseLayoutInflaterFactory(context).setCurrentInjectTarget(target);
    }

    private static BaseLayoutInflaterFactory getBaseLayoutInflaterFactory(Context context) {
        LayoutInflaterFactory factory = LayoutInflaterCompat.getFactory(LayoutInflater.from(context));
        BaseLayoutInflaterFactory baseLayoutInflaterFactory;

        if (factory instanceof BaseLayoutInflaterFactory) {
            baseLayoutInflaterFactory = (BaseLayoutInflaterFactory) factory;
        } else {
            throw new RuntimeException("請執行LayoutInflaterCompat.setFactory(BaseLayoutInflaterFactory)");
        }

        return baseLayoutInflaterFactory;
    }
}
