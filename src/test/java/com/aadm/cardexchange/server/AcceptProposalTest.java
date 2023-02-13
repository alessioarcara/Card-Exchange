package com.aadm.cardexchange.server;

import com.aadm.cardexchange.server.services.ExchangeServiceImpl;
import com.aadm.cardexchange.shared.exceptions.AuthException;
import com.aadm.cardexchange.shared.exceptions.ProposalNotFoundException;
import com.aadm.cardexchange.shared.models.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.easymock.EasyMock.*;

public class AcceptProposalTest {
    ServletConfig mockConfig;
    ServletContext mockCtx;
    PhysicalCard dummySenderPhysicalCard = new PhysicalCard(Game.randomGame(), 111, Status.randomStatus(), "This is a valid description");
    PhysicalCard dummyReceiverPhysicalCard = new PhysicalCard(Game.randomGame(), 222, Status.randomStatus(), "This is a valid description");
    Map<Integer, Proposal> dummyProposalMap = new HashMap<>() {{
        put(1, new Proposal("test2@test.it", "test@test.it",
                Collections.singletonList(dummySenderPhysicalCard), Collections.singletonList(dummyReceiverPhysicalCard)));
    }};

    @BeforeEach
    public void initialize() {
        mockConfig = createStrictMock(ServletConfig.class);
        mockCtx = createStrictMock(ServletContext.class);
    }

    private Deck createDummyOwnedDeck() {
        Deck deck = new Deck("Owned", true);
        for (PhysicalCard pCard : DummyData.createPhysicalCardDummyList(5))
            deck.addPhysicalCard(pCard);
        return deck;
    }

    @Test
    public void testAcceptProposalForValidParameters() throws ServletException, ProposalNotFoundException, AuthException {
        // init fakes, mocks
        FakeDB fakeDB = new FakeDB(dummyProposalMap,
                // deck map
                new HashMap<>() {{
                    Deck receiverOwnedDeck = createDummyOwnedDeck();
                    Deck senderOwnedDeck = createDummyOwnedDeck();
                    Deck receiverWishedDeck = new Deck("Wished", true);
                    Deck receiverCustomDeck = new Deck("Custom", false);
                    senderOwnedDeck.addPhysicalCard(dummySenderPhysicalCard);
                    receiverOwnedDeck.addPhysicalCard(dummyReceiverPhysicalCard);
                    receiverWishedDeck.addPhysicalCard(new PhysicalCard(dummySenderPhysicalCard.getGameType(), dummySenderPhysicalCard.getCardId(), Status.VeryDamaged, "Any card is fine"));
                    receiverCustomDeck.addPhysicalCard(dummyReceiverPhysicalCard);
                    put("test@test.it", new LinkedHashMap<>() {{
                        put("Owned", receiverOwnedDeck);
                        put("Wished", receiverWishedDeck);
                        put("Custom", receiverCustomDeck);
                    }});
                    put("test2@test.it", new LinkedHashMap<>() {{
                        put("Owned", senderOwnedDeck);
                        put("Wished", new Deck("Wished", true));
                    }});
                }});
        ExchangeServiceImpl exchangeService = new ExchangeServiceImpl(fakeDB);
        exchangeService.init(mockConfig);

        // expects
        expect(mockConfig.getServletContext()).andReturn(mockCtx).times(3);
        replay(mockConfig, mockCtx);
        Assertions.assertTrue(exchangeService.acceptProposal("validToken", 1));
        verify(mockConfig, mockCtx);

        Assertions.assertAll(() -> {
            Assertions.assertNull(fakeDB.getProposalMap().get(1));
            Assertions.assertTrue(fakeDB.getDeckMap().get("test@test.it").get("Owned").getPhysicalCards().stream().anyMatch(pCard -> pCard.equals(dummySenderPhysicalCard)));
            Assertions.assertTrue(fakeDB.getDeckMap().get("test2@test.it").get("Owned").getPhysicalCards().stream().anyMatch(pCard -> pCard.equals(dummyReceiverPhysicalCard)));
            Assertions.assertTrue(fakeDB.getDeckMap().get("test@test.it").get("Wished").getPhysicalCards().isEmpty());
            Assertions.assertTrue(fakeDB.getDeckMap().get("test@test.it").get("Custom").getPhysicalCards().isEmpty());
        });
    }

    @Test
    public void testAcceptProposalForNoLongerOwnedSenderPhysicalCard() throws ServletException {
        // init fakes, mocks
        FakeDB fakeDB = new FakeDB(dummyProposalMap, new HashMap<>() {{
            Deck senderDeck = createDummyOwnedDeck();
            Deck receiverDeck = createDummyOwnedDeck();
            receiverDeck.addPhysicalCard(dummyReceiverPhysicalCard);
            put("test@test.it", new LinkedHashMap<>() {{
                put("Owned", receiverDeck);
            }});
            put("test2@test.it", new LinkedHashMap<>() {{
                put("Owned", senderDeck);
            }});
        }});
        ExchangeServiceImpl exchangeService = new ExchangeServiceImpl(fakeDB);
        exchangeService.init(mockConfig);

        // expects
        expect(mockConfig.getServletContext()).andReturn(mockCtx).times(3);
        replay(mockConfig, mockCtx);
        Assertions.assertThrows(RuntimeException.class, () -> exchangeService.acceptProposal("validToken", 1));
        verify(mockConfig, mockCtx);
    }

    @Test
    public void testAcceptProposalForNoLongerOwnedReceiverPhysicalCard() throws ServletException {
        // init fakes, mocks
        FakeDB fakeDB = new FakeDB(dummyProposalMap, new HashMap<>() {{
            Deck senderDeck = createDummyOwnedDeck();
            Deck receiverDeck = createDummyOwnedDeck();
            senderDeck.addPhysicalCard(dummySenderPhysicalCard);
            put("test@test.it", new LinkedHashMap<>() {{
                put("Owned", receiverDeck);
            }});
            put("test2@test.it", new LinkedHashMap<>() {{
                put("Owned", senderDeck);
            }});
        }});
        ExchangeServiceImpl exchangeService = new ExchangeServiceImpl(fakeDB);
        exchangeService.init(mockConfig);

        // expects
        expect(mockConfig.getServletContext()).andReturn(mockCtx).times(3);
        replay(mockConfig, mockCtx);
        Assertions.assertThrows(RuntimeException.class, () -> exchangeService.acceptProposal("validToken", 1));
        verify(mockConfig, mockCtx);
    }
}
