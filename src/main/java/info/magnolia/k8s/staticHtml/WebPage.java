package info.magnolia.k8s.staticHtml;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.ShortNames;
import io.fabric8.kubernetes.model.annotation.Version;

/**
 * we can import io.fabric8.kubernetes.api.model.Namespaced; and add implements  Namespaced for secured deployment
 * This will change   scope: Cluster to  scope: Namespaced
 * It will need specific implementation to secure the deployment in terms of definining proper
 * In this case clusterrole.yaml, clusterrolebinding.yaml and service-account.yaml will need to be provided
 *
 */
@Group("info.magnolia.k8s.javaoperatorsdk")
@Version("v1")
@ShortNames("wp")
public class WebPage extends CustomResource<WebPageSpec, WebPageStatus> implements  Namespaced {

}
