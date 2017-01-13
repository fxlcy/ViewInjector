package cn.fxlcy.viewinjector;

/**
 * Created by fxlcy
 * on 2017/1/13.
 *
 * @author fxlcy
 * @version 1.0
 */

public interface InjectorDelegate {
    void addInjectTarget(Object target, Object eventTarget);

    void addInjectTarget(Object target);

    void removeInjectTarget(Object target);

    void clearInjectTarget();

    void setCurrentInjectTarget(Object target);
}
