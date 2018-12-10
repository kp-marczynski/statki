package statki;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Zawodnik {
    private Statek[][] planszaZawodnika;
    private Boolean[][] planszaPrzeciwnika;
    private int liczbaNiezatopionychStatkow;
    private int[] liczbaStatkow;
    private Scanner scanner;

    Zawodnik(int[] liczbaStatkow, Scanner scanner, boolean czyDodacStatkiLosowo) {
        this.scanner = scanner;
        planszaZawodnika = new Statek[Wspolrzedne.getMaxRozmiar()][Wspolrzedne.getMaxRozmiar()];
        planszaPrzeciwnika = new Boolean[Wspolrzedne.getMaxRozmiar()][Wspolrzedne.getMaxRozmiar()];
        this.liczbaStatkow = liczbaStatkow;
        for (int i = 0; i < liczbaStatkow.length; ++i) {
            liczbaNiezatopionychStatkow += liczbaStatkow[i];
        }
        if (czyDodacStatkiLosowo) dodajStatkiLosowo();
        else dodajStatki();
    }

    boolean sprawdzCzyKoniecGry() {
        return liczbaNiezatopionychStatkow == 0;
    }

    boolean podajWynikStrzalu(int x, int y) {
        if (planszaZawodnika[x][y] != null) {
            boolean wynik = planszaZawodnika[x][y].sprawdzCzyTrafiono(x, y);
            if (wynik) {
                if (planszaZawodnika[x][y].sprawdzCzyZatopiony()) {
                    liczbaNiezatopionychStatkow--;
                    System.out.println("TRAFIONY - ZATOPIONY!");
                } else System.out.println("TRAFIONY!");
                return true;
            } else return false;
        } else {
            System.out.println("PUDŁO!");
            return false;
        }
    }

    boolean sprawdzCzyMoznaOddacStrzal(int x, int y) {
        return planszaPrzeciwnika[x][y] == null;
    }

    void aktualizujWynikStrzalu(int x, int y, boolean wynik) {
        planszaPrzeciwnika[x][y] = wynik;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Plansza zawodnika:\n");
        for (int y = 0; y < Wspolrzedne.getMaxRozmiar(); ++y) {
            for (int x = 0; x < Wspolrzedne.getMaxRozmiar(); ++x) {
                if (planszaZawodnika[x][y] != null) {
                    if (planszaZawodnika[x][y].getWspolrzedneMasztow().contains(new Wspolrzedne(x, y)))
                        stringBuilder.append(0);
                    else stringBuilder.append(1);
                } else stringBuilder.append("-");
                stringBuilder.append(" ");
            }
            stringBuilder.append("\n");
        }
        stringBuilder.append("Plansza przeciwnika:\n");
        for (int y = 0; y < Wspolrzedne.getMaxRozmiar(); ++y) {
            for (int x = 0; x < Wspolrzedne.getMaxRozmiar(); ++x) {
                if (planszaPrzeciwnika[x][y] != null) {
                    if (planszaPrzeciwnika[x][y]) stringBuilder.append(1);
                    else stringBuilder.append(0);
                } else stringBuilder.append("-");
                stringBuilder.append(" ");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    private void dodajStatki() {
        ArrayList<Wspolrzedne> dostepneWspolrzedne = new ArrayList<>(Wspolrzedne.getMaxRozmiar() * Wspolrzedne.getMaxRozmiar());
        for (int x = 0; x < Wspolrzedne.getMaxRozmiar(); ++x) {
            for (int y = 0; y < Wspolrzedne.getMaxRozmiar(); ++y) {
                dostepneWspolrzedne.add(new Wspolrzedne(x, y));
            }
        }
        for (int i = liczbaStatkow.length - 1; i >= 0; i--) {
            for (int j = 0; j < liczbaStatkow[i]; j++) {
                Statek statek = new Statek(i + 1);
                int k = i + 1;
                System.out.println("Budowa " + k + " masztowca: ");
                int a = 1;
                while (!statek.czyBudowaZakonczona()) {
                    System.out.println("Podaj współrzędną x " + a + " masztu: ");
                    int x = scanner.nextInt();
                    System.out.println("Podaj współrzędną y " + a + " masztu: ");
                    int y = scanner.nextInt();
                    Wspolrzedne wspolrzedne = new Wspolrzedne(x, y);
                    if (dostepneWspolrzedne.isEmpty()){
                        System.out.println("Na planszy nie ma miejsca na zbudowanie tego statku.");
                        throw new IllegalStateException("Na planszy nie ma miejsca na zbudowanie tego statku.");
                    }
                    try {
                        if (!dostepneWspolrzedne.contains(wspolrzedne)){
                            System.out.println("Dla podanych wartości x i y nie można dodać nowego masztu.");
                            throw new IllegalArgumentException("Dla podanych wartości x i y nie można dodać nowego masztu.");
                        }
                        else {
                            if (statek.getWspolrzedneMasztow().isEmpty()) {
                                statek.dodajMaszt(x, y);
                            } else {
                                ArrayList<Wspolrzedne> dopuszczalneWspolrzedne = statek.dopuszczalneWspolrzedne();
                                if (dopuszczalneWspolrzedne.isEmpty()){
                                    System.out.println("W wybranym położeniu nie ma wystarczająco dużo miejsca na zbudowanie statku zadanej wielkości.");
                                    throw new IllegalAccessException("W wybranym położeniu nie ma wystarczająco dużo miejsca na zbudowanie statku zadanej wielkości.");
                                }
                                if (!dopuszczalneWspolrzedne.contains(wspolrzedne)){
                                    System.out.println("Dla podanych wartości x i y nie można dodać nowego masztu.");
                                    throw new IllegalArgumentException("Dla podanych wartości x i y nie można dodać nowego masztu.");
                                }
                                else statek.dodajMaszt(x, y);
                            }
                            a++;
                            dostepneWspolrzedne.remove(wspolrzedne);
                        }

                    } catch (IllegalArgumentException ignored) {
                    } catch (IllegalAccessException e) {
                        a = 1;
                        statek.zerujWspolrzedneMasztow();
                    }
                }
                usunOtaczajaceWspolrzedne(dostepneWspolrzedne, statek);
            }
        }
    }

    private void dodajStatkiLosowo() {
        ArrayList<Wspolrzedne> dostepneWspolrzedne = new ArrayList<>();
        for (int x = 0; x < Wspolrzedne.getMaxRozmiar(); ++x) {
            for (int y = 0; y < Wspolrzedne.getMaxRozmiar(); ++y) {
                dostepneWspolrzedne.add(new Wspolrzedne(x, y));
            }
        }
        for (int i = liczbaStatkow.length - 1; i >= 0; i--) {
            for (int j = 0; j < liczbaStatkow[i]; j++) {
                Statek statek = new Statek(i + 1);
                while (!statek.czyBudowaZakonczona()) {
                    if (dostepneWspolrzedne.isEmpty()) {
                        System.out.println("Na planszy nie ma miejsca na zbudowanie tego statku.");
                        throw new IllegalStateException("Na planszy nie ma miejsca na zbudowanie tego statku.");
                    }
                    Wspolrzedne wspolrzedne = dostepneWspolrzedne.get(new Random().nextInt(dostepneWspolrzedne.size()));
                    if (statek.getWspolrzedneMasztow().isEmpty()) {
                        statek.dodajMaszt(wspolrzedne.getX(), wspolrzedne.getY());
                        dostepneWspolrzedne.remove(wspolrzedne);
                    } else {
                        if (statek.dopuszczalneWspolrzedne().isEmpty())
                            statek.zerujWspolrzedneMasztow();
                        else if (statek.dopuszczalneWspolrzedne().contains(wspolrzedne)) {
                            statek.dodajMaszt(wspolrzedne.getX(), wspolrzedne.getY());
                            dostepneWspolrzedne.remove(wspolrzedne);
                        }
                    }
                }
                usunOtaczajaceWspolrzedne(dostepneWspolrzedne, statek);
            }
        }
    }

    private void usunOtaczajaceWspolrzedne(ArrayList dostepneWspolrzedne, Statek statek) {
        for (Wspolrzedne wspolrzedne : statek.getWspolrzedneMasztow()) {
            planszaZawodnika[wspolrzedne.getX()][wspolrzedne.getY()] = statek;
            dostepneWspolrzedne.remove(new Wspolrzedne(wspolrzedne.getX() - 1, wspolrzedne.getY()));
            dostepneWspolrzedne.remove(new Wspolrzedne(wspolrzedne.getX() - 1, wspolrzedne.getY() + 1));
            dostepneWspolrzedne.remove(new Wspolrzedne(wspolrzedne.getX(), wspolrzedne.getY() + 1));
            dostepneWspolrzedne.remove(new Wspolrzedne(wspolrzedne.getX() + 1, wspolrzedne.getY() + 1));
            dostepneWspolrzedne.remove(new Wspolrzedne(wspolrzedne.getX() + 1, wspolrzedne.getY()));
            dostepneWspolrzedne.remove(new Wspolrzedne(wspolrzedne.getX() + 1, wspolrzedne.getY() - 1));
            dostepneWspolrzedne.remove(new Wspolrzedne(wspolrzedne.getX(), wspolrzedne.getY() - 1));
            dostepneWspolrzedne.remove(new Wspolrzedne(wspolrzedne.getX() - 1, wspolrzedne.getY() - 1));
        }
    }
}
