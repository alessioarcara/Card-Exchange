package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.shared.exceptions.AuthException;
import com.aadm.cardexchange.shared.exceptions.BaseException;
import com.aadm.cardexchange.shared.exceptions.InputException;
import com.aadm.cardexchange.shared.models.PhysicalCard;
import com.aadm.cardexchange.shared.models.ProposalPayload;
import com.aadm.cardexchange.shared.models.Proposal;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;

@RemoteServiceRelativePath("exchanges")
public interface ExchangeService extends RemoteService {

    boolean addProposal(String token, String receiverUserEmail, List<PhysicalCard> senderPhysicalCards, List<PhysicalCard> receiverPhysicalCards) throws BaseException;

    List<Proposal> getProposalListReceived(String token) throws BaseException;

    List<Proposal> getProposalListSend(String token) throws BaseException;

    ProposalPayload getProposalCards(String token, int proposalId) throws AuthException, InputException, RuntimeException;

}
