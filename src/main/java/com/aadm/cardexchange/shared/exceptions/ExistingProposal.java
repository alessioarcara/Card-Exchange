package com.aadm.cardexchange.shared.exceptions;


public class ExistingProposal extends BaseException {
    private static final long serialVersionUID = -4945745055104091701L;

    public ExistingProposal(String errorMessage) {
        super(errorMessage);
    }

    public ExistingProposal() {
    }
}
