# Overview
Tron game development as part of com3014 group coursework

## Development Environment
* Java 1.8
* Maven 3.2.5
* bower 1.4.1
* git 2.2.0

## Install web components
* ```bower install``` to install web components

## Configure database
Create ```database.properties``` file with the following codes in the ```resources/META-INF/resources/properties``` folder.

    # YOUR MYSQL URL (following is the default so will probably work for everyone)
    spring.datasource.url= jdbc:mysql://localhost:3306/trondb
    spring.datasource.username=root
    #YOUR DATABASE ROOT PASSWORD
    spring.datasource.password=root
    
    # Keep the connection alive if idle for a long time (needed in production)
    spring.datasource.testWhileIdle = true
    spring.datasource.validationQuery = SELECT 1
    
    # Show or not log for each sql query
    spring.jpa.show-sql = false
    
    #create-drop: Recreate database everytime application is run. For production it is better to use: validate
    spring.jpa.hibernate.ddl-auto=create-drop
    
    # Naming strategy
    spring.jpa.hibernate.naming-strategy = org.hibernate.cfg.ImprovedNamingStrategy
    
    # The SQL dialect makes Hibernate generate better SQL for the chosen database
    spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL5Dialect

## Compile SASS files
* [SASS compiler plugin](http://www.geodienstencentrum.nl/sass-maven-plugin/plugin-info.html) has already been in the ```pom.xml``` file. So, SASS files should be compiled automatically during maven build. Please check the given link for more details and dependencies.
* In order to enable auto compilation whenever you have updated SASS files, just run ```mvn sass:watch``` from terminal or from your favourite IDE.

## Run application
* ```mvn clean spring-boot:run``` to compile and run the application
* go to ```//localhost:8080/```
* MySQL runs on: localhost:3306, with username/password: root/root

## Default jsp view file using templates
    <!DOCTYPE html>
    <html class="no-js" lang="en">
        <head>
            <!-- Include all necessary tags in the html head tag -->
            <%@include file="../template/head.jsp" %>
            <!-- Add any extra css files here -->
        </head>
        <!-- Include browserhappy tag to show warning for very old browser -->
        <%@include file="../template/browserupgrade.jsp"%>
        <body>
            <!-- Add your site or application content here -->
            
            <!-- Include all necessary javascript library files -->
            <%@include file="../template/scripts.jsp"%>
            <!-- Add any extra javascript files here -->
        </body>
    </html>

## ERD
![Entity Relationship Diagram](https://agthumoe@bitbucket.org/com3014/documentations.git/raw/master/images/ERD.png)

## Deployment notes
Need to redirect port 8080 to port 80
    iptables --insert INPUT --protocol tcp --dport 80 --jump ACCEPT
    iptables --insert INPUT --protocol tcp --dport 8080 --jump ACCEPT
    iptables --table nat --append PREROUTING --in-interface eth0 --protocol tcp --dport 80 --jump REDIRECT --to-port 8080
