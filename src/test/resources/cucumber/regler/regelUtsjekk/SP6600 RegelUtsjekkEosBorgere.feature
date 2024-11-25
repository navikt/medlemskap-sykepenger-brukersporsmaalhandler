# language: no
# encoding: UTF-8


Egenskap: SP6600 RegelUtsjekk Eos borgere

  Scenariomal: SP6600 bli kalt med nye brukerspørsmål og bare regel 3 bryter
    Gitt årsaker i gammel kjøring
      | REGELBRUDD |
      | REGEL_3    |

    Og utfoertArbeidUtenforNorge
      | Fra og med dato | Til og med dato | Har hatt arbeid utenfor Norge | LAND   |
      | null            | null            | <arbeidUtenforNorge>          | <LAND> |

    Og OppholdUtenforEos
      | Fra og med dato | Til og med dato | Har oppholdt seg utenfor EØS | LAND     |
      | null            | null            | <oppholdUtenforEOS>          | THAILAND |


    Når regel "SP6600" kjøres
    Så skal resultat av regel være  være "<Resultat>"

    Eksempler:
      | Resultat | arbeidUtenforNorge | oppholdUtenforEOS |
      | NEI      | true               | true              |
      | JA       | false              | false             |
      | JA       | false              | true              |
      | NEI      | true               | false             |


  Scenariomal: SP6600 bli kalt med nye brukerspørsmål der flere regler bryter
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


    Når regel "SP6600" kjøres
    Så skal resultat av regel være  være "<Resultat>"

    Eksempler:
      | Resultat | arbeidUtenforNorge | oppholdUtenforEOS |
      | NEI      | true               | true              |
      | NEI      | false              | false             |
      | NEI      | false              | true              |
      | NEI      | true               | false             |

  Scenariomal: SP6600 bli kalt med nye brukerspørsmål der  regleL 3 og REGEL_9 bryter
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


    Når regel "SP6600" kjøres
    Så skal resultat av regel være  være "<Resultat>"

    Eksempler:
      | Resultat | arbeidUtenforNorge | oppholdUtenforEOS |
      | NEI      | true               | true              |
      | JA       | false              | false             |
      | JA       | false              | true              |
      | NEI      | true               | false             |

  Scenariomal: SP6600 bli kalt med nye brukerspørsmål der regel 11.2.3 bryter
    Gitt årsaker i gammel kjøring
      | REGELBRUDD    |
      | REGEL_3       |
      | REGEL_11_2_3  |

    Og utfoertArbeidUtenforNorge
      | Fra og med dato | Til og med dato | Har hatt arbeid utenfor Norge | LAND   |
      | null            | null            | <arbeidUtenforNorge>          | <LAND> |

    Og OppholdUtenforEos
      | Fra og med dato | Til og med dato | Har oppholdt seg utenfor EØS | LAND     |
      | null            | null            | <oppholdUtenforEOS>          | THAILAND |


    Når regel "SP6600" kjøres
    Så skal resultat av regel være  være "<Resultat>"

    Eksempler:
      | Resultat | arbeidUtenforNorge | oppholdUtenforEOS |
      | NEI      | true               | true              |
      | NEI      | false              | false             |
      | NEI      | false              | true              |
      | NEI      | true               | false             |
