package info.magnolia.k8s.updatedValue;

import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Version;

@Group("info.magnolia.k8s.javaoperatorsdk")
@Version("v1")
public class UpdatedValue extends CustomResource<UpdatedValueSpec, UpdatedValueStatus>
//        implements Namespaced
{
}
