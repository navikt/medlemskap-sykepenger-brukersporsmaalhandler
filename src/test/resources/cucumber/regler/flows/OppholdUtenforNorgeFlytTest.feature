# language: no
# encoding: UTF-8


Egenskap: Flyt test av opphold utenfor Norge

  Scenariomal: opphold utenforNorge Norge blir kalt uten nye bruker spørsmål
    Gitt arbeidUtenforNorgeGammelModell er "false"
    Når oppholdUtenforNorgeRegler kjøres
    Så skal resultat av regel være  være "<Resultat>"
    Og årsak etter regelkjøring er "<ÅRSAK>"

    Eksempler:
      | Resultat |  ÅRSAK |
      | UAVKLART |  SP6401|

  Scenariomal: opphold utenforNorge blir kalt med nye bruker spørsmål
    Gitt OppholdUtenforNorge
      | Fra og med dato | Til og med dato | Har oppholdt seg utenfor Norge |LAND|
      | <FOM>           | <TOM>           | <oppholdUtenforNorge>          | <LAND>|

    Når oppholdUtenforNorgeRegler kjøres
    Så skal resultat av regel være  være "<Resultat>"
    Og årsak etter regelkjøring er "<ÅRSAK>"
    Og begrunnelse på årsak er "<BEGRUNNELSE>"

    Eksempler:
      | Resultat | oppholdUtenforNorge  |FOM          | TOM        |  LAND    |ÅRSAK    |BEGRUNNELSE                                  |
      | UAVKLART | true               |  2022-10-06 | 2023-10-02 | india    |  SP6414   |Oppholdet utenfor Norge er lengere en 180 dager|
      | JA       | true               |  2023-11-16 | 2023-11-19 | india    |  NULL     |null                                         |
      | JA       | false              |     NULL    | NULL       |  null    |  NULL     |null                                         |
      | UAVKLART | true               |  TODAYS_DATE| TODAYS_DATE|  india   |  SP6413   |Det er mindre en 90 dager siden oppholdet utenfor EØS ble avsluttet|

  Scenariomal: opphold utenforNorge Norge blir kalt med nye bruker spørsmål og flere utlandsopphold
    Gitt OppholdUtenforNorgeMedFlereInnslag

    Når oppholdUtenforNorgeRegler kjøres
    Så skal resultat av regel være  være "UAVKLART"
    Og årsak etter regelkjøring er "SP6412"
    Og begrunnelse på årsak er "Det er flere en ett utenlandsopphold registrert"
    Eksempler:
      | Resultat | oppholdUtenforEOS  |FOM          | TOM        |  LAND    |ÅRSAK      |BEGRUNNELSE                                  |
      | UAVKLART | true               |  2022-10-06 | 2023-10-02 | india    |  SP6314   |Oppholdet utenfor EØS er lengere en 180 dager|
