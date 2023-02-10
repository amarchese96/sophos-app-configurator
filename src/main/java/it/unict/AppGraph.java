package it.unict;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import io.fabric8.kubernetes.api.model.apps.Deployment;

public class AppGraph {

    private final String appGroupName;

    private final List<App> apps;

    AppGraph(String appGroupName) {
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
        apps.add(new App(deployment, appName, topologyIndex, cpuUsage, memoryUsage, inboundTraffic, outboundTraffic));
    }

    public void sortApps() {
        apps.sort(Comparator.comparing(App::getTopologyIndex));
    }
}
