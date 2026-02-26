package io.github.jtpadilla.example.javelit.widgetscheckbox;

import io.javelit.core.Jt;
import io.javelit.core.Server;
import org.icepear.echarts.Gauge;
import org.icepear.echarts.Line;
import org.icepear.echarts.charts.gauge.GaugeDataItem;
import org.icepear.echarts.charts.gauge.GaugeProgress;
import org.icepear.echarts.charts.gauge.GaugeSeries;

class WidgetsCheckBox {

    static void main(String[] args) {
        Server.builder(WidgetsCheckBox::javelit, 8080)
                .build()
                .start();

    }

    static private void javelit() {
        if (Jt.checkbox("Show the chart").use()) {
            Line line2 = new Line()
                    .addXAxis(new String[]{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"})
                    .addYAxis()
                    .addSeries(new Number[]{150, 230, 224, 218, 135, 147, 260});
            Jt.echarts(line2).use();
        }
    }

}