#!/usr/bin/env bash
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

GROUP_ID="info.magnolia.k8s"
PROJECT_ID="quarkus-operator-demo"

while true; do
  echo "--------------------------------------------------------------"
  echo " Quarkus operator setup"
  echo "--------------------------------------------------------------"
  echo "Choose what do you wanna do:"
  echo "   0 - Install quarkus CLI"
  echo "   1 - Install local docker registry"
  echo "   2 - Setup project & organization name"
  echo "   3 - Generate quarkus operator project"
  echo "   4 - Project dev"
  echo "   5 - Compile the project"
  echo "   6 - Generate docker image and publish locally"
  echo "   7 - deploy to current kubernetes cluster"
  echo "   n - none"
  echo "--------------------------------------------------------------"

  read -p "Which option do you wanna trigger? " userOption
  case $userOption in
    0 )
      echo "-->   0 - Install quarkus CLI"
      brew install quarkusio/tap/quarkus
      quarkus --version
      ;;
    1 )
      echo "-->   1 - Install local docker registry"
      docker run -d -p 5001:5000 --name registry registry:latest
      docker logs -f registry
      ;;
    2 )
      echo "-->   2 - Setup project & organization name"
      ;;
    3 )
      echo "-->   3 - Generate quarkus operator project"
      quarkus create app info.magnolia:qosdk-test-olm -x='qosdk,olm'
      ;;
    4 )
      echo "-->   4 - Project dev"
      quarkus dev
      ;;
    5 )
      echo "-->   5 - Compile the project"
      mvn clean package -Dquarkus.container-image.build=true -Dquarkus.container-image.registry=localhost:5001 -Dquarkus.container-image.group='' -Dquarkus.container-image.name='message-broker-operator-local' -Dquarkus.container-image.tag=latest
      ;;
    6 )
      echo "-->   6 - Generate docker image and publish locally"
      docker build -t message-broker-operator-local -f ./src/main/docker/Dockerfile.jvm .
      docker tag message-broker-operator-local:latest localhost:5001/message-broker-operator-local:latest
      docker push localhost:5001/message-broker-operator-local:latest
      ;;
    7 )
      echo "-->   7 - deploy to current kubernetes cluster"
      kubectl apply -f target/kubernetes/kubernetes.yml
      ;;
    [Nn]* )
      echo "Exiting..."
      break ;;
    * )
      echo "Invalid option. Please choose a valid option." ;;
  esac
done

echo "Rabbit cluster operator CLI finished"
