package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.shared.exceptions.AuthException;
import com.aadm.cardexchange.shared.exceptions.BaseException;
import com.aadm.cardexchange.shared.exceptions.InputException;
import com.aadm.cardexchange.shared.models.*;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;

@RemoteServiceRelativePath("exchanges")
public interface ExchangeService extends RemoteService {

    boolean addProposal(String token, String receiverUserEmail, List<PhysicalCard> senderPhysicalCards, List<PhysicalCard> receiverPhysicalCards) throws AuthException, InputException, AuthException;
    boolean CheckExistingPcardByIdCard(String token, Game game, int cardId) throws BaseException;
}
