#!/usr/bin/env bash

kubectl get --raw /openapi/v2 > api-spec.json
openapi-generator generate -i ./api-spec.json -g java -o ./kubernetes-cluster-generated