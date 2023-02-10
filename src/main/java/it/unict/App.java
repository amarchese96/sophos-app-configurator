package it.unict;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import io.fabric8.kubernetes.api.model.apps.Deployment;

public class App {

    private final Deployment deployment;

    private final String name;

    private final int topologyIndex;

    private final double cpuUsage;

    private final double memoryUsage;

    private final Map<String,Double> inboundTraffic;

    private final Map<String,Double> outboundTraffic;

    public App(Deployment deployment, String name, int topologyIndex, double cpuUsage, double memoryUsage,
            Map<String,Double> inboundTraffic, Map<String,Double> outboundTraffic) {
        this.deployment = deployment;
        this.name = name;
        this.topologyIndex = topologyIndex;
        this.cpuUsage = cpuUsage;
        this.memoryUsage = memoryUsage;
        this.inboundTraffic = inboundTraffic;
        this.outboundTraffic = outboundTraffic;
    }

    public Deployment getDeployment() {
        return deployment;
    }

    public String getName() {
        return name;
    }

    public int getTopologyIndex() {
        return topologyIndex;
    }

    public double getCpuUsage() {
        return cpuUsage;
    }

    public double getMemoryUsage() {
        return memoryUsage;
    }

    public Map<String, Double> getInboundTraffic() {
        return inboundTraffic;
    }

    public Map<String, Double> getOutboundTraffic() {
        return outboundTraffic;
    }

    public Map<String, Double> getTraffic() {
        return Stream.concat(
                inboundTraffic.entrySet().stream(),
                outboundTraffic.entrySet().stream()
        ).collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (v1, v2) -> v1)
        );
    }

    public Double getMaxTraffic() {
        List<Double> traffic = Stream.concat(
                inboundTraffic.values().stream(),
                outboundTraffic.values().stream()
        ).collect(Collectors.toList());

        if(traffic.isEmpty()) {
            return 0.0;
        }

        return Collections.max(traffic);
    }

    public Double getMinTraffic() {
        List<Double> traffic = Stream.concat(
                inboundTraffic.values().stream(),
                outboundTraffic.values().stream()
        ).collect(Collectors.toList());

        if(traffic.isEmpty()) {
            return 0.0;
        }

        return Collections.min(traffic);
    }
}
