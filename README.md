# Piattaforma di Gaming Integrata - MATCH PLAY - Backend

## Panoramica
Il backend della Piattaforma di Gaming Integrata gestisce la logica dell'applicazione, l'autenticazione degli utenti, 
la persistenza dei dati e l'integrazione con API esterne. È sviluppato con Java, utilizzando Spring Boot come framework principale.

## Tecnologie e Dipendenze
Il backend utilizza Spring Boot e una serie di dipendenze per fornire funzionalità come la sicurezza, l'accesso ai dati tramite JPA, 
la validazione e molto altro. Alcune delle dipendenze principali includono:

- spring-boot-starter-data-jpa
- spring-boot-starter-security
- spring-boot-starter-web
- postgresql
- lombok
- jjwt per la gestione dei token JWT
- e molte altre per test e integrazioni.

Per un elenco completo, consulta il file `pom.xml` nel repository.

## Configurazione del Progetto
Per eseguire il progetto, è necessario Java 11 o superiore e Maven configurato nel tuo ambiente.

Clona il repository e naviga nella cartella del progetto:

git clone https://github.com/JhonnyR10/Epicode-CAPSTONE-BackEnd.git
cd piattaforma-gaming-backend

Configura le variabili d'ambiente necessarie seguendo l'esempio fornito in `env.properties.example`.

Per avviare l'applicazione, esegui:

./mvnw spring-boot:run

L'applicazione sarà accessibile di default all'indirizzo `http://localhost:8080`.

## Link al Repository Frontend
Questo backend serve l'interfaccia utente sviluppata nel progetto frontend. 
Puoi trovare il repository del frontend e le relative istruzioni qui: https://github.com/JhonnyR10/epicode-capstone-frontend.git

## Contribuire
Le contribuzioni sono benvenute! Si prega di seguire le linee guida nel file CONTRIBUTING.md per contribuire al progetto.

## Autore
Giovanni Longo
