package info.izumin.java;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class SimpleEventEmitter<K extends Enum<K>, V> {
    private EnumMap<K, List<SimpleEventListener<V>>> mListeners;
    private Class<K> mEnumType;

    public SimpleEventEmitter(Class<K> enumType) {
        mEnumType = enumType;
        removeAllListeners();
    }

    public void on(K key, SimpleEventListener<V> listener) {
        if (!mListeners.containsKey(key)) {
            mListeners.put(key, new ArrayList<SimpleEventListener<V>>());
        }
        mListeners.get(key).add(listener);
    }

    public void addListener(K key, SimpleEventListener<V> listener) {
        on(key, listener);
    }

    public void removeListener(K key, SimpleEventListener<V> listener) {
        List<SimpleEventListener<V>> listeners = mListeners.get(key);
        if (listeners != null) {
            listeners.remove(listeners.lastIndexOf(listener));
        }
    }

    public void removeAllListeners(K key) {
        mListeners.put(key, new ArrayList<SimpleEventListener<V>>());
    }

    public void removeAllListeners() {
        mListeners = new EnumMap<>(mEnumType);
    }

    public int listenersCount(K key) {
        List<SimpleEventListener<V>> listeners = mListeners.get(key);
        if (listeners == null) {
            return 0;
        } else {
            return listeners.size();
        }
    }

    public void emit(K key, V value) {
        for (SimpleEventListener<V> listener : mListeners.get(key)) {
            listener.run(value);
        }
    }
}
