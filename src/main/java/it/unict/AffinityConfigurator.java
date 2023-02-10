package it.unict;

import java.util.*;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;

@ApplicationScoped
public class AffinityConfigurator {

    private final int minWeight = 1;

    private final int maxWeight = 100;

    private List<WeightedPodAffinityTerm> getPodAffinityTerms(Deployment deployment, Map<String,Integer> affinities) {
        List<WeightedPodAffinityTerm> podAffinityTerms = new ArrayList<>();

        affinities.forEach((key, value) -> {
            WeightedPodAffinityTerm wpat = new WeightedPodAffinityTermBuilder()
                        .withWeight(value)
                        .withPodAffinityTerm(new PodAffinityTermBuilder()
                                .withLabelSelector(new LabelSelectorBuilder()
                                        .withMatchLabels(
                                                new HashMap<>() {{
                                                    put("app-group", deployment.getMetadata().getLabels().get("app-group"));
                                                    put("app", key);
                                                }}
                                        ).build())
                                .withTopologyKey("no-key")
                                .build())
                        .build();
                podAffinityTerms.add(wpat);
        });

        return podAffinityTerms;
    }

    public void updateAffinities(AppGraph appGraph) {
        double maxTraffic = Collections.max(
                appGraph.getApps()
                        .stream()
                        .map(App::getMaxTraffic)
                        .collect(Collectors.toList())
        );

        double minTraffic = Collections.min(
                appGraph.getApps()
                        .stream()
                        .map(App::getMinTraffic)
                        .collect(Collectors.toList())
        );

        int newRange = maxWeight - minWeight;
        int oldRange = (int)(maxTraffic - minTraffic);

        appGraph.getApps().forEach(app -> {
            Map<String,Integer> affinities = app.getTraffic()
                    .entrySet()
                    .stream()
                    .map((entry) -> Map.entry(
                            entry.getKey(),
                            (oldRange == 0) ? minWeight : ((int)(entry.getValue() - minTraffic) * newRange / oldRange) + minWeight)
                    )
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            List<WeightedPodAffinityTerm> podAffinityTerms = getPodAffinityTerms(app.getDeployment(), affinities);
            List<WeightedPodAffinityTerm> podAntiAffinityTerms = new ArrayList<>();
            if(app.getTopologyIndex() == 0) {
                podAntiAffinityTerms = getPodAffinityTerms(app.getDeployment(), Map.of(app.getName(), maxWeight));
            }

            app.getDeployment()
                    .getSpec()
                    .getTemplate()
                    .getSpec()
                    .setAffinity(new AffinityBuilder()
                            .withPodAffinity(new PodAffinityBuilder()
                                    .withPreferredDuringSchedulingIgnoredDuringExecution(podAffinityTerms)
                                    .build())
                            .withPodAntiAffinity(new PodAntiAffinityBuilder()
                                    .withPreferredDuringSchedulingIgnoredDuringExecution(podAntiAffinityTerms)
                                    .build())
                            .build());
        });
    }
}
