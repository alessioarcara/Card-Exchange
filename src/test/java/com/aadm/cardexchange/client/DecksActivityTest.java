package com.aadm.cardexchange.client;

import com.aadm.cardexchange.client.auth.AuthSubject;
import com.aadm.cardexchange.client.presenters.DecksActivity;
import com.aadm.cardexchange.client.utils.BaseAsyncCallback;
import com.aadm.cardexchange.client.views.DecksView;
import com.aadm.cardexchange.shared.DeckServiceAsync;
import com.aadm.cardexchange.shared.exceptions.AuthException;
import com.aadm.cardexchange.shared.exceptions.InputException;
import com.aadm.cardexchange.shared.models.Game;
import com.aadm.cardexchange.shared.models.PhysicalCard;
import com.aadm.cardexchange.shared.models.PhysicalCardWithName;
import com.aadm.cardexchange.shared.models.Status;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.easymock.IMocksControl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.easymock.EasyMock.*;

public class DecksActivityTest {

    IMocksControl ctrl;
    DecksView mockDecksView;
    DecksActivity decksActivity;
    DeckServiceAsync mockRpcService;

    @BeforeEach
    public void initialize() {
        ctrl = createStrictControl();
        mockDecksView = ctrl.createMock(DecksView.class);
        mockRpcService = ctrl.mock(DeckServiceAsync.class);
        AuthSubject authSubject = new AuthSubject();
        decksActivity = new DecksActivity(mockDecksView, mockRpcService, authSubject);
    }

