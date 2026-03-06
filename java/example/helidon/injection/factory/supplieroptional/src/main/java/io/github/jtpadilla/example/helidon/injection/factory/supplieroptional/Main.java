package io.github.jtpadilla.example.helidon.injection.factory.supplieroptional;

import io.helidon.service.registry.Service;
import io.helidon.service.registry.Services;

import java.util.Optional;

public class Main {

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            createMain();
        }
    }

    static private void createMain() {

        Optional<MyService> myService = Services.first(MyService.class);

        if (myService.isPresent()) {
            Main main = new Main(myService.get());
            System.out.println(main);
        } else {
            System.out.println("No se encontró el servicio");
        }
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