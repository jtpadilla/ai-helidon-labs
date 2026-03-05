package io.github.jtpadilla.example.helidon.injection.simple;

import io.helidon.service.registry.Service;

@Service.Singleton
public class GreetingInjectionService {

    private final Greeter greeter;

    @Service.Inject
    GreetingInjectionService(Greeter greeter) {
        this.greeter = greeter;
    }

    void printGreeting(String name) {
        System.out.println(greeter.greet(name));
    }
}