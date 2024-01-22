# language: no
# encoding: UTF-8

# language: no
# encoding: UTF-8

Egenskap: test2

  Scenariomal: test
    Når medlemskap beregnes med følgende parametre
      | Fra og med dato | Til og med dato | Har hatt arbeid utenfor Norge |
      | 27.01.2021      | 12.02.2021      | <Arbeid utenfor Norge>                            |
    Så skal svar være <Svar>

    Eksempler:
      | Arbeid utenfor Norge | Svar     |
      | Ja                   | "NOT_OK"   |
      | Nei                  | "OK"       |