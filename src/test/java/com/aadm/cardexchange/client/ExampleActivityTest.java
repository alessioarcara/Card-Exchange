package com.aadm.cardexchange.client;

import com.aadm.cardexchange.client.presenters.CardsActivity;
import com.aadm.cardexchange.client.views.CardsView;
import com.aadm.cardexchange.shared.Card;
import com.aadm.cardexchange.shared.CardServiceAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;
import junit.framework.TestCase;
import org.easymock.IAnswer;
import org.junit.Test;

import static org.easymock.EasyMock.*;

public class ExampleActivityTest extends TestCase {
    private CardsActivity cardsActivity;
    private CardsView mockView;
    private CardServiceAsync mockRpcService;

    @Override
    protected void setUp() {
        mockView = createStrictMock(CardsView.class);
        mockRpcService = createStrictMock(CardServiceAsync.class);
        cardsActivity = new CardsActivity(mockView, mockRpcService);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testSuccessFetchCards() {
        Card[] expectedCards = {new Card("name1", "desc1"), new Card("name2", "desc2")};
        mockRpcService.getCards(isA(AsyncCallback.class));

        expectLastCall().andAnswer(new IAnswer<Object>() {
            @Override
            public Object answer() throws Throwable {
                /*
                 * 1) prendo la callback
                 * 2) invoco il metodo onSuccess della callback con un finto risultato
                 */
                AsyncCallback<Card[]> callback = (AsyncCallback<Card[]>) getCurrentArguments()[0];
                callback.onSuccess(expectedCards);
                return null;
            }
        });
        mockView.setData(expectedCards);
        expectLastCall();

        replay(mockRpcService, mockView);

        cardsActivity.fetchCards();

        verify(mockRpcService, mockView);
    }
}
