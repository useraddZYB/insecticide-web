package com.programmerartist.insecticide.web.controller.tunnel.domain;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by 程序员Artist on 16/3/25.
 */
public class LRULinkedHashMap<K,V> extends LinkedHashMap<K,V> {
    private int capacity;

    /**
     *
     * @param capacity
     */
    public LRULinkedHashMap(int capacity){
        super(16, 0.75f, true);
        this.capacity=capacity;
    }

    /**
     * 实现LRU的关键方法，如果map里面的元素个数大于了缓存最大容量，则删除链表的顶端元素
     *
     * @param
     * @return
     */
    @Override
    public boolean removeEldestEntry(Map.Entry<K, V> eldest){
        return size() > capacity;
    }


    /*@Override
    public V put(K key, V value) {
        if(!super.containsKey(key) && super.size() >= capacity ) {
            int willRemove = (super.size() - capacity) + 1;
            List<K> keys = new ArrayList<>(super.keySet());
            for(int i=0; i<willRemove; i++) super.remove(keys.get(i));
        }

        return super.put(key, value);
    }*/

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
