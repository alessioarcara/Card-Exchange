package com.aadm.cardexchange.shared.exceptions;


public class ProposalNotFoundException extends BaseException {
    private static final long serialVersionUID = 6535665235519385592L;

    public ProposalNotFoundException(String errorMessage) {
        super(errorMessage);
    }

    public ProposalNotFoundException() {
    }
}
