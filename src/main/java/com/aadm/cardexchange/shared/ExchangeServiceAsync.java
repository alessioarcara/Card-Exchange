package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.shared.models.PhysicalCard;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface ExchangeServiceAsync {
    void addProposal(String token, String receiverUserEmail, List<PhysicalCard> senderPhysicalCards, List<PhysicalCard> receiverPhysicalCards, AsyncCallback<Boolean> async);
}
