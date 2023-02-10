package it.unict;

import java.util.List;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import io.fabric8.kubernetes.api.model.apps.Deployment;

@ApplicationScoped
public class AppGraphBuilder {  
    
    @RestClient
    TelemetryService telemetryService;
    
    public AppGraph buildAppGraph(String appGroupName, List<Deployment> deploymentList) {
        AppGraph appGraph = new AppGraph(appGroupName);
    
        deploymentList.forEach(deployment -> {
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
              .getCpuUsage(appGroupName, appName)
              .await().indefinitely();
    
            double memoryUsage = telemetryService
              .getMemoryUsage(appGroupName, appName)
              .await()
              .indefinitely();
    
            Map<String,Double> inboundTraffic = telemetryService
              .getTraffic(appGroupName, appName, "inbound")
              .await()
              .indefinitely();
    
            Map<String,Double> outboundTraffic = telemetryService
              .getTraffic(appGroupName, appName, "outbound")
              .await()
              .indefinitely();
    
            appGraph.addApp(deployment, appName, topologyIndex, cpuUsage, memoryUsage, inboundTraffic, outboundTraffic);
        });
    
        appGraph.sortApps();
    
        return appGraph;
    }
}
