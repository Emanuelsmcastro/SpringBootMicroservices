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

>E como eu consigo garantir que toda a minha estrutura da minha api seja acessada por um único IP? Bom, esse trabalho fica com o nosso microsserviço de Gateway. Com ele somos capazes de eliminar o problema de um IP por microsserviço. O Gateway centraliza o acesso a nossa aplicação separando os microseserviços por rotas, e utilizando apenas o IP do Gateway as requisições. Para ficar mais claro, o que antes seria assim: http://localhost:${porta_do_microsserviço_A}, passa a ser assim: http://localhost:${porta_do_Gateway}/${rota_do_microsserviço_A}.

>Nesse projeto também foi utilizado um servidor de configurações para centralizar nossos arquivos de configurações de cada microsserviço em um repositório git: https://github.com/Emanuelsmcastro/ServerConfiguration. Complementando com o Actuator para executar um refresh nas nossas configurações, sendo possível capturar em runtime as modificações feitas nos arquivos de configurações que estão no repositório.

## Build e Dockerizing 
>Foi utilizado um projeto de gradle build feito em python criado por mim, está fora do escopo desse projeto detalhar o funcionamento do mesmo. Porém ele basicamente detecta os arquivos "build.gradle" que estãos nos microsserviços, builda os mesmos e pega o nome do .jar criado e atualiza no Dockerfile. Após isso ele executa o **docker-compose up --build -d** na raiz do projeto onde está localizado o arquivo "docker-compose.yml".
