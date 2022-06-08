package net.csdn.davinci.mvvm.bus;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

public interface DavinciObservable<T> {

    void setValue(T value);

    void postValue(T value);

    void postValueDelay(T value, long delay);

    void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer);

    void observeForever(@NonNull Observer<? super T> observer);

    void removeObserver(@NonNull Observer<? super T> observer);
}
