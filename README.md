# Test Against a Natively Compiled SpringBoot Application

## Introduction
Have you ever wondered how do you validate a natively complied java application has no
reflection error or bugs? Well this repository provides a solution for conducting tests
against a natively compiled RESTFul SpringBoot application. It uses Maven multimodule
build where the webserver module is our production application and integration module
contains tests that run against non-natively and natively compiled production application.

## Problem Statement and Solution
1. Problem Statement:
   1. The project owner intends to transition from deploying webserver module
   as a normal Docker image to a GraalVM native Docker image.
   2. Although webserver module adheres to Test-Driven Development (TDD) principles,
   we need to validate webserver module successfully complied to a native image without
   reflection errors, framework of 3rd-party dependencies not being able to be converted
   into a native image.
   3. Current validation against natively compiled image is relying on manual testing via
   click-ops (UI interactions to verify server responses).
2. Proposed Solution
   1. Utilizing a Maven multimodule setup where integration module contains all tests
   against natively compiled webserver module (production code).
   2. Dockerfiles to compile webserver module into a none-native and native image.
   3. Testcontainers to run tests against Dockerfile builds.
   4. Automation testing leveraging GitHub actions.

## Modules
1. fullstack module: a simple RESTful server that persists data to a MySQL database
2. integration module: runs api tests against native Docker images of fullstack and webserver
module.
3. webserver module: a simple RESTful server.

## Dockerfiles
1. Builds a jar for fullstack directory.
2. Dockerfile.fullstack and Dockerfile.webserver builds a native image for alpine linux OS.

## Pre-requisite
1. Docker installed

## Getting Started
1. git clone `https://github.com/emmanuelU17/multimodule.git`
2. In your terminal, `mvn clean install -DskipTests`
3. To run tests against a normal image, in your terminal `cd integration/ \
   && mvn clean test -Dtest="IntegrationTest"`
4. To run tests against a native image, in your terminal
   `cd integration/ \ && mvn clean test -Dtest="NativeImageTest"`

## Dependencies
1. Spring 3.2.x
2. Testcontainers
3. Webflux for testing in integration module

## Helpful Documentation
1. For more above Maven module [Maven docs](https://maven.apache.org/guides/mini/guide-multiple-modules.html)
2. Packaging Springboot application as dependencies in another module
[Spring docs](https://docs.spring.io/spring-boot/docs/current/maven-plugin/reference/htmlsingle/#packaging.repackage-goal.parameter-details.skip)
3. [Creating images on the fly](https://java.testcontainers.org/features/creating_images/)
4. [building specific modules](https://books.sonatype.com/mvnref-book/reference/_using_advanced_reactor_options.html#:~:text=Making%20a%20Subset%20of%20Projects,(either%20directly%20or%20indirectly).)
