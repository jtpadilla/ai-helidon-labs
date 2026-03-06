package io.github.jtpadilla.example.helidon.injection.factory.supplieroptional;

import java.util.Optional;
import java.util.function.Supplier;

public class MyServiceOptionalProvider implements Supplier<Optional<MyService>> {

    static private boolean enabled = true;

    @Override
    public Optional<MyService> get() {
        System.out.println("Enabled="+ enabled);
        try {
            return enabled ? Optional.of(new MyService()) : Optional.empty();
        } finally {
            enabled = !enabled;
        }
    }

}
