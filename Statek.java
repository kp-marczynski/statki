package statki;

import java.util.ArrayList;

class Statek {
    private int liczbaMasztow;
    private int liczbaTrafien;
    private ArrayList<Wspolrzedne> wspolrzedneMasztow;

    Statek(int liczbaMasztow) {
        this.liczbaMasztow = liczbaMasztow;
        liczbaTrafien = 0;
        wspolrzedneMasztow = new ArrayList<>();
    }

    void zerujWspolrzedneMasztow() {
        wspolrzedneMasztow = new ArrayList<>();
    }

    ArrayList<Wspolrzedne> getWspolrzedneMasztow() {
        return wspolrzedneMasztow;
    }

    boolean sprawdzCzyZatopiony() {
        return liczbaMasztow == liczbaTrafien;
    }

    boolean sprawdzCzyTrafiono(int x, int y) {
        if (wspolrzedneMasztow.contains(new Wspolrzedne(x, y))) {
            wspolrzedneMasztow.remove(new Wspolrzedne(x, y));
            liczbaTrafien++;
            return true;
        }
        return false;
    }

    ArrayList<Wspolrzedne> dopuszczalneWspolrzedne() {
        ArrayList<Wspolrzedne> wspolrzedne = new ArrayList<>();
        for (Wspolrzedne element : wspolrzedneMasztow) {
            if (element.getX() - 1 >= 0 && !wspolrzedneMasztow.contains(new Wspolrzedne(element.getX() - 1, element.getY())))
                wspolrzedne.add(new Wspolrzedne(element.getX() - 1, element.getY()));
            if (element.getX() + 1 < Wspolrzedne.getMaxRozmiar() && !wspolrzedneMasztow.contains(new Wspolrzedne(element.getX() + 1, element.getY())))
                wspolrzedne.add(new Wspolrzedne(element.getX() + 1, element.getY()));
            if (element.getY() - 1 >= 0 && !wspolrzedneMasztow.contains(new Wspolrzedne(element.getX(), element.getY() - 1)))
                wspolrzedne.add(new Wspolrzedne(element.getX(), element.getY() - 1));
            if (element.getY() + 1 < Wspolrzedne.getMaxRozmiar() && !wspolrzedneMasztow.contains(new Wspolrzedne(element.getX(), element.getY() + 1)))
                wspolrzedne.add(new Wspolrzedne(element.getX(), element.getY() + 1));
        }
        return wspolrzedne;

    }

    boolean czyBudowaZakonczona() {
        return liczbaMasztow <= wspolrzedneMasztow.size() + liczbaTrafien;
    }

    void dodajMaszt(int x, int y) {
        wspolrzedneMasztow.add(new Wspolrzedne(x,y));
    }
}
