package com.aadm.cardexchange.server.mapdb;

import org.mapdb.Serializer;

import javax.servlet.ServletContext;
import java.util.Map;

public interface MapDB {
    <K, V> Map<K, V> getCachedMap(ServletContext ctx, String mapName, Serializer<K> keySerializer,
                                  Serializer<V> valueSerializer);

    <K, V> Map<K, V> getPersistentMap(ServletContext ctx, String mapName, Serializer<K> keySerializer,
                                      Serializer<V> valueSerializer);

    void flush(ServletContext ctx);
}
