# language: no
# encoding: UTF-8


Egenskap: Flyt test av arbeid utenfor norge

  Scenariomal: Arbeid utenforNorge flyt blir kalt uten nye bruker spørsmål
    Gitt arbeidUtenforNorgeGammelModell er "<arbeidUtenforNorge>"

    Når arbeidutenforNorgeFlyt kjøres
    Så skal resultat av regel være  være "<Resultat>"
    Og årsak etter regelkjøring er "<årsak>"

    Eksempler:
      | Resultat | arbeidUtenforNorge | årsak |
      | UAVKLART | true               | SP6100|
      | JA       |false               |  NULL |

  Scenariomal: Arbeid utenforNorge flyt blir kalt med nye bruker spørsmål
    Gitt utfoertArbeidUtenforNorge
      | Fra og med dato | Til og med dato | Har hatt arbeid utenfor Norge |LAND|
      | <FOM>           | <TOM>           | <arbeidUtenforNorge>          | <LAND>|

    Når arbeidutenforNorgeFlyt kjøres
    Så skal resultat av regel være  være "<Resultat>"
    Og årsak etter regelkjøring er "<årsak>"

    Eksempler:
      | Resultat | arbeidUtenforNorge |FOM        | TOM        | årsak |LAND    |
      | UAVKLART | true               |  1.1.2022 | 1.1.2024   |SP6120 | india  |
      | JA       | false              |     NULL  | NULL       |  NULL |  null  |