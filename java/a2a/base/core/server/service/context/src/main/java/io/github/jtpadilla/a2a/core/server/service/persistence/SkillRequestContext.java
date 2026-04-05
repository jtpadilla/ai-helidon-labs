package io.github.jtpadilla.a2a.core.server.service.persistence;

import com.google.lf.a2a.v1.SendMessageRequest;

public sealed class SkillRequestContext permits SkillRequestContextSimple, SkillRequestContextStream {

    final private SendMessageRequest request;

    public SkillRequestContext(SendMessageRequest request) {
        this.request = request;
    }

    public SendMessageRequest request() {
        return request;
    }

}
