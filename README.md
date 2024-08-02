# Generated Project Skeleton

A simple operator that deploys a static website page in a kubernetes cluster

Project originally generated using the official JOSDK

```bash
mvn io.javaoperatorsdk:bootstrapper:4.9.2:create -DprojectGroupId=org.acme -DprojectArtifactId=getting-started
```

## Requirements

- JDK 21

## Basic usage

- Run a mvn clean package to generate the initial configuration

```bash
mvn clean package -U -DskipTests -DskipITs -Dmaven.test.skip=true
```

- Check file generation under target/classes/META-INF/fabric8/webpages.info.magnolia.k8s.javaoperatorsdk-v1.yml
- Install the generated CRD

```bash
kubectl apply -f target/classes/META-INF/fabric8/webpages.info.magnolia.k8s.javaoperatorsdk-v1.yml
...
customresourcedefinition.apiextensions.k8s.io/webpages.info.magnolia.k8s.javaoperatorsdk created
```

- Run the project with the runner class
- Deploy the webpage static file after running the operator project

```bash
kubectl apply -f k8s/webpage.yaml
```

### Further Details

Please, check the how-to file for details and references
Open how-to.md



