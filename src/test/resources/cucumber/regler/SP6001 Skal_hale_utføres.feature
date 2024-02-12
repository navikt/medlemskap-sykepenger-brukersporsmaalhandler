# language: no
# encoding: UTF-8


Egenskap: SP6001 Skal hale utføres

  Scenariomal: Regelkjøreing for SP6001. Forskjellig svar ut fra hvilke regler som bryter i gammel regel motor
    Når gammelt resultat for gammelregelkjøring er "<GammelKjøring>"
    Og årsaker er "<Årsaker>"
    Så skal resultat av regel være  være "<Resultat>"

    Eksempler:
      | Resultat | GammelKjøring  |   Årsaker          |
      | JA       | UAVKLART       |    REGEL_3         |
      | JA       | UAVKLART       |    REGEL_3,REGEL_9 |
      | NEI      | UAVKLART       |    REGEL_3,REGEL_12|
      | NEI      | JA             |                    |