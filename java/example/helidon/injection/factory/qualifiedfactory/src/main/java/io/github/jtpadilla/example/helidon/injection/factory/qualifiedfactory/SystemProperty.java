package io.github.jtpadilla.example.helidon.injection.factory.qualifiedfactory;

import io.helidon.service.registry.Service;

@Service.Qualifier
@interface SystemProperty {
    String value();
}
