# Voting Module

This is a project developed in the Java language with the purpose of providing a personalized voting service.

## Overview

![uc_overview](https://user-images.githubusercontent.com/1280690/207084599-94c26511-18ba-47cf-9941-993309c42eae.jpg)

## Services

| Operation | URI |
| ------ | ------ |
| Create agenda | [POST]  /agenda |
| Create voting options | [POST]  /voting-option |
| Start voting agenda | [PUT]   /agenda |
| Vote Agenda | [POST]  /vote |
| Count votes and define a winner | [GET]   /vote/vote-counting |

#### Create agenda

This operation saves the data in the agenda table.

Below are the definitions of the agenda table.

![tb_agenda](https://user-images.githubusercontent.com/1280690/207092702-b42608cd-40bd-4e22-bfc2-0e0a8e86c43c.png)



