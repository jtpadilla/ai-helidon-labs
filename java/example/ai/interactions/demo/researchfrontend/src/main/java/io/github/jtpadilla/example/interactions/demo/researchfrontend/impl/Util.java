package io.github.jtpadilla.example.interactions.demo.researchfrontend.impl;

import io.github.glaforge.gemini.interactions.model.Content;
import io.github.glaforge.gemini.interactions.model.Interaction;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Util {

    public static final ObjectMapper MAPPER = JsonMapper.builder().build();

    public static List<String> getTopics(Interaction interaction) {
        if (interaction.outputs() == null)
            return List.of();

        return interaction.outputs().stream()
                .filter(c -> c instanceof Content.TextContent)
                .map(c -> ((Content.TextContent) c).text())
                .flatMap(text -> {
                    try {
                        return MAPPER.readValue(text, new TypeReference<List<String>>() {
                        }).stream();
                    } catch (Exception e) {
                        return Stream.<String>empty();
                    }
                })
                .collect(Collectors.toList());
    }

    public static String getText(Interaction interaction) {
        if (interaction.outputs() == null)
            return "";
        return interaction.outputs().stream()
                .filter(c -> c instanceof Content.TextContent)
                .map(c -> ((Content.TextContent) c).text())
                .collect(Collectors.joining("\n"));
    }

    public static byte[] getInfographicData(Interaction interaction) {
        if (interaction.outputs() == null)
            return null;
        return interaction.outputs().stream()
                .filter(c -> c instanceof Content.ImageContent)
                .map(c -> ((Content.ImageContent) c).data())
                .findFirst()
                .orElse(null);
    }

    static String transformCitations(String report) {
        if (report == null || report.isBlank()) {
            return report;
        }

        // 1. Find the sources section
        int sourcesIndex = report.lastIndexOf("**Sources:**");
        if (sourcesIndex == -1) {
            sourcesIndex = report.lastIndexOf("Sources:");
        }

        if (sourcesIndex == -1) {
            return report;
        }

        String contentBefore = report.substring(0, sourcesIndex);
        String sourcesSection = report.substring(sourcesIndex);

        // 2. Parse sources to build ID -> URL map
        Map<String, String> urlMap = new HashMap<>();

        // Regex for parsing sources lines: "1. [Title](URL)"
        // Using multiline mode to match multiple lines
        Pattern sourcePattern = Pattern.compile("^\\s*(\\d+)\\.\\s+\\[.*?\\]\\((.*?)\\)", Pattern.MULTILINE);
        Matcher sourceMatcher = sourcePattern.matcher(sourcesSection);

        while (sourceMatcher.find()) {
            String id = sourceMatcher.group(1);
            String url = sourceMatcher.group(2);
            urlMap.put(id, url);
        }

        // 3. Replace citations in the content
        // Pattern for "[cite: 1, 2]"
        Pattern citePattern = Pattern.compile("\\[cite:\\s*([\\d,\\s]+)\\]");
        Matcher citeMatcher = citePattern.matcher(contentBefore);

        StringBuilder sb = new StringBuilder();
        while (citeMatcher.find()) {
            String idsPart = citeMatcher.group(1);
            String[] ids = idsPart.split(",");
            StringBuilder replaced = new StringBuilder();
            List<String> links = new ArrayList<>();
            for (String id : ids) {
                id = id.trim();
                String url = urlMap.get(id);
                if (url != null) {
                    links.add("[" + id + "](" + url + ")");
                } else {
                    links.add("[" + id + "](#source-" + id + ")");
                }
            }
            replaced.append("<sup>").append(String.join(", ", links)).append("</sup>");
            citeMatcher.appendReplacement(sb, Matcher.quoteReplacement(replaced.toString()));
        }
        citeMatcher.appendTail(sb);

        return sb.toString() + sourcesSection;
    }

}
