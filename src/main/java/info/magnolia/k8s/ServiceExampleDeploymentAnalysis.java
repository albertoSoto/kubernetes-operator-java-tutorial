/**
 * This file Copyright (c) 2024 Magnolia International
 * Ltd.  (http://www.magnolia-cms.com). All rights reserved.
 * <p>
 * <p>
 * This program and the accompanying materials are made
 * available under the terms of the Magnolia Network Agreement
 * which accompanies this distribution, and is available at
 * http://www.magnolia-cms.com/mna.html
 * <p>
 * Any modifications to this file must keep this entire header
 * intact.
 */
package info.magnolia.k8s;

import info.magnolia.k8s.rabbitMq.RabbitmqCluster;
import info.magnolia.k8s.rabbitMq.RabbitmqClusterList;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 *
 */
public class ServiceExampleDeploymentAnalysis {
    private static final Logger log = LoggerFactory.getLogger(Runner.class);

    public static void main(String[] args) {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            log.info("Kubernetes client created");
            DeploymentList deployments = client.apps().deployments().list();
            deployments.getItems().forEach(deployment -> {
                log.info("Deployment " + deployment.getMetadata().getName());

            });
        } catch (io.fabric8.kubernetes.client.KubernetesClientException e) {
            log.error("KubernetesClientException: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Exception while accessing the Kubernetes cluster: ", e);
        }
    }

}
