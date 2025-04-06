package szachy;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        print_zasady();

        //1. przygootowanie do gry - utwprzenie planszy i przydzielenie graczom figur
        Gracz gracz1 = new GraczImp(Kolor.BIALY, PoczatkoweUstawienieFigur.getPoczatkoweFigury(Kolor.BIALY));
        Gracz gracz2 = new GraczImp(Kolor.CZARNY, PoczatkoweUstawienieFigur.getPoczatkoweFigury(Kolor.CZARNY));
        Plansza plansza = new PlanszaImp(gracz1, gracz2);


        System.out.println("Czy wczytać nową plansze? [tak -> wczytanie, nie -> poczatek na standardowym ustawieniu]");
        Scanner input = new Scanner(System.in);
        String odp0 = input.nextLine();
        if(odp0.equalsIgnoreCase("tak"))
        {
            try {
                plansza.wczytaj();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }

        plansza.wyswietl();

        boolean play = true;
        int nr_gracza = 0;

        while (play) {
            System.out.println("\nRuch gracza " + (nr_gracza == 0 ? "bialego" : "czarnego"));

            Scanner ruch = new Scanner(System.in);
            String odp = ruch.nextLine();

            if (odp.equalsIgnoreCase("save")) {
                try {
                    plansza.zapisDoPliku();
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
                Scanner wlasciwy_ruch = new Scanner(System.in);
                odp = wlasciwy_ruch.nextLine();
            }
            if(odp.equalsIgnoreCase("wczytaj"))
            {
                try {
                    plansza.wczytaj();
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
            }

            if (odp.equals("remis")) {
                System.out.println("Odpowiedz gracza " + ((nr_gracza + 1) % 2 == 0 ? "bialego" : "czarnego"));
            } else {
                System.out.println("Podaj wspolrzedne pola na ktore chcesz przesunac figure");
            }

            Scanner ruch2 = new Scanner(System.in);
            String odp2 = ruch2.nextLine();

            if (odp2.equalsIgnoreCase("save")) {
                try {
                    plansza.zapisDoPliku();
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
                Scanner wlasciwy_ruch = new Scanner(System.in);
                odp2 = wlasciwy_ruch.nextLine();
            }
            if(odp2.equalsIgnoreCase("wczytaj"))
            {
                try {
                    plansza.wczytaj();
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
            }

            String wydarzenie = analizator_odpowiedzi(odp, odp2);
            switch (wydarzenie) {
                case "remis" -> {
                    System.out.println("zostal uzgodniony remis");
                    play = false;
                }
                case "brak_zgody" -> {
                    System.out.println("nie udalo sie uzgodnic remisu \ngramy dalej!");
                }
                case "normalny_ruch" -> {
                    if (plansza.ruchSieWykonal(wspolrzedne_pola(odp), wspolrzedne_pola(odp2), plansza.getGracze()[nr_gracza])) {
                        nr_gracza = (nr_gracza + 1) % 2;
                        if(plansza.jestMat(nr_gracza)) {
                            System.out.println("szach! koniec gry\n zwycieza gracz " + plansza.getGracze()[(nr_gracza + 1) % 2].getKolor());
                            play = false;}
                    } else {
                        System.out.println("Wykonanie tego ruchu jest nie mozliwe");
                    }
                    plansza.wyswietl();
                }
                case "blad" -> {
                    System.out.println("ruch ma zla skladnie \ndla przypomnienia:");
                    print_zasady();
                    System.out.println("Sprobuj ponownie");
                }
            }
        }
    }

    public static void print_zasady() {
        System.out.println("""
                zasady ruchu:
                Zeby zapisac plansze nalezy napisac "save" a nastepnie podac sciezke
                Analogicznie - odczyt planszy nastepuje po wpisaniu "wczytaj" a potem podaniu sciezki
                Wykonaniue ruchu:
                zeby przesunac figure na dane pole nalezy najpierw podac jej wspolrzedne\s
                potem wspolrzedne pola na ktore ma zostac przesunieta (najpierw litera potem liczba)\s
                zeby zadeklarowac remis nalezy napisac "remis" jezeli drugi gracz udzieli tej samej odpowiedzi
                gra zostaje zakonczona remisem
                wielkosc liter nie ma znaczenia
                """);
    }

    public static String analizator_odpowiedzi(String a, String b) {
        if (a.equalsIgnoreCase("remis")) {
            if (b.equalsIgnoreCase("remis")) {
                return "remis";
            } else {
                return "brak_zgody";
            }
        }
        if (poprawnosc_odpowiedzi(a) && poprawnosc_odpowiedzi(b) && !a.equals(b)) {
            return "normalny_ruch";
        }
        return "blad";
    }

    public static boolean poprawnosc_odpowiedzi(String odpowiedz) {
        return odpowiedz.matches("[a-hA-H][1-8]");
    }

    public static Pozycja wspolrzedne_pola(String pole_string) {
        return new Pozycja(pole_string.toUpperCase().charAt(0) - 'A' + 1, pole_string.charAt(1) - '0');
    }

}