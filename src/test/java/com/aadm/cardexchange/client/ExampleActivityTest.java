package com.aadm.cardexchange.client;

import com.aadm.cardexchange.client.presenters.CardsActivity;
import com.aadm.cardexchange.client.utils.BaseAsyncCallback;
import com.aadm.cardexchange.client.views.CardsView;
import com.aadm.cardexchange.shared.Card;
import com.aadm.cardexchange.shared.CardImpl;
import com.aadm.cardexchange.shared.CardServiceAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.easymock.EasyMock.*;

public class ExampleActivityTest {
    private CardsActivity cardsActivity;
    private CardsView mockView;
    private CardServiceAsync mockRpcService;

    @BeforeEach
    protected void initialize() {
        mockView = createStrictMock(CardsView.class);
        mockRpcService = createStrictMock(CardServiceAsync.class);
        cardsActivity = new CardsActivity(mockView, mockRpcService);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testSuccessFetchCards() {
        CardImpl[] expectedCards = {new CardImpl("name1", "desc1", "type1"), new CardImpl("name2", "desc2", "type2")};
        mockRpcService.getCards(isA(BaseAsyncCallback.class));

        expectLastCall().andAnswer(() -> {
            /*
             * 1) prendo la callback
             * 2) invoco il metodo onSuccess della callback con un finto risultato
             */
            AsyncCallback<Card[]> callback = (AsyncCallback<Card[]>) getCurrentArguments()[0];
            callback.onSuccess(expectedCards);
            return null;
        });
        mockView.setData(expectedCards);
        expectLastCall();

        replay(mockRpcService, mockView);

        cardsActivity.fetchCards();

        verify(mockRpcService, mockView);
    }
}
