package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.shared.models.PhysicalCard;
import com.aadm.cardexchange.shared.models.Proposal;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface ExchangeServiceAsync {
    void addProposal(String token, String receiverUserEmail, List<PhysicalCard> senderPhysicalCards, List<PhysicalCard> receiverPhysicalCards, AsyncCallback<Boolean> callback);

    void GetProposalListReceived(String token, AsyncCallback<List<Proposal>> async);

    void GetProposalListSend(String token, AsyncCallback<List<Proposal>> async);
}
