package ua.kpi.mc.mctotg.utils;

import java.util.LinkedHashMap;
import java.util.Map;

public class MaxSizeHashMap<K, V> extends LinkedHashMap<K, V> {
    private final int MAXSIZE;

    public MaxSizeHashMap(int max_size) {
        this.MAXSIZE = max_size;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > MAXSIZE;
    }
}
