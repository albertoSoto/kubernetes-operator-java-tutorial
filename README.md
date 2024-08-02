# Generated Project Skeleton

A simple operator that deploys a static website page in a kubernetes cluster

Project originally generated using the official JOSDK
Based on the repository https://github.com/operator-framework/java-operator-sdk/tree/main/sample-operators/webpage


```bash
$> mvn io.javaoperatorsdk:bootstrapper:4.9.2:create -DprojectGroupId=org.acme -DprojectArtifactId=getting-started
```

## Requirements

- JDK 21

## Basic usage

- Run a mvn clean package to generate the initial configuration

```bash
$> mvn clean package -U -DskipTests -DskipITs -Dmaven.test.skip=true
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

> TBD generate the service layer and deployment which fails at time of writing


### Example 2

You can proceed the same way with a custom config map definition. Proceed as it follows

```bash
$> mvn clean package
$> kubectl describe configmap hellowp 
$> 
```


### Further Details

Please, check the how-to file for details and references
Open how-to.md



