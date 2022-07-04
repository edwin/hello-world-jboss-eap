# Hello World using Servlet and JNDI on JBoss EAP in Openshift 4

A simple test project for showing a hello world app, by using a customized database acess thru DataSource and accessible from application by using JNDI configuration.

Using `registry.redhat.io/jboss-eap-7/eap74-openjdk11-openshift-rhel8` as base image, and a custom `standalone-openshift.xml` for a much more granular and flexible configuration.

## Database Configuration
```xml
<subsystem xmlns="urn:jboss:domain:datasources:6.0">
    <datasources>
        <datasource jndi-name="java:/datasources/MysqlDS" pool-name="MysqlDS" enabled="true">
            <connection-url>${env.MYSQL_URL}</connection-url>
            <driver>mysql-connector-java-8.0.20.jar</driver>
            <pool>
                <min-pool-size>5</min-pool-size>
                <max-pool-size>20</max-pool-size>
            </pool>
            <security>
                <user-name>${env.MYSQL_USERNAME}</user-name>
                <password>${env.MYSQL_PASSWORD}</password>
            </security>
            <validation>
                <valid-connection-checker class-name="org.jboss.jca.adapters.jdbc.extensions.mysql.MySQLValidConnectionChecker"/>
                <validate-on-match>true</validate-on-match>
                <background-validation>false</background-validation>
                <exception-sorter class-name="org.jboss.jca.adapters.jdbc.extensions.mysql.MySQLExceptionSorter"/>
            </validation>
        </datasource>
        <datasource jndi-name="java:/datasources/PostgresqlDS" pool-name="PostgresqlDS" enabled="true">
            <connection-url>${env.PGSQL_URL}</connection-url>
            <driver>postgresql-42.2.12.jar</driver>
            <pool>
                <min-pool-size>5</min-pool-size>
                <max-pool-size>20</max-pool-size>
            </pool>
            <security>
                <user-name>${env.PGSQL_USERNAME}</user-name>
                <password>${env.PGSQL_PASSWORD}</password>
            </security>
        </datasource>
    </datasources>
</subsystem>
```

## Java Code
```java
dataSource = (DataSource) new InitialContext().lookup("java:/datasources/MysqlDS");
connection = dataSource.getConnection();

statement = connection.prepareStatement("select * from this_table");
```

## Database Drivers
Put all the drivers required on `./drivers/` folder.

## How to Build
```
$ docker build -t hello-world-jboss-eap .
```

## How to Run
```
$ docker run -p 8080:8080 --add-host=HOST:192.168.56.1 -e MYSQL_USERNAME=root -e MYSQL_PASSWORD=password \
    -e MYSQL_URL=jdbc:mysql://192.168.56.1:3306/db_test -e PGSQL_USERNAME=postgres -e PGSQL_PASSWORD=postgres \
    -e  PGSQL_URL=jdbc:postgresql://192.168.56.1:5432/dbtraining \ 
    hello-world-jboss-eap
```

## How to Test
For postgresql connection,
```
$ curl -kv http://localhost:8080/app01/postgresql
```

For mysql connection,
```
$ curl -kv http://localhost:8080/app01/mysql
```