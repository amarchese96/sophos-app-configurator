package it.unict;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ReplicasConfigurator {
    public void updateReplicas(AppGraph appGraph, int minReplicas, int maxReplicas) {
        appGraph.getApps().forEach(app -> {
            if(app.getTopologyIndex() == 0) {
                int numReplicas = Math.min(maxReplicas, Math.max(app.getInboundTraffic().size(), minReplicas));
                app.getDeployment()
                        .getSpec()
                        .setReplicas(numReplicas);
            }
        });
    }
}
