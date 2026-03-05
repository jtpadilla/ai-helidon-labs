package io.github.jtpadilla.example.helidon.injection.simple;

import io.helidon.service.registry.Service;

@Service.Singleton
class Greeter {

    String greet(String name) {
        return "Hello %s!".formatted(name);
    }

}