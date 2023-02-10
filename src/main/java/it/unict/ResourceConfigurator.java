package it.unict;

import java.util.HashMap;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.ResourceRequirements;

@ApplicationScoped
public class ResourceConfigurator {

    private ResourceRequirements getResourceRequirements(double cpuUsage, double memoryUsage) {
        HashMap<String, Quantity> resourceRequirementsMap = new HashMap<>();

        if(cpuUsage > 0.0) {
            resourceRequirementsMap.put("cpu", new Quantity(Math.round(cpuUsage) + "m"));
        }
        if(memoryUsage > 0.0) {
            resourceRequirementsMap.put("memory", new Quantity(Math.round(memoryUsage) + "Mi"));
        }

        ResourceRequirements resourceRequirements  = new ResourceRequirements();
        resourceRequirements.setRequests(resourceRequirementsMap);
        return resourceRequirements;
    }
    
    public void updateResources(AppGraph appGraph) {
        appGraph.getApps().forEach(app -> {
            String containerName = appGraph.getAppGroupName() + "-" + app.getName();

            List<Container> containers = app.getDeployment()
                .getSpec()
                .getTemplate()
                .getSpec()
                .getContainers();
            
            containers.forEach(container -> {
                if(container.getName().equals(containerName)) {
                    container.setResources(getResourceRequirements(app.getCpuUsage(), app.getMemoryUsage()));
                }
            });
        });
    }
}
