package io.github.jtpadilla.example.helidon.injection.factory.servicefactory;

import io.helidon.service.registry.Service;
import io.helidon.service.registry.Services;

import java.util.List;
import java.util.stream.Collectors;

@Service.Singleton
public class Main {

    public static void main(String[] args) {
        System.out.println(Services.get(Main.class));
    }

    final private List<MyService> myServiceList;

    @Service.Inject
    public Main(List<MyService> myServiceList) {
        this.myServiceList = myServiceList;
    }

    @Override
    public String toString() {
        String collect = myServiceList.stream().map(MyService::getText).collect(Collectors.joining(","));
        return "Main{" +
                "myService=" + collect +
                '}';
    }

}