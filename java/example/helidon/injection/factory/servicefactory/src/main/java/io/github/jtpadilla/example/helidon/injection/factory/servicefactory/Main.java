package io.github.jtpadilla.example.helidon.injection.factory.servicefactory;

import io.helidon.service.registry.Service;
import io.helidon.service.registry.Services;

@Service.Singleton
public class Main {

    public static void main(String[] args) {
        System.out.println(Services.get(Main.class));
    }

    final private MyService myService;

    @Service.Inject
    public Main(MyService myService) {
        this.myService = myService;
    }

    @Override
    public String toString() {
        return "Main{" +
                "myService=" + myService.getText() +
                '}';
    }

}