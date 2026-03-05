package io.github.jtpadilla.example.helidon.injection.simple;

import io.helidon.service.registry.Services;

class Main {

    public static void main(String[] args) {
        var greetings = Services.get(GreetingInjectionService.class);
        greetings.printGreeting("David");
    }
    
}