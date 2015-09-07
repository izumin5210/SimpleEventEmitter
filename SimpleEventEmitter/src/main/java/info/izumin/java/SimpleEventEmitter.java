package info.izumin.java;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleEventEmitter<K extends Enum<K>> {
    private EnumMap<K, Map<Class, List<SimpleEventListener>>> mListeners;
    private Class<K> mEnumType;

    public SimpleEventEmitter(Class<K> enumType) {
        mEnumType = enumType;
        removeAllListeners();
    }

    public <T> void on(K key, Class<T> type, SimpleEventListener<T> listener) {
        if (!mListeners.containsKey(key)) {
            init(key, type);
        }
        mListeners.get(key).get(type).add(listener);
    }

    public <T> void addListener(K key, Class<T> type, SimpleEventListener<T> listener) {
        on(key, type, listener);
    }

    public <T> void removeListener(K key, Class<T> type, SimpleEventListener<T> listener) {
        List<SimpleEventListener> listeners = mListeners.get(key).get(type);
        if (listeners != null) {
            listeners.remove(listeners.lastIndexOf(listener));
        }
    }

    public <T> void removeAllListeners(K key, Class<T> type) {
        mListeners.get(key).put(type, new ArrayList<SimpleEventListener>());
    }

    public void removeAllListeners() {
        mListeners = new EnumMap<>(mEnumType);
    }

    public <T> int listenersCount(K key, Class<T> type) {
        Map<Class, List<SimpleEventListener>> listeners = mListeners.get(key);
        if (listeners == null) {
            return 0;
        } else {
            return listeners.get(type).size();
        }
    }

    public <T> void emit(K key, Class<T> type, T value) {
        if (!mListeners.containsKey(key)) {
            init(key, type);
        }
        for (SimpleEventListener<T> listener : mListeners.get(key).get(type)) {
            listener.run(value);
        }
    }

    private <T> void init(K key, Class<T> type) {
        Map<Class, List<SimpleEventListener>> map = new HashMap<>();
        map.put(type, new ArrayList<SimpleEventListener>());
        mListeners.put(key, map);
    }
}
