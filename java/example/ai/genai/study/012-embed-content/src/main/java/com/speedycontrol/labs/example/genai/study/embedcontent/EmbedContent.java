package com.speedycontrol.labs.example.genai.study.embedcontent;

import com.google.genai.Client;
import com.google.genai.errors.GenAiIOException;
import com.google.genai.types.EmbedContentResponse;
import com.speedycontrol.labs.example.genai.common.GenAIServiceSelector;
import io.github.jtpadilla.gcloud.genai.IGenAIService;

public class EmbedContent {

    public static void main(String[] args) {

        final IGenAIService genAIService = GenAIServiceSelector.fromDefault();

        try (Client client = genAIService.createClient()) {
            final EmbedContentResponse response = client.models.embedContent(
                    genAIService.getEmbedModel(),
                    "why is the sky blue?",
                    null
            );
            System.out.println("Embedding response: " + response);
        } catch (GenAiIOException e) {
            System.out.println("An error occurred working with GenAI: " + e.getMessage());
        }

    }

}
