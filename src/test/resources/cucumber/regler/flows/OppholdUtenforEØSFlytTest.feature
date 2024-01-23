# language: no
# encoding: UTF-8


Egenskap: Flyt test av opphold utenfor EØS

  Scenariomal: opphold utenforNorge EØS blir kalt uten nye bruker spørsmål
    Gitt arbeidUtenforNorgeGammelModell er "true"
    Når oppholdUtenforEØSRegler kjøres
    Så skal resultat av regel være  være "<Resultat>"
    Og årsak etter regelkjøring er "<årsak>"

    Eksempler:
      | Resultat |  årsak |
      | UAVKLART |  SP6301|

  Scenariomal: opphold utenforNorge EØS blir kalt med nye bruker spørsmål
    Gitt OppholdUtenforEos
      | Fra og med dato | Til og med dato | Har oppholdt seg utenfor EØS |LAND|
      | <FOM>           | <TOM>           | <oppholdUtenforEOS>          | <LAND>|

    Når oppholdUtenforEØSRegler kjøres
    Så skal resultat av regel være  være "<Resultat>"
    Og årsak etter regelkjøring er "<årsak>"

    Eksempler:
      | Resultat | oppholdUtenforEOS |FOM        | TOM        | årsak |LAND    |
      | UAVKLART | true               |  1.1.2022 | 1.1.2024   |SP6311 | india  |
      | JA       | false              |     NULL  | NULL       |  NULL |  null  |