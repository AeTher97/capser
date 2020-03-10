# Capser
Elo caps server and frontend for playing caps games. 

## Frontend development
Acutal development fronend files are stored in repository capser_frontend. This repository only contains production ready frontend files.

## How to run
Install maven from [here](https://maven.apache.org/download.cgi), make sure that maven executable is added to PATH. After that cd into repository folder and run:
~~~~
mvn install
~~~~
after that cd into **/target** folder and execute:
~~~~
java -jar capser-1.0-SNAPSHOT.jar
~~~~
Server will run on localhost:8080.

To run postgreSQL database is required with credentials **postgres:postgres** and database **capser** on port **5432** (you can change that in settings). You can download postgres [here](https://www.postgresql.org/download/).

Full API documentation is available [here](https://viviclabs.postman.co/collections/6663409-e35bd261-336c-4464-af11-df6089135b6b?version=latest&workspace=3d193ef3-1f0d-423e-8e4c-d49403a22963).
