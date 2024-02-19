# language: no
# encoding: UTF-8


Egenskap: Flyt test av opphold utenfor EØS

  Scenariomal: opphold utenforNorge EØS blir kalt uten nye bruker spørsmål
    Gitt arbeidUtenforNorgeGammelModell er "false"
    Når oppholdUtenforEØSRegler kjøres
    Så skal resultat av regel være  være "<Resultat>"
    Og årsak etter regelkjøring er "<ÅRSAK>"

    Eksempler:
      | Resultat |  ÅRSAK |
      | UAVKLART |  SP6301|

  Scenariomal: opphold utenforNorge EØS blir kalt med nye bruker spørsmål
    Gitt OppholdUtenforEos
      | Fra og med dato | Til og med dato | Har oppholdt seg utenfor EØS |LAND|
      | <FOM>           | <TOM>           | <oppholdUtenforEOS>          | <LAND>|

    Når oppholdUtenforEØSRegler kjøres
    Så skal resultat av regel være  være "<Resultat>"
    Og årsak etter regelkjøring er "<ÅRSAK>"
    Og begrunnelse på årsak er "<BEGRUNNELSE>"

    Eksempler:
      | Resultat | oppholdUtenforEOS  |FOM          | TOM        |  LAND    |ÅRSAK      |BEGRUNNELSE                                  |
      | UAVKLART | true               |  2022-10-06 | 2023-10-02 | india    |  SP6314   |Oppholdet utenfor EØS er lengere en 180 dager|
      | JA       | true               |  2023-11-16 | 2023-11-19 | india    |  NULL     |null                                         |
      | JA       | false              |     NULL    | NULL       |  null    |  NULL     |null                                         |
      | UAVKLART | true               |  TODAYS_DATE| TODAYS_DATE|  india   |  SP6313   |Det er mindre en 90 dager siden oppholdet utenfor EØS ble avsluttet|

  Scenariomal: opphold utenforNorge EØS blir kalt med nye bruker spørsmål og flere utlandsopphold
    Gitt OppholdUtenforEosMedFlereInnslag

    Når oppholdUtenforEØSRegler kjøres
    Så skal resultat av regel være  være "UAVKLART"
    Og årsak etter regelkjøring er "SP6312"
    Og begrunnelse på årsak er "Det er flere en ett utenlandsopphold registrert"
    Eksempler:
      | Resultat | oppholdUtenforEOS  |FOM          | TOM        |  LAND    |ÅRSAK      |BEGRUNNELSE                                  |
      | UAVKLART | true               |  2022-10-06 | 2023-10-02 | india    |  SP6314   |Oppholdet utenfor EØS er lengere en 180 dager|
