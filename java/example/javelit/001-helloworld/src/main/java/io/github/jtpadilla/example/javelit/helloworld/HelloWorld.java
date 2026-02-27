package io.github.jtpadilla.example.javelit.helloworld;

import io.javelit.core.Jt;
import io.javelit.core.Server;

class HelloWorld {

    static void main(String[] args) {
        final var server = Server.builder(HelloWorld::javelit, 8080).build();
        server.start();
    }

    static private void javelit() {
        Jt.text("Hello World").use();
    }

}