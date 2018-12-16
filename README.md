# k8s-config-maps
The project started with:
* [Optimus Prime](/optimus-prime/README.md)

## Commands:
```bash
# Autobots
$ kubectl create -f autobots-config.yml --save-config
configmap/autobots-config created
$ kubectl apply -f optimus-prime.yml
deployment.apps/optimus-prime created
service/optimus-prime-entrypoint created

# Decepticons
$ kubectl create -f ../decepticons-k8s-configs/decepticons-config.yml --save-config
$ kubectl apply -f ../decepticons-k8s-configs/megatron-prime.yml
deployment.apps/megatron created
service/megatron-entrypoint created
$ kubectl get pods
NAME                              READY     STATUS    RESTARTS   AGE
megatron-766478b685-6wnxb         0/1       Running   0          11s
megatron-766478b685-k4fnr         0/1       Running   0          11s
$ kubectl port-forward pod/megatron-766478b685-6wnxb :8080 &
Forwarding from 127.0.0.1:53899 -> 8080
Forwarding from [::1]:53899 -> 8080
$ kubectl delete -f ../decepticons-k8s-configs/megatron-prime.yml
deployment.apps "megatron" deleted
service "megatron-entrypoint" deleted
```