    @Test
    public void testFetchUserDeckForSuccess() {
        mockRpcService.getMyDeck(anyString(), anyString(), isA(BaseAsyncCallback.class));
        List<PhysicalCardWithName> pcards = new ArrayList<>() {{
            add(new PhysicalCardWithName(
                    new PhysicalCard(Game.MAGIC, 111, Status.Good, "This is a valid description"),
                    "Test Card"));
            add(new PhysicalCardWithName(
                    new PhysicalCard(Game.MAGIC, 111, Status.Good, "This is a valid description"),
                    "Test Card"));
        }};

        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<List<PhysicalCardWithName>> callback = (AsyncCallback<List<PhysicalCardWithName>>) args[args.length - 1];
            callback.onSuccess(pcards);
            return null;
        });
        ctrl.replay();
        decksActivity.fetchUserDeck("Owned", Assertions::assertNotNull);
        ctrl.verify();
    }

    private static Stream<Arguments> provideDifferentTypeOfErrors() {
        return Stream.of(
                Arguments.of(new AuthException("Invalid token")),
                Arguments.of(new InputException("Invalid description")),
                Arguments.of(new RuntimeException())
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void testRemovePhysicalCardFromDeckForInvalidDeckName(String input) {
        mockDecksView.displayAlert(anyString());
        ctrl.replay();
        decksActivity.removePhysicalCardFromDeck(input, new PhysicalCard(Game.MAGIC, 111, Status.Good, "This is a valid description"), (Boolean bool) -> {
        });
        ctrl.verify();
    }

    @Test
    public void testRemovePhysicalCardFromDeckForNullPhysicalCard() {
        mockDecksView.displayAlert(anyString());
        ctrl.replay();
        decksActivity.removePhysicalCardFromDeck("Owned", null, (Boolean bool) -> {
        });
        ctrl.verify();
    }

    @ParameterizedTest
    @MethodSource("provideDifferentTypeOfErrors")
    public void testRemovePhysicalCardFromDeckForFailure(Exception e) {
        mockRpcService.removePhysicalCardFromDeck(anyString(), anyString(), isA(PhysicalCard.class), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<Boolean> callback = (AsyncCallback<Boolean>) args[args.length - 1];
            callback.onFailure(e);
            return null;
        });
        mockDecksView.displayAlert(anyString());

        ctrl.replay();
        decksActivity.removePhysicalCardFromDeck("Owned", new PhysicalCard(Game.MAGIC, 111, Status.Good, "This is a valid description"),
                (Boolean bool) -> {
                });
        ctrl.verify();
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void testRemovePhysicalCardFromDeckForSuccess(Boolean input) {
        mockRpcService.removePhysicalCardFromDeck(anyString(), anyString(), isA(PhysicalCard.class), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<Boolean> callback = (AsyncCallback<Boolean>) args[args.length - 1];
            callback.onSuccess(input);
            return null;
        });

        ctrl.replay();
        decksActivity.removePhysicalCardFromDeck("Owned", new PhysicalCard(Game.MAGIC, 111, Status.Good, "This is a valid description"), Assertions::assertNotNull);
        ctrl.verify();
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void testCreateCustomDeckForInvalidDeckName(String input) {
        mockDecksView.displayAlert(anyString());
        ctrl.replay();
        decksActivity.createCustomDeck(input);
        ctrl.verify();
    }

    @ParameterizedTest
    @MethodSource("provideDifferentTypeOfErrors")
    public void testCreateCustomDeckForValidDeckNameForFailure(Exception e) {
        mockRpcService.addDeck(anyString(), anyString(), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<Boolean> callback = (AsyncCallback<Boolean>) args[args.length - 1];
            callback.onFailure(e);
            return null;
        });
        mockDecksView.displayAlert(anyString());
        ctrl.replay();
        decksActivity.createCustomDeck("custom_deck");
        ctrl.verify();
    }

    @Test
    public void testCreateCustomDeckForValidDeckNameForSuccessAndFalseReturn() {
        mockRpcService.addDeck(anyString(), anyString(), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<Boolean> callback = (AsyncCallback<Boolean>) args[args.length - 1];
            callback.onSuccess(false);
            return null;
        });
        mockDecksView.displayAlert("deck already exists");
        ctrl.replay();
        decksActivity.createCustomDeck("custom_deck");
        ctrl.verify();
    }

    @Test
    public void testCreateCustomDeckForValidDeckNameForSuccessAndTrueReturn() {
        String deckName = "custom_deck";
        mockRpcService.addDeck(anyString(), anyString(), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<Boolean> callback = (AsyncCallback<Boolean>) args[args.length - 1];
            callback.onSuccess(true);
            return null;
        });
        mockDecksView.displayAddedCustomDeck(deckName);
        ctrl.replay();
        decksActivity.createCustomDeck(deckName);
        ctrl.verify();
    }

    @ParameterizedTest
    @NullSource
    public void testDeleteCustomDeckForInvalidDeckName(String input) {
        mockDecksView.displayAlert(anyString());
        ctrl.replay();
        decksActivity.deleteCustomDeck(input, (Boolean bool) -> {
        });
        ctrl.verify();
    }

    @ParameterizedTest
    @MethodSource("provideDifferentTypeOfErrors")
    public void testDeleteCustomDeckForValidDeckNameForFailure(Exception e) {
        mockRpcService.removeCustomDeck(anyString(), anyString(), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<Boolean> callback = (AsyncCallback<Boolean>) args[args.length - 1];
            callback.onFailure(e);
            return null;
        });
        mockDecksView.displayAlert(anyString());

        ctrl.replay();
        decksActivity.deleteCustomDeck("Test", (Boolean bool) -> {
        });
        ctrl.verify();
    }

    @Test
    public void testDeleteCustomDeckForValidDeckForSuccessAndFalseReturn() {
        mockRpcService.removeCustomDeck(anyString(), anyString(), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<Boolean> callback = (AsyncCallback<Boolean>) args[args.length - 1];
            callback.onSuccess(false);
            return null;
        });

        ctrl.replay();
        decksActivity.deleteCustomDeck("Test", Assertions::assertFalse);
        ctrl.verify();
    }

    @Test
    public void testDeleteCustomDeckForValidDeckForSuccessAndTrueReturn() {
        mockRpcService.removeCustomDeck(anyString(), anyString(), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<Boolean> callback = (AsyncCallback<Boolean>) args[args.length - 1];
            callback.onSuccess(true);
            return null;
        });

        ctrl.replay();
        decksActivity.deleteCustomDeck("Test", Assertions::assertTrue);
        ctrl.verify();
    }
}