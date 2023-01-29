package com.aadm.cardexchange.client;

import com.aadm.cardexchange.client.AuthSubject.AuthSubject;
import com.aadm.cardexchange.client.presenters.DecksActivity;
import com.aadm.cardexchange.client.views.DecksView;
import com.aadm.cardexchange.shared.DeckServiceAsync;
import org.easymock.IMocksControl;
import org.junit.jupiter.api.BeforeEach;

import static org.easymock.EasyMock.createStrictControl;

public class DecksActivityTest {
    DecksActivity decksActivity;
    DeckServiceAsync mockRpcService;

    @BeforeEach
    public void initialize() {
        IMocksControl ctrl = createStrictControl();
        DecksView decksView = ctrl.createMock(DecksView.class);
        mockRpcService = ctrl.mock(DeckServiceAsync.class);
        AuthSubject authSubject = new AuthSubject(null);
        decksActivity = new DecksActivity(decksView, mockRpcService, authSubject);
    }
}
