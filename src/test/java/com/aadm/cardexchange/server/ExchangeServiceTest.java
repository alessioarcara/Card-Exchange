package com.aadm.cardexchange.server;

import com.aadm.cardexchange.server.mapdb.MapDB;
import com.aadm.cardexchange.server.services.ExchangeServiceImpl;
import com.aadm.cardexchange.shared.exceptions.AuthException;
import com.aadm.cardexchange.shared.exceptions.BaseException;
import com.aadm.cardexchange.shared.exceptions.InputException;
import com.aadm.cardexchange.shared.models.*;
import org.easymock.IMocksControl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mapdb.Serializer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.*;

import static com.aadm.cardexchange.server.MockCardData2.createMagicDummyMap2;
import static org.easymock.EasyMock.*;

public class ExchangeServiceTest {
    private IMocksControl ctrl;
    private MapDB mockDB;
    ServletConfig mockConfig;
    ServletContext mockCtx;
    private ExchangeServiceImpl exchangeService;

    @BeforeEach
    public void initialize() throws ServletException {
        ctrl = createStrictControl();
        mockDB = ctrl.createMock(MapDB.class);
        exchangeService = new ExchangeServiceImpl(mockDB);
        mockConfig = ctrl.createMock(ServletConfig.class);
        mockCtx = ctrl.createMock(ServletContext.class);
        exchangeService.init(mockConfig);
    }

    //#### SPECIFIC SETUP FOR TESTS #####
    private void setupForValidToken() {
        Map<String, LoginInfo> mockLoginMap = new HashMap<>() {{
            put("validToken", new LoginInfo("test@test.it", System.currentTimeMillis() - 10000));
        }};
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(mockLoginMap);
    }

    private void setupForValidEmail() {
        Map<String, User> userMap = new HashMap<>();
        userMap.put("valid@receiverUserEmail.it", new User("valid@receiverUserEmail.it", "password"));
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(userMap);
    }

    private PhysicalCard generateValidPCard(int i) {
        return new PhysicalCard(Game.randomGame(), (i+3000), Status.randomStatus(), "This is a valid description.");
    }


    private  List<PhysicalCard> generateValidListPcard(int n) {
        List<PhysicalCard> myList = new ArrayList<>();
        for (int i = 0; i<n; i++ ) {
            //PhysicalCardImpl mockPCard = new PhysicalCardImpl(Game.randomGame(), (i+3000), Status.randomGame(), "This is a valid description.");
            PhysicalCard mockPCard = generateValidPCard(i);
            myList.add(mockPCard);
        }
        return myList;
    }


    private  Map<Integer, MagicCard>  generateValidMagicCardMap(){
        return createMagicDummyMap2();
    }

    private Map<String, Deck>  generateValidDeckofMagicPhysicalCardMap(Map<Integer, MagicCard> magicCardMap, int i, String deckName){
        Map<String, Deck> deckMap = new HashMap<>() {{
            put(deckName, new Deck(deckName, true));
        }};
        int count = 0;
        for (Map.Entry<Integer, MagicCard> entry : magicCardMap.entrySet()) {
            if(count<i) {
                deckMap.get(deckName).addPhysicalCard(new PhysicalCard(Game.MAGIC, entry.getKey(), Status.randomStatus(), "This is a super valid description"));
            }
            count++;
            }
        return deckMap;
    }


    private Map<String, Deck> generateValidDeck(int nCard, String mail, String deckName) {
       Map<String, Deck> deckMap = new HashMap<>() {{
           put(deckName, new Deck(deckName, true));
       }};
        for (int i=0; i<nCard; i++) {
            PhysicalCard mockPCard = generateValidPCard(i);
            deckMap.get(deckName).addPhysicalCard(mockPCard);
        }
        return deckMap;
    }
    private Map<String, Map<String, Deck>> generateValidUserDecksMap(String mail,Map<String, Deck> singleDeck) {
        Map<String, Map<String, Deck>> mockDeckMap = new HashMap<String, Map<String, Deck>>() {
            {
                put(mail, singleDeck);
            }};
        return mockDeckMap;
    }

    private Map<String, Map<String, Deck>> generateCardFromId(String mail,Map<String, Deck> singleDeck) {
        Map<String, Map<String, Deck>> mockDeckMap = new HashMap<String, Map<String, Deck>>() {
            {
                put(mail, singleDeck);
            }};
        return mockDeckMap;
    }

