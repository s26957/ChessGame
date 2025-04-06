package szachy;

import szachy.figury.*;

import java.util.ArrayList;
import java.util.List;

public class PoczatkoweUstawienieFigur {

    public static List<Figura> getPoczatkoweFigury(Kolor kolor_gracza) {
        List<Figura> figury = new ArrayList<>();
        int wiersz_figur = kolor_gracza == Kolor.BIALY ? 1 : 8;
        int wiersz_pionow = kolor_gracza == Kolor.BIALY ? 2 : 7;
        figury.add(new Wieza(new Pozycja(1, wiersz_figur)));
        figury.add(new Wieza(new Pozycja(8, wiersz_figur)));
        figury.add(new Goniec(new Pozycja(3, wiersz_figur)));
        figury.add(new Goniec(new Pozycja(6, wiersz_figur)));
        figury.add(new Skoczek(new Pozycja(2, wiersz_figur)));
        figury.add(new Skoczek(new Pozycja(7, wiersz_figur)));
        figury.add(new Krolowa(new Pozycja(4, wiersz_figur)));
        figury.add(new Krol(new Pozycja(5, wiersz_figur)));

        for (int i = 8; i < 16; i++) {
            figury.add(new Pionek(new Pozycja(i - 7, wiersz_pionow)));
        }

        if (kolor_gracza.equals(Kolor.CZARNY)) {
            ustawFiguryNaCzarne(figury);
        }

        return figury;
    }

    private static void ustawFiguryNaCzarne(List<Figura> figury) {
        for (Figura f : figury) {
            f.setCzarna();
        }
    }

}