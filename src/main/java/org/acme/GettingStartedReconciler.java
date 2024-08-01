package org.acme;

import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.ControllerConfiguration;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;
import io.javaoperatorsdk.operator.api.reconciler.dependent.Dependent;
import org.acme.templateCustomResource.GettingStartedCustomResource;

@ControllerConfiguration(dependents = {@Dependent(type = ConfigMapDependentResource.class)})
public class GettingStartedReconciler implements Reconciler<GettingStartedCustomResource> {

    public UpdateControl<GettingStartedCustomResource> reconcile(GettingStartedCustomResource primary,
                                                     Context<GettingStartedCustomResource> context) {

        return UpdateControl.noUpdate();
    }
}
