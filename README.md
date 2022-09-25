# Spring-Boot: A Test Driven Security Approach

I've made this repository to exemplify how to integrate security tests inside your unit/integration test suite.

This where made to mitigate the following OWASP Top 10 risks:
* **A01:2021** - *Broken Access Control*
* **A05:2021** - *Security Misconfiguration*
* **A07:2021** - *Identification and Authentication Failures*

Inside you will find the following libraries:
* *spring-boot-starter-web*
* *spring-boot-starter-test*
* *spring-boot-starter-security*
* *spring-security-test*

And the following endpoints with their respective security configuration:
* **GET** `/about`, accessible to *everyone*
* **POST** `/about`, accessible only to *ADMIN's*
* **GET** `/greeting`, accessible only when *authenticated*
* GET `/submissions`, accessible only to *SPEAKER's*

This security configurations can be found inside `SecurityConfiguration.class`