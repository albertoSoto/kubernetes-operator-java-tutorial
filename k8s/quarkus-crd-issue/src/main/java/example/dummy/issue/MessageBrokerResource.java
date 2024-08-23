package example.dummy.issue;

import com.rabbitmq.v1beta1.RabbitmqCluster;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.ConfigMapBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.NonDeletingOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Singleton
public class MessageBrokerResource extends CRUDKubernetesDependentResource<ConfigMap, RabbitmqCluster> {

    @Inject
    KubernetesClient client;

    public MessageBrokerResource() {
        super(ConfigMap.class);
    }

    @Override
    protected ConfigMap desired(RabbitmqCluster primary, Context<RabbitmqCluster> context) {
        String namespace = primary.getMetadata().getNamespace();
        Map<String, String> data = new HashMap<>();
        primary.getSpec().getService();
        String configMapName = namespace + "-" + "configmap-public";
        ConfigMap cfgMap = new ConfigMapBuilder().withNewMetadata().withName(configMapName).withNamespace(namespace).endMetadata().build();
        Resource<ConfigMap> resource = client.configMaps()
                .inNamespace(namespace)
                .resource(cfgMap);

        Function<NonDeletingOperation<ConfigMap>, ConfigMap> replaceFn = configMapNonDeletingOperation -> {
            ConfigMap patch = configMapNonDeletingOperation.patch();
            patch.setData(data);
            return patch;
        };
        return resource.createOr(replaceFn);
    }
}
