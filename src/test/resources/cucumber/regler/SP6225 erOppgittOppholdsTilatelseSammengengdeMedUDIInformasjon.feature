# language: no
# encoding: UTF-8


Egenskap: SP6225 sammenslåing av bruker data og data fra UDI

  Scenariomal: Regelkjøreing for SP6225 med to midlertidige oppholdstilatelser

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



  Scenariomal: Regelkjøreing for SP6225 med bruker innput midlertidig og UDI PERMANENT

    Gitt brukersvar om oppholdstitatelse
      | Permanent   | FOM   | TOM   | VDATO   |
      | <Permanent> | <FOM> | <TOM> | <VDATO> |

    Og UDIOpplysninger om oppholdstilatelse
      | TYPE   | UDI_FOM   | UDI_TOM   |
      | PERMANENT   | <UDI_FOM> | <UDI_TOM> |

    Når regel "SP6225" kjøres
    Så skal resultat av regel være  være "<Resultat>"

    Eksempler:
      | Permanent | FOM         | TOM         | VDATO       | Resultat |  UDI_FOM          |   UDI_TOM          |
      | false     | 2023-01-01  | 2025-01-01  | 2023-01-01  | JA       |   2024-01-01      | NULL               |
      | false     | 2023-01-01  | 2023-12-30  | 2023-01-01  | NEI      |   2024-01-01      | NULL               |