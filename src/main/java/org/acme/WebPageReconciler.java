package org.acme;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.ReconcilerUtils;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.ControllerConfiguration;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;

import org.acme.customResource.WebPage;
import org.acme.customResource.WebPageStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Map;

@ControllerConfiguration
public class WebPageReconciler implements Reconciler<WebPage> {

    private static final Logger log = LoggerFactory.getLogger(WebPageReconciler.class);

    private KubernetesClient client;

    public WebPageReconciler(KubernetesClient client) {
        this.client = client;
    }

    /**
     * we can inject and define the concilliation of the differents resources for k8s
     *
     * It is responsible for updating the status of the WebPage custom resource based on the reconciliation result.
     * Updates the status of the WebPage custom resource to indicate that it is ready, and then reschedules the reconciliation
     * process to run again after 1 second.
     * This is done to ensure that the WebPage custom resource remains in sync with the actual state of the Kubernetes
     * resources it manages.
     *
     * @param webPage the resource that has been created or updated
     * @param context the context with which the operation is executed
     * @return
     */
    @Override
    public UpdateControl<WebPage> reconcile(WebPage webPage, Context<WebPage> context) {
        client.configMaps().resource(desiredConfigMap(webPage)).serverSideApply();
        client.services().resource(desiredService(webPage)).serverSideApply();
        client.apps().deployments().resource(desiredDeployment(webPage)).serverSideApply();
        log.info("Reconciliation completed for WebPage {}", webPage.getMetadata().getName());
        webPage.setStatus(new WebPageStatus());
        webPage.getStatus().setReady(true);
        return UpdateControl.patchStatus(webPage).rescheduleAfter(Duration.ofSeconds(1));
    }

    /**
     * Let's define a k8s resource, which we need a name as metadata and we can inject the one from the static resource
     * Name space should be also considered.
     * In terms of the data, we can inject the resource value.
     * There is a tricky point that is the owner reference
     *
     * @param webPage
     * @return
     */
    private ConfigMap desiredConfigMap(WebPage webPage) {
        ConfigMap desiredConfigMap = new ConfigMapBuilder()
                .withMetadata(new ObjectMetaBuilder()
                        .withName(webPage.getMetadata().getName())
                        .withNamespace(webPage.getMetadata().getNamespace())
                        .build())
                .withData(Map.of("index.html", webPage.getSpec().getHtml()))
                .build();
        desiredConfigMap.addOwnerReference(webPage);
        return desiredConfigMap;
    }

    /**
     * The desiredService method is responsible for creating a Service resource that exposes the WebPage application
     * to the network.
     *
     * Creates a Service resource that exposes the WebPage application
     * to the network, using the configuration specified in a predefined YAML file.
     * The method also establishes a relationship between the Service and the WebPage object,
     * ensuring that the Service is managed in conjunction with the WebPage resource.
     *
     * @param webPage
     * @return
     */
    private Service desiredService(WebPage webPage) {
        Service service = ReconcilerUtils.loadYaml(Service.class, getClass(), "service.yaml");
        service.setMetadata(new ObjectMetaBuilder().withName(webPage.getMetadata().getName())
                .withNamespace(webPage.getMetadata().getNamespace())
                .build());
        service.getSpec()
                .setSelector(Map.of("app", webPage.getMetadata().getName()));
        service.addOwnerReference(webPage);
        return service;
    }


    private Deployment desiredDeployment(WebPage webPage) {
        Deployment deployment = ReconcilerUtils.loadYaml(Deployment.class, getClass(), "deployment.yaml");

        var deploymentName = webPage.getMetadata().getName();
        deployment.setMetadata(new ObjectMetaBuilder().withName(deploymentName)
                .withNamespace(webPage.getMetadata().getNamespace())
                .build());

        deployment.getSpec().getSelector().getMatchLabels().put("app", deploymentName);
        deployment.getSpec().getTemplate().getMetadata().getLabels().put("app", deploymentName);

        deployment
                .getSpec()
                .getTemplate()
                .getSpec()
                .getVolumes()
                .get(0)
                .setConfigMap(new ConfigMapVolumeSourceBuilder().withName(webPage.getMetadata().getName()).build());

        deployment.addOwnerReference(webPage);
        return deployment;
    }

}
