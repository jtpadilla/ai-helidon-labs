package io.github.jtpadilla.example.langchain4j.agenticpure1;

import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.supervisor.SupervisorAgent;
import dev.langchain4j.agentic.supervisor.SupervisorResponseStrategy;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import io.github.jtpadilla.example.langchain4j.agenticpure1.agent.creditagent.CreditAgent;
import io.github.jtpadilla.example.langchain4j.agenticpure1.agent.creditagent.CreditAgentImpl;
import io.github.jtpadilla.example.langchain4j.agenticpure1.agent.exchangeagent.ExchangeAgent;
import io.github.jtpadilla.example.langchain4j.agenticpure1.agent.exchangeagent.ExchangeAgentImpl;
import io.github.jtpadilla.example.langchain4j.agenticpure1.agent.withdrawagent.WithdrawAgent;
import io.github.jtpadilla.example.langchain4j.agenticpure1.agent.withdrawagent.WithdrawAgentImpl;
import io.github.jtpadilla.example.langchain4j.agenticpure1.tool.BankTool;
import io.github.jtpadilla.example.langchain4j.agenticpure1.tool.ExchangeTool;
import io.helidon.config.Config;

public class AgentDemo {

    final static private String MODEL = "gemini-3.1-flash-lite-preview";

    final static private String API_KEY = Config.global().get("gemini-api-key").asString().orElseThrow(
            () -> new IllegalStateException("Configuration key 'gemini-api-key' is required"));

    public static void main(String[] args) {

        ChatModel chatModel = GoogleAiGeminiChatModel.builder()
                .apiKey(API_KEY)
                .modelName(MODEL)
                .logRequestsAndResponses(true)
                .build();

        BankTool bankTool = new BankTool();
        bankTool.createAccount("Mario", 1000.0);
        bankTool.createAccount("Georgios", 1000.0);

        WithdrawAgent withdrawAgent = WithdrawAgentImpl.build(chatModel, bankTool);
        CreditAgent creditAgent = CreditAgentImpl.build(chatModel, bankTool);
        ExchangeAgent exchangeAgent = ExchangeAgentImpl.build(chatModel, new ExchangeTool());

        SupervisorAgent bankSupervisor = AgenticServices
                .supervisorBuilder()
                .chatModel(chatModel)
                //.chatModel(PLANNER_MODEL) // En el ejemplo original habla de este modelo pero no se en que se diferencia.
                .subAgents(withdrawAgent, creditAgent, exchangeAgent)
                .responseStrategy(SupervisorResponseStrategy.SUMMARY)
                .build();

    }

}
