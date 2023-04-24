# kemono-dl

Recommend java version: **Java 17+**

## settings

### proxy server

use jvm options `-Dhttps.proxyHost=<host> -Dhttps.proxyPort=<port>`

Aria2 will use the same proxy server

Note: if you use the docker image build from `Dockerfile`, please set 

```JAVA_OPTS="-Dhttps.proxyHost=<host> -Dhttps.proxyPort=<port>"```

environment variable for your container.


### Configuration file

configuration file is <workdir/config.xml>

sample:

```xml
<?xml version="1.0" encoding="utf-8" ?>
<config>
    <session>THE SESSION FIELD IN YOUR COOKIE</session>
    <aria2>
        <host>localhost</host>
        <port>6800</port>
    </aria2>

    <download>
        <favourite>
            <enabled>true</enabled>
        </favourite>
        <users>
            <user site="SERVICE" id="USERID"/>
        </users>
    </download>
</config>
```
