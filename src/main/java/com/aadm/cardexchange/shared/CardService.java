package com.aadm.cardexchange.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

// associa il service a un path relativo al modulo
@RemoteServiceRelativePath("cards")
public interface CardService extends RemoteService {
    CardImpl[] getCards();
}
