# mvn-outdated
This is maven plugin for checking outdated dependencies.

# How to execute
1. Add dependency in your pom.xml
```xml
<plugins>
    <plugin>
        <groupId>dev.i-whammy</groupId>
        <artifactId>outdated-maven-plugin</artifactId>
        <version>0.0.1</version>
    </plugin>
</plugins>

```

2. Execute following
```sh
$ mvn outdated:outdated
```
