# WebMonitoringTool


This application is designed to monitor the state of other applications and websites online. It is a demo version and it can be extended to larger capabilities.
Currently, it aggregates data about the HTTP requests that it makes periodically and evaluates the responses according to user specifications. 
It can be used as a convenient way of keeping track of online projects or websites of user's interest. 


# Technologies

- Spring
- Hibernate
- Jpa, H2 embedded database
- Maven
- WebSockets, SockJS


# Application Structure

- WebSocket API
- Database Manager
- Executor Manager

All three components are written on Java and communicate with each other over easy to use and straightforward interfaces. The application design is easily expandable and can be grown to advanced capabilities. Embedded database H2 for storing the data in a local file, makes application mobile and reliable, while multithreaded monitoring process has a potential for large amounts of data to be processed. WebSockets provide highly responsive user interface and instant display of data produced from monitoring.


# Application User Guide

On application start, server opens endpoints for client-server communication. Clinet sends message right after loading the page and sets up a Web-Socket connection. After Connection is recieved and confirmed, client sends request to the server about the data stored in database and displays the result received in two tables. The one above displays all the monitoring results stored in Database, the other one below lists the websites with parameters, that were chosen by user that added them to monitoring. 

User has just two main buttons on a page and one for each website. 

The top button, 'Start Monitoring', turns on monitoring process on all websites specified previously, when monitoring is off, and turns it off ('Stop Monitoring'), when the process is running.

On the bottom of the page there is a short form with the Second button, that is dedicated to add a new website to the platform system for monitoring. User should specify a few parameters, like 'name', 'url', expected 'time of response', 'size', desired 'frequency of monitoring' and it is ready to go for tracking.

Also, each of the rows on the lower table contains button to remove website from monitoring, in the column with the label 'Delete'.


# Deployment 


If you already have the source code of the project, open command prompt and navigate to the project's root folder. From there, type the following command - 'sudo jar -cvf WebAgentApp.war *'. This should have created a .war file that you could copy to your Linux server in folder ./webapps and open via 'http://localhost:8080/WebAgentApp/'.

Also, you could find a good instructions on how to deploy a .war file on the following link: https://documentation.magnolia-cms.com/display/DOCS60/Deploying+a+WAR+on+Apache+Tomcat#DeployingaWARonApacheTomcat-DeployWAR.



Certainly, there are things to improve on optimisation level, given more time, functionality can be extended, implementation of some components can be made more secure and safe. For now, it can process multiple websites at once and print monitoring results in a nicely designed data tables.

Hope you'll find it interesting. Feel free to let me know what you think about it!
Thanks for your interest.
