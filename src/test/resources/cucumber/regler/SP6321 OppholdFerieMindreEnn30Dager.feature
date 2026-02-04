# language: no
# encoding: UTF-8


Egenskap: Utenlandsopphold mindre enn 30 dager

  Scenariomal: Ingen brukersvar for regel SP6321
    Gitt arbeidUtenforNorgeGammelModell er "false"
    Når oppholdUtenforEØSRegler kjøres
    Så skal resultat av regel være "<Resultat>"
    Og årsak etter regelkjøring er "<ÅRSAK>"

    Eksempler:
      | Resultat | ÅRSAK  |
      | UAVKLART | SP6301 |

  Scenariomal: SP6321 blir kalt med nye brukerspørsmål der bruker ikke oppgir ferie
    Gitt OppholdUtenforEos
      | Fra og med dato | Til og med dato | Har oppholdt seg utenfor EØS | LAND   |
      | <FOM>           | <TOM>           | <oppholdUtenforEOS>          | <LAND> |

    Når regel "SP6321" kjøres
    Så skal resultat av regel være "<Resultat>"

    Eksempler:
      | Resultat | oppholdUtenforEOS | FOM         | TOM         | LAND  |
      | NEI      | true              | 2022-10-06  | 2023-10-02  | india |
      | NEI      | true              | 2023-11-16  | 2023-11-19  | india |
      | NEI      | false             | NULL        | NULL        | null  |
      | NEI      | true              | TODAYS_DATE | TODAYS_DATE | india |

  Scenariomal: SP6321 blir kalt med nye brukerspørsmål der bruker oppgir ferie
    Gitt OppholdUtenforEosMedFerie
      | Fra og med dato | Til og med dato | Har oppholdt seg utenfor EØS | LAND   |
      | <FOM>           | <TOM>           | <oppholdUtenforEOS>          | <LAND> |

    Og InputPeriode
      | Fra og med dato | Til og med dato |
      | 2024-10-01      | 2024-10-06      |


    Når regel "SP6321" kjøres
    Så skal resultat av regel være "<Resultat>"

    Eksempler:
      | Resultat | oppholdUtenforEOS | FOM         | TOM         | LAND  |
      | NEI       | true             | 2024-09-20 | 2024-09-29  | india |
      | NEI       | true             | 2024-01-01 | 2024-05-29  | india |
      | JA        | true             | 2024-01-01 | 2024-01-15  | Spania |