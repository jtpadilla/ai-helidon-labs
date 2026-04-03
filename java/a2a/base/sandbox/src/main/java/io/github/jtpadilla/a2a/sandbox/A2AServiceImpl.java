package io.github.jtpadilla.a2a.sandbox;

import com.google.lf.a2a.v1.A2AServiceGrpc;
import com.google.lf.a2a.v1.AgentCard;
import com.google.lf.a2a.v1.GetExtendedAgentCardRequest;
import com.google.lf.a2a.v1.Message;
import com.google.lf.a2a.v1.Part;
import com.google.lf.a2a.v1.Role;
import com.google.lf.a2a.v1.SendMessageRequest;
import com.google.lf.a2a.v1.SendMessageResponse;
import com.google.lf.a2a.v1.StreamResponse;
import io.grpc.stub.StreamObserver;

import java.util.UUID;

public class A2AServiceImpl extends A2AServiceGrpc.A2AServiceImplBase {

    private final AgentCard agentCard;

    public A2AServiceImpl(AgentCard agentCard) {
        this.agentCard = agentCard;
    }

    @Override
    public void sendMessage(SendMessageRequest request, StreamObserver<SendMessageResponse> responseObserver) {
        String text = request.getMessage().getPartsList().stream()
                .filter(Part::hasText)
                .map(Part::getText)
                .findFirst()
                .orElse("");

        Message echoMessage = Message.newBuilder()
                .setMessageId(UUID.randomUUID().toString())
                .setContextId(request.getMessage().getContextId())
                .setRole(Role.ROLE_AGENT)
                .addParts(Part.newBuilder().setText(text).build())
                .build();

        responseObserver.onNext(SendMessageResponse.newBuilder()
                .setMessage(echoMessage)
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void sendStreamingMessage(SendMessageRequest request, StreamObserver<StreamResponse> responseObserver) {
        String text = request.getMessage().getPartsList().stream()
                .filter(Part::hasText)
                .map(Part::getText)
                .findFirst()
                .orElse("");

        Message echoMessage = Message.newBuilder()
                .setMessageId(UUID.randomUUID().toString())
                .setContextId(request.getMessage().getContextId())
                .setRole(Role.ROLE_AGENT)
                .addParts(Part.newBuilder().setText(text).build())
                .build();

        responseObserver.onNext(StreamResponse.newBuilder()
                .setMessage(echoMessage)
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void getExtendedAgentCard(GetExtendedAgentCardRequest request, StreamObserver<AgentCard> responseObserver) {
        responseObserver.onNext(agentCard);
        responseObserver.onCompleted();
    }
}
