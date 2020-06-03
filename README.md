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
4. Simple /stats request
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
