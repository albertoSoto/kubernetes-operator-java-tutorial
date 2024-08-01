package org.acme;

//import io.fabric8.kubernetes.client.KubernetesClient;
//import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.javaoperatorsdk.operator.Operator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Runner {

    private static final Logger log = LoggerFactory.getLogger(Runner.class);

    public static void main(String[] args) {
        //new - to check
//        KubernetesClient client = new KubernetesClientBuilder().build();
        Operator operator = new Operator();
        operator.register(new GettingStartedReconciler());
//        operator.register(new WebPageReconciler(client));
        operator.start();
        log.info("Operator started.");
    }
}
