package com.aadm.cardexchange.shared;


import com.aadm.cardexchange.shared.models.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;

public interface ExchangeServiceAsync {
    void addProposal(String token, String receiverUserEmail, List<PhysicalCard> senderPhysicalCards, List<PhysicalCard> receiverPhysicalCards, AsyncCallback<Boolean> async);

    void CheckExistingPcardByIdCard(String token, Game game, int cardId, AsyncCallback<Boolean> async);
}
