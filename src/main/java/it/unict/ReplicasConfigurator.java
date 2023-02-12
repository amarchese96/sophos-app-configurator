package it.unict;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ReplicasConfigurator {
    public void updateReplicas(AppGroupGraph appGroupGraph, int minReplicas, int maxReplicas) {
        appGroupGraph.getApps().forEach(app -> {
            if(app.getTopologyIndex() == 0) {
                int numReplicas = Math.min(maxReplicas, Math.max(app.getInboundTraffic().size(), minReplicas));
                app.getDeployment()
                        .getSpec()
                        .setReplicas(numReplicas);
            }
        });
    }
}
