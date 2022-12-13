# Voting Module

This is a project developed in the Java language with the objective of providing a customized voting service.

## Overview

![uc_overview](https://user-images.githubusercontent.com/1280690/207198924-820d8c1e-c488-4c82-889c-d9b055f1cd63.png)

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

This operation saves the data in the agenda table.

Below are the definitions of the agenda table.

![tb_agenda](https://user-images.githubusercontent.com/1280690/207092702-b42608cd-40bd-4e22-bfc2-0e0a8e86c43c.png)

The id, code, created_date, last_modified_date and status columns are filled in automatically.

The duration column has a range of values between 1 and 480 minutes.



