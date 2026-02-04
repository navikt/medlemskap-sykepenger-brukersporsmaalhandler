# language: no
# encoding: UTF-8


Egenskap: SP6510 RegelUtsjekk Norske borgere

  Scenariomal: SP6510 bli kalt med nye brukerspørsmål og bare regel 3 bryter
    Gitt årsaker i gammel kjøring
      | REGELBRUDD |
      | REGEL_3    |

    Og utfoertArbeidUtenforNorge
      | Fra og med dato | Til og med dato | Har hatt arbeid utenfor Norge | LAND   |
      | null            | null            | <arbeidUtenforNorge>          | <LAND> |

    Og OppholdUtenforEos
      | Fra og med dato | Til og med dato | Har oppholdt seg utenfor EØS | LAND     |
      | null            | null            | <oppholdUtenforEOS>          | THAILAND |


    Når regel "SP6510" kjøres
    Så skal resultat av regel være "<Resultat>"

    Eksempler:
      | Resultat | arbeidUtenforNorge | oppholdUtenforEOS |
      | JA       | true               | true              |
      | JA       | false              | false             |
      | JA       | false              | true              |
      | JA       | true               | false             |


  Scenariomal: SP6510 bli kalt med nye brukerspørsmål der flere regler bryter
    Gitt årsaker i gammel kjøring
      | REGELBRUDD |
      | REGEL_3    |
      | REGEL_19_3 |

    Og utfoertArbeidUtenforNorge
      | Fra og med dato | Til og med dato | Har hatt arbeid utenfor Norge | LAND   |
      | null            | null            | <arbeidUtenforNorge>          | <LAND> |

    Og OppholdUtenforEos
      | Fra og med dato | Til og med dato | Har oppholdt seg utenfor EØS | LAND     |
      | null            | null            | <oppholdUtenforEOS>          | THAILAND |


    Når regel "SP6510" kjøres
    Så skal resultat av regel være "<Resultat>"

    Eksempler:
      | Resultat | arbeidUtenforNorge | oppholdUtenforEOS |
      | NEI      | true               | true              |
      | NEI      | false              | false             |
      | NEI      | false              | true              |
      | NEI      | true               | false             |

  Scenariomal: SP6510 bli kalt med nye brukerspørsmål der  regleL 3 og REGEL_9 bryter
    Gitt årsaker i gammel kjøring
      | REGELBRUDD |
      | REGEL_3    |
      | REGEL_9    |

    Og utfoertArbeidUtenforNorge
      | Fra og med dato | Til og med dato | Har hatt arbeid utenfor Norge | LAND   |
      | null            | null            | <arbeidUtenforNorge>          | <LAND> |

    Og OppholdUtenforEos
      | Fra og med dato | Til og med dato | Har oppholdt seg utenfor EØS | LAND     |
      | null            | null            | <oppholdUtenforEOS>          | THAILAND |


    Når regel "SP6510" kjøres
    Så skal resultat av regel være "<Resultat>"

    Eksempler:
      | Resultat | arbeidUtenforNorge | oppholdUtenforEOS |
      | JA      | true               | true              |
      | JA       | false              | false             |
      | JA       | false              | true              |
      | JA      | true               | false             |

  Scenariomal: SP6510 bli kalt med nye brukerspørsmål der REGEL_15 bryter
    Gitt årsaker i gammel kjøring
      | REGELBRUDD |
      | REGEL_15   |

    Og utfoertArbeidUtenforNorge
      | Fra og med dato | Til og med dato | Har hatt arbeid utenfor Norge | LAND   |
      | null            | null            | <arbeidUtenforNorge>          | <LAND> |

    Og OppholdUtenforEos
      | Fra og med dato | Til og med dato | Har oppholdt seg utenfor EØS | LAND     |
      | null            | null            | <oppholdUtenforEOS>          | THAILAND |


    Når regel "SP6510" kjøres
    Så skal resultat av regel være "<Resultat>"

    Eksempler:
      | Resultat | arbeidUtenforNorge | oppholdUtenforEOS |
      | JA      | true               | true              |
      | JA       | false              | false             |
      | JA      | false              | true              |
      | JA      | true               | false             |

  Scenariomal: SP6510 bli kalt med nye brukerspørsmål der REGEL_C bryter
    Gitt årsaker i gammel kjøring
      | REGELBRUDD |
      | REGEL_C   |

    Og utfoertArbeidUtenforNorge
      | Fra og med dato | Til og med dato | Har hatt arbeid utenfor Norge | LAND   |
      | null            | null            | <arbeidUtenforNorge>          | <LAND> |

    Og OppholdUtenforEos
      | Fra og med dato | Til og med dato | Har oppholdt seg utenfor EØS | LAND     |
      | null            | null            | <oppholdUtenforEOS>          | THAILAND |


    Når regel "SP6510" kjøres
    Så skal resultat av regel være "<Resultat>"

    Eksempler:
      | Resultat | arbeidUtenforNorge | oppholdUtenforEOS |
      | JA      | true               | true              |
      | JA       | false              | false             |
      | JA       | false              | true              |
      | JA      | true               | false             |

  Scenariomal: SP6510 bli kalt med nye brukerspørsmål der REGEL_X bryter
    Gitt årsaker i gammel kjøring
      | REGELBRUDD |
      | REGEL_X   |

    Og utfoertArbeidUtenforNorge
      | Fra og med dato | Til og med dato | Har hatt arbeid utenfor Norge | LAND   |
      | null            | null            | <arbeidUtenforNorge>          | <LAND> |

    Og OppholdUtenforEos
      | Fra og med dato | Til og med dato | Har oppholdt seg utenfor EØS | LAND     |
      | null            | null            | <oppholdUtenforEOS>          | THAILAND |


    Når regel "SP6510" kjøres
    Så skal resultat av regel være "<Resultat>"

    Eksempler:
      | Resultat | arbeidUtenforNorge | oppholdUtenforEOS |
      | NEI      | true               | true              |
      | NEI      | false              | false             |
      | NEI      | false              | true              |
      | NEI      | true               | false             |


  Scenariomal: SP6510 bli kalt med nye brukerspørsmål der REGEL_11 bryter
    Gitt årsaker i gammel kjøring
      | REGELBRUDD     |
      | REGEL_11_2     |
      | REGEL_11_3     |
      | REGEL_11_4     |
      | REGEL_11_2_2_1 |

    Og utfoertArbeidUtenforNorge
      | Fra og med dato | Til og med dato | Har hatt arbeid utenfor Norge | LAND   |
      | null            | null            | <arbeidUtenforNorge>          | <LAND> |

    Og OppholdUtenforEos
      | Fra og med dato | Til og med dato | Har oppholdt seg utenfor EØS | LAND     |
      | null            | null            | <oppholdUtenforEOS>          | THAILAND |


    Når regel "SP6510" kjøres
    Så skal resultat av regel være "<Resultat>"

    Eksempler:
      | Resultat | arbeidUtenforNorge | oppholdUtenforEOS |
      | JA      | true               | true              |
      | JA      | false              | false             |
      | JA      | false              | true              |
      | JA      | true               | false             |

  Scenariomal: SP6510 bli kalt med nye brukerspørsmål der REGEL_12 bryter
    Gitt årsaker i gammel kjøring
      | REGELBRUDD     |
      | REGEL_12       |


    Og utfoertArbeidUtenforNorge
      | Fra og med dato | Til og med dato | Har hatt arbeid utenfor Norge | LAND   |
      | null            | null            | <arbeidUtenforNorge>          | <LAND> |

    Og OppholdUtenforEos
      | Fra og med dato | Til og med dato | Har oppholdt seg utenfor EØS | LAND     |
      | null            | null            | <oppholdUtenforEOS>          | THAILAND |


    Når regel "SP6510" kjøres
    Så skal resultat av regel være "<Resultat>"

    Eksempler:
      | Resultat | arbeidUtenforNorge | oppholdUtenforEOS |
      | JA      | true               | true              |
      | JA      | false              | false             |
      | JA      | false              | true              |
      | JA      | true               | false             |

  Scenariomal: SP6510 bli kalt med nye brukerspørsmål der REGEL_21 bryter
    Gitt årsaker i gammel kjøring
      | REGELBRUDD |
      | REGEL_21   |

    Og utfoertArbeidUtenforNorge
      | Fra og med dato | Til og med dato | Har hatt arbeid utenfor Norge | LAND   |
      | null            | null            | <arbeidUtenforNorge>          | <LAND> |

    Og OppholdUtenforEos
      | Fra og med dato | Til og med dato | Har oppholdt seg utenfor EØS | LAND     |
      | null            | null            | <oppholdUtenforEOS>          | THAILAND |


    Når regel "SP6510" kjøres
    Så skal resultat av regel være "<Resultat>"

    Eksempler:
      | Resultat | arbeidUtenforNorge | oppholdUtenforEOS |
      | JA      | true               | true              |
      | JA      | false              | false             |
      | JA      | false              | true              |
      | JA      | true               | false             |

  Scenariomal: SP6510 bli kalt med nye brukerspørsmål der REGEL_25 bryter
    Gitt årsaker i gammel kjøring
      | REGELBRUDD |
      | REGEL_25   |

    Og utfoertArbeidUtenforNorge
      | Fra og med dato | Til og med dato | Har hatt arbeid utenfor Norge | LAND   |
      | null            | null            | <arbeidUtenforNorge>          | <LAND> |

    Og OppholdUtenforEos
      | Fra og med dato | Til og med dato | Har oppholdt seg utenfor EØS | LAND     |
      | null            | null            | <oppholdUtenforEOS>          | THAILAND |


    Når regel "SP6510" kjøres
    Så skal resultat av regel være "<Resultat>"

    Eksempler:
      | Resultat | arbeidUtenforNorge | oppholdUtenforEOS |
      | JA      | true               | true              |
      | JA      | false              | false             |
      | JA      | false              | true              |
      | JA      | true               | false             |

  Scenariomal: SP6510 bli kalt med nye brukerspørsmål der REGEL_10 bryter
    Gitt årsaker i gammel kjøring
      | REGELBRUDD |
      | REGEL_10   |

    Og utfoertArbeidUtenforNorge
      | Fra og med dato | Til og med dato | Har hatt arbeid utenfor Norge | LAND   |
      | null            | null            | <arbeidUtenforNorge>          | <LAND> |

    Og OppholdUtenforEos
      | Fra og med dato | Til og med dato | Har oppholdt seg utenfor EØS | LAND     |
      | null            | null            | <oppholdUtenforEOS>          | THAILAND |


    Når regel "SP6510" kjøres
    Så skal resultat av regel være "<Resultat>"

    Eksempler:
      | Resultat | arbeidUtenforNorge | oppholdUtenforEOS |
      | JA      | true               | true              |
      | JA      | false              | false             |
      | JA      | false              | true              |
      | JA      | true               | false             |

  Scenariomal: SP6510 bli kalt med nye brukerspørsmål der REGEL_5 bryter
    Gitt årsaker i gammel kjøring
      | REGELBRUDD |
      | REGEL_5   |

    Og utfoertArbeidUtenforNorge
      | Fra og med dato | Til og med dato | Har hatt arbeid utenfor Norge | LAND   |
      | null            | null            | <arbeidUtenforNorge>          | <LAND> |

    Og OppholdUtenforEos
      | Fra og med dato | Til og med dato | Har oppholdt seg utenfor EØS | LAND     |
      | null            | null            | <oppholdUtenforEOS>          | THAILAND |


    Når regel "SP6510" kjøres
    Så skal resultat av regel være "<Resultat>"

    Eksempler:
      | Resultat | arbeidUtenforNorge | oppholdUtenforEOS |
      | JA      | true               | true              |
      | JA      | false              | false             |
      | JA      | false              | true              |
      | JA      | true               | false             |