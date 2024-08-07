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
package info.magnolia.k8s.serviceAnalysis;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentCondition;
import io.fabric8.kubernetes.api.model.apps.DeploymentStatus;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.ControllerConfiguration;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;
/**
 * Read also:
 *
 * - https://javaoperatorsdk.io/docs/configuration/#reconciler-level-configuration
 * - https://javaoperatorsdk.io/docs/features/#using-updatecontrol-and-deletecontrol
 * -
 */
@ControllerConfiguration(labelSelector = "app=hello-world")
public class HelloWorldServiceReconciler implements Reconciler<Deployment> {
    public static final String STATUS_MESSAGE = "Reconciled by DeploymentReconciler";

    private static final Logger log = LoggerFactory.getLogger(HelloWorldServiceReconciler.class);
    private final AtomicInteger numberOfExecutions = new AtomicInteger(0);

    @Override
    public UpdateControl<Deployment> reconcile(
            Deployment resource, Context<Deployment> context) {

        log.info("Reconcile deployment: {}", resource);
        numberOfExecutions.incrementAndGet();
        if (resource.getStatus() == null) {
            resource.setStatus(new DeploymentStatus());
        }
        if (resource.getStatus().getConditions() == null) {
            resource.getStatus().setConditions(new ArrayList<>());
        }
        var conditions = resource.getStatus().getConditions();
        var condition =
                conditions.stream().filter(c -> c.getMessage().equals(STATUS_MESSAGE)).findFirst();
        if (condition.isEmpty()) {
            conditions.add(new DeploymentCondition(null, null, STATUS_MESSAGE, null,
                    "unknown", "DeploymentReconciler"));
            return UpdateControl.patchStatus(resource);
        } else {
            return UpdateControl.noUpdate();
        }
    }

    public int getNumberOfExecutions() {
        return numberOfExecutions.get();
    }
}
