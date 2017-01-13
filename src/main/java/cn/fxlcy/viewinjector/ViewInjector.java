package cn.fxlcy.viewinjector;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;

import cn.fxlcy.viewinjector.annotation.Event;
import cn.fxlcy.viewinjector.annotation.InjectView;

/**
 * Created by fxlcy on 2016/12/8.
 */

public final class ViewInjector {
    private final static Class<InjectView> INJECT_VIEW_CLASS = InjectView.class;
    private final static Class<Event> EVENT_CLASS = Event.class;
    private final static Class<View> VIEW_CLASS = View.class;
    private final static int MAX_VIEW_SIZE = 100000;

    private SparseArray<InjectResolver> mInjectViews;
    private SparseArray<InjectEventResolverList> mInjectEvents;

    private static HashMap<Class<InjectEvent>, InjectEvent> sInjectEvent;


    static {
        sInjectEvent = new HashMap<>();
    }

    public static InjectorDelegate getDelegate(Context context) {
        return getDelegate(context, BaseLayoutInflaterFactory.class);
    }

    public static InjectorDelegate getDelegate(Context context, Class<? extends BaseLayoutInflaterFactory> inflaterClazz) {
        BaseLayoutInflaterFactory factory;
        if (inflaterClazz == BaseLayoutInflaterFactory.class) {
            factory = new BaseLayoutInflaterFactory();
        } else {
            try {
                factory = inflaterClazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("请设置" + inflaterClazz.getName() + "<init>()", e);
            }
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        LayoutInflaterCompat.setFactory(inflater, factory);

        return factory;
    }

    ViewInjector(Object target) {
        this(target, null);
    }

    ViewInjector(Object target, @Nullable Object eventTarget) {
        mInjectViews = new SparseArray<>();
        mInjectEvents = new SparseArray<>();

        addTarget(target, eventTarget);
    }

    public void addTarget(Object target) {
        addTarget(target, null);
    }

    public void addTarget(Object target, @Nullable Object eventTarget) {
        eventTarget = eventTarget == null ? target : eventTarget;

        Class targetClass = target.getClass();
        Class eventTargetClass = eventTarget.getClass();

        Field[] fields = targetClass.getDeclaredFields();
        Method[] methods = eventTargetClass.getDeclaredMethods();

        int hashCode = getTargetHashCode(target);

        for (Field f : fields) {
            InjectView injectView = f.getAnnotation(INJECT_VIEW_CLASS);

            if (injectView != null) {
                if (VIEW_CLASS.isAssignableFrom(f.getType())) {
                    InjectResolver ir = new InjectResolver(f, target);
                    mInjectViews.put(injectView.value() + hashCode, ir);
                    f.setAccessible(true);
                } else {
                    throw new RuntimeException("Annotation:" + injectView.getClass() + "只能應用在View或View的子類上");
                }
            }
        }

        for (Method method : methods) {
            Event event = method.getAnnotation(EVENT_CLASS);
            if (event != null) {
                method.setAccessible(true);

                Class<? extends InjectEvent> injectEventClass = event.injectEvent();
                InjectEvent injectEvent;
                injectEvent = sInjectEvent.get(injectEventClass);
                if (injectEvent == null) {
                    try {
                        injectEvent = injectEventClass.newInstance();
                    } catch (Exception e) {
                        e.printStackTrace();

                        throw new RuntimeException("請設置" + injectEventClass.getName() + "public <init>()", e);
                    }
                }

                Object listener = Proxy.newProxyInstance(getClass().getClassLoader(),
                        new Class[]{event.eventType()}, new DynaHandler(eventTarget, method));
                InjectEventResolverList.InjectEventResolver resolver = new InjectEventResolverList.InjectEventResolver(listener, injectEvent);

                int[] viewIds = event.value();
                for (int id : viewIds) {
                    int _id = id + hashCode;
                    InjectEventResolverList injectEventResolvers = mInjectEvents.get(_id);
                    if (injectEventResolvers == null) {
                        injectEventResolvers = new InjectEventResolverList(target, eventTarget);
                        mInjectEvents.put(_id, injectEventResolvers);
                    }

                    injectEventResolvers.add(resolver);
                }
            }
        }
    }

    public void removeTarget(Object target) {
        int size = mInjectViews.size();

        for (int i = (size - 1); i >= 0; i--) {
            if (mInjectViews.valueAt(i).getTarget() == target) {
                mInjectViews.removeAt(i);
            }
        }

        size = mInjectEvents.size();

        for (int i = (size - 1); i >= 0; i--) {
            if (mInjectEvents.valueAt(i).getTarget() == target) {
                mInjectEvents.removeAt(i);
            }
        }
    }

    public void clearTarget() {
        mInjectViews.clear();
        mInjectEvents.clear();
    }

    public boolean isEmpty() {
        return mInjectViews.size() == 0;
    }

    public boolean containsTarget(Object target) {
        int size = mInjectViews.size();

        for (int i = 0; i < size; i++) {
            if (mInjectViews.valueAt(i).getTarget() == target) {
                return true;
            }
        }

        size = mInjectEvents.size();
        for (int i = 0; i < size; i++) {
            if (mInjectEvents.valueAt(i).getTarget() == target) {
                return true;
            }
        }

        return false;
    }

    public void inject(Object target, View view) {
        int id = view.getId();
        if (id > 0) {
            id += getTargetHashCode(target);

            InjectResolver ir = remove(mInjectViews, id);
            if (ir != null) {
                ir.inject(view);
            }

            InjectEventResolverList iers = remove(mInjectEvents, id);
            if (iers != null) {
                Object eventTarget = iers.getEventTarget();
                for (InjectEventResolverList.InjectEventResolver ier : iers) {
                    ier.inject(eventTarget, view);
                }
            }
        }
    }


    private int getTargetHashCode(Object target) {
        int hashCode = target.hashCode();

        if (hashCode < 0 && hashCode > -MAX_VIEW_SIZE) {
            hashCode -= MAX_VIEW_SIZE;
        } else if (hashCode > 0 && hashCode < MAX_VIEW_SIZE) {
            hashCode += MAX_VIEW_SIZE;
        }
        return hashCode;
    }

    private <T> T remove(SparseArray<T> array, int key) {
        int len = array.size() - 1;

        T t = null;

        for (int i = len; i >= 0; i--) {
            if (key == array.keyAt(i)) {
                t = array.valueAt(i);
                array.removeAt(i);
            }
        }

        return t;
    }

    private static class DynaHandler implements InvocationHandler {
        Object target = null;
        Method method = null;

        DynaHandler(Object target, Method method) {
            super();
            this.target = target;
            this.method = method;
        }

        /**
         * 这个函数就是动态注册的回调方法
         */
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            // 这里调用动注入的方法
            return this.method.invoke(target, args);
        }
    }
}
