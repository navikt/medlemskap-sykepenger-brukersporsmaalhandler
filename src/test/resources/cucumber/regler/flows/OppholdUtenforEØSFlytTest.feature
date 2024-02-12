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
      | Resultat | oppholdUtenforEOS  |FOM        | TOM        |  LAND    |ÅRSAK      |BEGRUNNELSE                                |
      | UAVKLART | true               |  1.1.2022 | 1.1.2024   | india    |  SP6311   |Bruker har oppgitt JA i Opphold utenfor EØS|
      | JA       | false              |     NULL  | NULL       |  null    |  NULL     |null                                       |