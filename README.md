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
Create ```database.properties``` files with the following code in the ```resources/META-INF/resources/properties``` folder.

    # YOUR MYSQL URL (following is the default so will probably work for everyone)
    spring.datasource.url= jdbc:mysql://localhost:3306/trondb
    spring.datasource.username=root
    #YOUR DATABASE ROOT PASSWORD
    spring.datasource.password=root
    
    # Keep the connection alive if idle for a long time (needed in production)
    spring.datasource.testWhileIdle = true
    spring.datasource.validationQuery = SELECT 1
    
    # Show or not log for each sql query
    spring.jpa.show-sql = true
    
    #create-drop: Recreate database everytime application is run. For production it is better to use: validate
    spring.jpa.hibernate.ddl-auto=create-drop
    
    # Naming strategy
    spring.jpa.hibernate.naming-strategy = org.hibernate.cfg.ImprovedNamingStrategy
    
    # The SQL dialect makes Hibernate generate better SQL for the chosen database
    spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL5Dialect

## Run application
* ```mvn clean spring-boot:run``` to compile and run the application
* go to ```//localhost:8080/```
* MySQL runs on: localhost:3306, with username/password: root/root
