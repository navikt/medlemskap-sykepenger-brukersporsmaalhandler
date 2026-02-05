# language: no
# encoding: UTF-8


Egenskap: Flyt test av Oppholdstilatelse

  Scenariomal: Oppholdstilatelse blir kalt uten brukerspørsmål om oppholdstilatelse skal regelflyt svare JA
    Gitt gammelt resultat for gammel kjøring er "<FIL>"
    Når oppholdstilatelseRegler kjøres
    Så skal resultat av regel være "<Resultat>"

    Eksempler:
      | Resultat | ÅRSAK | FIL                                                                                |
      | JA       |       | BrukerBrudd_REGEL19_3_1_MedBrukerSvarMenIkkeOmOppholdsTilatelseSkalGiUAVKLART.json |


  Scenariomal: Oppholdstilatelse blir kalt med brukerspørsmål om oppholdstilatelse
    Gitt følgende brukersvar om oppholdstillatelse
      | Vedtakstype permanent | fom   | tom   | Vedtaksdato | Svar |
      | <Permanent>           | <FOM> | <TOM> | <VDATO>     | Ja   |

    Gitt gammelt resultat for gammel kjøring er "<FIL>"
    Når oppholdstilatelseRegler kjøres
    Så skal resultat av regel være "<Resultat>"
    Og årsak etter regelkjøring er "<ÅRSAK>"

    Eksempler:
      | Permanent | FOM        | TOM        | VDATO      | Resultat | ÅRSAK  | FIL                                                                  |
      | false     | 2023-05-14 | 2024-05-14 | 2023-05-14 | JA       | null   | Regel19_3_1_Brudd_med_PDL_OppholdsDataOgIngenNyeBrukerSporsmaal.json |
      | false     | 2023-05-14 | 2024-05-10 | 2023-05-14 | UAVKLART | SP6225 | Regel19_3_1_Brudd_med_PDL_OppholdsDataOgIngenNyeBrukerSporsmaal.json |
      | false     | 2023-01-01 | 2024-05-14 | 2023-05-14 | JA       | null   | Regel19_3_1_Brudd_med_PDL_OppholdsDataOgIngenNyeBrukerSporsmaal.json |