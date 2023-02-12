package it.unict;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.fabric8.kubernetes.api.model.apps.Deployment;

public class AppGroupGraph {

    private final String appGroupName;

    private final List<App> apps;

    AppGroupGraph(String appGroupName) {
        this.appGroupName = appGroupName;
        apps = new ArrayList<>();
    }

    public String getAppGroupName() {
        return appGroupName;
    }

    public List<App> getApps() {
        return apps;
    }

    public void addApp(Deployment deployment, String appName, int topologyIndex, double cpuUsage, double memoryUsage, Map<String, Double> inboundTraffic, Map<String, Double> outboundTraffic) {
        List<Channel> inboundChannels = inboundTraffic.entrySet()
                .stream()
                .map((entry) -> new Channel(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        List<Channel> outboundChannels = outboundTraffic.entrySet()
                .stream()
                .map((entry) -> new Channel(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        apps.add(new App(deployment, appName, topologyIndex, cpuUsage, memoryUsage, inboundChannels, outboundChannels));
    }

    public void sortApps() {
        apps.sort(Comparator.comparing(App::getTopologyIndex));
    }
}
