package cn.fxlcy.viewinjector;

import android.support.annotation.Nullable;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by fxlcy on 2016/12/8.
 */

class InjectEventResolverList extends LinkedList<InjectEventResolverList.InjectEventResolver> {
    private Object mTarget;
    private Object mEventTarget;

    InjectEventResolverList(Object target, @Nullable Object eventTarget) {
        mTarget = target;
        mEventTarget = eventTarget == null ? mTarget : eventTarget;
    }

    public Object getTarget() {
        return mTarget;
    }

    public Object getEventTarget() {
        return mEventTarget;
    }

    static class InjectEventResolver {
        private Object mMethod;
        private InjectEvent mInjectEvent;

        InjectEventResolver(Object method, InjectEvent injectEvent) {
            mMethod = method;
            mInjectEvent = injectEvent;
        }


        public InjectEvent getInjectEvent() {
            return mInjectEvent;
        }

        public void inject(Object eventTarget, View view) {
            mInjectEvent.setEvent(eventTarget, view, mMethod);
        }
    }
}
