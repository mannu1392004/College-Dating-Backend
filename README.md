# College Dating App
## Overview
The College Dating App is an ongoing project designed to connect college students based on shared interests.
This app features a robust backend developed with Spring Boot and MongoDB, and a mobile application developed using Jetpack Compose for Android. The project aims to offer a seamless and engaging user experience,
integrating various functionalities to enhance connectivity among students.

## Features
### User Authentication: Secure login and registration with email and OTP verification.
### Profile Management: Users can create, edit, and view profiles with personal information and interests.
### Matching System: Connect students based on shared interests and preferences.
### Posts and Interaction: Users can create and interact with posts, including likes and comments.
### Chat Functionality: Real-time messaging and chat features.
### College Information: Integration of college data for user profiling and matching.
# Technologies
Backend: Spring Boot, MongoDB
Frontend: Jetpack Compose (Android)
Cloud Services: AWS EC2, S3
Other Tools: RESTful APIs, JWT for authentication
Backend
# Overview
The backend for the College Dating App is built using Spring Boot,
providing a scalable and maintainable RESTful API. It handles user authentication, profile management, matching logic, and chat functionalities. MongoDB is used for data storage, ensuring flexibility and scalability.

# Setup
 open  the project in vs code or intelij and before building paste this in application. properties

aws.accessKeyId=defineId
aws.secretKey=addsecretkey
aws.s3.bucketName=bucketname
aws.s3.region=yourregion



# Email Configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your gmail
spring.mail.password=password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
spring.mail.properties.mail.smtp.ssl.trust=smtp.gmail.com

after filling details now buid it 