    //#### UNIT TESTS #####

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"invalidToken"})
    public void testAddProposalForInvalidToken(String input) {
        Map<String, LoginInfo> loginInfoMap = new HashMap<>() {{
            put("validToken1", new LoginInfo("test@test.it", System.currentTimeMillis() - 10000));
            put("validToken2", new LoginInfo("test2@test.it", System.currentTimeMillis() - 20000));
            put("validToken3", new LoginInfo("test3@test.it", System.currentTimeMillis() - 30000));
        }};
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(loginInfoMap);
        ctrl.replay();
        Assertions.assertThrows(AuthException.class, () -> exchangeService.addProposal(input, "valid@receiverUserEmail.it", generateValidListPcard(2), generateValidListPcard(2)));
        ctrl.verify();
    }
    @ParameterizedTest
    @NullAndEmptySource
    public void testAddProposalForInvalidReceiverUserEmail(String input) {
        setupForValidToken();
        ctrl.replay();
        Assertions.assertThrows(InputException.class, () -> {
            exchangeService.addProposal("validToken", input, generateValidListPcard(2), generateValidListPcard(2));
        });
        ctrl.verify();
    }
    @Test
    public void testAddProposalForNotExistingReceiverUserEmail() {
        setupForValidToken();
        setupForValidEmail();
        ctrl.replay();
        Assertions.assertThrows(InputException.class, () -> {
            exchangeService.addProposal("validToken", "NotExisting@Mail.it", generateValidListPcard(2), generateValidListPcard(2));
        });
        ctrl.verify();
    }
    @Test
    public void testAddProposalForEmptySenderPCards() throws AuthException {
        setupForValidToken();
        setupForValidEmail();
        ctrl.replay();
        Assertions.assertThrows(InputException.class, () -> {
                exchangeService.addProposal("validToken", "valid@receiverUserEmail.it", generateValidListPcard(0), generateValidListPcard(2));
            });
        ctrl.verify();
    }
    @Test
    public void testAddProposalForNullSenderPCards() throws AuthException {
        setupForValidToken();
        setupForValidEmail();
        ctrl.replay();
        Assertions.assertThrows(InputException.class, () -> {
            exchangeService.addProposal("validToken", "valid@receiverUserEmail.it", null, generateValidListPcard(2));
        });
        ctrl.verify();
    }

    @Test
    public void testAddProposalForEmptyReceiverPCards() throws AuthException {
        setupForValidToken();
        setupForValidEmail();
        ctrl.replay();
        Assertions.assertThrows(InputException.class, () -> {
            exchangeService.addProposal("validToken", "valid@receiverUserEmail.it", generateValidListPcard(2), generateValidListPcard(0));
        });
        ctrl.verify();
    }
    @Test
    public void testAddProposalForNullReceiverPCards() throws AuthException {
        setupForValidToken();
        setupForValidEmail();
        ctrl.replay();
        Assertions.assertThrows(InputException.class, () -> {
            exchangeService.addProposal("validToken", "valid@receiverUserEmail.it", generateValidListPcard(2), null);
        });
        ctrl.verify();
    }

    @Test
    public void testAddProposalSuccess() throws AuthException, InputException {
        setupForValidToken();
        setupForValidEmail();
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(new HashMap<>());
        ctrl.replay();
        Assertions.assertTrue(exchangeService.addProposal("validToken", "valid@receiverUserEmail.it", generateValidListPcard(1), generateValidListPcard(2)));
        ctrl.verify();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"invalidToken"})
    public void testCheckExistingPcardByCardForInvalidToken(String input) {
        Map<String, LoginInfo> loginInfoMap = new HashMap<>() {{
            put("validToken1", new LoginInfo("test@test.it", System.currentTimeMillis() - 10000));
            put("validToken2", new LoginInfo("test2@test.it", System.currentTimeMillis() - 20000));
            put("validToken3", new LoginInfo("test3@test.it", System.currentTimeMillis() - 30000));
        }};
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(loginInfoMap);
        ctrl.replay();
        Assertions.assertThrows(AuthException.class, () -> exchangeService.CheckExistingPcardByIdCard(input, Game.randomGame(), 1));
        ctrl.verify();
    }

    @ParameterizedTest
    @NullSource
    public void testCheckExistingPcardByCardForNullGame(Game input) {
        setupForValidToken();
        ctrl.replay();
        Assertions.assertThrows(InputException.class, () -> {
            exchangeService.CheckExistingPcardByIdCard("validToken", input, 1);
            ctrl.verify();
        });
    }

    @Test
    public void testCheckExistingPcardByCardForNegativeCardId() {
        setupForValidToken();
        ctrl.replay();
        Assertions.assertThrows(InputException.class, () -> {
            exchangeService.CheckExistingPcardByIdCard("validToken", Game.randomGame(), -1);
            ctrl.verify();
        });
    }
    @Test
    public void testCheckExistingPcardByCardForUserWithoutDecksMap() {
        setupForValidToken();
        Map<String, Map<String, Deck>> mockDeckMap = new HashMap<>();
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(mockDeckMap);
        ctrl.replay();
        Assertions.assertThrows(RuntimeException.class, () ->
                exchangeService.CheckExistingPcardByIdCard("validToken", Game.randomGame(), 3)
        );
        ctrl.verify();
    }
    @Test
    public void testCheckExistingPcardByCardForUserWithDeckNull() {
        setupForValidToken();
        Map<String, Map<String, Deck>> mockDeckMap = new HashMap<>() {{
            put("test@test.it", null);
        }};
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(mockDeckMap);
        ctrl.replay();
        Assertions.assertThrows(RuntimeException.class, () ->
                exchangeService.CheckExistingPcardByIdCard("validToken", Game.randomGame(), 3)
        );
        ctrl.verify();
    }
    @Test
    public void testCheckExistingPcardByCardForUserWithEmptyDeckMap() {
        setupForValidToken();
        Map<String, Deck> deckMap = new HashMap<>();
        Map<String, Map<String, Deck>> mockDeckMap = new HashMap<>() {{
            put("test@test.it", deckMap);
        }};
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(mockDeckMap);
        ctrl.replay();
        Assertions.assertThrows(BaseException.class, () ->
                exchangeService.CheckExistingPcardByIdCard("validToken", Game.randomGame(), 3)
        );
        ctrl.verify();
    }
    @Test
    public void testCheckExistingPcardByCardForUserWithoutCardsInOwnedDeck() throws BaseException {
        setupForValidToken();
        Map<String, Deck> deckMap = new HashMap<>() {{
            put("Owned", new Deck("Wished", true));
        }};
        Map<String, Map<String, Deck>> mockDeckMap = new HashMap<>() {{
            put("test@test.it", deckMap);
        }};
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(mockDeckMap);
        ctrl.replay();
        Assertions.assertFalse(exchangeService.CheckExistingPcardByIdCard("validToken", Game.randomGame(), 3));
        ctrl.verify();
    }

    @Test
    public void testCheckExistingPcardByCardWithoutOwnedDeck() throws InputException, AuthException {
        //serve per catturare l'eccezione BaseException, con mazzo "Owned" non trovato
        setupForValidToken();
        Map<String, Deck> deckMap =  generateValidDeckofMagicPhysicalCardMap(generateValidMagicCardMap(),5,"owd");
        Map<String, Map<String, Deck>> mockDeckMap = new HashMap<>() {{
            put("test@test.it", deckMap);
        }};
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(mockDeckMap);
        ctrl.replay();
        Assertions.assertThrows(BaseException.class, () ->
                exchangeService.CheckExistingPcardByIdCard("validToken", Game.randomGame(), 3)
        );
        ctrl.verify();
    }


    @Test
    public void testCheckExistingPcardByCardForUserWithoutPCardsInOwnedDeck() throws BaseException {
        setupForValidToken();
        Map<String, Deck> deckMap = new HashMap<>() {{
            put("Owned", new Deck("Owned", true));
        }};
        Map<String, Map<String, Deck>> mockDeckMap = new HashMap<>() {{
            put("test@test.it", deckMap);
        }};
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(mockDeckMap);
        ctrl.replay();
        Assertions.assertFalse(exchangeService.CheckExistingPcardByIdCard("validToken", Game.randomGame(), 3));
        ctrl.verify();
    }

    @Test
    public void testCheckExistingPcardByCardNotMatchingIdCard() throws BaseException {
        setupForValidToken();
        Map<String, Deck> deckMap =  generateValidDeckofMagicPhysicalCardMap(generateValidMagicCardMap(),5,"Owned");
        Map<String, Map<String, Deck>> mockDeckMap = new HashMap<>() {{
            put("test@test.it", deckMap);
        }};
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(mockDeckMap);
        ctrl.replay();
        Assertions.assertFalse(exchangeService.CheckExistingPcardByIdCard("validToken", Game.randomGame(), 55));
        ctrl.verify();
    }
    @Test
    public void testCheckExistingPcardByCardMatchingIdCard() throws BaseException {
        setupForValidToken();
        Map<String, Deck> deckMap =  generateValidDeckofMagicPhysicalCardMap(generateValidMagicCardMap(),5,"Owned");
        Map<String, Map<String, Deck>> mockDeckMap = new HashMap<>() {{
            put("test@test.it", deckMap);
        }};
        Set<PhysicalCard> PcardList = deckMap.get("Owned").getPhysicalCards();
        int randomRealCardIdToFound = PcardList.stream().skip(new Random().nextInt(PcardList.size())).findFirst().orElse(null).getCardId();
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(mockDeckMap);
        ctrl.replay();
        Assertions.assertTrue(exchangeService.CheckExistingPcardByIdCard("validToken", Game.randomGame(), randomRealCardIdToFound));
        ctrl.verify();
    }

}