# Mutants (Mercadolibre exam)

## Introduction
Mutants is a very simple spring boot app for detecting mutant patterns in a DNA sequence and to keep track of detected mutants.
For a full description of the requirements see: [click here](https://github.com/fedeLarregle/mutants/blob/master/mutants_exam/pom.xml)

## Running the application locally

### Requirements
Before starting, you'll need to have Java 8 installed and Maven.
1. Clone the repository:
```
$ git clone https://github.com/fedeLarregle/mutants.git
```
2. Package it in a jar file by doing:
```
$ mvn clean package
```
3. Run the package (look for the jar file in the generated `target` directory of the project):
```
$ java -jar mutants_exam-0.0.1-SNAPSHOT.jar
```

### curl examples
1. /mutant request with a mutant DNA sequence
```
curl --location --request POST 'localhost:8080/mutant' \
--header 'Content-Type: application/json' \
--data-raw '{
	"dna": ["AAAAAA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]
}'
```
2. /mutant request with BAD_REQUEST DNA sequence (non nitrogenous base 'H')
```
curl --location --request POST 'localhost:8080/mutant' \
--header 'Content-Type: application/json' \
--data-raw '{
	"dna": ["AAAAAH","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]
}'
```
3. /mutant request with BAD_REQUEST DNA sequence (non square shape NxN)
```
curl --location --request POST 'localhost:8080/mutant' \
--header 'Content-Type: application/json' \
--data-raw '{
	"dna": ["AAAAA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]
}'
```
4. /mutant request with FORBIDDEN DNA sequence (non mutant)
```
curl --location --request POST 'localhost:8080/mutant' \
--header 'Content-Type: application/json' \
--data-raw '{
	"dna": ["AAACAG","CAGTGC","TTATGT","AGAAGG","CTCCTA","TCACTG"]
}'
```
5. Simple /stats request
```
curl --location --request GET 'localhost:8080/stats'
```

## Running the application on the cloud (AWS)
This application is deployed in AWS so you can "test it" without the need to clone it by requesting it as follows:

### curl examples

1. /mutant request with a mutant DNA sequence
```
curl --location --request POST 'MercadolibreMutants-env.eba-vp7fk7yp.sa-east-1.elasticbeanstalk.com/mutant' \
--header 'Content-Type: application/json' \
--data-raw '{
  "dna":["ATGCAA", "CGGATA", "TAATGT", "AAGGGG", "CCCTTA", "TTACTT"]
}'
```

Well, you get the idea, same as before but with `MercadolibreMutants-env.eba-vp7fk7yp.sa-east-1.elasticbeanstalk.com/`


## Further improvements

1. The application lacks of [Metrics](https://docs.spring.io/spring-metrics/docs/current/public/datadog), this is a very helpfull thing to have in production not only to monitor but also to define SLA with any possible clients.

2. The way /stats is counting `count_mutant_dna` and `count_human_dna` is by transactionally incrementing a `BITINT` in `Human_Gender_Stats` table when ever we "detect"/"analize" a new DNA sequence.
However, this could have been done using Redis `INCR key` command, they even have a pattern for that, so... [INCR key](https://redis.io/commands/incr).

3. reads to `Human_Gender_Stats` table could/should be cached but it is not as simple as putting `@Cacheable` as with access to Human_Gender. We should think of a good enough TTL (Time To Live) for this cache and it should be invalidated/evicted every time we "detect"/"analize" a new DNA sequence. Also this could be avoided by implementing the previous section 1.

4. `spring.jpa.hibernate.ddl-auto=create` property is extremely bad for production environments, this deletes and re-creates the database on every deployment. A more proper solution is to use a migration tool like [Liquibase](https://www.liquibase.org/) and to let DBAs review the migrations.
