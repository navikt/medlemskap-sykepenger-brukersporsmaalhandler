# language: no
# encoding: UTF-8


Egenskap: SP6500 RegelUtsjekk Norske borgere

  Scenariomal: SP6500 bli kalt med nye brukerspørsmål og bare regel 3 bryter
    Gitt årsaker i gammel kjøring
    |REGELBRUDD|
    |REGEL_3   |

    Og utfoertArbeidUtenforNorge
    | Fra og med dato | Til og med dato | Har hatt arbeid utenfor Norge |LAND|
    | null          | null          | <arbeidUtenforNorge>          | <LAND>|

    Og OppholdUtenforEos
      | Fra og med dato | Til og med dato | Har oppholdt seg utenfor EØS |LAND|
      | null          | null              | <oppholdUtenforEOS>          | THAILAND|


    Når regel "SP6500" kjøres
    Så skal resultat av regel være  være "<Resultat>"

    Eksempler:
      | Resultat  | arbeidUtenforNorge | oppholdUtenforEOS |
      | NEI       | true               |         true      |
      | JA        | false              |         false     |
