package com.aadm.cardexchange.server;

import com.aadm.cardexchange.shared.UserService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;


public class UserServiceImpl extends RemoteServiceServlet implements UserService {
    private static final long serialVersionUID = 5646081795222815765L;
    private final MapDB db;

    public UserServiceImpl() {
        db = new MapDBImpl();
    }

    public UserServiceImpl(MapDB mockDB) {
        db = mockDB;
    }
}
