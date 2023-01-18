[![Maven Central](https://img.shields.io/maven-central/v/dev.i-whammy/outdated-maven-plugin?versionPrefix=0.1.0)](https://central.sonatype.dev/artifact/dev.i-whammy/outdated-maven-plugin/0.1.0)

# outdated-maven-plugin
This is maven plugin for checking outdated dependencies.

# How to execute
1. Add dependency in your pom.xml
```xml
<plugins>
    <plugin>
        <groupId>dev.i-whammy</groupId>
        <artifactId>outdated-maven-plugin</artifactId>
        <version>0.1.0</version>
    </plugin>
</plugins>

```

2. Execute following
```sh
$ mvn outdated:outdated
```
