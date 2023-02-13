package it.unict;

public class AppGroupSpec {

    private String name;

    private String namespace;

    private Boolean affinityConfiguratorEnabled;

    private Boolean resourceConfiguratorEnabled;

    private Boolean replicasConfiguratorEnabled;

    private Integer minReplicas;

    private Integer maxReplicas;

    private Integer runPeriod;

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
