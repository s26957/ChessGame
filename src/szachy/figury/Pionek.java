package szachy.figury;

import szachy.Pozycja;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class Pionek extends FiguraAbs {

    private boolean byl_ruszony = false;
    private int poczatkowy_Y = 2;
    public Pionek(Pozycja pozycja) {
        super(pozycja);
        setType(TypFigury.PIONEK);
        byl_ruszony = !(pozycja.getY() == poczatkowy_Y);
    }

    @Override
    public void setCzarna() {
        super.setCzarna();
        poczatkowy_Y = 7;
        byl_ruszony = !(getPozycja().getY() == poczatkowy_Y);
    }
    @Override
    public boolean bylaRuszona() {
        return byl_ruszony;
    }

    public boolean mozliwoscPodamiany() {
        return abs(getPozycja().getY() - poczatkowy_Y) == 6;
    }

    @Override
    public void setStan(boolean czy_byl_ruszony) {
        this.byl_ruszony = czy_byl_ruszony;
    }

    @Override
    public List<Pozycja> getDopuszczalnePola() {

        List<Pozycja> dopuszczalne_Pola = new ArrayList<>();
        dopuszczalne_Pola.add(new Pozycja(getPozycja().getX(), this.jestBiala() ? getPozycja().getY() + 1 : getPozycja().getY() - 1));
        if (!byl_ruszony) {
            dopuszczalne_Pola.add(new Pozycja(getPozycja().getX(), this.jestBiala() ? getPozycja().getY() + 2 : getPozycja().getY() - 2));
        }
        return dopuszczalne_Pola;
    }

    @Override
    public String getZnak() {
        if (!this.jestBiala()) {
            return ("\u2659");
        }
        return ("\u265F");
    }

}
