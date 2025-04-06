package szachy.figury;

import szachy.Pozycja;

import java.util.ArrayList;
import java.util.List;

public class Goniec extends FiguraAbs {

    public Goniec(Pozycja pozycja) {
        super(pozycja);
        setType(TypFigury.GONIEC);
    }

    @Override
    public List<Pozycja> getDopuszczalnePola() {
        List<Pozycja> dopuszczalne_pola = new ArrayList<>(dopuszczalnePola(Kierunki.SkosLD_PG));
        dopuszczalne_pola.addAll(dopuszczalnePola(Kierunki.SkosLG_PD));
        return dopuszczalne_pola;
    }

    @Override
    public String getZnak() {
        if (!this.jestBiala()) {
            return ("\u2657");
        }
        return "\u265D";
    }
}
