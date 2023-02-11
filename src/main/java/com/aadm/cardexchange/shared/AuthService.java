package com.aadm.cardexchange.shared;

import com.aadm.cardexchange.shared.exceptions.AuthException;
import com.aadm.cardexchange.shared.payloads.AuthPayload;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("users")
public interface AuthService extends RemoteService {
    String me(String token) throws AuthException;

    AuthPayload signup(String email, String password) throws AuthException;

    AuthPayload signin(String email, String password) throws AuthException;

    Boolean logout(String token) throws AuthException;
}
