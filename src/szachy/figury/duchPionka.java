package szachy.figury;

import szachy.Pozycja;

import java.util.List;

public class duchPionka extends FiguraAbs {

    private final FiguraAbs wlasciwyPionek;

    public duchPionka(Pozycja pozycja, Figura pionek) {
        super(pozycja);
        wlasciwyPionek = (FiguraAbs) pionek;
        setType(TypFigury.DUCH_PIONKA);
    }

    @Override
    public List<Pozycja> getDopuszczalnePola() {
        return null;
    }

    @Override
    public String getZnak() {
        return "   ";
    }

    @Override
    public void zbij() {
        wlasciwyPionek.przesun(new Pozycja(0, 0));
        //duch idzie na poza plansze:
        this.przesun(new Pozycja(9, 9));
    }
}
