# Spring Boot Microservices
*Estrutura simplificada do projeto:*
<img width="1600" alt="Microservices" src="https://github.com/Emanuelsmcastro/SpringBootMicroservices/assets/93106680/dc756ba7-d66a-43d1-adbd-0e088f74222b">

## Core
- Java 17
- Spring Boot 3
- Docker

## Tecnologias utilizadas:
-  Lombok
-  Spring Data JPA
-  H2 Database
-  Resiliense4j
-  OpenFeign
-  jjwt
-  Spring Security Crypto
-  Eureka Server
-  Eureka Client
-  Spring Cloud Gateway
-  Config Server
-  Config Client
-  Actuator

## Sobre
>Esse projeto utiliza a estrutura de microsserviços utilizando a arquitetura MVC(Model-View-Controller) para disponibilizar o acesso à informações sobre os trabalhadores de uma empresa fictícia. Nesse sistema é possível obter um trabalhador e seu pagamento, ambos sendo microsservições separados porém com dependência entre o microsserviço de pagamento para com o microsserviço de trabalhadores.

>Nesse sistema também há um microsserviço de authenticação o "hr-oauth" responsável por ser o servidor de autenticação disponibilizando um token JWT, permitindo assim o acesso para os microsserviços. Sendo limitado apenas pela role do usuário, por exemplo, um usuário só pode ter acesso ao pagamento de um trabalhador se ele tiver a permissão "ROLE_ADMIN".

## Como o sistema é estruturado
**A base desse projeto é exemplificar uma estrutura complexa de uma arquitetura de microsserviços e para isso foram utilizadas os seguintes elementos:**

>O sistema de microsserviços é gerenciado por um servidor Eureka, permitindo acesso aos mesmos pela sua propriedade: **spring.application.name**. Um exemplo está na criação do FeignClient para o trabalhador, chamando o microsserviço de trabalhadores por "hr-worker".

```
@Component
@FeignClient(name = "hr-worker", path = "/api/v1/workers")
public interface WorkerFeignClient {

	@GetMapping(value = "/{id}")
	public ResponseEntity<Worker> findById(@PathVariable(value = "id") Long id, @RequestHeader(value = "token") String token);
}
```
