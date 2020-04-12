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
