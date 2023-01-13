package com.aadm.cardexchange.server;

import org.mapdb.Serializer;

import javax.servlet.ServletContext;
import java.util.Map;

public interface MapDB {
    <K, V> Map<K, V> getCachedMap(ServletContext ctx, String MAP_NAME, Serializer<K> keySerializer,
                                  Serializer<V> valueSerializer);
}
