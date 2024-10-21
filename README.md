# Aplikacija za evidentiranje zahtjeva za izdavanje kreditnih kartica

## 1. API dokumentacija i HTTP sučelje

- API dokuemntacija implementirana je pomoću alata **Swagger**
- Moguće joj je pristupiti na resursu **/swagger-ui/index.html** deploy-anog rješenja
- Unutar **Swagger** sučelja moguće je isprobati sve API metode aplikacije

## 2. Napomene za pokretanje i korištenje
- Prije pokretanja rješenja pomoću alata **maven** potrebno je *build*-ati rješenje pomoću naredbe **mvn clean install** 
- Pri pokretanju potrebno je postaviti *environment* varijablu **KAFKA_SERVER=<<kafka_server>>**, gdje **<<kafka_server>>** predstavlja putanju do Kafka servera koji će se koristiti u testiranju
- Za promjenu statusa zahtjeva koristi se topic **credit-card-client-service.status-change**, sa porukama u formatu:
```json
{
  "oib": "11111111119",
  "status": "Approved"
}
```
- Za testiranje proslijeđivanja zahtjeva na vanjski API, očekuje se vanjski servis na putanji **http://api.something.com/v1** , sa POST HTTP metodom na resursu **/api/v1/card-request**. SSL nije podržan u trenutnom rješenju.
