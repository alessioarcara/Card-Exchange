package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.shared.exceptions.AuthException;
import com.aadm.cardexchange.shared.exceptions.BaseException;
import com.aadm.cardexchange.shared.models.Game;
import com.aadm.cardexchange.shared.models.PhysicalCardDecorator;
import com.aadm.cardexchange.shared.models.PhysicalCardImpl;
import com.aadm.cardexchange.shared.models.Status;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;

public interface ExchangeServiceAsync {
    void addProposal(String token, String receiverUserEmail, List<PhysicalCardImpl> senderPhysicalCards, List<PhysicalCardImpl> receiverPhysicalCards, AsyncCallback<Boolean> async);
}
