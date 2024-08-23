package example.dummy.issue;

import com.rabbitmq.v1beta1.RabbitmqCluster;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;

public class CustomKindReconciler implements Reconciler<RabbitmqCluster> {
    @Override
    public UpdateControl<RabbitmqCluster> reconcile(RabbitmqCluster resource, Context<RabbitmqCluster> context) {
        return UpdateControl.noUpdate();
    }
}