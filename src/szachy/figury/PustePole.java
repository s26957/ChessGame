package szachy.figury;

import szachy.Plansza;
import szachy.Pozycja;

import java.util.List;

public class PustePole extends FiguraAbs {

    public PustePole() {
        super(new Pozycja(0, 8));
    }

    @Override
    public boolean exists() {

        return false;
    }

    @Override
    public List<Pozycja> getDopuszczalnePola() {
        return null;
    }

    @Override
    public String getZnak() {
        return "  ";
    }

    @Override
    public boolean trasaWolna(Pozycja[] trasa, Plansza plansza) {
        return false;
    }

    @Override
    public Pozycja getPozycja() {
        return super.getPozycja();
    }


}
