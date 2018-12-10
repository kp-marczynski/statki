package statki;

import java.util.Scanner;

public class Gra {
    public static void main(String[] args) {
        new Gra();
    }

    private Tura tura;
    private Zawodnik uzytkownik;
    private Zawodnik komputer;
    private Scanner scanner;

    public enum Tura {
        UZYTKOWNIK, KOMPUTER
    }

    public Gra() {
        tura = Tura.UZYTKOWNIK;
        scanner = new Scanner(System.in);
        System.out.println("Podaj rozmiar planszy: ");
        int rozmiar = scanner.nextInt();
        if (rozmiar <= 0) throw new IllegalArgumentException("Rozmiar planszy nie może być <= 0");
        Wspolrzedne.setMaxRozmiar(rozmiar);
        System.out.println("Podaj liczbę 5-masztowych statków");
        int maszt5 = scanner.nextInt();
        if (maszt5 < 0) throw new IllegalArgumentException("Liczba statków nie może być ujemna");
        System.out.println("Podaj liczbę 4-masztowych statków");
        int maszt4 = scanner.nextInt();
        if (maszt4 < 0) throw new IllegalArgumentException("Liczba statków nie może być ujemna");
        System.out.println("Podaj liczbę 3-masztowych statków");
        int maszt3 = scanner.nextInt();
        if (maszt3 < 0) throw new IllegalArgumentException("Liczba statków nie może być ujemna");
        System.out.println("Podaj liczbę 2-masztowych statków");
        int maszt2 = scanner.nextInt();
        if (maszt2 < 0) throw new IllegalArgumentException("Liczba statków nie może być ujemna");
        System.out.println("Podaj liczbę 1-masztowych statków");
        int maszt1 = scanner.nextInt();
        if (maszt1 < 0) throw new IllegalArgumentException("Liczba statków nie może być ujemna");

        if ((maszt5 * 5 + maszt4 * 4 + maszt3 * 3 + maszt2 * 2 + maszt1) * 4 > rozmiar * rozmiar) {
            System.out.println("Na podanym rozmiarze planszy nie da się zmieścić podaj liczby statków");
            throw new IllegalStateException("Na podanym rozmiarze planszy nie da się zmieścić podaj liczby statków");
        }
        int[] liczbaStatkow = {maszt1, maszt2, maszt3, maszt4, maszt5};
        uzytkownik = new Zawodnik(liczbaStatkow, scanner, false);
        komputer = new Zawodnik(liczbaStatkow, scanner, true);
        rozpocznijGre();
    }

    public void rozpocznijGre() {
        boolean oddanoStrzal;
        while (!uzytkownik.sprawdzCzyKoniecGry() && !komputer.sprawdzCzyKoniecGry()) {
            switch (tura) {
                case UZYTKOWNIK:
                    System.out.println("\n***** Tura użytkownika: *****");
                    System.out.println(uzytkownik);
                    oddanoStrzal = false;
                    while (!oddanoStrzal) {
                        try {
                            System.out.println("Podaj współrzędną x strzału: ");
                            int x = scanner.nextInt();
                            if (x < 0 || x > Wspolrzedne.getMaxRozmiar()) {
                                System.out.println("Podana wartosc x nie zawiera się w planszy.");
                                throw new IllegalArgumentException("Podana wartosc x nie zawiera się w planszy.");
                            }

                            System.out.println("Podaj współrzędną y strzału: ");
                            int y = scanner.nextInt();
                            if (y < 0 || y > Wspolrzedne.getMaxRozmiar()) {
                                System.out.println("Podana wartosc y nie zawiera się w planszy.");
                                throw new IllegalArgumentException("Podana wartosc y nie zawiera się w planszy.");
                            }
                            if (!uzytkownik.sprawdzCzyMoznaOddacStrzal(x, y))
                                System.out.println("W podanych wspolrzednych już oddano strzał.");
                            else {
                                boolean wynikStrzalu = komputer.podajWynikStrzalu(x, y);
                                uzytkownik.aktualizujWynikStrzalu(x, y, wynikStrzalu);
                                oddanoStrzal = true;
                                if (!wynikStrzalu) tura = Tura.KOMPUTER;
                            }
                        } catch (IllegalArgumentException e) {
                            oddanoStrzal = false;
                        }
                    }
                    break;
                case KOMPUTER:
                    System.out.println("\n***** Tura komputera: *****");
                    //System.out.println(komputer);
                    oddanoStrzal = false;
                    while (!oddanoStrzal) {
                        int x = Wspolrzedne.losujWspolrzedne();
                        int y = Wspolrzedne.losujWspolrzedne();
                        if (komputer.sprawdzCzyMoznaOddacStrzal(x, y)) {
                            System.out.println("Strzał: (" + x + ", " + y + ")");
                            boolean wynikStrzalu = uzytkownik.podajWynikStrzalu(x, y);
                            komputer.aktualizujWynikStrzalu(x, y, wynikStrzalu);
                            oddanoStrzal = true;
                            if (!wynikStrzalu) tura = Tura.UZYTKOWNIK;
                        }
                    }
            }
        }
        if (uzytkownik.sprawdzCzyKoniecGry()) System.out.println("GRA SKOŃCZONA - PRZEGRANA!");
        if (komputer.sprawdzCzyKoniecGry()) System.out.println("GRA SKOŃCZONA - WYGRANA!");
    }

}
