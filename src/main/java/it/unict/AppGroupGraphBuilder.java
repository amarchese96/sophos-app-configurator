package it.unict;

import java.util.List;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import io.fabric8.kubernetes.api.model.apps.Deployment;

@ApplicationScoped
public class AppGroupGraphBuilder {
    
    @RestClient
    TelemetryService telemetryService;
    
    public AppGroupGraph buildAppGroupGraph(String appGroupName, List<Deployment> deployments) {
        AppGroupGraph appGroupGraph = new AppGroupGraph(appGroupName);
    
        deployments.forEach(deployment -> {
            String appName = deployment
              .getMetadata()
              .getLabels()
              .get("app");
    
            int topologyIndex = Integer.parseInt(deployment
                .getSpec()
                .getTemplate()
                .getMetadata()
                .getLabels()
                .get("index")
            );
    
            double cpuUsage = telemetryService
              .getAppCpuUsage(appGroupName, appName)
              .await().indefinitely();
    
            double memoryUsage = telemetryService
              .getAppMemoryUsage(appGroupName, appName)
              .await()
              .indefinitely();
    
            Map<String,Double> inboundTraffic = telemetryService
              .getAppTraffic(appGroupName, appName, "inbound")
              .await()
              .indefinitely();
    
            Map<String,Double> outboundTraffic = telemetryService
              .getAppTraffic(appGroupName, appName, "outbound")
              .await()
              .indefinitely();
    
            appGroupGraph.addApp(deployment, appName, topologyIndex, cpuUsage, memoryUsage, inboundTraffic, outboundTraffic);
        });
    
        appGroupGraph.sortApps();
    
        return appGroupGraph;
    }
}
