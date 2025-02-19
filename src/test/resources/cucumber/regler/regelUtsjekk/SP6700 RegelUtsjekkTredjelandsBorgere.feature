# language: no
# encoding: UTF-8


Egenskap: 6700 RegelUtsjekk Norske borgere

  Scenariomal: SP6700 bli kalt med nye brukerspørsmål og bare regel 3 bryter
    Gitt årsaker i gammel kjøring
      | REGELBRUDD |
      | REGEL_3    |

    Og utfoertArbeidUtenforNorge
      | Fra og med dato | Til og med dato | Har hatt arbeid utenfor Norge | LAND   |
      | null            | null            | <arbeidUtenforNorge>          | <LAND> |

    Og OppholdUtenforNorge
      | Fra og med dato | Til og med dato | Har oppholdt seg utenfor EØS | LAND     |
      | null            | null            | <OppholdUtenforNorge>          | THAILAND |


    Når regel "SP6700" kjøres
    Så skal resultat av regel være  være "<Resultat>"

    Eksempler:
      | Resultat | arbeidUtenforNorge | OppholdUtenforNorge |
      | JA       | true               | true              |
      | JA       | false              | false             |
      | JA       | false              | true              |
      | JA       | true               | false             |


  Scenariomal: SP6700 bli kalt med nye brukerspørsmål der flere regler bryter
    Gitt årsaker i gammel kjøring
      | REGELBRUDD |
      | REGEL_3    |
      | REGEL_19_3 |

    Og utfoertArbeidUtenforNorge
      | Fra og med dato | Til og med dato | Har hatt arbeid utenfor Norge | LAND   |
      | null            | null            | <arbeidUtenforNorge>          | <LAND> |

    Og OppholdUtenforNorge
      | Fra og med dato | Til og med dato | Har oppholdt seg utenfor EØS | LAND     |
      | null            | null            | <OppholdUtenforNorge>          | THAILAND |


    Når regel "SP6700" kjøres
    Så skal resultat av regel være  være "<Resultat>"

    Eksempler:
      | Resultat | arbeidUtenforNorge | OppholdUtenforNorge |
      | NEI      | true               | true              |
      | NEI      | false              | false             |
      | NEI      | false              | true              |
      | NEI      | true               | false             |

  Scenariomal: SP6700 bli kalt med nye brukerspørsmål der  regleL 3 og REGEL_9 bryter
    Gitt årsaker i gammel kjøring
      | REGELBRUDD |
      | REGEL_3    |
      | REGEL_9    |

    Og utfoertArbeidUtenforNorge
      | Fra og med dato | Til og med dato | Har hatt arbeid utenfor Norge | LAND   |
      | null            | null            | <arbeidUtenforNorge>          | <LAND> |

    Og OppholdUtenforNorge
      | Fra og med dato | Til og med dato | Har oppholdt seg utenfor EØS | LAND     |
      | null            | null            | <OppholdUtenforNorge>          | THAILAND |


    Når regel "SP6700" kjøres
    Så skal resultat av regel være  være "<Resultat>"

    Eksempler:
      | Resultat | arbeidUtenforNorge | OppholdUtenforNorge |
      | JA      | true               | true              |
      | JA       | false              | false             |
      | JA       | false              | true              |
      | JA      | true               | false             |

  Scenariomal: SP6700 bli kalt med nye brukerspørsmål der REGEL_15 bryter
    Gitt årsaker i gammel kjøring
      | REGELBRUDD |
      | REGEL_15   |

    Og utfoertArbeidUtenforNorge
      | Fra og med dato | Til og med dato | Har hatt arbeid utenfor Norge | LAND   |
      | null            | null            | <arbeidUtenforNorge>          | <LAND> |

    Og OppholdUtenforNorge
      | Fra og med dato | Til og med dato | Har oppholdt seg utenfor EØS | LAND     |
      | null            | null            | <OppholdUtenforNorge>          | THAILAND |


    Når regel "SP6700" kjøres
    Så skal resultat av regel være  være "<Resultat>"

    Eksempler:
      | Resultat | arbeidUtenforNorge | OppholdUtenforNorge |
      | JA      | true               | true              |
      | JA       | false              | false             |
      | JA      | false              | true              |
      | JA      | true               | false             |

  Scenariomal: SP6700 bli kalt med nye brukerspørsmål der REGEL_C bryter
    Gitt årsaker i gammel kjøring
      | REGELBRUDD |
      | REGEL_C   |

    Og utfoertArbeidUtenforNorge
      | Fra og med dato | Til og med dato | Har hatt arbeid utenfor Norge | LAND   |
      | null            | null            | <arbeidUtenforNorge>          | <LAND> |

    Og OppholdUtenforNorge
      | Fra og med dato | Til og med dato | Har oppholdt seg utenfor EØS | LAND     |
      | null            | null            | <OppholdUtenforNorge>          | THAILAND |


    Når regel "SP6700" kjøres
    Så skal resultat av regel være  være "<Resultat>"

    Eksempler:
      | Resultat | arbeidUtenforNorge | OppholdUtenforNorge |
      | JA      | true               | true              |
      | JA       | false              | false             |
      | JA       | false              | true              |
      | JA      | true               | false             |

  Scenariomal: SP6700 bli kalt med nye brukerspørsmål der REGEL_X bryter
    Gitt årsaker i gammel kjøring
      | REGELBRUDD |
      | REGEL_X   |

    Og utfoertArbeidUtenforNorge
      | Fra og med dato | Til og med dato | Har hatt arbeid utenfor Norge | LAND   |
      | null            | null            | <arbeidUtenforNorge>          | <LAND> |

    Og OppholdUtenforNorge
      | Fra og med dato | Til og med dato | Har oppholdt seg utenfor EØS | LAND     |
      | null            | null            | <OppholdUtenforNorge>          | THAILAND |


    Når regel "SP6700" kjøres
    Så skal resultat av regel være  være "<Resultat>"

    Eksempler:
      | Resultat | arbeidUtenforNorge | OppholdUtenforNorge |
      | NEI      | true               | true              |
      | NEI      | false              | false             |
      | NEI      | false              | true              |
      | NEI      | true               | false             |



  Scenariomal: SP6700 bli kalt med nye brukerspørsmål der REGEL_12 bryter
    Gitt årsaker i gammel kjøring
      | REGELBRUDD     |
      | REGEL_12       |


    Og utfoertArbeidUtenforNorge
      | Fra og med dato | Til og med dato | Har hatt arbeid utenfor Norge | LAND   |
      | null            | null            | <arbeidUtenforNorge>          | <LAND> |

    Og OppholdUtenforNorge
      | Fra og med dato | Til og med dato | Har oppholdt seg utenfor EØS | LAND     |
      | null            | null            | <OppholdUtenforNorge>          | THAILAND |


    Når regel "SP6700" kjøres
    Så skal resultat av regel være  være "<Resultat>"

    Eksempler:
      | Resultat | arbeidUtenforNorge | OppholdUtenforNorge |
      | NEI     | true               | true              |
      | NEI     | false              | false             |
      | NEI     | false              | true              |
      | NEI     | true               | false             |

  Scenariomal: SP6700 bli kalt med nye brukerspørsmål der REGEL_20 bryter
    Gitt årsaker i gammel kjøring
      | REGELBRUDD |
      | REGEL_20   |

    Og utfoertArbeidUtenforNorge
      | Fra og med dato | Til og med dato | Har hatt arbeid utenfor Norge | LAND   |
      | null            | null            | <arbeidUtenforNorge>          | <LAND> |

    Og OppholdUtenforNorge
      | Fra og med dato | Til og med dato | Har oppholdt seg utenfor EØS | LAND     |
      | null            | null            | <OppholdUtenforNorge>          | THAILAND |


    Når regel "SP6700" kjøres
    Så skal resultat av regel være  være "<Resultat>"

    Eksempler:
      | Resultat | arbeidUtenforNorge | OppholdUtenforNorge |
      | JA      | true               | true              |
      | JA      | false              | false             |
      | JA      | false              | true              |
      | JA      | true               | false             |

  Scenariomal: SP6700 bli kalt med nye brukerspørsmål der REGEL_34 bryter
    Gitt årsaker i gammel kjøring
      | REGELBRUDD |
      | REGEL_34   |

    Og utfoertArbeidUtenforNorge
      | Fra og med dato | Til og med dato | Har hatt arbeid utenfor Norge | LAND   |
      | null            | null            | <arbeidUtenforNorge>          | <LAND> |

    Og OppholdUtenforNorge
      | Fra og med dato | Til og med dato | Har oppholdt seg utenfor EØS | LAND     |
      | null            | null            | <OppholdUtenforNorge>          | THAILAND |


    Når regel "SP6700" kjøres
    Så skal resultat av regel være  være "<Resultat>"

    Eksempler:
      | Resultat | arbeidUtenforNorge | OppholdUtenforNorge |
      | JA      | true               | true              |
      | JA      | false              | false             |
      | JA      | false              | true              |
      | JA      | true               | false             |