# JOSDK - Kubernetes operators in java tutorial

Java operators can currently be implemented by Quarkus or by the Official JOSDK
In this case, we will show how it works with the up to date JOSDK.

One of the use cases for an operator is to create a template (custom resource) where some values are set in the creation time. The biggest difference between a file template and an operator is that the common content (in the case of a template) is static, whereas in an operator it is set programmatically, which means that you’ve got the freedom to change the definition of the common part dynamically. This is known as a custom resource, in which, instead of using a well-known Kubernetes resource, you implement your own custom Kubernetes resource with your own fields.

Another use case might be to react/operate when something happens inside the cluster. Suppose you’ve got some in-memory data grids deployed on the cluster, and one of these instances dies. Maybe in this case what you want is to notify all living instances that one of the elements of the data grid cluster has been stopped.

As you can see, it is about not only the creation of a resource but also applying some tasks that are specific to your application that need to be done atop one of the tasks that Kubernetes is already doing.

The Kubernetes Operator uses Kubernetes API to decide when and how to run some of these customizations.


Project originally generated using the official JOSDK
Based on the repository https://github.com/operator-framework/java-operator-sdk/tree/main/sample-operators/webpage
Check references for more information

 - Case 1: A simple operator that deploys a static website page in a kubernetes cluster
 - Case 2: A hello-world web page with multiple pods and a operator that will be called on autoscaling
 - Case 3: Logic to deploy the official rabbitmq operator

Start just typing the following:

```bash
$> mvn io.javaoperatorsdk:bootstrapper:4.9.2:create -DprojectGroupId=org.acme -DprojectArtifactId=getting-started
```

## Requirements

- JDK 21
- Kubectl on a local cluster for k8s

## Basic usage

Generally talking we need to compile the project which will generate the CRD definitions.
Once the CRD definitions are installed we can install the descriptor that uses our new type.

On the java side, we can execute the project which will implemente a Reconciler. The reconciler is a kind of implementation
that inherits from the official types of Kubernetes. This will hook the kubernetes cycle loop, and inject our logic, 
therefore, our logic will be a callback for the kubernetes resources.
The main class will be executing the client for kubernetes and inject the operator in the cycle loop.

Kubernetes will call the logic when resources get updated.

Recap:

A normal definition of a kubernetes descriptor has a type defined by "kind"
A CRD o Custom resource definition will generate a new type of resources to use in your pipeline.

```yaml
apiVersion: rabbitmq.com/v1beta1
kind: RabbitmqCluster
metadata:
  name: rabbitmq-cluster
```

Check full tutorial located at [java-operators.md](java-operators.md) file


### General usage details

- Run a mvn clean package to generate the initial configuration

```bash
$> mvn clean package
```

- Check file generation under target/classes/META-INF/fabric8/webpages.info.magnolia.k8s.javaoperatorsdk-v1.yml
- Install the generated CRD

```bash
$> kubectl apply -f target/classes/META-INF/fabric8/webpages.info.magnolia.k8s.javaoperatorsdk-v1.yml
...
customresourcedefinition.apiextensions.k8s.io/webpages.info.magnolia.k8s.javaoperatorsdk created
#Check if it is valid or see as another resource with the following 
$> kubectl get crd
NAME                                         CREATED AT
webpages.info.magnolia.k8s.javaoperatorsdk   2024-08-02T07:17:07Z
```

- Run the project with the runner class
- Deploy the webpage static file after running the operator project

```bash
$> kubectl apply -f k8s/webpage.yaml
```

Although we have deployed a resource of type WebPage we can see that the different 
environments that are managed by it's trigerred logic are created. 

```bash
$> kubectl get configmap
NAME               DATA   AGE
hellowp            1      12m
kube-root-ca.crt   1      2d1h

$> kubectl describe configmap hellowp
Name:         hellowp
Namespace:    default
Labels:       <none>
Annotations:  <none>

Data
====
index.html:
----
<html>
  <head>
    <title>Hello world</title>
  </head>
  <body>
    <h1>Hello world!!!!!</h1>
  </body>
</html>


BinaryData

```

Other services for deployment and service layer are going to be 
generated from the resource template in the classpath resource folder

