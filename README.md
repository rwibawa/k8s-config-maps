# k8s-config-maps
The project started with:
* [Optimus Prime](/optimus-prime/README.md)

## 1. Commands:
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

# 2. Change config maps:
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

## 3. Rolling update with config maps v2:
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

## 4. Troubleshoot helm charts:
```bash
$ helm init
$HELM_HOME has been configured at /home/rwibawa/.helm.

Tiller (the Helm server-side component) has been installed into your Kubernetes Cluster.

Please note: by default, Tiller is deployed with an insecure 'allow unauthenticated users' policy.
To prevent this, run `helm init` with the --tiller-tls-verify flag.
For more information on securing your installation see: https://docs.helm.sh/using_helm/#securing-your-helm-installation
Happy Helming!

$ helm install --name=transformers1 ./transformers/
Error: could not find a ready tiller pod

$ kubectl -n kube-system get po
NAME                                    READY     STATUS    RESTARTS   AGE
tiller-deploy-74d5d98d7d-9bwfx          1/1       Running   0          2m

$ kubectl logs --namespace kube-system tiller-deploy-74d5d98d7d-9bwfx
```

## 5. Helm Charts:
```bash
$ cd helm
$ helm install --name=transformers1 ./transformers/
NAME:   transformers1
LAST DEPLOYED: Sun Dec 16 19:21:34 2018
NAMESPACE: default
STATUS: DEPLOYED

RESOURCES:
==> v1/Deployment
NAME                        DESIRED  CURRENT  UP-TO-DATE  AVAILABLE  AGE
transformers1-optimusprime  2        0        0           0          1s

==> v1/Pod(related)
NAME                                         READY  STATUS   RESTARTS  AGE
transformers1-optimusprime-65454d4577-nfdqh  0/1    Pending  0         0s
transformers1-optimusprime-65454d4577-wvvq2  0/1    Pending  0         0s

==> v1/ConfigMap
NAME                              DATA  AGE
transformers1-autobots-config     1     1s
transformers1-decepticons-config  1     1s

==> v1/Service
NAME                        TYPE      CLUSTER-IP    EXTERNAL-IP  PORT(S)          AGE
transformers1-optimusprime  NodePort  10.99.136.25  <none>       30080:30142/TCP  1s


$ kubectl get pods
NAME                                          READY     STATUS    RESTARTS   AGE
transformers1-optimusprime-65454d4577-nfdqh   1/1       Running   0          2m
transformers1-optimusprime-65454d4577-wvvq2   1/1       Running   0          2m
$ kubectl get services
NAME                         TYPE           CLUSTER-IP     EXTERNAL-IP   PORT(S)           AGE
transformers1-optimusprime   NodePort       10.99.136.25   <none>        30080:30142/TCP   2m

19:28 $ helm status transformers1
LAST DEPLOYED: Sun Dec 16 19:21:34 2018
NAMESPACE: default
STATUS: DEPLOYED

RESOURCES:
==> v1/ConfigMap
NAME                              DATA  AGE
transformers1-autobots-config     1     7m36s
transformers1-decepticons-config  1     7m36s

==> v1/Service
NAME                        TYPE      CLUSTER-IP    EXTERNAL-IP  PORT(S)          AGE
transformers1-optimusprime  NodePort  10.99.136.25  <none>       30080:30142/TCP  7m36s

==> v1/Deployment
NAME                        DESIRED  CURRENT  UP-TO-DATE  AVAILABLE  AGE
transformers1-optimusprime  2        2        2           2          7m36s

==> v1/Pod(related)
NAME                                         READY  STATUS   RESTARTS  AGE
transformers1-optimusprime-65454d4577-nfdqh  1/1    Running  0         7m35s
transformers1-optimusprime-65454d4577-wvvq2  1/1    Running  0         7m35s


$ $ helm del --purge transformers1
release "transformers1" deleted
```

