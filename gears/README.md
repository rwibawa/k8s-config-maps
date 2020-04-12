# Gears

## 1. `kubernetes-maven-plugin`
[https://www.eclipse.org/jkube/docs/kubernetes-maven-plugin](https://www.eclipse.org/jkube/docs/kubernetes-maven-plugin)

```xml
<plugin>
    <groupId>org.eclipse.jkube</groupId>
    <artifactId>kubernetes-maven-plugin</artifactId>
    <version>1.0.0-alpha-1</version>
    <configuration>
        <generateRoute>false</generateRoute>
    </configuration>
</plugin>
```

## 2. `fabric8-maven-plugin`
[http://maven.fabric8.io/](http://maven.fabric8.io/)

### `settings.xml`
```xml
<settings>
      ...

      <pluginGroups>
        <pluginGroup>io.fabric8</pluginGroup>
      </pluginGroups>

      ...
</settings>
```

### `pom.xml`
```xml
<plugin>
  <groupId>io.fabric8</groupId>
  <artifactId>fabric8-maven-plugin</artifactId>
  <version>4.4.1</version>

  <configuration>
     ....
     <images>
        <!-- A single's image configuration -->
        <image>
          ...
          <build>
           ....
          </build>
        </image>
        ....
     </images>
  </configuration>

  <!-- Connect fabric8:resource, fabric8:build and fabric8:helm to lifecycle phases -->
  <executions>
    <execution>
       <id>fabric8</id>
       <goals>
         <goal>resource</goal>
         <goal>build</goal>
         <goal>helm</goal>
       </goals>
    </execution>
  </executions>
</plugin>
```

### Maven command line (in `hela` - microk8s):
```sh
# Deploy
$ ../mvnw clean fabric8:deploy \
-Dkubernetes.auth.tryKubeConfig=true -Dkubeconfig=~/.kube/config-microk8s:~/.kube/config-microk8s-dev \
-Dkubernetes.master=https://127.0.0.1:16443 \
-Dkubernetes.auth.basic.username=admin -Dkubernetes.auth.basic.password=<password> \
-Dmaven.test.skip=true -Dmaven.repo.local=../.m2/repository -s ../settings.xml

# Undeploy
$ ../mvnw fabric8:undeploy \
-Dkubernetes.auth.tryKubeConfig=true -Dkubeconfig=~/.kube/config-microk8s:~/.kube/config-microk8s-dev \
-Dkubernetes.master=https://127.0.0.1:16443 \
-Dkubernetes.auth.basic.username=admin -Dkubernetes.auth.basic.password=<password> \
-Dmaven.test.skip=true -Dmaven.repo.local=../.m2/repository -s ../settings.xml

# Create helm charts:
$ ../mvnw fabric8:resource fabric8:helm \
-Dkubernetes.auth.tryKubeConfig=true -Dkubeconfig=~/.kube/config-microk8s:~/.kube/config-microk8s-dev \
-Dkubernetes.master=https://127.0.0.1:16443 \
-Dkubernetes.auth.basic.username=admin -Dkubernetes.auth.basic.password=<password> \
-Dmaven.test.skip=true -Dmaven.repo.local=../.m2/repository -s ../settings.xml

$ ll target/fabric8/helm/kubernetes/gears/
$ helm install target/fabric8/helm/kubernetes/gears
```

## 3. helm charts
```sh
$ helm create gears
$ helm lint gears

$ helm install ./gears
NAME:   gangly-starfish
LAST DEPLOYED: Sun Apr 12 01:19:13 2020
NAMESPACE: rw-dev
STATUS: DEPLOYED

RESOURCES:
==> v1/Deployment
NAME                   AGE
gangly-starfish-gears  0s

==> v1/Pod(related)
NAME                                    AGE
gangly-starfish-gears-5c75b855f9-sbpjt  0s

==> v1/Service
NAME                   AGE
gangly-starfish-gears  0s


NOTES:
1. Get the application URL by running these commands:
  export POD_NAME=$(kubectl get pods --namespace rw-dev -l "app.kubernetes.io/name=gears,app.kubernetes.io/instance=gangly-starfish" -o jsonpath="{.items[0].metadata.name}")
  echo "Visit http://127.0.0.1:8080 to use your application"
  kubectl port-forward $POD_NAME 8080:80

$ kubectl port-forward svc/gangly-starfish-gears 28080:8080 &

$ helm ls
NAME                    REVISION        UPDATED                         STATUS          CHART                   APP VERSION     NAMESPACE
gangly-starfish         1               Sun Apr 12 01:19:13 2020        DEPLOYED        gears-0.1.0             1.0             rw-dev   

$ helm delete --purge gangly-starfish
```

## 4. GitHub docker registry
[Configuring Docker for use with GitHub Packages](https://help.github.com/en/packages/using-github-packages-with-your-projects-ecosystem/configuring-docker-for-use-with-github-packages)
```shell script
# authenticate
$ cat ~/github-token_hela-docker.txt | docker login docker.pkg.github.com -u rwibawa --password-stdin
$ docker tag rwibawa/gears:1.0 docker.pkg.github.com/rwibawa/k8s-config-maps/gears:1.0
$ docker push docker.pkg.github.com/rwibawa/k8s-config-maps/gears:1.0
```

## 5. Maven docker registry
[Configuring Apache Maven for use with GitHub Packages](https://help.github.com/en/packages/using-github-packages-with-your-projects-ecosystem/configuring-apache-maven-for-use-with-github-packages)
* [settings.xml](../settings.xml)
* [pom.xml](./pom.xml)
```shell script
$ mvn deploy
```