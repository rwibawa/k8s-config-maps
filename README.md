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

## Rolling update with config maps v2:
Add `./decepticons-k8s-configs/decepticons-config-v2.yml`, and modify `megatron.yml` and `shockwave.yml` to use \
`decepticons-config-v2`
```bash
$ kubectl apply -f decepticons-k8s-configs --record
configmap/decepticons-config-v2 created
configmap/decepticons-config configured
deployment.apps/megatron configured
service/megatron-entrypoint configured
deployment.apps/shockwave configured
service/shockwave-entrypoint configured
$ kubectl get pods -l serviceType=megatron -w
NAME                        READY     STATUS    RESTARTS   AGE
megatron-766478b685-k4fnr   1/1       Running   0          2h
megatron-79f9d8dd8-8ck42    0/1       Running   0          26s
megatron-79f9d8dd8-qnsft    0/1       Running   0          26s
megatron-79f9d8dd8-qnsft   1/1       Running   0         59s
megatron-766478b685-k4fnr   1/1       Terminating   0         2h
megatron-766478b685-k4fnr   0/1       Terminating   0         2h
megatron-79f9d8dd8-8ck42   1/1       Running   0         1m
megatron-766478b685-k4fnr   0/1       Terminating   0         2h
megatron-766478b685-k4fnr   0/1       Terminating   0         2h
$ kubectl get pods -l serviceType=megatron
NAME                       READY     STATUS    RESTARTS   AGE
megatron-79f9d8dd8-8ck42   1/1       Running   0          2m
megatron-79f9d8dd8-qnsft   1/1       Running   0          2m
```