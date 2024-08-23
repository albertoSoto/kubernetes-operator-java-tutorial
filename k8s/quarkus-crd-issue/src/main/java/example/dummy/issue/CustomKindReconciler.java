package example.dummy.issue;

import com.rabbitmq.v1beta1.RabbitmqCluster;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.ControllerConfiguration;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.javaoperatorsdk.operator.api.reconciler.Constants.WATCH_ALL_NAMESPACES;

@ControllerConfiguration(namespaces = WATCH_ALL_NAMESPACES)
public class CustomKindReconciler implements Reconciler<RabbitmqCluster> {
    private static final Logger log = LoggerFactory.getLogger(CustomKindReconciler.class);
    @Override
    public UpdateControl<RabbitmqCluster> reconcile(RabbitmqCluster resource, Context<RabbitmqCluster> context) throws Exception {
        log.info("Reconciling RabbitMQ cluster, the status is {}", resource.getStatus());
        return UpdateControl.noUpdate();
    }
}