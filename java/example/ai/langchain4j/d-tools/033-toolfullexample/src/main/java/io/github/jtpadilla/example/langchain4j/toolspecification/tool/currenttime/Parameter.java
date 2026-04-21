package io.github.jtpadilla.example.langchain4j.toolspecification.tool.currenttime;

import dev.langchain4j.model.chat.request.json.JsonObjectSchema;

class Parameter {

    static public JsonObjectSchema SPEC = JsonObjectSchema.builder().build();

    static public Parameter create(String argumentsJson) {
        return new Parameter();
    }

    private Parameter() {
    }

}
