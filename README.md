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
$ kubectl apply -f ./autobots-k8s-configs/gears.yml
$ kubectl get pods -l serviceType=gears -w

# Decepticons
$ kubectl create -f ./decepticons-k8s-configs/decepticons-config.yml --save-config
$ kubectl apply -f ./decepticons-k8s-configs/megatron.yml
$ kubectl apply -f ../decepticons-k8s-configs/shockwave.yml
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

# Change config maps:
![Change config autobots config maps](data/ChangeConfig1.jpg)
```bash
$ kubectl scale deployment/optimus-prime --replicas=0
deployment.extensions/optimus-prime scaled
15:40 $ kubectl get pods -l serviceType=optimus-prime
No resources found.
15:40 $ kubectl scale deployment/optimus-prime --replicas=3
deployment.extensions/optimus-prime scaled
15:40 $ kubectl get pods -l serviceType=optimus-prime -w
NAME                             READY     STATUS              RESTARTS   AGE
optimus-prime-7989f696bf-2n5wg   0/1       ContainerCreating   0          5s
optimus-prime-7989f696bf-plp69   0/1       ContainerCreating   0          5s
optimus-prime-7989f696bf-vxvbw   0/1       ContainerCreating   0          5s
optimus-prime-7989f696bf-vxvbw   0/1       Running   0          26s
optimus-prime-7989f696bf-plp69   1/1       Running   0         49s
optimus-prime-7989f696bf-2n5wg   1/1       Running   0         53s
```