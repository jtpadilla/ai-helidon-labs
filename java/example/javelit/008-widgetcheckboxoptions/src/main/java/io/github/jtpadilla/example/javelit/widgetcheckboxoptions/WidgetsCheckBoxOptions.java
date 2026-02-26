package io.github.jtpadilla.example.javelit.widgetscheckboxoptions;

import io.javelit.core.Jt;
import io.javelit.core.Server;

import java.util.List;

class WidgetsCheckBoxOptions {

    static void main(String[] args) {
        Server.builder(WidgetsCheckBoxOptions::javelit, 8080)
                .build()
                .start();
    }

    static private void javelit() {
        String option = Jt.selectbox(
                "Which fruit do you like best?",
                List.of("Apple", "Banana", "Kiwi")).use();

        Jt.text("You selected: " + option).use();    }

}