package net.csdn.mvvm.bus;

import java.util.HashMap;
import java.util.Map;

public class LiveDataBus {
    private final Map<String, BusMutableLiveData<Object>> bus;

    private LiveDataBus() {
        bus = new HashMap<>();
    }

    private static class SingletonHolder {
        private static final LiveDataBus INSTANCE = new LiveDataBus();
    }

    public static LiveDataBus getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public synchronized <T> Observable<T> with(String key, Class<T> type) {
        if (!bus.containsKey(key)) {
            bus.put(key, new BusMutableLiveData<>(key));
        }
        return (Observable<T>) bus.get(key);
    }

    public synchronized Observable<Object> with(String key) {
        return with(key, Object.class);
    }

    Map<String, BusMutableLiveData<Object>> getBus() {
        return bus;
    }
}
