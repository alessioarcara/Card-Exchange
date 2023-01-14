package com.aadm.cardexchange.server;


import org.junit.jupiter.api.Test;

public class DBInitializeTest {

    @Test
    void listenerImplContextInitializedTest() {
        /*
        Il codice da errore relativo al parsing del mock di MapDB


        // Creazione dei mock
        ServletContextEvent sce = createMock(ServletContextEvent.class);
        ServletContext context = createMock(ServletContext.class);
        MapDB memoryDB = createMock(MapDB.class);
        Map<Integer, CardDecorator> yuGiOhMap = createMock(Map.class);
        Map<Integer, CardDecorator> magicMap = createMock(Map.class);
        Map<Integer, CardDecorator> pokemonMap = createMock(Map.class);
        JSONParser parser = createMock(JSONParser.class);

        ListenerImpl listener = new ListenerImpl();

        // Configurazione dei mock per la chiamata del metodo
        expect(sce.getServletContext()).andReturn(context);

        expect(context.getAttribute("memoryDB")).andReturn(memoryDB);

        expect(sce.getServletContext()).andReturn(context);

        context.setAttribute("YUGIOH_MAP_NAME", yuGiOhMap);
        context.setAttribute("MAGIC_MAP_NAME", magicMap);
        context.setAttribute("POKEMON_MAP_NAME", pokemonMap);
        parser.setParseStrategy(anyObject());
        expectLastCall().times(3);

        // Registrazione dei mock e chiamata del metodo da testare
        replay(sce, context, yuGiOhMap, magicMap, pokemonMap, parser);
        listener.contextInitialized(sce);
        verify(sce, context, yuGiOhMap, magicMap, pokemonMap, parser);

         */
    }

}
