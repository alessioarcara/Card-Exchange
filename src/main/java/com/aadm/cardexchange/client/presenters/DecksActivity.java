package com.aadm.cardexchange.client.presenters;

import com.aadm.cardexchange.client.AuthSubject.AuthSubject;
import com.aadm.cardexchange.client.views.DecksView;
import com.aadm.cardexchange.shared.models.*;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import java.util.Arrays;

public class DecksActivity extends AbstractActivity implements DecksView.Presenter {
    private final DecksView view;
    private final AuthSubject authSubject;

    public DecksActivity(DecksView view, AuthSubject authSubject) {
        this.view = view;
        this.authSubject = authSubject;
    }

    @Override
    public void start(AcceptsOneWidget acceptsOneWidget, EventBus eventBus) {
        view.setPresenter(this);
        acceptsOneWidget.setWidget(view.asWidget());
        fetchUserDecks();
    }

    @Override
    public void onStop() {
        view.resetData();
    }

    private void fetchUserDecks() {
        Deck ownedDeck = new Deck(authSubject.getToken(), "Owned", true);
        Deck customDeck = new Deck(authSubject.getToken(), "Custom", false);
        CardDecorator card = new CardDecorator(
                new CardImpl("Guts", "Membro della squadra dei falchi", "Berserk"));
        PhysicalCard pCard = new PhysicalCard(89, Status.Excellent, "Come nuova");
        PhysicalCard pCard2 = new PhysicalCard(89, Status.Damaged, "È un po' rovinata in un bordo");

        ownedDeck.addPhysicalCard(pCard.getId());
        ownedDeck.addPhysicalCard(pCard2.getId());

        view.setData(Arrays.asList(ownedDeck, customDeck));
    }
}
