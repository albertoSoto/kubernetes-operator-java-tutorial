package org.acme;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.ControllerConfiguration;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;

import org.acme.customResource.WebPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerConfiguration
public class WebPageReconciler implements Reconciler<WebPage> {

    private static final Logger log = LoggerFactory.getLogger(WebPageReconciler.class);
    
    private KubernetesClient client;

    public WebPageReconciler(KubernetesClient client){
        this.client = client;
    }

    @Override
    public UpdateControl<WebPage> reconcile(WebPage arg0, Context<WebPage> arg1) throws Exception {
        // throw new UnsupportedOperationException("Unimplemented method 'reconcile'");
        return UpdateControl.noUpdate();
    }
    
}
