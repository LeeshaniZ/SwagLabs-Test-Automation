# Swag Labs UI Test Automation

A browser-automation project for exercising user flows on the Swag Labs demo shopping application.

## Technology

- Java
- Selenium WebDriver
- TestNG
- ChromeDriver

## Test coverage

The current Java test source focuses on interacting with the Swag Labs web interface. Review the test class for the exact scenarios implemented.

## Prerequisites

- JDK compatible with the project dependencies
- Google Chrome
- A matching ChromeDriver version
- Selenium and TestNG dependencies configured in the Java project

## Running the tests

Open the project in your Java IDE, configure the required Selenium and TestNG dependencies, ensure ChromeDriver is available, and run the test class with TestNG.

## Quality improvements

For maintainable automation, prefer explicit TestNG assertions over console-only comparisons, use explicit waits instead of fixed sleeps, keep locators centralised where practical, and capture useful failure details.

## Scope

This repository is a learning/test-automation project. The implemented test cases should be treated as the source of truth for current coverage.
