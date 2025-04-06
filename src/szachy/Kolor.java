package szachy;

public enum Kolor {
    BIALY(0), CZARNY(1);
    int nr_gracza;
    Kolor(int nr_gracza) {
        this.nr_gracza = nr_gracza;
    }

    public int getNrGracza() {
        return nr_gracza;
    }

}
