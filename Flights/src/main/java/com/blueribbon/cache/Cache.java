package com.blueribbon.cache;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * A cache with time to live.
 * @param <K> The Key type.
 * @param <V> The value type.
 */
public class Cache<K,V> {
    private Map<K, ValueWithTimeToLive<V>> map;
    private Function<K, ValueWithTimeToLive<V>> function;

    public Cache(long expirationMillis, Function<K, V> function) {
        this.map = new ConcurrentHashMap<>();
        this.function = k->new ValueWithTimeToLive<>(function.apply(k), System.currentTimeMillis() + expirationMillis);
    }

    public V get(K k) {
        return map.compute(k, (kk, vv)->(vv==null || vv.isExpired()) ? function.apply(k) : vv).v;
    }

    private static class ValueWithTimeToLive<V> {
        private V v;
        private long expirationTime;

        public ValueWithTimeToLive(V v, long expirationTime) {
            this.v = v;
            this.expirationTime = expirationTime;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expirationTime;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ValueWithTimeToLive<?> that = (ValueWithTimeToLive<?>) o;
            return v.equals(that.v);
        }

        @Override
        public int hashCode() {
            return Objects.hash(v);
        }
    }
}
