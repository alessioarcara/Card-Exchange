package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.shared.models.PhysicalCard;
import com.aadm.cardexchange.shared.models.Proposal;
import com.aadm.cardexchange.shared.payloads.ProposalPayload;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface ExchangeServiceAsync {
    void addProposal(String token, String receiverUserEmail, List<PhysicalCard> senderPhysicalCards, List<PhysicalCard> receiverPhysicalCards, AsyncCallback<Boolean> callback);

    void getProposal(String token, int proposalId, AsyncCallback<ProposalPayload> callback);

    void getProposalListReceived(String token, AsyncCallback<List<Proposal>> callback);

    void getProposalListSent(String token, AsyncCallback<List<Proposal>> callback);
}
