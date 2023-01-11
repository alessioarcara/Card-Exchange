package com.aadm.cardexchange.server;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

import javax.servlet.ServletContext;
import java.util.Map;

public class MapDBImpl implements MapDB, MapDBConstants {

    private DB getMemoryDB(ServletContext ctx) {
        synchronized (ctx) {
            DB dbMemory = (DB) ctx.getAttribute(MEMORY_DB_CTX_ATTRIBUTE);
            if (dbMemory == null) {
                dbMemory = DBMaker.memoryDB().closeOnJvmShutdown().make();
                ctx.setAttribute(MEMORY_DB_CTX_ATTRIBUTE, dbMemory);
            }
            return dbMemory;
        }
    }

    public <K, V> Map<K, V> getCachedMap(ServletContext ctx, String MAP_NAME, Serializer<K> keySerializer,
                                         Serializer<V> valueSerializer) {
        DB db = getMemoryDB(ctx);
        return db
                .hashMap(MAP_NAME, keySerializer, valueSerializer)
                .createOrOpen();
    }
}
