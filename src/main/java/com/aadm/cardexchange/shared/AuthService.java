package com.aadm.cardexchange.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("users")
public interface AuthService extends RemoteService {

    String signup(String email, String password);

    String signin(String email, String password);

    Boolean logout(String token);
}
