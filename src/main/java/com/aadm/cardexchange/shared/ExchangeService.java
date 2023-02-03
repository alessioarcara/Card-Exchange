package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.shared.exceptions.BaseException;
import com.aadm.cardexchange.shared.models.PhysicalCard;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;

@RemoteServiceRelativePath("decks")
public interface ExchangeService extends RemoteService {

    boolean addProposal(String token, String receiverUserEmail, List<PhysicalCard> senderPhysicalCards, List<PhysicalCard> receiverPhysicalCards) throws BaseException;

}
