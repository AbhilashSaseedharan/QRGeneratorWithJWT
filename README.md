# QRGeneratorWithJWT

- clone
- Start redis :  brew services start redis 
- excute ./mvnw spring-boot:run

##Generate JWT Token
- open Swagger page - http://localhost:8080/swagger-ui/index.html
    - create user using /api/signup/
    - create JWT using /api/authenticate
    - click on the Authorize button on the top right of the swagger UI
    - Enter the JWT and login
 
##Generate QR
-use /api/qr/generate to generate QR for the given data
  
