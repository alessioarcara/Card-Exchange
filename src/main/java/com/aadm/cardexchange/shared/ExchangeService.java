package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.shared.exceptions.AuthException;
import com.aadm.cardexchange.shared.exceptions.BaseException;
import com.aadm.cardexchange.shared.exceptions.ProposalNotFoundException;
import com.aadm.cardexchange.shared.models.PhysicalCard;
import com.aadm.cardexchange.shared.models.Proposal;
import com.aadm.cardexchange.shared.payloads.ProposalPayload;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;

@RemoteServiceRelativePath("exchanges")
public interface ExchangeService extends RemoteService {

    boolean addProposal(String token, String receiverUserEmail, List<PhysicalCard> senderPhysicalCards, List<PhysicalCard> receiverPhysicalCards) throws BaseException;

    ProposalPayload getProposal(String token, int proposalId) throws BaseException;

    boolean acceptProposal(String token, int proposalId) throws AuthException, ProposalNotFoundException;

    List<Proposal> getProposalListReceived(String token) throws BaseException;

    List<Proposal> getProposalListSent(String token) throws BaseException;
    boolean refuseOrWithdrawProposal(String token, int proposalId) throws AuthException, InputException;
}
