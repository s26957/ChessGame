package szachy;

import szachy.figury.Figura;
import szachy.figury.TypFigury;

import java.util.List;

public class GraczImp implements Gracz {

    private final Kolor kolor;
    private final List<Figura> figury;

    public GraczImp(Kolor kolor, List<Figura> figury) {
        this.kolor = kolor;
        this.figury = figury;
    }

    public List<Figura> getFigury() {
        return figury;
    }

    public void addFigura(Figura figura) {
        figury.add(figura);
    }

    public Kolor getKolor() {
        return kolor;
    }

    public boolean maFigure(Figura szukana_figura) {
        for (Figura f : figury) {
            if (f.equals(szukana_figura)) {
                return true;
            }
        }
        return false;
    }

    public Pozycja getPoleKrola() {

        for (Figura f : figury) {
            if (f.getType() == TypFigury.KROL) {
                return f.getPozycja();
            }
        }
        //blad:
        return new Pozycja(0, 1);
    }
}
