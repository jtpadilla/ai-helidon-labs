package io.github.jtpadilla.example.langchain4j.toolfullexample2.domain;

public class TemperatureQueryException extends Exception {

    public TemperatureQueryException(String message) {
        super(message);
    }

    public TemperatureQueryException(String message, Throwable cause) {
        super(message, cause);
    }

}