```bash
$>  kubectl get services

NAME         TYPE        CLUSTER-IP     EXTERNAL-IP   PORT(S)        AGE
hellowp      NodePort    10.96.250.22   <none>        80:31792/TCP   4m53s
kubernetes   ClusterIP   10.96.0.1      <none>        443/TCP        2d2h

$>  kubectl get deployments

NAME      READY   UP-TO-DATE   AVAILABLE   AGE
hellowp   1/1     1            1           4m21s
```

The generated page is going to be exposed to the url
Get the service name: kubectl get service -n test
URL to a kubernetes service is service-name.namespace.svc.cluster.local:service-port where cluster.local is the kubernetes cluster name.
To get the cluster name: kubectl config get-contexts | awk {'print $2'}

URL to service in your case will be hellowp.svc.cluster.local:80

```bash
kubectl describe service hellowp 
 
```


TBD here 

You can proceed the same way with a custom config map definition. Proceed as it follows

```bash
$> mvn clean package
$> kubectl describe configmap hellowp 
$> 
```

## Specific use cases

### Case 1: A simple operator that deploys a static website page in a kubernetes cluster

- compile the project as to generate the CRD ()
- install the crd
- execute the main class located at src/main/java/info/magnolia/k8s/Runner.java
- install the webpage

In a nutshell: 

```bash
$> mvn clean package
$> kubectl apply -f target/classes/META-INF/fabric8/webpages.info.magnolia.k8s.javaoperatorsdk-v1.yml
$> java info.magnolia.k8s.Runner
$> kubectl apply -f k8s/webpage.yaml
```

You will see a wrong loop when running with the following

```log
2024-08-08 14:19:17,733 62 i.m.k.WebPageReconciler        [INFO ] Reconciliation completed for WebPage hellowp
```

What is going to happen is that after deploying the static webpage, a whole setup will be created, 
using deployments, service and even an ingress deployment. Check them using the following command

```bash
$> kubectl -n default get all
NAME                            READY   STATUS    RESTARTS   AGE
pod/hellowp-67c6586c4-88fgx     1/1     Running   0          67s

NAME                             TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)                        AGE
service/hellowp                  NodePort    10.107.221.171   <none>        80:30995/TCP                   67s

NAME                      READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/hellowp   1/1     1            1           67s

NAME                                DESIRED   CURRENT   READY   AGE
replicaset.apps/hellowp-67c6586c4   1         1         1       67s
```

### Case 2: A hello-world web page with multiple pods and a operator that will be called on autoscaling

In this case we are going to use a common approach for a deployment with an autoscaled cluster of hello-world pods with web content
After deploying the cluster we will launch our Operator which will listen to the changes in the infra.
We use this approach to listen to a common specific resource in the cluster when autoscaling


How to proceed:

```bash
$> kubectl apply -f k8s/serviceExample/deployment.yaml
deployment.apps/hello-world created
service/hello-world created
#optional: check if it works
$> kubectl get pods                                                                                               ✔  1812  14:32:53
NAME                           READY   STATUS    RESTARTS   AGE
hello-world-7c8fd6c57f-2cqtw   1/1     Running   0          17s
hello-world-7c8fd6c57f-8hdsg   1/1     Running   0          17s
hello-world-7c8fd6c57f-8p922   1/1     Running   0          17s
hello-world-7c8fd6c57f-vntl4   1/1     Running   0          17s
$> kubectl port-forward hello-world-7c8fd6c57f-2cqtw 9090:8080
$> curl http://localhost:9090
Hello, world!
Version: 1.0.0
Hostname: hello-world-7c8fd6c57f-2cqtw
#operator start up
$> java info.magnolia.k8s.Runner
# Replace the value of 'replicas' with the desired number
$> sed -i "s/replicas: 4/replicas: 5/" deployment.yaml
$> kubectl apply -f k8s/serviceExample/deployment.yaml
```

If place a breakpoint it will show all cluster info.

Explanation:

If you check src/main/java/info/magnolia/k8s/serviceAnalysis/HelloWorldServiceReconciler.java

```java
@ControllerConfiguration(labelSelector = "app=hello-world")
public class HelloWorldServiceReconciler implements Reconciler<Deployment> {}
```

will listen to a specific label selector. This label selector must be placed in the metadata of the descriptor:

```yaml
metadata:
  labels:
    app: hello-world
```


