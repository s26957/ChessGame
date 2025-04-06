package szachy;

public class Pozycja {
    int x;
    int y;

    public Pozycja(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {

        return this.x;
    }

    public int getY() {

        return this.y;
    }

    public boolean equals(Pozycja p2) {
        return x == p2.getX() && y == p2.getY();
    }

    public String toString() {
        return x + " " + y;
    }

}
