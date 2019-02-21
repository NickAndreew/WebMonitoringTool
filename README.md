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

Certainly, there are things to improve on optimisation level, given more time, implementation of some components can be made more secure and safe. For now, it can process multiple websites at once and print monitoring results in a nicely designed data tables.

Hope you'll find it usefull and interesting. Feel free to let me know what you think about it!
Thanks for your interest.
