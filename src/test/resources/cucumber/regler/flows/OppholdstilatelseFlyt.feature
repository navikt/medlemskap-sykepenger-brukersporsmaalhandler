# language: no
# encoding: UTF-8


Egenskap: Flyt test av Oppholdstilatelse

  Scenariomal: Oppholdstilatelse blir kalt uten brukerspørsmål om oppholdstilatelse skal regelflyt svare JA
    Gitt gammelt resultat for gammel kjøring er "<FIL>"
    Når oppholdstilatelseRegler kjøres
    Så skal resultat av regel være  være "<Resultat>"

    Eksempler:
      | Resultat | ÅRSAK | FIL                                                                                |
      | JA       |       | BrukerBrudd_REGEL19_3_1_MedBrukerSvarMenIkkeOmOppholdsTilatelseSkalGiUAVKLART.json |


  Scenariomal: Oppholdstilatelse blir kalt med brukerspørsmål om oppholdstilatelse
    Gitt brukersvar om oppholdstitatelse
      | Permanent   | FOM   | TOM   | VDATO   |
      | <Permanent> | <FOM> | <TOM> | <VDATO> |

    Gitt gammelt resultat for gammel kjøring er "<FIL>"
    Når oppholdstilatelseRegler kjøres
    Så skal resultat av regel være  være "<Resultat>"
    Og årsak etter regelkjøring er "<ÅRSAK>"

    Eksempler:
      | Permanent | FOM         | TOM         | VDATO       | Resultat | ÅRSAK  | FIL                                                                                |
      | true      | 2023-02-26  | null        | TODAYS_DATE | UAVKLART | SP6223 | BrukerBrudd_REGEL19_3_1_MedBrukerSvarMenIkkeOmOppholdsTilatelseSkalGiUAVKLART.json |
      | true      | TODAYS_DATE | null        | 2024-02-26  | UAVKLART | SP6222 | BrukerBrudd_REGEL19_3_1_MedBrukerSvarMenIkkeOmOppholdsTilatelseSkalGiUAVKLART.json |
      | true      | 2023-02-26  | null        | 2023-02-26  | JA       | null   | BrukerBrudd_REGEL19_3_1_MedBrukerSvarMenIkkeOmOppholdsTilatelseSkalGiUAVKLART.json |
      | false     | 2023-02-26  | 2026-02-26  | 2023-02-26  | JA       | null   | BrukerBrudd_REGEL19_3_1_MedBrukerSvarMenIkkeOmOppholdsTilatelseSkalGiUAVKLART.json |
      | false     | TODAYS_DATE | 2026-02-26  | 2023-02-26  | UAVKLART | SP6231 | BrukerBrudd_REGEL19_3_1_MedBrukerSvarMenIkkeOmOppholdsTilatelseSkalGiUAVKLART.json |
      | false     | 2023-02-26  | TODAYS_DATE | 2023-02-26  | UAVKLART | SP6241 | BrukerBrudd_REGEL19_3_1_MedBrukerSvarMenIkkeOmOppholdsTilatelseSkalGiUAVKLART.json |
