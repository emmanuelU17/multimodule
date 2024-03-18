# Run Endpoint Tests Against a Natively Compiled SpringBoot Application

## Introduction
This repository provides a solution for conducting RESTful endpoint tests on
a SpringBoot application complied to a native GraalVM image, leveraging
Testcontainers. Ensuring the accuracy and reliability of native images before
deployment to production.

## Problem Statement and Solution
1. Problem Statement:
   1. The project owner intends to transition from deploying their application
   as a normal Docker image to a GraalVM native image.
   2. Despite adhering to Test-Driven Development (TDD) practices in the development
   of this application, validating compatibility with native images requires
   addressing potential reflection issues and other complexities.
   3. Current validation methods rely on manual testing via click-ops, involving UI
   interactions to verify server responses.
2. Proposed Solution
   1. Utilizing a Maven multimodule setup, the integration directory will play a
   pivotal role in compiling webserver directory to a native image prior to deploying.
   2. Using Testcontainers, integration directory compiles webserver directory using the
   Dockerfile in the root of webserver.

By automation of testing procedures, the project aims to streamline the
validation process for GraalVM native images before deployment to production.

## Documentation
1. For more above Maven module [Maven docs](https://maven.apache.org/guides/mini/guide-multiple-modules.html)
2. Packaging Springboot application as dependencies in another module
[Spring docs](https://docs.spring.io/spring-boot/docs/current/maven-plugin/reference/htmlsingle/#packaging.repackage-goal.parameter-details.skip)