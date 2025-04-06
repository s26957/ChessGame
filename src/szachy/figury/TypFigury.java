package szachy.figury;

import java.util.HashMap;
import java.util.Map;

public enum TypFigury {
    PIONEK(0), GONIEC(4), WIEZA(3), SKOCZEK(5), KROL(1), KROLOWA(2), DUCH_PIONKA(6);

    private static Map<Integer, TypFigury> map;
    int nrFigury;
    TypFigury(int nrFigury)
    {
        this.nrFigury = nrFigury;
    }

    public int getNrFigury()
    {
        return this.nrFigury;
    }

    public static TypFigury getFigura(int i)
    {
        map = new HashMap<>();
        for(TypFigury typ : TypFigury.values())
        {
            map.put(typ.nrFigury, typ);
        }
        return map.get(i);
    }
    public static TypFigury[] getTypyFigur() { return values(); }
}
