package szachy.figury;

import szachy.Pozycja;

import java.util.ArrayList;
import java.util.List;

public class Krolowa extends FiguraAbs {
    public Krolowa(Pozycja pozycja) {
        super(pozycja);
        setType(TypFigury.KROLOWA);
    }

    @Override
    public List<Pozycja> getDopuszczalnePola() {
        List<Pozycja> dopuszczalne_pola = new ArrayList<>();

        for (Pozycja pole : dopuszczalnePola(Kierunki.WzgledemOY)) {
            dopuszczalne_pola.add(pole);
        }

        for (Pozycja pole : dopuszczalnePola(Kierunki.WzgledemOX)) {
            dopuszczalne_pola.add(pole);
        }

        for (Pozycja pole : dopuszczalnePola(Kierunki.SkosLG_PD)) {
            dopuszczalne_pola.add(pole);
        }
        for (Pozycja pole : dopuszczalnePola(Kierunki.SkosLD_PG)) {
            dopuszczalne_pola.add(pole);
        }

        return dopuszczalne_pola;
    }

    @Override
    public String getZnak() {
        if (!this.jestBiala()) {
            return ("\u2655");
        }
        return "\u265B";
    }

}
