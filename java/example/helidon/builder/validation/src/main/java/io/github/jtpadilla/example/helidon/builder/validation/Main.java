package io.github.jtpadilla.example.helidon.builder.validation;

public class Main {

    public static void main(String[] args) {

        DatabaseConfig config = DatabaseConfig.builder()
                .jdbcUrl("jdbc:postgresql://localhost:5432/mydb")
                //.username("admin")
                .entorno("PROD")
                .build();

        System.out.println(config);
        System.out.println(config.username().orElse("null"));


    }

}
