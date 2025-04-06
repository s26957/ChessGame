package szachy;

import szachy.figury.Figura;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface Plansza {
    boolean ruchSieWykonal(Pozycja pozycja, Pozycja pozycja_docelowa, Gracz gracz);

    void zapisDoPliku() throws IOException;

    void wyswietl();

    Gracz[] getGracze();

    Figura figuraAt(Pozycja pozycja_docelowa);

    boolean jestMat(int nr_gracza);

    void wczytaj() throws IOException;
}
