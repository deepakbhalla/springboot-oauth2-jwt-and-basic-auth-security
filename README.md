# springboot-oauth2-jwt-and-basic-auth-security
Application demonstrates how to configure multiple HttpSecurity instances in Spring Security to secure
one API with basic authentication and all other APIs with OAuth2 JWT authentication.

- User signup API with 'No Auth' as authentication type
- Get access token (OAuth2 JWT) with 'Basic Auth' (Username and Password) authentication type.
- Use access token to access all other APIs

### Implementation Approach

1. Firstly, presenting an access token to a server for authentication is part of the Oauth2 standard.  Hence, we are going 
to implement a small part of the Oauth2 spec and not necessarily implement the entire oauth2 specification.

2. Secondly, Spring Security has in-built support for the OAuth2 Resource server and BearerTokenAuthenticationFilter to 
parse the request for bearer tokens and make an authentication attempt. Therefore, we don’t need to implement our own 
custom filters as we did in our previous articles.

3. Thirdly, Jwt and Opaque Token are the only supported formats for bearer tokens in Spring Security. This application  
configures OAuth2 resource server with Jwt-encoded bearer token support.

### Application Functionalities

1. <b>User Registration (No Auth)</b>
   - New user registration without API authentication
2. <b> Get Access Token (Basic Auth)</b>
3. <b>User Management (Bearer Token)</b>
   - Get all users
   - Delete a user
4. <b>Account Management (Bearer Token)</b>
    - Get all accounts
    - Get an account
    - Create new account
    - Update existing account
    - Delete existing account
5. <b>Transaction Management (Bearer Token)</b>
    - Deposit an amount to an account
    - Withdraw an amount from an account

### API Security In Action

![springboot_oauth_jwt_basicauth_security_postman_execution.gif](screenshots%2Fspringboot_oauth_jwt_basicauth_security_postman_execution.gif)

### OAuth 2.0 Terminologies

###### 1. OAuth 2.0

[OAuth 2.0](https://oauth.net/2/) is the industry-standard protocol for authorization and it uses Access Tokens for that.

###### 2. Access Token

Firstly, an [OAuth Access Token](https://oauth.net/2/access-tokens/) is a string that the OAuth client uses to make requests 
to the resource server. Secondly, access tokens do not have to be in any particular format, and in practice, various OAuth 
servers have chosen many different formats for their access tokens. Most importantly, access tokens may be either 
“[bearer tokens](https://oauth.net/2/bearer-tokens/)” or “sender-constrained” tokens.

###### 3. Bearer Token

[Bearer Tokens](https://oauth.net/2/bearer-tokens/) are the predominant type of access token used with OAuth 2.0. A 
Bearer Token is an opaque string, not intended to have any meaning to clients using it. Some servers will issue tokens 
that are a short string of hexadecimal characters, while others may use structured tokens such as 
[JSON Web Tokens](https://oauth.net/2/jwt/).

###### 4. JSON Web Token (JWT)
[JSON Web Token](https://oauth.net/2/jwt/) (JWT, [RFC 7519](https://tools.ietf.org/html/rfc7519)) is a way to encode 
claims in a JSON document that is then signed. JWTs can be used as OAuth 2.0 [Bearer Tokens](https://oauth.net/2/bearer-tokens/) 
to encode all relevant parts of an access token into the access token itself instead of having to store them in a database.

###### 5. Resource Server
A server that protects the user’s resources and receives access requests from the Client. It accepts and validates an 
Access Token from the Client and returns the appropriate resources to it.

### Insides of the Application

This application has implemented JWT authentication with Spring Security 6:

* Generate private & public key pairs for signing/verifying the token.
* Configure Spring Security to enable OAuth 2.0 Resource Server with JWT bearer token support
* Define JwtEncoder & JwtDecoder beans for token generation and verification
* Expose a POST API with mapping /signup. On passing the user details in the request body without authentication, the new user will be registered.
* Expose a POST API with mapping /token. On passing the username and password in the request body, it will generate a JSON Web Token (JWT).
* Using this JWT token, access all other API with Bearer Token as authentication type

### Project Technologies

- Java version: 17.0.8, vendor: Oracle Corporation
- Apache Maven 3.2.3
- Spring boot 3.1.5
- PostgreSql Database
- Spring boot JPA
- Spring security 6.1.5 (Implemented Basic Auth in this project)
- Lombok
- OpenApi 3 Specifications

### Generating Keys

For generating keys, OpenSSL has been used.

- Generate an RSA private key, of size 2048, and output it to a file named key.pem:
- Generate a public RSA key with the private key as input and output it to a file named public.pem:

![openssl_generate_private_public_pem-files.PNG](screenshots%2Fopenssl_generate_private_public_pem-files.PNG)

### Maven Dependencies in pom.xml

![pom_file.png](screenshots%2Fpom_file.png)

### Spring Security Configurations

Configuration class which implements the Spring security for both Basic Auth and Bearer Token.

- Basic Auth security for generating the access token using username and password

![basic_auth_spring_security_config.png](screenshots%2Fbasic_auth_spring_security_config.png)

- Makes use of the RSA keys that we have generated earlier to define the JwtEncoder & JwtDecoder 
beans, defines SecurityFilterChain bean for securing the private APIs with the OAuth2 Resource server.

![oauth2_jwt_bearer_token_spring_security_config.png](screenshots%2Foauth2_jwt_bearer_token_spring_security_config.png)

### Database

PostgreSQL has been used in this project. Also, this project stores account's events in the form of JSON in JSONB column
in the application table.

JSON data types are for storing JSON (JavaScript Object Notation) data. Such data can also be stored as text, but the 
JSON data types have the advantage of enforcing that each stored value is valid according to the JSON rules. There are 
also assorted JSON-specific functions and operators available for data stored in these data types.

PostgreSQL offers two types for storing JSON data: 

- JSON 
- JSONB. 

1. JSON stores white space, and that is why we can see spaces when key "a" is stored, while JSONB does not.
2. JSON stores all the values of a key. This is the reason you can see multiple values (2 and 1) against the key "a", while JSONB only "stores" the last value.
3. JSON maintains the order in which elements are inserted, while JSONB maintains the "sorted" order.
4. JSONB objects are stored as a decompressed binary as opposed to "raw data" in JSON, where no reparsing of data is required during retrieval.
5. JSONB also supports indexing, which can be a significant advantage.

### OpenAPI Specification

- Path

http://localhost:8081/my-application/swagger-ui/index.html

- Swagger Authorization

![available_swagger_authentications.PNG](screenshots%2Favailable_swagger_authentications.PNG)

- Swagger UI Details

![swagger_ui.png](screenshots%2Fswagger_ui.png)

### Disable OpenAPI Swagger for Production Environment

We can disable OpenAPI swagger for any environment based upon profiles. We can supply a VM argument 
'-Dspring.profiles.active=<environment name>' to the application configurations.
Using spring profile annotation @Profile("prod"), we can control the display of swagger.

- VM Argument

![vm_argument_spring_active_profile.png](screenshots%2Fvm_argument_spring_active_profile.png)

If the value of spring profile is 'prod', then swagger won't be available. Please refer the below screenshot:

![swagger_not_available.png](screenshots%2Fswagger_not_available.png)
