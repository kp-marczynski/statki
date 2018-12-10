package statki;

import java.util.Random;

public class Wspolrzedne {
    private int x;
    private int y;
    private static int maxRozmiar;

    static void setMaxRozmiar(int maxRozmiar) {
        Wspolrzedne.maxRozmiar = maxRozmiar;
    }

    static int getMaxRozmiar() {
        return maxRozmiar;
    }

    Wspolrzedne(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Wspolrzedne)) return false;

        Wspolrzedne that = (Wspolrzedne) o;

        return x == that.x && y == that.y;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    static int losujWspolrzedne() {
        return new Random().nextInt(maxRozmiar);
    }
}
