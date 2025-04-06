package szachy.figury;

import szachy.Pozycja;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class Wieza extends FiguraAbs {
    boolean byl_ruszony = false;

    public Wieza(Pozycja pozycja) {
        super(pozycja);
        setType(TypFigury.WIEZA);
    }

    @Override
    public boolean bylaRuszona() {
        //spr czy byla ruszona rpzed odpaleniem
        boolean byl_wczesniej_ruszony = true;
        if(this.jestBiala())
        {
            if(getPozycja().equals(new Pozycja(1,1)) || getPozycja().equals(new Pozycja(8,1)))
            {
                byl_wczesniej_ruszony = false;
            }
        }
        else
        {
            if(getPozycja().equals(new Pozycja(1,8)) || getPozycja().equals(new Pozycja(8,8)))
            {
                byl_wczesniej_ruszony = false;
            }
        }

        return byl_ruszony || byl_wczesniej_ruszony;
    }

    @Override
    public void setStan(boolean czy_byl_ruszony) {
        this.byl_ruszony = czy_byl_ruszony;
    }

    @Override
    public List<Pozycja> getDopuszczalnePola() {

        List<Pozycja> dopuszczalne_pola = new ArrayList<>(dopuszczalnePola(Kierunki.WzgledemOY));

        dopuszczalne_pola.addAll(dopuszczalnePola(Kierunki.WzgledemOX));

        return dopuszczalne_pola;
    }

    @Override
    public String getZnak() {
        if (!this.jestBiala()) {
            return "\u2656";
        }
        return "\u265C";
    }

    @Override
    public void roszada(Pozycja pozycja_krola) {
            //jak 3 to krotka
            int pozycjaX = abs(getPozycja().getX() - pozycja_krola.getX()) == 3 ? 6 : 4;
            this.przesun(new Pozycja(pozycjaX, getPozycja().getY()));
    }

}