### All charts:
```bash
$ helm install --name=transformers1 ./transformers/
NAME:   transformers1
LAST DEPLOYED: Sun Dec 16 19:42:11 2018
NAMESPACE: default
STATUS: DEPLOYED

RESOURCES:
==> v1/ConfigMap
NAME                              DATA  AGE
transformers1-autobots-config     1     7s
transformers1-decepticons-config  1     7s

==> v1/Service
NAME                        TYPE      CLUSTER-IP     EXTERNAL-IP  PORT(S)          AGE
transformers1-gears         NodePort  10.98.23.64    <none>       30081:31152/TCP  7s
transformers1-megatron      NodePort  10.102.152.48  <none>       30082:32127/TCP  6s
transformers1-optimusprime  NodePort  10.111.85.187  <none>       30080:32435/TCP  6s
transformers1-shockwave     NodePort  10.104.168.64  <none>       30082:30866/TCP  6s

==> v1/Deployment
NAME                        DESIRED  CURRENT  UP-TO-DATE  AVAILABLE  AGE
transformers1-gears         2        2        2           0          6s
transformers1-megatron      2        2        2           0          6s
transformers1-optimusprime  2        2        2           0          6s
transformers1-shockwave     2        2        2           0          6s

==> v1/Pod(related)
NAME                                         READY  STATUS             RESTARTS  AGE
transformers1-gears-85c4cf8b9d-8vxdl         0/1    Pending            0         5s
transformers1-gears-85c4cf8b9d-n9mzm         0/1    ContainerCreating  0         5s
transformers1-megatron-58576f6d9f-7gwgr      0/1    ContainerCreating  0         5s
transformers1-megatron-58576f6d9f-hwmmg      0/1    Pending            0         5s
transformers1-optimusprime-65454d4577-875zf  0/1    Pending            0         5s
transformers1-optimusprime-65454d4577-w8zzb  0/1    ContainerCreating  0         5s
transformers1-shockwave-86d8985969-55rq4     0/1    Pending            0         5s
transformers1-shockwave-86d8985969-q9w6g     0/1    ContainerCreating  0         5s


$ kubectl port-forward svc/transformers1-megatron 30082:30082 &
Forwarding from 127.0.0.1:30082 -> 8080
Forwarding from [::1]:30082 -> 8080
$ kubectl port-forward svc/transformers1-gears 30081:30081 &
Forwarding from 127.0.0.1:30081 -> 8080
Forwarding from [::1]:30081 -> 8080
```

## Upgrade helm charts:
```bash
$ helm upgrade transformers1 --recreate-pods --set autobots.mode=disguised,decepticons.mode=robot ./transformers/
Release "transformers1" has been upgraded. Happy Helming!
LAST DEPLOYED: Sun Dec 16 19:58:43 2018
NAMESPACE: default
STATUS: DEPLOYED

RESOURCES:
==> v1/ConfigMap
NAME                              DATA  AGE
transformers1-autobots-config     1     16m
transformers1-decepticons-config  1     16m

==> v1/Service
NAME                        TYPE      CLUSTER-IP     EXTERNAL-IP  PORT(S)          AGE
transformers1-gears         NodePort  10.98.23.64    <none>       30082:30873/TCP  16m
transformers1-megatron      NodePort  10.102.152.48  <none>       30081:30429/TCP  16m
transformers1-optimusprime  NodePort  10.111.85.187  <none>       30080:32435/TCP  16m
transformers1-shockwave     NodePort  10.104.168.64  <none>       30083:31668/TCP  16m

==> v1/Deployment
NAME                        DESIRED  CURRENT  UP-TO-DATE  AVAILABLE  AGE
transformers1-gears         2        2        2           0          16m
transformers1-megatron      2        2        2           0          16m
transformers1-optimusprime  2        2        2           0          16m
transformers1-shockwave     2        2        2           0          16m

==> v1/Pod(related)
NAME                                         READY  STATUS             RESTARTS  AGE
transformers1-gears-85c4cf8b9d-8vxdl         1/1    Terminating        0         16m
transformers1-gears-85c4cf8b9d-kfnlt         0/1    ContainerCreating  0         15s
transformers1-gears-85c4cf8b9d-n9mzm         1/1    Terminating        0         16m
transformers1-gears-85c4cf8b9d-sdqhk         0/1    ContainerCreating  0         15s
transformers1-megatron-58576f6d9f-7gwgr      0/1    Terminating        0         16m
transformers1-megatron-58576f6d9f-g5mxg      0/1    ContainerCreating  0         14s
transformers1-megatron-58576f6d9f-hwmmg      1/1    Terminating        0         16m
transformers1-megatron-58576f6d9f-p2zkk      0/1    ContainerCreating  0         13s
transformers1-optimusprime-65454d4577-875zf  1/1    Terminating        0         16m
transformers1-optimusprime-65454d4577-8jvr9  0/1    ContainerCreating  0         10s
transformers1-optimusprime-65454d4577-bm9gh  0/1    ContainerCreating  0         11s
transformers1-optimusprime-65454d4577-w8zzb  1/1    Terminating        0         16m
transformers1-shockwave-86d8985969-4krfk     0/1    ContainerCreating  0         7s
transformers1-shockwave-86d8985969-55rq4     1/1    Terminating        0         16m
transformers1-shockwave-86d8985969-nrnwc     0/1    ContainerCreating  0         4s
transformers1-shockwave-86d8985969-q9w6g     1/1    Terminating        0         16m

```

## 6. Config Map from `application.properties` file:
```bash
$ cd k8s-config-maps/autobots-k8s-configs/
$ kubectl create configmap autobots-config --from-file=./application.properties
$ kubectl get cm
$ kubectl create -f optimus-prime2.yml
```