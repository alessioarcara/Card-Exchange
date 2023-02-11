package com.aadm.cardexchange.server.mapdb;

import org.mapdb.Serializer;

import javax.servlet.ServletContext;
import java.util.Map;
import java.util.function.Consumer;

public interface MapDB {
    <K, V> Map<K, V> getCachedMap(ServletContext ctx, String mapName, Serializer<K> keySerializer,
                                  Serializer<V> valueSerializer);

    <K, V> Map<K, V> getPersistentMap(ServletContext ctx, String mapName, Serializer<K> keySerializer,
                                      Serializer<V> valueSerializer);

    <K, V> boolean writeOperation(ServletContext ctx, String mapName, Serializer<K> keySerializer,
                                  Serializer<V> valueSerializer, Consumer<Map<K, V>> operation);
}
