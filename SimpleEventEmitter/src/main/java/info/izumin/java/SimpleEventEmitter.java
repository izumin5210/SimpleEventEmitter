package info.izumin.java;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class SimpleEventEmitter<K extends Enum<K>, V> {
    private EnumMap<K, List<SimpleEventListener<V>>> mListeners;

    public SimpleEventEmitter(Class<K> enumType) {
        mListeners = new EnumMap<>(enumType);
    }

    public void on(K key, SimpleEventListener<V> listener) {
        if (!mListeners.containsKey(key)) {
            mListeners.put(key, new ArrayList<SimpleEventListener<V>>());
        }
        mListeners.get(key).add(listener);
    }

    public void emit(K key, V value) {
        for (SimpleEventListener<V> listener : mListeners.get(key)) {
            listener.run(value);
        }
    }
}
