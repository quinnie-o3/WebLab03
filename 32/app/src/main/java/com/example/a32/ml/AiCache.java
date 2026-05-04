package com.example.a32.ml;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AiCache {
    private final Map<String, List<String>> cache = new HashMap<>();

    public boolean contains(String key) {
        return cache.containsKey(key);
    }

    public List<String> get(String key) {
        return cache.get(key);
    }

    public void put(String key, List<String> labels) {
        cache.put(key, labels);
    }

    public void clear() {
        cache.clear();
    }
}
