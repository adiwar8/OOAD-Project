# Bug Tracker
<img align="left" src="https://img.shields.io/github/license/0l1v3rr/bug-tracker?color=red&style=flat">
<img align="left" src="https://img.shields.io/github/last-commit/0l1v3rr/bug-tracker?color=blue&style=flat">
<img align="left" src="https://img.shields.io/badge/Spring_Boot-%236DB33F.svg?style=flat&logo=springboot&logoColor=white">
<img align="left" src="https://img.shields.io/badge/Thymeleaf-%23005C0F.svg?style=flat&logo=Thymeleaf&logoColor=white">
<br>

## Features
- Authentication (login)
- CRUD projects, bugs and tasks
- Project README, which supports HTML syntax
- Employee, Admin role
- Employees panel for Admin users
  - Add employee
- Mark bugs and tasks as `pending`, `in progress` or `completed`
- Dashboard
- Profile page
- Password changing option
- List of bugs, tasks, and projects
- Notifications

## Setup
Download [git](https://git-scm.com/) and [maven](https://maven.apache.org/download.cgi)<br><br>
Clone this repository
```sh
git clone https://github.com/0l1v3rr/bug-tracker.git
cd bug-tracker
```
Now, run the queries in the [data.sql](./src/main/resources/data.sql) file.  
This will create the necessary tables and insert the values.  
It creates the **Admin** user. The default password will be **pass**, but you can easily change it later.  
Now in the [DB.java](https://github.com/0l1v3rr/bug-tracker/blob/master/src/main/java/org/oliverr/bugtracker/DB.java) file, change the URL, USERNAME, and PASSWORD to the appropriate values.<br>
```java
private final String URL = "jdbc:mysql://<ip>:<port>/<database_name>";
private final String USERNAME = "<username>";
private final String PASSWORD = "<password>";
```
If you have successfully set up the database, then you can run this project:
```sh
sh run.sh
```
The app starts running here: [localhost:8080](http://localhost:8080/)

## Template
I used [THIS](https://github.com/BootstrapDash/corona-free-dark-bootstrap-admin-template) awesome template for this project.
