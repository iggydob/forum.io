# Forum_Management_System
## Description
Internet forum about self improvements and motivation powered by Spring Framework and MySQL. This web application enables inter alia: adding posts and comments, as well as browsing, editing or deleting some of them. As well, providing oprportunity to moderators to have an overview and manage all users, posts and comments.  
## Features
- Users before Login can see basic functionality of the forum, post activities:
  ![image](https://github.com/GrishaDaradjanovv/Forum_Management_System/assets/94899409/0747a343-2dce-4364-820a-d441c5afa944)
- User can login:
  ![image](https://github.com/GrishaDaradjanovv/Forum_Management_System/assets/94899409/e201a334-f6cf-46b0-9630-f4f07fed61bd)
- User can register
![image](https://github.com/GrishaDaradjanovv/Forum_Management_System/assets/94899409/514c3f01-0c93-4958-8d07-8d218d712b2e)
- Users can browse through the existing posts with key words and / or to filter / sort 
- Each user can browse every posts, to like / dislike as well to leave a comment. The Comments as well can be Liked if not the author: 
- If the user is the author of the post / comment, the content or title can be edited as well it can be deleted:
- Users can Create Posts: 
- Admins / Moderators can ultilize the Admin Panel to promote users and to ban users:
![image](https://github.com/GrishaDaradjanovv/Forum_Management_System/assets/94899409/6644a251-2776-4c67-8bf6-428efccf92e3)
- Admins can Delete Posts / Comments



## Getting Started
- Clone the repository
  - git clone https://github.com/GrishaDaradjanovv/Forum_Management_System.git
- Connect to MariaDB
- Create a MariaDB database named 'forum-system' 
- Update application.properties with your information
    - database.url=jdbc:mariadb://localhost:3306/forum_system (check the availability of the port)
    - database.username= your username
    - database.password= your password 
- Populate the tables in your DB
  - Go to ../db/create.sql and run
- (Optionally) Populate data in the db: 
  - Go to ../db/insert-data.sql and run
- Run all dependencies
   - Go to ../build.gradle 
- Run the application and check out the website at http://localhost:8080
  
## Technologies
- Java:
  - Spring Framework:
    - Spring MVC:
      - application-level on the basis on design pattern: model-view-controller
      - using methods "get" and "post" with parameterising URLs 
    - Swagger:
       - Enter http://localhost:8080/swagger-ui/index.html in order to test all of the rest controllers
       - Related methods have RequestHeader "Credentials" which is combination of username:password. Mandatory to be filled before each request
    - Spring Data:
      - using JPQL and ready-made methods from `JpaRepository` to creating, reading, updating and deleting data
      - implementation of native queries
    - Spring Security:
      - own login form with authentication of users on the basis of database
      - restricting access to some pages for offline users
    - Spring Boot:
      - automatic configuration and launching application 
  - JPA & Hibernate:
    - specifying relations between entities in database and parameters of columns in tables
  - Java 8 SE:
    - Optionals, LocalDateTime
- HTML:
  - Thymeleaf
  - data validation in login form and registration form
  - semantic elements from HTML5
- CSS:
  - Materialize

## Software tools
- IntelliJ IDEA 2017.2.4
- Gradle 3.5.1
- Marria DB [MySQL]
## Project structure
```forum
 │   db:
     ├───create.sql
     └───insert_data.sql
 └───src:
    ├───main:
    │   ├───java/org/forum/web/forum:
    │   │               │   ForumApplication.java
    │   │               │
    │   │               ├───configuration:
    │   │               │       HibernateConfig.java
    │   │               │       SwaggerConfig.java
    │   │               │
    │   │               ├───controllers:
    │   │               │        ├─── mvc:
    │   │               │        │       │    AuthenticationMVCController.java
    │   │               │        │       │    HomeMVCController.java
    │   │               │        │       │    PostMVCController.java
    │   │               │        │       └───UserMVCController.java
    │   │               │        │     
    │   │               │        └───rest:
    │   │               │               │     CommentRestController.java
    │   │               │               │     TagRestController.java
    │   │               │               │     PostRestController.java
    │   │               │               └─── UserRestController.java
    │   │               │          
    │   │               ├───exceptions:
    │   │               │       AuthorizationException.java
    │   │               │       EntityDuplicateException.java
    │   │               │       EntityNotFoundException.java
    │   │               │       UnauthorizedOperationException.java
    │   │               │
    │   │               ├───helpers:
    │   │               │       AuthenticationHelper.java
    │   │               │       AuthorizationHelper.java
    │   │               │       PhoneConverter.java
    │   │               │       mappers: 
    │   │               │          │    CommentMapper.java
    │   │               │          │    PostMapper.java
    │   │               │          │    TagMapper.java 
    │   │               │          └─── UserMapper.java 
    │   │               │
    │   │               ├───models:
    │   │               │       Comment.java
    │   │               │       Like.java
    │   │               │       LikePost.java
    │   │               │       PhoneNumber.java
    │   │               │       Post.java
    │   │               │       PostTag.java
    │   │               │       User.java
    │   │               │       Dtos: 
    │   │               │          │    CommentDTO.java: 
    │   │               │          │    LoginDto.java: 
    │   │               │          │    PostDto.java: 
    │   │               │          │    PostFilterDto.java: 
    │   │               │          │    RegisterDto.java: 
    │   │               │          │    TagDto.java: 
    │   │               │          │    UserDto.java: 
    │   │               │          └─── UserFilterDto.java: 
    │   │               │       filters: 
    │   │               │          │    PostFilterOptions.java: 
    │   │               │          └───UserFilterOptions.java: 
    │   │               │
    │   │               ├───repository:
    │   │               │       CommentRepositoryImpl.java
    │   │               │       LikePostRepositoryImpl.java
    │   │               │       PhoneNumberRepositoryImpl.java
    │   │               │       PostRepositoryImpl.java
    │   │               │       TagRepositoryImpl.java
    │   │               │       UserRepositoryImpl.java
    │   │               │       contracts:
    │   │               │
    │   │               │
    │   │               └───service:
    │   │                      CommentServiceImpl.java
    │   │                      LikePostServiceImpl.java
    │   │                      PhoneNumberServiceImpl.java
    │   │                      PostServiceyImpl.java
    │   │                      TagServiceImpl.java
    │   │                      UserServiceImpl.java
    │   │                      contracts:
    │   │
    │   └───resources:
    │       │   application.properties
    │       │   messages.properties
    │       ├───static:
    │           │   assets:
    │           │   css:
    │           └───js:
    │       └───templates:
    │           │   AboutView.html
    │           │   AdminPanelVIew.html
    │           │   AdminView.html
    │           │   BannedView.html
    │           │   CommentUpdateView.html
    │           │   EditUserView.html
    │           │   ErrorView.html
    │           │   HomePageNotLogged.html
    │           │   HomePageView.html
    │           │   LoginView.html
    │           │   PostCreateView.html
    │           │   PostUpdateView.html
    │           │   PostViewTheme.html
    │           │   RegisterView.html
    │           │   UserPostsView.html
    │           └───fragments:
    │                   FilteFragment.html
    │                   PageFrame.html       
    │                   UserForm.html
    │                   UserPanel.html    
    │
    └───test:
        └───java/org/forum/web/forum:
                            ForumApplicationTests.java
                            CommentServiceTests.java
                            Helpers.java
                            PostServiceTests.java
                            UserServiceTests.java


```
## Comments


