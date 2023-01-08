package com.aadm.cardexchange.server;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

import javax.servlet.ServletContext;
import java.io.File;
import java.util.Map;

public class MapDBImpl implements MapDB, MapDBConstants {
    /*
     * Salviamo il DB in un servlet context, ovvero un gruppo di Servlet,
     * pagine JSP o altre pagine web che condividono tra di loro risorse e dati.
     */
    /*
     * Inoltre, senza l'utilizzo del Singleton, ogni remote call a getCards
     * aprirebbe e chiuderebbe una nuova connessione al DB.
     */
    private DB getDB(ServletContext ctx) {
        synchronized (ctx) {
            DB db = (DB) ctx.getAttribute("DB");
            if (db == null) {
                db = DBMaker.fileDB(new File(DB_FILENAME)).closeOnJvmShutdown().make();
                ctx.setAttribute("DB", db);
            }
            return db;
        }
    }

    public <K, V> Map<K, V> getMap(ServletContext ctx, String HASHMAP_NAME, Serializer<K> keySerializer,
                                   Serializer<V> valueSerializer) {
        DB db = getDB(ctx);
        return db
                .hashMap(HASHMAP_NAME, keySerializer, valueSerializer)
                .createOrOpen();
    }
}