### Case 3: Logic to deploy the official rabbitmq operator

In this case we need to deploy and install an operator.
Is a very tricky situation as installing the operator is easy, but playing with the operator to install new types
is more tricky

To proceed in detail, run k8s/rabbit/installOperator.sh

It will show a menu like the following:

```bash
--------------------------------------------------------------
 RabbitMQ cluster setup
--------------------------------------------------------------
Choose what do you wanna do:
   0 - Overwrite let's encrypt configuration from the operator
   1 - Install official RabbitMq Operators (cluster-operator, messaging-topology-operator)
   2 - Deploy RabbitMQ cluster
   3 - Check rabbitmq cluster status
   4 - Execute installation for custom resource on rabbitmqCluster with messaging-topology-operator
   5 - Get kubectl system status
   6 - List pod 0 queues
   7 - Inspect topology operator
   8 - Forward rabbit dashboard to localhost
   n - none
--------------------------------------------------------------
Which option do you wanna trigger? 
```

You will need to execute option 0 before option 4.
Please, pay attention that the setup under step 1 will install certManager locally avoiding the error with https.
You may need helm install to run it too. (step 0)

The port forwarding method is very helpful, as the rabbitmq cluster will have user/pass autogenerated


## References


Video training:

- Tech with Nana https://www.youtube.com/watch?v=QoDqxm7ybLc
- Attila Meszkaros (creator) https://www.youtube.com/watch?v=CvftaV-xrB4
  Repo at official demo https://github.com/operator-framework/java-operator-sdk/tree/main/sample-operators/webpage
- RabbitMQ Operator* https://www.youtube.com/watch?v=GxdyQSUEj5U

- Redhat and quarkus https://www.youtube.com/watch?v=s56LRtdbSB4
  Repo at https://hatmarch.github.io/java-operator-sdk-tutorial/java-operator-tutorial/demo/setup.html
  https://github.com/hatmarch/java-operator-sdk-tutorial
- Devoxx'20 https://www.youtube.com/watch?v=Q9nuMJ6usFY

Read training:
- https://kubernetes.io/docs/concepts/extend-kubernetes/api-extension/custom-resources/
- https://blog.container-solutions.com/cloud-native-java-infrastructure-automation-with-kubernetes-operators
- https://developers.redhat.com/blog/2019/10/07/write-a-simple-kubernetes-operator-in-java-using-the-fabric8-kubernetes-client#
- https://www.baeldung.com/java-kubernetes-operator-sdk
- https://github.com/quarkiverse/quarkus-operator-sdk/blob/main/README.md
- https://quarkus.io/guides/getting-started
- https://blog.lunatech.com/posts/2021-06-11-kubernetes-operator-with-java-and-quarkus
- https://javaoperatorsdk.io/docs/using-samples/

From redhat
- https://quarkus.io/guides/deploying-to-kubernetes#deploying < Important!
- https://developers.redhat.com/articles/2023/08/16/how-implement-kubernetes-operators-java-operator-sdk#
- https://developers.redhat.com/articles/2022/03/22/write-kubernetes-java-java-operator-sdk-part-2#


Repos:
- https://github.com/operator-framework/java-operator-sdk
- https://github.com/hatmarch/java-operator-sdk-tutorial
- https://github.com/quarkiverse/quarkus-operator-sdk?tab=readme-ov-file

RabbitMQ operator:
- https://www.rabbitmq.com/kubernetes/operator/operator-overview
- https://www.rabbitmq.com/kubernetes/operator/quickstart-operator
- https://github.com/rabbitmq/messaging-topology-operator/blob/main/docs/api/rabbitmq.com.ref.asciidoc
- https://github.com/rabbitmq/cluster-operator/tree/main/docs/examples/hello-world

Books:
- https://learning.oreilly.com/library/view/kubernetes-operators/9781492048039/


Additional:
- Quarkus CLI https://quarkus.io/guides/cli-tooling
- https://docs.quarkiverse.io/quarkus-operator-sdk/dev/deploy-with-olm.html
- https://www.docker.com/blog/develop-kubernetes-operators-in-java-without-breaking-a-sweat/
- https://blog.container-solutions.com/cloud-native-java-infrastructure-automation-with-kubernetes-operators


