package info.magnolia.k8s;

import info.magnolia.k8s.updatedValue.ConfigMapDependentResource;
import info.magnolia.k8s.updatedValue.UpdatedValue;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.ControllerConfiguration;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;
import io.javaoperatorsdk.operator.api.reconciler.dependent.Dependent;

@ControllerConfiguration(dependents = {@Dependent(type = ConfigMapDependentResource.class)})
public class UpdatedValueReconciler implements Reconciler<UpdatedValue> {

    public UpdateControl<UpdatedValue> reconcile(UpdatedValue primary,
                                                 Context<UpdatedValue> context) {

        return UpdateControl.noUpdate();
    }
}
