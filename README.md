****1. Spring Boot with mongoDB Cloud, Restful Api****

> swagger url : http://localhost:8080/swagger-ui.html

---
**적용된 것**

 1. AOP 활용하여 param, result logging
 2. Swagger 적용
 3. ControllerAdvice(AOP) 활용하여 Exception 공통 처리
 4. Reflection + Annotation 조합하여 Common Method
 5. MongoDB TTL INDEX 적용(User Log)
 6. MongoDB Transaction Template : custom transaction annotation을 aop에 적용함
 
 