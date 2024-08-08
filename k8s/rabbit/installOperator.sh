#!/usr/bin/env bash
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

while true; do
  echo "--------------------------------------------------------------"
  echo " RabbitMQ cluster setup"
  echo "--------------------------------------------------------------"
  echo "Choose what do you wanna do:"
  echo "   1 - Install official RabbitMq Operators (cluster-operator, messaging-topology-operator)"
  echo "   2 - Deploy RabbitMQ cluster"
  echo "   3 - Check rabbitmq cluster status"
  echo "   4 - Execute installation for custom resource on rabbitmqCluster with messaging-topology-operator"
  echo "   5 - Get kubectl system status"
  echo "   6 - List pod 0 queues"
  echo "   7 - Inspect topology operator"
  echo "   8 - Forward rabbit dashboard to localhost"
#  echo "   9 - install basic exchange and queue for demonstration"
  echo "   n - none"
  echo "--------------------------------------------------------------"

  read -p "Which option do you wanna trigger? " userOption
  case $userOption in
    1 )
      echo "> Installing rabbitmq cluster operator"
      kubectl apply -f https://github.com/rabbitmq/cluster-operator/releases/latest/download/cluster-operator.yml
#      echo "> Installing helm "
#      curl -fsSL -o get_helm.sh https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3
#      chmod 700 get_helm.sh
#      ./get_helm.sh
      echo "> Installing certmanager and topology operators"
      kubectl apply -f https://github.com/jetstack/cert-manager/releases/download/v1.3.1/cert-manager.yaml
      echo "> Disable resource validation on the cert-manager namespace for local development"
      kubectl label namespace cert-manager certmanager.k8s.io/disable-validation=true
      kubectl apply -f https://github.com/rabbitmq/messaging-topology-operator/releases/latest/download/messaging-topology-operator-with-certmanager.yaml
      echo " -------------------------------- Operators installed --------------------------------" ;;
    2 )
      echo "> Installing CRD descriptor for rabbitmq cluster "
      kubectl apply -f ./rabbitmqCluster.yaml ;;
    3 )
      echo "> Checking current system status for rabbitmq cluster"
      kubectl -n rabbitmq-system get all ;;
    4 )
      echo "> Execute queue on rabbitmqCluster with messaging-topology-operator"
      echo "> Creating basic exchange and queue from topologies folder"
      kubectl apply -f ./topologies/exchange.yaml
#      kubectl apply -f ./topologies/policy.yaml
      kubectl apply -f ./topologies/queue.yaml
      kubectl apply -f ./topologies/binding.yaml ;;
    5 )
      kubectl get all ;;
    6 )
      kubectl exec -it pod/rabbitmq-cluster-server-0   -- /bin/bash -c "rabbitmqctl list_queues" ;;
    7 )
      #    https://www.rabbitmq.com/kubernetes/operator/troubleshooting-topology-operator
      kubectl -n rabbitmq-system logs -l app.kubernetes.io/name=messaging-topology-operator ;;
    8 )
      echo "> visit http://localhost:9090 with these credentials"
      username="$(kubectl get secret rabbitmq-cluster-default-user -o jsonpath='{.data.username}' | base64 --decode)"
      echo "> username: $username"
      password="$(kubectl get secret rabbitmq-cluster-default-user -o jsonpath='{.data.password}' | base64 --decode)"
      echo "> password: $password"
      kubectl port-forward pod/rabbitmq-cluster-server-0 9090:15672 ;;
    [Nn]* )
      echo "Exiting..."
      break ;;
    * )
      echo "Invalid option. Please choose a valid option." ;;
  esac
done

echo "Rabbit cluster operator CLI finished"
