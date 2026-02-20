# language: no
# encoding: UTF-8


Egenskap: Flyt test av opphold utenfor EØS

  Scenariomal: opphold utenforNorge EØS blir kalt uten nye bruker spørsmål
    Gitt arbeidUtenforNorgeGammelModell er "false"
    Når oppholdUtenforEØSRegler kjøres
    Så skal resultat av regel være "<Resultat>"
    Og årsak etter regelkjøring er "<ÅRSAK>"

    Eksempler:
      | Resultat | ÅRSAK  |
      | UAVKLART | SP6301 |

  Scenariomal: opphold utenforNorge EØS blir kalt med nye bruker spørsmål
    Gitt Følgende brukersvar om opphold utenfor EØS
      | fom   | tom   | Svar                | Land   |
      | <FOM> | <TOM> | <oppholdUtenforEOS> | <LAND> |

    Når oppholdUtenforEØSRegler kjøres
    Så skal resultat av regel være "<Resultat>"
    Og årsak etter regelkjøring er "<ÅRSAK>"
    Og begrunnelse på årsak er "<BEGRUNNELSE>"

    Eksempler:
      | Resultat | oppholdUtenforEOS | FOM        | TOM        | LAND  | ÅRSAK  | BEGRUNNELSE                                                                 |
      | UAVKLART | Ja                | 2022-10-06 | 2023-10-02 | india | SP6314 | Oppholdet utenfor EØS er lengere en 180 dager                               |
      | JA       | Ja                | 2023-11-16 | 2023-11-19 | india |        |                                                                             |
      | UAVKLART | Ja                | 2026-01-01 | 2026-01-01 | india | SP6321 | Opphold ikke ferie, eller mer enn 30 dager, eller mindre enn 30 dager siden |

  Scenariomal: opphold utenforNorge EØS blir kalt med nye bruker spørsmål og flere utlandsopphold
    Gitt OppholdUtenforEosMedFlereInnslag

    Når oppholdUtenforEØSRegler kjøres
    Så skal resultat av regel være "UAVKLART"
    Og årsak etter regelkjøring er "SP6312"
    Og begrunnelse på årsak er "Det er flere en ett utenlandsopphold registrert"
    Eksempler:
      | Resultat | oppholdUtenforEOS | FOM        | TOM        | LAND  | ÅRSAK  | BEGRUNNELSE                                   |
      | UAVKLART | true              | 2022-10-06 | 2023-10-02 | india | SP6314 | Oppholdet utenfor EØS er lengere en 180 dager |
