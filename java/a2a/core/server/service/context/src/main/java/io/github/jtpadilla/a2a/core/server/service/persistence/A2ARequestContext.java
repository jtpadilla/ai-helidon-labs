package io.github.jtpadilla.a2a.core.server.service.persistence;

import com.google.lf.a2a.v1.SendMessageRequest;

public sealed class A2ARequestContext permits A2ARequestContextSimple, A2ARequestContextStream {

    final private SendMessageRequest request;

    public A2ARequestContext(SendMessageRequest request) {
        this.request = request;
    }

    public SendMessageRequest request() {
        return request;
    }

}
