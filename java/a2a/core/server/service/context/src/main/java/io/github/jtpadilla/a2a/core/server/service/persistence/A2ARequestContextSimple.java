package io.github.jtpadilla.a2a.core.server.service.persistence;

import com.google.lf.a2a.v1.SendMessageRequest;
import com.google.lf.a2a.v1.SendMessageResponse;
import io.grpc.stub.StreamObserver;

final public class A2ARequestContextSimple extends A2ARequestContext {

    final private StreamObserver<SendMessageResponse> responseObserver;

    public A2ARequestContextSimple(SendMessageRequest request, StreamObserver<SendMessageResponse> responseObserver) {
        super(request);
        this.responseObserver = responseObserver;
    }

    public StreamObserver<SendMessageResponse> responseObserver() {
        return responseObserver;
    }

}
