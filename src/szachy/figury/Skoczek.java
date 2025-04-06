package szachy.figury;

import szachy.Plansza;
import szachy.Pozycja;

import java.util.ArrayList;
import java.util.List;

public class Skoczek extends FiguraAbs {
    public Skoczek(Pozycja pozycja) {
        super(pozycja);
        setType(TypFigury.SKOCZEK);
    }

    @Override
    public List<Pozycja> getDopuszczalnePola() {

        return dopuszczalnePola(Kierunki.Skok);
    }

    @Override
    public String getZnak() {
        if (!this.jestBiala()) {
            return "\u2658";
        }
        return "\u265E";
    }

    @Override
    public boolean trasaWolna(Pozycja[] trasa, Plansza plansza) {
        return true;
    }

    @Override
    protected List<Pozycja> dopuszczalnePola(Kierunki kierunek) {
        List<Pozycja> dopuszczalne_pola = new ArrayList<>();
        int[] przesuniecieX = {-2, 2};
        int[] przesuniecieY = {-1, 1};

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                int x1 = getPozycja().getX() + przesuniecieX[i];
                int y1 = getPozycja().getY() + przesuniecieY[j];

                int x2 = getPozycja().getX() + przesuniecieY[j];
                int y2 = getPozycja().getY() + przesuniecieX[i];

                if (x1 <= 8 && x1 >= 1 && y1 <= 8 && y1 >= 1) {
                    dopuszczalne_pola.add(new Pozycja(x1, y1));
                }

                if (x2 <= 8 && x2 >= 1 && y2 <= 8 && y2 >= 1) {
                    dopuszczalne_pola.add(new Pozycja(x2, y2));
                }
            }
        }
        return dopuszczalne_pola;
    }

}
