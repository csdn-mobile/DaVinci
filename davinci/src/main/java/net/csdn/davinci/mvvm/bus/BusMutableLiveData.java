package net.csdn.davinci.mvvm.bus;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

class BusMutableLiveData<T> extends MutableLiveData<T> implements DavinciObservable<T> {

    private final String key;
    private final Map<Observer<? super T>, Observer<? super T>> observerMap = new HashMap<>();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private class PostValueRunnable implements Runnable {
        private final T newValue;

        public PostValueRunnable(@NonNull T newValue) {
            this.newValue = newValue;
        }

        @Override
        public void run() {
            setValue(newValue);
        }
    }

    public BusMutableLiveData(String key) {
        this.key = key;
    }

    @Override
    public void postValue(T value) {
        mainHandler.post(new PostValueRunnable(value));
    }

    @Override
    public void postValueDelay(T value, long delay) {
        mainHandler.postDelayed(new PostValueRunnable(value), delay);
    }

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        super.observe(owner, observer);
        hook(observer);
    }

    @Override
    public void observeForever(@NonNull Observer<? super T> observer) {
        if (!observerMap.containsKey(observer)) {
            observerMap.put(observer, new ObserverWrapper(observer));
        }
        super.observeForever(observerMap.get(observer));
    }

    @Override
    public void removeObserver(@NonNull Observer<? super T> observer) {
        Observer<? super T> realObserver = null;
        if (observerMap.containsKey(observer)) {
            realObserver = observerMap.remove(observer);
        }
        if (realObserver == null) {
            realObserver = observer;
        }
        super.removeObserver(realObserver);
        if (!hasObservers()) {
            DavinciBus.getInstance().getBus().remove(key);
        }
    }

    private void hook(@NonNull Observer<? super T> observer) {
        try {
            //get wrapper's version
            Class<LiveData> classLiveData = LiveData.class;
            Field fieldObservers = classLiveData.getDeclaredField("mObservers");
            fieldObservers.setAccessible(true);
            Object objectObservers = fieldObservers.get(this);
            Class<?> classObservers = objectObservers.getClass();
            Method methodGet = classObservers.getDeclaredMethod("get", Object.class);
            methodGet.setAccessible(true);
            Object objectWrapperEntry = methodGet.invoke(objectObservers, observer);
            Object objectWrapper = null;
            if (objectWrapperEntry instanceof Map.Entry) {
                objectWrapper = ((Map.Entry) objectWrapperEntry).getValue();
            }
            if (objectWrapper == null) {
                throw new NullPointerException("Wrapper can not be bull!");
            }
            Class<?> classObserverWrapper = objectWrapper.getClass().getSuperclass();
            Field fieldLastVersion = classObserverWrapper.getDeclaredField("mLastVersion");
            fieldLastVersion.setAccessible(true);
            //get livedata's version
            Field fieldVersion = classLiveData.getDeclaredField("mVersion");
            fieldVersion.setAccessible(true);
            Object objectVersion = fieldVersion.get(this);
            //set wrapper's version
            fieldLastVersion.set(objectWrapper, objectVersion);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class ObserverWrapper<T> implements Observer<T> {

        private Observer<T> observer;

        public ObserverWrapper(Observer<T> observer) {
            this.observer = observer;
        }

        @Override
        public void onChanged(@Nullable T t) {
            if (observer != null) {
                if (isCallOnObserve()) {
                    return;
                }
                observer.onChanged(t);
            }
        }

        private boolean isCallOnObserve() {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            if (stackTrace.length > 0) {
                for (StackTraceElement element : stackTrace) {
                    // observeForever不感知生命周期
                    if ("android.arch.lifecycle.LiveData".equals(element.getClassName()) &&
                            "observeForever".equals(element.getMethodName())) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
