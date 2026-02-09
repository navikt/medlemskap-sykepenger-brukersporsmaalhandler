# language: no
# encoding: UTF-8

Egenskap: SP6226 sammenslåing av UDIdata og brukerspørsmål

  Scenariomal: Regelkjøring for SP6226.

    Gitt følgende brukersvar om oppholdstillatelse
      | Vedtakstype permanent | fom   | tom   | Vedtaksdato | Svar |
      | <Permanent>           | <FOM> | <TOM> | <VDATO>     | Ja   |

    Og følgende oppholdstillatelse fra UDI
      | Type        | fom       | tom       |
      | MIDLERTIDIG | <UDI_FOM> | <UDI_TOM> |

    Når regel "SP6226" kjøres
    Så skal resultat av regel være "<Resultat>"

    Eksempler:
      | Permanent | FOM        | TOM        | VDATO      | Resultat | UDI_FOM    | UDI_TOM    |                                                                                                             |
#| false     | 2023-02-26  | 2026-02-26  | 2023-02-26  | NEI      |   2023-02-26      | 2026-02-26         ||
      | false     | 2022-02-26 | 2023-02-26 | 2023-02-26 | JA       | 2023-02-26 | 2026-05-26 | UDI_TOM er satt langt frem i tid som en midlertidig håndtering slik at testen ikke vil feile i snar fremtid |
