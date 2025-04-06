package szachy.figury;

import szachy.Gracz;
import szachy.Plansza;
import szachy.Pozycja;

import java.util.List;

public interface Figura {
    List<Pozycja> getDopuszczalnePola();

    String getZnak();

    boolean jestBiala();

    void setCzarna();

    Pozycja getPozycja();

    TypFigury getType();

    boolean exists();

    void zbij();

    void przesun(Pozycja pozycja_docelowa);

    boolean mozliwoscRuchu(Pozycja pozycja_docelowa, Gracz gracz, Plansza plansza);

    boolean trasaWolna(Pozycja[] trasa, Plansza plansza);

    Pozycja[] trasa(Pozycja pozycja_docelowa);

    //dla "specjalnych" przypadkow:
    void roszada(Pozycja pozycja_docelowa);

    boolean bylaRuszona();

    boolean mozliwoscPodamiany();

    void setStan(boolean b); //stan tzn. ruszona czy nie
}
