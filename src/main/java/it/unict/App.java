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

    private final List<Channel> inboundChannels;

    private final List<Channel> outboundChannels;

    public App(Deployment deployment, String name, int topologyIndex, double cpuUsage, double memoryUsage,
               List<Channel> inboundChannels, List<Channel> outboundChannels) {
        this.deployment = deployment;
        this.name = name;
        this.topologyIndex = topologyIndex;
        this.cpuUsage = cpuUsage;
        this.memoryUsage = memoryUsage;
        this.inboundChannels = inboundChannels;
        this.outboundChannels = outboundChannels;
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
        return inboundChannels.stream()
                .collect(Collectors.toMap(
                        Channel::getNeighborAppName,
                        Channel::getTraffic,
                        (v1, v2) -> v1
                ));
    }

    public Map<String, Double> getTraffic() {
        return Stream.concat(
                inboundChannels.stream(),
                outboundChannels.stream()
        ).collect(Collectors.toMap(
                Channel::getNeighborAppName,
                Channel::getTraffic,
                (v1, v2) -> v1
        ));
    }

    public Double getMaxTraffic() {
        List<Double> traffic = Stream.concat(
                inboundChannels.stream(),
                outboundChannels.stream()
        ).map(Channel::getTraffic).collect(Collectors.toList());

        if(traffic.isEmpty()) {
            return 0.0;
        }

        return Collections.max(traffic);
    }

    public Double getMinTraffic() {
        List<Double> traffic = Stream.concat(
                inboundChannels.stream(),
                outboundChannels.stream()
        ).map(Channel::getTraffic).collect(Collectors.toList());

        if(traffic.isEmpty()) {
            return 0.0;
        }

        return Collections.min(traffic);
    }
}
