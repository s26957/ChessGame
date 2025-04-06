package szachy;

import szachy.figury.Figura;

import java.util.List;

public interface Gracz {
    Pozycja getPoleKrola();

    Kolor getKolor();

    List<Figura> getFigury();

    boolean maFigure(Figura szukana_figura);

    void addFigura(Figura figura);
}
