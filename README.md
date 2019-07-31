# ProfiNet - Software System for Searching and Offering Professional Services
[![N|Solid](https://i.imgur.com/dK5fi9J.jpg)](https://nodesource.com/products/nsolid)

## Abstract
ProfiNet is a web platform for searching and offering services. Its users are  experts in different areas (e.g. carpenters, bakers, tailors, etc.), and people in need of a professional service.
The aim of the project is to create a channel between professionals and their future clients, which also makes getting in contact and following the workflow easier. It also protects the data of the ones offering the services, and optimizes the search results.
Some of the platforms main functionalities are: giving the users the possibility to list, filter and search among offers, contact professionals, make appointments and also rate their work. Users can also create expert profiles, post their own announcements and filter the contact and appointment requests. The experts’ offers are ranked based on previous feedbacks.

## Introduction
The purpose of the software is to help users find trustworthy specialists, to communicate with them, to find services and to make use of them. Furthermore, it provides a platform for the experts to advertise their services for potential clients. To illustrate the functioning of the system, a concrete example of an (unfortunately) frequent problem can be considered: a water pipe breaks. Our first
thought in these situations: a plumber is needed. However, it is not certain that it will be easy to find fast the perfect specialist, especially if we are, for example, new in the city.
In this case we can try to ask friends or acquaintances for telephone numbers or contact information. We can try the Internet, but it’s not sure either, that we will find easily an expert, who is trustworthy, skilled in the right way, is close enough to us and speaks atleast one common language. ProfiNet could give a helping hand in this kind of situation.
The system’s users who are registered as experts are listed based on the feedbacks given by their previous clients. Their user profile contains their timetable, geographic location, and also, their spoken languages.
ProfiNet takes care of the privacy of the specialist, as well. The expert can decide if he wants to share his contact information with a user, he can communicate with his future clients using the platform, and he can set the time interval for a work appointment, preventing this way any misunderstandings. The project presented in the thesis has two main parts: a central server and a web client application. Using the web user interface, the system’s administrators can manage the data and review statistics about the system’s condition. The users can send contact requests for the registered experts, exchange messages with them, and become one of
them by completing the corresponding expert profile. The specialists are also users of the system, but they can manage client requests, as well.
The central server is implemented in Java, using the Spring framework. Themost important technology used for the development of the web user interface is AngularJS.

The tools and technologies used in the project are presented in the first two chapters. In the next part, the functioning and the implementation of the ProfiNet system is described. In the last part, further development possibilities and some final
conclusions are listed.
The ProfiNet project has been developed within the framework of the Codespring Mentoring program. The development team consisted of two members: Balog Csongor and Vass Lilla. The technical coordination was done by three mentors: Kandó Norbert, Kintzel Levente and dr. Simon Károly. Many thanks for their help, support and advices.
The ProfiNet project was presented at the Transylvanian Students’ Scientific
Conference.

## Server-side used technologies 
The server side of the ProfiNet system is built on the Spring framework (Spring Boot, Spring Data JPA, Spring Data Elasticsearch, Spring Security, Spring MVC REST), which provides solutions for all the functionalities needed by a complex web application. The
server uses a MySQL database.
### Example
The model contains the main entities represented as JavaBeans, organized in a class hierarchy; their common attributes are stored in superclasses. These model classes are JPA entities, their attributes are annotated with JPA annotations. The object-relational mapping is resolved by the persistence framework based on these annotations. For example, by the @Column annotation it is indicated the
database column corresponding to the annotated attribute. Not only plain attributes are mapped by these annotations, but also more complicated things, like relations between specific entities (@ManyToOne, @OneToMany, @ManyToMany).
The Hibernate JPA implementation is responsible for data persistence. JPA also provides and object-oriented query language, called JPQL (Java Persistence Query Language) for the implementation of more complex queries. Using Spring DATA – JPA, these queries have to be configured using the @Query annotation before the signature of the methods:
```sql
@Query("select expertField.field.name, (100.0 *
Count(expertField.field.id) / (Select Count(*) From ExpertField)) From
ExpertField expertField Group By expertField.field.name")
List<Object[]> getStatisticByCategories();
```

## Client-side web technologies
AngularJS is an open source MVW (Model-View-Whatever) framework developed by Google. It’s a JavaScript-based client-side technology for developing web applications. With the help of the framework, the client module can be divided into four parts:
model layer, view layer, controller layer and service layer. The HTML pages constitute the view layer. The pages are completed by special tags provided by AngularJS, used for specifying attributes. These attributes specify what kind of data appears, and how the interface responds to some events.

The service layer is responsible for the communication with the server. AngularJS gives an opportunity to write our own directives for manipulating DOM objects and for specifying their behavior. AngularJS also supports binding HTML sites to URLs. Within the HTML code
it is possible to define when to load sites bound to specific URLs. The web interface of ProfiNet is built with the help of the Bootstrap  framework. Bootstrap is a predefined toolkit, which gives an opportunity for designing the style and appearance of the web client applications. Besides the predefined CSS attributes, it also contains numerous JavaScript extensions. It was developed at Twitter, and since 2011 it has become an open source project.

## The most important requirements and functionalities
The ProfiNet system is composed by two major components: the server and the web user interface. The server is responsible for data persistence, for the implementation of the business logic and for the communication with the client side.
The functionalities of the system are published for the users through the web interface.

## Use cases
The system has four user roles: guests, registered users, experts and admins.
### Role hierarchy:
![N|Solid](https://i.imgur.com/HlBgFNq.png)
### Use case diagram for the Guest actor:
![N|Solid](https://i.imgur.com/jhMOulJ.png)
### Use case diagram for the User actor:
![N|Solid](https://i.imgur.com/f2bODIc.png)
### Use case diagram for the Expert actor:
![N|Solid](https://i.imgur.com/2qqk3ui.png)
### Use case diagram for the Admin actor:
![N|Solid](https://i.imgur.com/u8v4Dm6.png)

## The domain class diagram (without the constructors, getters and setters and overridden methods from the Object superclass):
The edu.codespring.profinet.domain package contains the ProfiNet server’s central entities, represented by JavaBeans.
The domain classes inherit from the BaseEntity class, which provides an ID corresponding to the primary key. It implements the java.io.Serializable interface.
![N|Solid](https://i.imgur.com/lVdZ5UV.png)
### ProfiNet’s system architecture – Client module:
![N|Solid](https://i.imgur.com/bs7WvFR.png)
### ProfiNet’s system architecture – Server module:
![N|Solid](https://i.imgur.com/BpNNcjw.png)

## Activity diagram in ProfiNet
The user sends a contact request to establish a connection with a chosen expert. The expert has two options: s/he can accept or deny this request. If everything goes well, and the expert has chosen to let the aforementioned user contact him/her, the user can send an appointment request, mentioning the details of the requested work. The decision is, again, taken by the expert. After they have agreed on an appointment, and the job is done, the user can rate the completed work.
![N|Solid](https://i.imgur.com/hbGTFDB.png)

## Case study
The ProfiNet application can be accessed from any web browser. Before authentication the user’s possibilities are limited: s/he can register or log in, s/he can see the offers marked on a map or in a list view, ordered based on previous reviews. The list
can be filtered by language, category and keywords. However, the user cannot communicate with the experts.
### The interface available for guests:
![N|Solid](https://i.imgur.com/8ulscDI.png)
### The offers appear on the front page in a list view:
![N|Solid](https://i.imgur.com/wBJBUkw.png)
### The menu expands after authentication:
![N|Solid](https://i.imgur.com/ycRWIhg.png)
### New options appear for the logged in users in the list view:
![N|Solid](https://i.imgur.com/JSFX2fH.png)
### The user can ask for an appointment
![N|Solid](https://i.imgur.com/gYxXMh7.png)
### The expert’s profile:
![N|Solid](https://i.imgur.com/BaEFZbH.png)

## Offers by categories
The system’s administrators are responsible for the revision and management of different entities (categories, keywords, languages, experts, appointments etc.). Moreover, they can track the server application’s status, the statistics related to the HTTP requests, and they can access the system logs. The administrator can also ban users and has an access to the statistics made about the behavior of the users.
![N|Solid](https://i.imgur.com/yPCfvzX.png)

## Conclusions and further development possibilities
In its current state ProfiNet is a prototype, the team was able to develop a demo version for the system. The software could help people in searching for services, and also provides possibility for experts to advertise their offers.
There are a lot of further development possibilities. The most important ideas which came up during the development process:
- Filtering based on distance between the specialists;
- Directions to the specialists;
- Developing client applications for other platforms (e.g. mobile: Android,
iPhone);
- Social media integration (Facebook, Google+, Twitter, Linkedin, etc.);
- Making the notification system more efficient;
- Switching the authentication mechanism to an OAuth-based one
