package com.aadm.cardexchange.server.mapdb;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

import javax.servlet.ServletContext;
import java.util.Map;


/*
    TODO: Refactor MapDBImpl
 */
public class MapDBImpl implements MapDB, MapDBConstants {

    private static DB getDB(ServletContext ctx, String dbType) {
        synchronized (ctx) {
            DB db = (DB) ctx.getAttribute(dbType + "_CTX_ATTRIBUTE");
            if (db == null) {
                if (dbType.equals(DB_MEMORY_TYPE)) {
                    db = DBMaker.memoryDB().closeOnJvmShutdown().make();
                } else if (dbType.equals(DB_FILE_TYPE)) {
                    db = DBMaker.fileDB(DB_FILENAME).closeOnJvmShutdown().make();
                }
                ctx.setAttribute(dbType + "_CTX_ATTRIBUTE", db);
            }
            return db;
        }
    }

    @Override
    public <K, V> Map<K, V> getCachedMap(ServletContext ctx, String mapName, Serializer<K> keySerializer,
                                         Serializer<V> valueSerializer) {
        return getDB(ctx, DB_MEMORY_TYPE).hashMap(mapName, keySerializer, valueSerializer).createOrOpen();
    }

    @Override
    public <K, V> Map<K, V> getPersistentMap(ServletContext ctx, String mapName, Serializer<K> keySerializer,
                                             Serializer<V> valueSerializer) {
        return getDB(ctx, DB_FILE_TYPE).hashMap(mapName, keySerializer, valueSerializer).createOrOpen();
    }
}
