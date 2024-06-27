# language: no
# encoding: UTF-8


Egenskap: SP6225 Validering av PDL data

  Scenariomal: Regelkjøreing for SP6225.

    Gitt brukersvar om oppholdstitatelse
      | Permanent   | FOM   | TOM   | VDATO   |
      | <Permanent> | <FOM> | <TOM> | <VDATO> |

    Og UDIOpplysninger om oppholdstilatelse
      | TYPE   | UDI_FOM   | UDI_TOM   |
      | MIDLERTIDIG   | <UDI_FOM> | <UDI_TOM> |

    Når regel "SP6225" kjøres
    Så skal resultat av regel være  være "<Resultat>"

    Eksempler:
      | Permanent | FOM         | TOM         | VDATO       | Resultat |  UDI_FOM          |   UDI_TOM          |
      | false     | 2023-02-26  | 2026-02-26  | 2023-02-26  | NEI      |   2023-02-26      | 2026-02-26         |
      | false     | 2022-02-26  | 2023-02-26  | 2023-02-26  | JA       |   2023-02-26      | 2026-02-26         |



