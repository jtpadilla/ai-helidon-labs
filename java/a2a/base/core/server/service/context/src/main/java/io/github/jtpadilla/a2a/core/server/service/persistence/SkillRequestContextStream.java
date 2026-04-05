package io.github.jtpadilla.a2a.core.server.service.persistence;

import com.google.lf.a2a.v1.SendMessageRequest;
import com.google.lf.a2a.v1.StreamResponse;

final public class SkillRequestContextStream extends SkillRequestContext {

    final private StreamResponse responseObsever;

    public SkillRequestContextStream(SendMessageRequest request, StreamResponse responseObsever) {
        super(request);
        this.responseObsever = responseObsever;
    }

    public StreamResponse responseObsever() {
        return responseObsever;
    }

}
