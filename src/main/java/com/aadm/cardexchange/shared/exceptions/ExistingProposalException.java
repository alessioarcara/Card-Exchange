package com.aadm.cardexchange.shared.exceptions;


public class ExistingProposalException extends BaseException {
    private static final long serialVersionUID = -4945745055104091701L;

    public ExistingProposalException(String errorMessage) {
        super(errorMessage);
    }

    public ExistingProposalException() {
    }
}
