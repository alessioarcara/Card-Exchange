package com.aadm.cardexchange.client.presenters;

import com.aadm.cardexchange.client.auth.AuthSubject;
import com.aadm.cardexchange.client.utils.BaseAsyncCallback;
import com.aadm.cardexchange.client.views.DecksView;
import com.aadm.cardexchange.shared.DeckServiceAsync;
import com.aadm.cardexchange.shared.exceptions.AuthException;
import com.aadm.cardexchange.shared.exceptions.InputException;
import com.aadm.cardexchange.shared.models.PhysicalCard;
import com.aadm.cardexchange.shared.models.PhysicalCardWithName;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class DecksActivity extends AbstractActivity implements DecksView.Presenter {
    private final DecksView view;
    private final DeckServiceAsync rpcService;
    private final AuthSubject authSubject;

    public DecksActivity(DecksView view, DeckServiceAsync rpcService, AuthSubject authSubject) {
        this.view = view;
        this.rpcService = rpcService;
        this.authSubject = authSubject;
    }

    @Override
    public void start(AcceptsOneWidget acceptsOneWidget, EventBus eventBus) {
        view.setPresenter(this);
        acceptsOneWidget.setWidget(view.asWidget());
        fetchUserDeckNames();
    }

    @Override
    public void onStop() {
        view.resetData();
    }

    private void fetchUserDeckNames() {
        rpcService.getUserDeckNames(authSubject.getToken(), new BaseAsyncCallback<List<String>>() {
            @Override
            public void onSuccess(List<String> result) {
                view.setData(result);
            }
        });
    }

    @Override
    public void fetchUserDeck(String deckName, BiConsumer<List<PhysicalCardWithName>, String> setDeckData) {
        rpcService.getMyDeck(authSubject.getToken(), deckName, new BaseAsyncCallback<List<PhysicalCardWithName>>() {
            @Override
            public void onSuccess(List<PhysicalCardWithName> result) {
                setDeckData.accept(result, null);
            }
        });
    }

    private boolean checkDeckNameInvalidity(String deckName) {
        return deckName == null || deckName.isEmpty();
    }

    @Override
    public void removePhysicalCardFromDeck(String deckName, PhysicalCard pCard, Consumer<Boolean> isRemoved) {
        if (checkDeckNameInvalidity(deckName)) {
            view.displayAlert("Invalid deck name");
            return;
        }
        if (pCard == null) {
            view.displayAlert("Invalid physical card");
            return;
        }
        rpcService.removePhysicalCardFromDeck(authSubject.getToken(), deckName, pCard, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof AuthException) {
                    view.displayAlert(((AuthException) caught).getErrorMessage());
                } else if (caught instanceof InputException) {
                    view.displayAlert(((InputException) caught).getErrorMessage());
                } else {
                    view.displayAlert("Internal server error: " + caught.getMessage());
                }
            }

            @Override
            public void onSuccess(Boolean result) {
                isRemoved.accept(result);
            }
        });
    }

    @Override
    public void createCustomDeck(String deckName) {
        if (checkDeckNameInvalidity(deckName)) {
            view.displayAlert("Invalid deck name");
            return;
        }

        rpcService.addDeck(authSubject.getToken(), deckName, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof AuthException) {
                    view.displayAlert(((AuthException) caught).getErrorMessage());
                } else {
                    view.displayAlert("Internal server error: " + caught.getMessage());
                }
            }

            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    view.displayAddedCustomDeck(deckName);
                } else {
                    view.displayAlert("deck already exists");
                }
            }
        });
    }

    @Override
    public void deleteCustomDeck(String deckName, Consumer<Boolean> isRemoved) {
        if (checkDeckNameInvalidity(deckName)) {
            view.displayAlert("Invalid deck name");
            return;
        }

        rpcService.removeCustomDeck(authSubject.getToken(), deckName, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof AuthException) {
                    view.displayAlert(((AuthException) caught).getErrorMessage());
                } else {
                    view.displayAlert("Internal server error: " + caught.getMessage());
                }
            }

            @Override
            public void onSuccess(Boolean result) {
                isRemoved.accept(result);
            }
        });
    }
}
