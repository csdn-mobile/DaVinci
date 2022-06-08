package net.csdn.davinci.mvvm.bus;

import java.util.HashMap;
import java.util.Map;

public class DavinciBus {
    private final Map<String, BusMutableLiveData<Object>> bus;

    private DavinciBus() {
        bus = new HashMap<>();
    }

    private static class SingletonHolder {
        private static final DavinciBus INSTANCE = new DavinciBus();
    }

    public static DavinciBus getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public synchronized <T> DavinciObservable<T> with(String key, Class<T> type) {
        if (!bus.containsKey(key)) {
            bus.put(key, new BusMutableLiveData<>(key));
        }
        return (DavinciObservable<T>) bus.get(key);
    }

    public synchronized DavinciObservable<Object> with(String key) {
        return with(key, Object.class);
    }

    Map<String, BusMutableLiveData<Object>> getBus() {
        return bus;
    }
}
