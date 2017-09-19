package com.worldpay.service.response;

import com.worldpay.service.model.token.DeleteTokenReply;

public class DeleteTokenResponse extends AbstractServiceResponse {

    private DeleteTokenReply deleteTokenReply;

    public DeleteTokenReply getDeleteTokenReply() {
        return deleteTokenReply;
    }

    public void setDeleteTokenResponse(DeleteTokenReply deleteTokenReply) {
        this.deleteTokenReply = deleteTokenReply;
    }
}
