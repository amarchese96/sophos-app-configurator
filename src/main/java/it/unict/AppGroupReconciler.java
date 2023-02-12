package it.unict;

import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppGroupReconciler implements Reconciler<AppGroup> {

  private static final Logger log = LoggerFactory.getLogger(AppGroupReconciler.class);

  private final KubernetesClient client;

  @Inject
  AppGroupGraphBuilder appGroupGraphBuilder;

  @Inject
  AffinityConfigurator affinityConfigurator;

  @Inject
  ResourceConfigurator resourceConfigurator;

  @Inject
  ReplicasConfigurator replicasConfigurator;

  public AppGroupReconciler(KubernetesClient client) {
    this.client = client;
  }

  @Override
  public UpdateControl<AppGroup> reconcile(AppGroup resource, Context context) {
    log.info("Reconciling app group {}", resource.getSpec().getName());

    List<Deployment> deployments = client.apps()
            .deployments()
            .inNamespace(resource.getSpec().getNamespace())
            .withLabel("app-group", resource.getSpec().getName())
            .list()
            .getItems();

    AppGroupGraph appGroupGraph = appGroupGraphBuilder.buildAppGroupGraph(resource.getSpec().getName(), deployments);

    if(resource.getSpec().isAffinityConfiguratorEnabled()) {
      log.info("Updating affinities for app group {}", resource.getSpec().getName());
      affinityConfigurator.updateAffinities(appGroupGraph);
    }

    if(resource.getSpec().isResourceConfiguratorEnabled()) {
      log.info("Updating resources for app group {}", resource.getSpec().getName());
      resourceConfigurator.updateResources(appGroupGraph);
    }

    if(resource.getSpec().isReplicasConfiguratorEnabled()) {
      log.info("Updating replicas for app group {}", resource.getSpec().getName());
      replicasConfigurator.updateReplicas(appGroupGraph, resource.getSpec().getMinReplicas(), resource.getSpec().getMaxReplicas());
    }

    appGroupGraph.getApps().forEach(app -> {
      Deployment deployment = app.getDeployment();
      client.apps()
              .deployments()
              .inNamespace(deployment.getMetadata().getNamespace())
              .withName(deployment.getMetadata().getName()).patch(deployment);
    });

    return UpdateControl.<AppGroup>noUpdate().rescheduleAfter(resource.getSpec().getRunPeriod(), TimeUnit.SECONDS);
  }
}

