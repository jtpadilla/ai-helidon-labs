package io.github.jtpadilla.example.langchain4j.goalorientedagenticpattern1;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.web.search.WebSearchInformationResult;
import dev.langchain4j.web.search.WebSearchOrganicResult;
import dev.langchain4j.web.search.WebSearchResults;
import dev.langchain4j.web.search.WebSearchTool;
import io.github.jtpadilla.example.util.Format;
import io.github.jtpadilla.example.util.GoogleModels;
import io.helidon.config.Config;

import java.net.URI;
import java.util.List;
import java.util.Map;

public class AgentDemo {

    final static private String API_KEY = Config.global().get("gemini-api-key").asString().orElseThrow(
            () -> new IllegalStateException("La clave de configuración 'gemini-api-key' es obligatoria"));

    public enum Sign {
        ARIES,
        TAURO,
        GEMINIS,
        CANCER,
        LEO,
        VIRGO,
        LIBRA,
        ESCORPIO,
        SAGITARIO,
        CAPRICORNIO,
        ACUARIO,
        PISCIS
    }

    public record Person(String name, String horoscope) {
    }

    public interface HoroscopeGenerator {
        @SystemMessage("Eres un astrólogo que genera horóscopos basándose en el nombre y el signo zodiacal del usuario.")
        @UserMessage("Genera el horóscopo para {{person}} que es {{sign}}.")
        @Agent("Un astrólogo que genera horóscopos basándose en el nombre y el signo zodiacal del usuario.")
        String horoscope(@V("person") Person person, @V("sign") Sign sign);
    }

    public interface PersonExtractor {

        @UserMessage("Extrae una persona del siguiente texto: {{prompt}}")
        @Agent("Extrae una persona del texto del usuario")
        Person extractPerson(@V("prompt") String prompt);
    }

    public interface SignExtractor {

        @UserMessage("Extrae el signo zodiacal de una persona del siguiente texto: {{prompt}}")
        @Agent("Extrae el signo zodiacal del texto del usuario")
        Sign extractSign(@V("prompt") String prompt);
    }

    public interface Writer {
        @UserMessage("""
                Crea un texto divertido para {{person}} basándote en lo siguiente:
                - su horóscopo: {{horoscope}}
                - una noticia actual: {{story}}
                """)
        @Agent("Crea un texto divertido para la persona objetivo basándose en su horóscopo y noticias actuales")
        String write(@V("person") Person person, @V("horoscope") String horoscope, @V("story") String story);
    }

    public interface StoryFinder {

        @SystemMessage("""
                Eres un buscador de historias. Usa las herramientas de búsqueda web proporcionadas,
                llamándolas una única vez, para encontrar una historia ficticia y divertida en internet
                sobre el tema indicado por el usuario.
                """)
        @UserMessage("""
                Busca en internet una historia para {{person}} que tiene el siguiente horóscopo: {{horoscope}}.
                """)
        @Agent("Busca en internet una historia para una persona dado su horóscopo")
        String findStory(@V("person") Person person, @V("horoscope") String horoscope);
    }

    public static void main(String[] args) {

        ChatModel chatModel = GoogleAiGeminiChatModel.builder()
                .apiKey(API_KEY)
                .modelName(GoogleModels.geminiFlashLite())
                .logRequestsAndResponses(true)
                .sendThinking(true)
                .returnThinking(true)
                .build();

        HoroscopeGenerator horoscopeGenerator = AgenticServices.agentBuilder(HoroscopeGenerator.class)
                .chatModel(chatModel)
                .outputKey("horoscope")
                .build();

        PersonExtractor personExtractor = AgenticServices.agentBuilder(PersonExtractor.class)
                .chatModel(chatModel)
                .outputKey("person")
                .build();

        SignExtractor signExtractor = AgenticServices.agentBuilder(SignExtractor.class)
                .chatModel(chatModel)
                .outputKey("sign")
                .build();

        Writer writer = AgenticServices.agentBuilder(Writer.class)
                .chatModel(chatModel)
                .outputKey("writeup")
                .build();

        StoryFinder storyFinder = AgenticServices.agentBuilder(StoryFinder.class)
                .chatModel(chatModel)
                .tools(WebSearchTool.from(request -> WebSearchResults.from(
                        WebSearchInformationResult.from(1L),
                        List.of(WebSearchOrganicResult.from(
                                "Un Sagitario intentó predecir el futuro con una bola de cristal y acabó pidiendo pizza",
                                URI.create("https://example.com/funny-story"),
                                "Noticias ficticias del universo",
                                "Un astrólogo sagitariano aseguró haber visto el futuro en su bola de cristal, pero sólo vio su reflejo pidiendo una pizza de pepperoni a las 3am."
                        ))
                )))
                .outputKey("story")
                .build();

        UntypedAgent horoscopeAgent = AgenticServices.plannerBuilder()
                .subAgents(horoscopeGenerator, personExtractor, signExtractor, writer, storyFinder)
                .outputKey("writeup")
                .planner(GoalOrientedPlanner::new)
                .build();

        Map<String, Object> input = Map.of("prompt", "Me llamo Mario y mi signo zodiacal es piscis");
        String writeup = (String) horoscopeAgent.invoke(input);
        System.out.println(Format.markdown(writeup));

    }

}

