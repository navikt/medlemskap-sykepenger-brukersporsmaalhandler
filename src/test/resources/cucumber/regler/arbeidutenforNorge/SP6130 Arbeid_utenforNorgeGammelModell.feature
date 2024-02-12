# language: no
# encoding: UTF-8


Egenskap: SP6130 Arbeid utenfor Norge gammel modell (dvs at bruker ikke har fått nye brukerspørmål)

  Scenariomal: SP6000 bli kalt med gammel modell for Arbeid utland
    Gitt arbeidUtenforNorgeGammelModell er "<arbeidUtenforNorge>"

    Når regel "SP6130" kjøres
    Så skal resultat av regel være  være "<Resultat>"

    Eksempler:
      | Resultat | arbeidUtenforNorge |
      | JA       | true               |
      | NEI      | false              |
