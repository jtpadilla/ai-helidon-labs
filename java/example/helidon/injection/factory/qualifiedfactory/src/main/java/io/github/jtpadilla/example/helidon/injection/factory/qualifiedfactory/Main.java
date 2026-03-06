package io.github.jtpadilla.example.helidon.injection.factory.qualifiedfactory;

import io.helidon.service.registry.Service;
import io.helidon.service.registry.Services;

@Service.Singleton
public class Main {

    public static void main(String[] args) {
        System.setProperty("http.host", "localhost");
        System.setProperty("http.port", "8080");

        Main main = Services.get(Main.class);
        System.out.println("Inyectado: " + main.props);
    }

    private final SystemProperties props;

    @Service.Inject
    public Main(SystemProperties props) {
        this.props = props;
    }

}