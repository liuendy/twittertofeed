package uk.co.landbit.twittertofeed.feed.repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface RedisRepository<K, V> {

//    void put(T obj);
//
//    T get(T key);
//
//    void delete(T key);
//
//    List<T> findAll();

    void put(K key, V value);

    void multiPut(Map<K, V> keyValues);

    V get(K key);

    List<V> multiGet(Collection<K> keys);

    void delete(K key);

}