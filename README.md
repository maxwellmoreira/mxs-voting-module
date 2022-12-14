# Voting Module

This is a project developed in the Java language with the objective of providing a customized voting service.

## Overview

![uc_overview](https://user-images.githubusercontent.com/1280690/207211006-78573770-55a0-466f-9e99-c1e80ddf75d2.png)

## Services

| Operation | URI |
| ------ | ------ |
| Create agenda | [POST]  /agenda |
| Create voting options | [POST]  /voting-option |
| Start voting agenda | [PUT]   /agenda |
| Vote Agenda | [POST]  /vote |
| Count votes and define a winner | [GET]   /vote/vote-counting |

>This project supports the springdoc-openapi library used for automated documentation generation in Spring Boot projects. So Swagger-ui and OpenAPI 3 will be implemented in the project.

>The Swagger UI page will then be available at http://server:port/context-path/swagger-ui.html and the OpenAPI description will be available at the following url for json format: http://server:port/context-path/v3/api-docs

![swagger](https://user-images.githubusercontent.com/1280690/207200385-aa7324c1-8c12-4a89-9160-31e74a39306f.png)

#### Create agenda

This operation creates an agenda that will serve as a topic for voting. Basically, you'll need to enter a title, a description and the duration of the voting period.

Here is an example of a request:

```
{
    "title" : "World Cup",
    "description" : "Which country do you think will win the world cup?",
    "duration" : 5
}
```

The process generates a response code that will be used to identify the agenda:

```
{
    "code": "3072be22-b392-4f8d-8e1c-71a4a8a0ca83"
}
```

The data will be saved in the agenda table. Below is its structure.

![tb_agenda](https://user-images.githubusercontent.com/1280690/207092702-b42608cd-40bd-4e22-bfc2-0e0a8e86c43c.png)

The table below describes which columns are changed during the process of registering an agenda.

| Column | Function |
| ------ | ------ |
| id | Audit |
| code | Used to identify a agenda |
| title | Agenda title |
| description | Detailed description of the agenda |
| duration | Voting duration. Allowable values must be in the range between 1 and 480 minutes |
| status_agenda | Voting period status |
| created_date | Indicates the date of registration of the agenda |
| last_modified_date | Audit |
| status | Audit |



