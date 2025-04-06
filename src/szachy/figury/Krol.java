package szachy.figury;

import szachy.Pozycja;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class Krol extends FiguraAbs {

    boolean byl_ruszony = false;

    public Krol(Pozycja pozycja) {
        super(pozycja);
        setType(TypFigury.KROL);
    }

    @Override
    public boolean bylaRuszona() {

        boolean byl_wczesniej_ruszony = true;

        if(this.jestBiala())
        {
            if(this.getPozycja().equals(new Pozycja(5,1)))
            {
                byl_wczesniej_ruszony = false;
            }
        }
        else
        {
            if(this.getPozycja().equals(new Pozycja(5,8)))
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

        List<Pozycja> dopuszczalne_Pola = new ArrayList<>(dopuszczalnePola(Kierunki.WzgledemOY, 1));
        dopuszczalne_Pola.addAll(dopuszczalnePola(Kierunki.WzgledemOX, 1));
        dopuszczalne_Pola.addAll(dopuszczalnePola(Kierunki.SkosLD_PG, 1));
        dopuszczalne_Pola.addAll(dopuszczalnePola(Kierunki.SkosLG_PD, 1));

        return dopuszczalne_Pola;
    }

    @Override
    public String getZnak() {
        if (!this.jestBiala()) {
            return "\u2654";
        }
        return "\u265A";
    }

    @Override
    public void roszada(Pozycja pozycja_wiezy) {
        //jak 3 to krotka
        int pozycjaX = abs(getPozycja().getX() - pozycja_wiezy.getX()) == 3 ? 7 : 3;
        this.przesun(new Pozycja(pozycjaX, getPozycja().getY()));
    }

}
