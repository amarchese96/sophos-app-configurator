package it.unict;

public class AppGroupSpec {

    private final String name;

    private final String namespace;

    private final Boolean affinityConfiguratorEnabled;

    private final Boolean resourceConfiguratorEnabled;

    private final Boolean replicasConfiguratorEnabled;

    private final Integer minReplicas;

    private final Integer maxReplicas;

    private final Integer runPeriod;

    public AppGroupSpec(String name, String namespace, Boolean affinityConfiguratorEnabled, Boolean resourceConfiguratorEnabled, Boolean replicasConfiguratorEnabled, Integer minReplicas, Integer maxReplicas, Integer runPeriod) {
        this.name = name;
        this.namespace = namespace;
        this.affinityConfiguratorEnabled = affinityConfiguratorEnabled;
        this.resourceConfiguratorEnabled = resourceConfiguratorEnabled;
        this.replicasConfiguratorEnabled = replicasConfiguratorEnabled;
        this.minReplicas = minReplicas;
        this.maxReplicas = maxReplicas;
        this.runPeriod = runPeriod;
    }

    public String getName() {
        return name;
    }

    public String getNamespace() {
        return namespace;
    }

    public Boolean isAffinityConfiguratorEnabled() {
        return affinityConfiguratorEnabled;
    }

    public Boolean isResourceConfiguratorEnabled() {
        return resourceConfiguratorEnabled;
    }

    public Boolean isReplicasConfiguratorEnabled() {
        return replicasConfiguratorEnabled;
    }

    public Integer getMinReplicas() { return minReplicas; }

    public Integer getMaxReplicas() { return maxReplicas; }

    public Integer getRunPeriod() { return runPeriod; }
}
