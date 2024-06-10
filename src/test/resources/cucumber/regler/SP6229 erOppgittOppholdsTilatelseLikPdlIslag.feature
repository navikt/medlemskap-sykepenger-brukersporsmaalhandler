# language: no
# encoding: UTF-8


Egenskap: SP6229 Validering av PDL data

  Scenariomal: Regelkjøreing for SP6229.

    Gitt brukersvar om oppholdstitatelse
      | Permanent   | FOM   | TOM   | VDATO   |
      | <Permanent> | <FOM> | <TOM> | <VDATO> |

    Og pdlOpplysninger om oppholdstilatelse
      | TYPE   | PDL_FOM   | PDL_TOM   |
      | MIDLERTIDIG   | <PDL_FOM> | <PDL_TOM> |

    Når regel "SP6229" kjøres
    Så skal resultat av regel være  være "<Resultat>"

    Eksempler:
      | Permanent | FOM         | TOM         | VDATO       | Resultat |  PDL_FOM          |   PDL_TOM          |
      | false     | 2023-02-26  | 2026-02-26  | 2023-02-26  | JA       |   2023-02-26      | 2026-02-26         |


  Scenariomal: Regelkjøreing for SP6229 med flere innslag i pdl_oppholdstilatelse

    Gitt brukersvar om oppholdstitatelse
      | Permanent   | FOM   | TOM   | VDATO   |
      | <Permanent> | <FOM> | <TOM> | <VDATO> |

    Og pdlOpplysninger om oppholdstilatelse med flere innslag
      | TYPE   | PDL_FOM   | PDL_TOM   |
      | MIDLERTIDIG   | 2023-02-26 | 2026-02-26  |
      | MIDLERTIDIG   | 2018-02-26 | 2019-02-26  |
      | MIDLERTIDIG   | 2020-02-26 | 2021-02-26  |
      | MIDLERTIDIG   | 2022-02-26 | 2023-02-26  |

    Når regel "SP6229" kjøres
    Så skal resultat av regel være  være "<Resultat>"

    Eksempler:
      | Permanent | FOM         | TOM         | VDATO       | Resultat |
      | false     | 2023-02-26  | 2026-02-26  | 2023-02-26  | JA       |
      | false     | 2023-02-20  | 2026-02-26  | 2023-02-26  | NEI      |
      | false     | 2023-02-26  | 2026-02-20  | 2023-02-26  | NEI      |

