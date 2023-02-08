package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.shared.models.PhysicalCard;
import com.aadm.cardexchange.shared.models.ProposalPayload;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface ExchangeServiceAsync {
    void addProposal(String token, String receiverUserEmail, List<PhysicalCard> senderPhysicalCards, List<PhysicalCard> receiverPhysicalCards, AsyncCallback<Boolean> callback);

    void getProposalCards(String token, int proposalId, AsyncCallback<ProposalPayload> callback);
}
