package szachy.figury;

import szachy.Gracz;
import szachy.Plansza;
import szachy.Pozycja;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public abstract class FiguraAbs implements Figura {
    private Pozycja pozycja;
    private TypFigury typFigury;
    private boolean jest_biala;

    //domyslnie tworzona figura jest biala
    public FiguraAbs(Pozycja pozycja) {
        this.pozycja = pozycja;
        jest_biala = true;
    }
    @Override
    public abstract List<Pozycja> getDopuszczalnePola();
    @Override
    public abstract String getZnak();
    @Override
    public Pozycja getPozycja() {
        return pozycja;
    }
    @Override
    public TypFigury getType() {
        return typFigury;
    }

    @Override
    public boolean jestBiala() {
        return jest_biala;
    }

    public void setType(TypFigury typFigury) {
        this.typFigury = typFigury;
    }
    @Override
    public void setCzarna() {
        jest_biala = false;
    }

    @Override
    public void setStan(boolean czy_byla_ruszona) {
    }

    @Override
    public boolean bylaRuszona() {
        return false;
    }

    @Override
    public boolean exists() {
        return true;
    }

    @Override
    public boolean mozliwoscPodamiany() {
        return false;
    }

    @Override
    public void roszada(Pozycja pozycja_docelowa) {
        throw new RuntimeException("Proba roszadowania nie roszadowalnej!");
    }

    public boolean equals(FiguraAbs f2) {
        return pozycja.equals(f2.getPozycja());
    }

    @Override
    public void przesun(Pozycja pozycja_docelowa) {
        this.pozycja = pozycja_docelowa;
    }
    @Override
    public void zbij() {
        this.przesun(new Pozycja(0, 0));
    }

    public boolean biciePionem(Pozycja pozycja_docelowa, Gracz gracz, Plansza plansza) {
        //umozliwienie ruchu po przekatnej dla piona jezeli na polu znajduje sie figura i nalezy do innego gracza
        if (this.getType() == TypFigury.PIONEK) {
            if (plansza.figuraAt(pozycja_docelowa).exists() && !gracz.maFigure(plansza.figuraAt(pozycja_docelowa))) {
                return (abs(getPozycja().getX() - pozycja_docelowa.getX())) == 1 && abs(getPozycja().getY() - pozycja_docelowa.getY()) == 1;
            }
        }
        return false;
    }

    @Override
    public boolean mozliwoscRuchu(Pozycja pozycja_docelowa, Gracz gracz, Plansza plansza) {
        /*gdy gracz rusza sie swoja figura, na trasie nie ma zadnych figur, na polu docelowym nie ma krola albo figury gracza
        i figura ma to pole w dopuszczalnych ruchach, albo przypadek specjalny - jest to bicie pionem  */
        return gracz.maFigure(this) &&
                trasaWolna(trasa(pozycja_docelowa), plansza) &&
                dostepnoscPolaDocelowego(plansza, pozycja_docelowa, gracz) &&
                (poleWDopuszczalnychRuchach(pozycja_docelowa) || biciePionem(pozycja_docelowa, gracz, plansza));
    }

    private boolean dostepnoscPolaDocelowego(Plansza plansza, Pozycja pole, Gracz gracz) //nie kolor i nie krol
    {
        return !(gracz.maFigure(plansza.figuraAt(pole)) || (plansza.figuraAt(pole).getType() == TypFigury.KROL));
    }

    public boolean poleWDopuszczalnychRuchach(Pozycja pozycja_docelowa) {
        boolean poleDopuszczalne = false;
        for (Pozycja pozycja : getDopuszczalnePola()) {
            if (pozycja.equals(pozycja_docelowa)) {
                return true;
            }
        }
        System.out.println("pole nie w dopuszczalnych ruchach");
        return false;
    }

    @Override
    public boolean trasaWolna(Pozycja[] trasa, Plansza plansza) {
        for (Pozycja pole : trasa) {
            if (plansza.figuraAt(pole).exists()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Pozycja[] trasa(Pozycja pozycja_docelowa) {

        int x = pozycja.getX();
        int y = pozycja.getY();

        int dx = pozycja_docelowa.getX() - x;
        int dy = pozycja_docelowa.getY() - y;

        int liczba_pol = Math.max(abs(dx), abs(dy)) - 1;

        Pozycja[] pola_na_trasie = new Pozycja[liczba_pol];
        for (int i = 0; i < pola_na_trasie.length; i++) {
            x = x + krokPrzyPrzesunieciu(dx);
            y = y + krokPrzyPrzesunieciu(dy);
            pola_na_trasie[i] = new Pozycja(x, y);
        }
        return pola_na_trasie;
    }

    private int krokPrzyPrzesunieciu(int delta) {
        if (delta > 0) {
            return 1;
        }
        if (delta < 0) {
            return -1;
        }
        return 0;
    }

    protected List<Pozycja> dopuszczalnePola(Kierunki kierunek) {
        return dopuszczalnePola(kierunek, 8);
    }

    protected List<Pozycja> dopuszczalnePola(Kierunki kierunek, int promien) {
        List<Pozycja> pola = new ArrayList<>();

        int l_krokow = 0;

        int przesuniecieX = 0;
        int przesuniecieY = 0;
        switch (kierunek) {
            case WzgledemOX -> przesuniecieX = 1;
            case WzgledemOY -> przesuniecieY = 1;
            case SkosLG_PD -> {
                przesuniecieX = 1;
                przesuniecieY = -1;
            }
            case SkosLD_PG -> {
                przesuniecieX = 1;
                przesuniecieY = 1;
            }
        }

        //uzupelnianie polami "ponizej":
        int x = getPozycja().getX() - przesuniecieX;
        int y = getPozycja().getY() - przesuniecieY;

        while (indeksWPlanszy(x) && indeksWPlanszy(y) && l_krokow < promien) {
            pola.add(new Pozycja(x, y));
            x = x - przesuniecieX;
            y = y - przesuniecieY;
            l_krokow++;
        }

        //uzupelnianie polami "powyzej":
        l_krokow = 0;
        x = getPozycja().getX() + przesuniecieX;
        y = getPozycja().getY() + przesuniecieY;

        while (indeksWPlanszy(x) && indeksWPlanszy(y) && l_krokow < promien) {
            pola.add(new Pozycja(x, y));
            x = x + przesuniecieX;
            y = y + przesuniecieY;
            l_krokow++;
        }

        return pola;
    }

    private boolean indeksWPlanszy(int x) {
        return x < 9 && x > 0;
    }
}