package com.aadm.cardexchange.server;

import com.aadm.cardexchange.shared.UserService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class UserServiceImpl extends RemoteServiceServlet implements UserService {
    @Override
    public boolean me() {
        return true;
    }
}
