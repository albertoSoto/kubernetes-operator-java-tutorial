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
  echo "   n - none"
  echo "--------------------------------------------------------------"

  read -p "Which option do you wanna trigger? " userOption
  case $userOption in
    1 )
      kubectl apply -f https://github.com/rabbitmq/cluster-operator/releases/latest/download/cluster-operator.yml
      kubectl apply -f https://github.com/rabbitmq/messaging-topology-operator/releases/latest/download/messaging-topology-operator-with-certmanager.yaml
      echo " -------------------------------- Operators installed --------------------------------" ;;
    2 )
      echo " > Installing CRD descriptor for rabbitmq cluster "
      kubectl apply -f ./rabbitmqCluster.yaml ;;
    3 )
      echo " > Checking current system status for rabbitmq cluster"
      kubectl -n rabbitmq-system get all ;;
    4 )
      echo " > Execute installation for custom resource on rabbitmqCluster with messaging-topology-operator"
      # Add your commands for option 4 here
      ;;
    5 )
      kubectl get all ;;
    [Nn]* )
      echo "Exiting..."
      break ;;
    * )
      echo "Invalid option. Please choose a valid option." ;;
  esac
done

echo "Finishing"
