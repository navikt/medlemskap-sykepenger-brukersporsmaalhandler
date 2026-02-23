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
    Gitt Følgende brukersvar om opphold utenfor EØS
      | fom   | tom   | Svar                | Land   |
      | <FOM> | <TOM> | <oppholdUtenforEOS> | <LAND> |

    Når regel "SP6321" kjøres
    Så skal resultat av regel være "<Resultat>"

    Eksempler:
      | Resultat | oppholdUtenforEOS | FOM        | TOM        | LAND  |
      | NEI      | Ja                | 2022-10-06 | 2023-10-02 | india |
      | NEI      | Ja                | 2023-11-16 | 2023-11-19 | india |
      | NEI      | Ja                | 2026-01-01 | 2026-01-01 | india |

  Scenariomal: SP6321 blir kalt med nye brukerspørsmål der bruker oppgir ferie
    Gitt Følgende brukersvar om opphold utenfor EØS
      | fom   | tom   | Svar                | Land   | Grunn   |
      | <FOM> | <TOM> | <oppholdUtenforEOS> | <LAND> | <Grunn> |

    Og Følgende inputperiode
      | fom        | tom        |
      | 2024-10-01 | 2024-10-06 |


    Når regel "SP6321" kjøres
    Så skal resultat av regel være "<Resultat>"

    Eksempler:
      | Resultat | oppholdUtenforEOS | FOM        | TOM        | LAND   | Grunn            |
      | NEI      | Ja                | 2024-09-20 | 2024-09-29 | india  |                  |
      | NEI      | Ja                | 2024-01-01 | 2024-05-29 | india  |                  |
      | JA       | Ja                | 2024-01-01 | 2024-01-15 | Spania | Jeg var på ferie |