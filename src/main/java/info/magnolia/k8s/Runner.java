package info.magnolia.k8s;

import info.magnolia.k8s.updatedValue.UpdatedValue;
import io.javaoperatorsdk.operator.Operator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Runner {

    private static final Logger log = LoggerFactory.getLogger(Runner.class);

    public static void main(String[] args) {
//        KubernetesClient client = new KubernetesClientBuilder().build();
        Operator operator = new Operator();
        operator.register(new UpdatedValueReconciler());
        operator.register(new WebPageReconciler(operator.getConfigurationService().getKubernetesClient()));
//        operator.register(new WebPageReconciler(client));
        operator.start();
        log.info("Operator started.");
    }
}
