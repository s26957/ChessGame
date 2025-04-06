package szachy;

import szachy.figury.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.Math.*;

public class PlanszaImp implements Plansza {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
    Figura[][] plansza;
    Gracz[] gracze = new GraczImp[2];

    public PlanszaImp(Gracz gracz1, Gracz gracz2) {
        plansza = new Figura[9][9];
        gracze[0] = gracz1;
        gracze[1] = gracz2;
        updatePlanszy();
    }

    public Gracz[] getGracze() {
        return gracze;
    }
    public Figura figuraAt(Pozycja pozycja) {
        return plansza[pozycja.getY()][pozycja.getX()];
    }


    private boolean jestSzach(Gracz gracz) {
        return czyPoleAtakowane(gracz, gracz.getPoleKrola());
    }
    @Override
    public boolean jestMat(int nr_gracza) {
        Gracz gracz = gracze[nr_gracza];

        if(jestSzach(gracz))
        {
            //jezeli jest jakies pole na ktore krol moze uciec to nie ma mata
            for(Pozycja pole : figuraAt(gracz.getPoleKrola()).getDopuszczalnePola()) {
                if ((!czyPoleAtakowane(gracz, pole) && !gracz.maFigure(figuraAt(pole)))) {
                    return false;
                }
            }

            //jezeli ktoras z figur moze ruszyc sie tak, ze nie ma szacha to nie ma mata
            for(Figura f : gracz.getFigury())
            {
                for(Pozycja pole : f.getDopuszczalnePola())
                {
                    //jezeli istnieje takie ze po ruchu nie ma szacha to
                    if(f.mozliwoscRuchu(pole, gracz, this))
                    {
                        Pozycja poz_poczatkowa = f.getPozycja();
                        f.przesun(pole);
                        updatePlanszy();
                        if(!jestSzach(gracz))
                        {
                            f.przesun(poz_poczatkowa);
                            updatePlanszy();
                            return false;
                        }
                        f.przesun(poz_poczatkowa);
                        updatePlanszy();
                    }
                }
            }
            return true;
        }
        //jezeli nie ma szacha to nie ma mata:
        return false;
    }

    private boolean czyPoleAtakowane(Gracz gracz, Pozycja sprawdzane_pole) {
        int nr_gracza = gracz.getKolor().nr_gracza;
        int nr_przeciwnika = nr_gracza == 0 ? 1 : 0;

        for (Figura f : gracze[nr_przeciwnika].getFigury()) {
            for (Pozycja pole : f.getDopuszczalnePola()) {
                if (pole.equals(sprawdzane_pole) && f.trasaWolna(f.trasa(sprawdzane_pole), this)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean ruchSieWykonal(Pozycja pozycja, Pozycja pozycja_docelowa, Gracz gracz) {
        Figura ruszana_figura = figuraAt(pozycja);
        ruch(pozycja, pozycja_docelowa, gracz);
        if(!figuraAt(pozycja).equals(ruszana_figura))
        {
            ruszana_figura.setStan(true);
            return true;
        }
        return false;
    }

    public void ruch(Pozycja pozycja, Pozycja pozycja_docelowa, Gracz gracz) {
        if (ruchJestRoszada(pozycja, pozycja_docelowa, gracz)) {
            if (roszadaDozwolona(pozycja, pozycja_docelowa, gracz)) {
                figuraAt(pozycja).roszada(pozycja_docelowa);
                figuraAt(pozycja_docelowa).roszada(pozycja);
                updatePlanszy();
            }
        }
        else {
            zwyklyRuch(pozycja, pozycja_docelowa, gracz);
        }
    }

    private boolean roszadaDozwolona(Pozycja pozycja1, Pozycja pozycja2, Gracz gracz) {
        if (figuraAt(pozycja1).bylaRuszona() || figuraAt(pozycja2).bylaRuszona()) {
            System.out.println("Nie moge wykonac roszady - ktoras z figur byla ruszona");
            return false;
        }
        for (Pozycja pole : figuraAt(pozycja1).trasa(pozycja2)) {
            if (figuraAt(pole).exists()) {
                System.out.println("Nie moge wykonac roszady \nktores z pol miedzy figurami jest zajete");
                return false;
            }
        }

        int nowa_pozycja_krola = (pozycja1.getX() == 1 || pozycja2.getX() == 1)? 3 : 7; //jezeli ktoras z figur to wieza na pozycji (1, y) to nowa pozycja krola to 3, inaczej 7
        for(Pozycja pole : figuraAt(new Pozycja(5, pozycja1.getY())).trasa(new Pozycja(nowa_pozycja_krola ,pozycja1.getY())))
        {
            if(czyPoleAtakowane(gracz, pole))
            {
                System.out.println("Nie moge wykonac roszady - krol przechodzi przez szachowane pole");
                return false;
            }
        }
        return true;
    }

    private boolean ruchJestRoszada(Pozycja pozycja, Pozycja pozycja_docelowa, Gracz gracz) {
        //spr czy obydwie figury sa gracza i jedna z nich jest krolem a druga wieza
        return gracz.maFigure(figuraAt(pozycja)) && gracz.maFigure(figuraAt(pozycja_docelowa)) &&
                ((figuraAt(pozycja).getType() == TypFigury.KROL &&
                        figuraAt(pozycja_docelowa).getType() == TypFigury.WIEZA) ||
                        (figuraAt(pozycja).getType() == TypFigury.WIEZA &&
                                figuraAt(pozycja_docelowa).getType() == TypFigury.KROL));
    }

    public void zwyklyRuch(Pozycja pozycja, Pozycja pozycja_docelowa, Gracz gracz) {

        if (figuraAt(pozycja).mozliwoscRuchu(pozycja_docelowa, gracz, this)) {
            boolean byla_zbita_figura = false;
            Figura potencjalnie_zbijana_figura = figuraAt(pozycja_docelowa);
            if (figuraAt(pozycja_docelowa).exists()) {
                byla_zbita_figura = true;
                figuraAt(pozycja_docelowa).zbij();
            }

            figuraAt(pozycja).przesun(pozycja_docelowa);

            //spr czy na planszy sa jakies "duchy pionkow" ktore nalezaloby zapamietac i przywrocic, jezeli ruch sie jednak nie wykona
            Figura duch = new PustePole();
            if(jestDuchNaPlanszy()) {
                duch = getDuchaZPlanszy();
            }

            updatePlanszy();

            if (jestSzach(gracz)) {
                System.out.println("nie ma mozliwosci ruchu - powodowalby szachowanie!");
                figuraAt(pozycja_docelowa).przesun(pozycja);
                if(byla_zbita_figura) {
                    potencjalnie_zbijana_figura.przesun(pozycja_docelowa);
                }
                addFigura(duch);
                updatePlanszy();
            } else {//AKCJE SPECJALNE - ruch pionkiem: spr czy mozliwosc bicia w przelocie albo czy nie bylo podmiany
                if (figuraAt(pozycja_docelowa).getType() == TypFigury.PIONEK) {
                    //bicie w przelocie (mozliwosc po ruchu pionem o 2 pola):
                    if (abs(pozycja_docelowa.getY() - pozycja.getY()) == 2) {
                        int przesuniecie = figuraAt(pozycja_docelowa).jestBiala() ? 1 : -1;
                        addFigura(new duchPionka(new Pozycja(pozycja.getX(), pozycja.getY()  + przesuniecie), figuraAt(pozycja_docelowa)));
                    }

                    //podmiana:
                    if (figuraAt(pozycja_docelowa).mozliwoscPodamiany()) {
                        Podmiana(gracz, pozycja_docelowa);
                        updatePlanszy();
                    }
                }
            }
        }
    }

    private void Podmiana(Gracz gracz, Pozycja pozycja) {
        System.out.println("Czy chcesz dokonac podmiany?\nJezeli nie napisz \"nie\"\nJezeli tak podaj nazwe figury na ktora chcesz podmienic");
        boolean poprawnoscOdpowiedzi = false;
        TypFigury typZmienionej;
        while (!poprawnoscOdpowiedzi) {
            Scanner input_odpowiedz = new Scanner(System.in);
            String odpowiedz = input_odpowiedz.nextLine();
            if (!odpowiedz.equals("nie")) {
                for (TypFigury typ : TypFigury.getTypyFigur()) {
                    if (typ.toString().equals(odpowiedz)) {
                        poprawnoscOdpowiedzi = true;
                        figuraAt(pozycja).zbij();
                        DajGraczowiFigure(gracz, pozycja, typ);
                    }
                }
            } else {
                poprawnoscOdpowiedzi = true;
            }
        }
    }

    private void DajGraczowiFigure(Gracz gracz, Pozycja pozycja, TypFigury typ) {
        switch (typ) {
            case KROL -> gracz.addFigura(new Krol(pozycja));
            case GONIEC -> gracz.addFigura(new Goniec(pozycja));
            case KROLOWA -> gracz.addFigura(new Krolowa(pozycja));
            case WIEZA -> gracz.addFigura(new Wieza(pozycja));
            case SKOCZEK -> gracz.addFigura(new Skoczek(pozycja));
            case PIONEK -> System.out.print("ok, w takim razie zostaje pionek");
        }

    }

    private void updatePlanszy() {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                plansza[i][j] = new PustePole();
            }
        }
        for (int i = 0; i < 2; i++) {
            for (Figura f : gracze[i].getFigury()) {
                addFigura(f);
            }
        }
    }

    private void addFigura(Figura f) {
        plansza[f.getPozycja().getY()][f.getPozycja().getX()] = f;
    }

    private boolean jestDuchNaPlanszy()
    {
        for(int i = 1; i < 9; i ++)
        {
            if(figuraAt(new Pozycja(i, 3)).getType() == TypFigury.DUCH_PIONKA)
            {
                return true;
            }
            if(figuraAt(new Pozycja(i, 6)).getType() == TypFigury.DUCH_PIONKA)
            {
                return true;
            }
        }
        return false;
    }

    public Figura getDuchaZPlanszy()
    {
        for(int i = 1; i < 9; i ++)
        {
            if(figuraAt(new Pozycja(i, 3)).getType() == TypFigury.DUCH_PIONKA)
            {
                return figuraAt(new Pozycja(i, 3));
            }
            if(figuraAt(new Pozycja(i, 6)).getType() == TypFigury.DUCH_PIONKA)
            {
                return figuraAt(new Pozycja(i, 6));
            }
        }
        return new PustePole();
    }

    public void wyswietl() {
        for (int i = 1; i < 9; i++) {
            System.out.print(i);
            for (int j = 1; j < 9; j++) {
                String znakFigury = figuraAt(new Pozycja(j, i)).getZnak();
                if ((i + j) % 2 == 0) {
                    wyswietl_na_czarnym_tle(znakFigury);
                } else {
                    wyswietl_na_bialym_tle(znakFigury);
                }
            }
            System.out.println();
        }
        System.out.println("   A   B   C   D   E   F   G   H");
    }
    public static void wyswietl_na_bialym_tle(String znakFigury) {
        System.out.printf("%4s",ANSI_WHITE_BACKGROUND + " " + znakFigury + " " + ANSI_RESET);
    }
    public static void wyswietl_na_czarnym_tle(String znakFigury) {
        System.out.printf("%4s"," " + znakFigury + " " );
    }

    public void zapisDoPliku() throws IOException {
        System.out.println("podaj sciezke:");
        Scanner input = new Scanner(System.in);
        String sciezka = input.nextLine();

        FileOutputStream fos = new FileOutputStream(sciezka);
        for(Gracz gracz : gracze)
        {
            for(Figura figura : gracz.getFigury())
            {
                zapiszFigure(figura, fos);
            }
        }
        fos.close();
    }

    private void zapiszFigure(Figura figura, FileOutputStream fos) throws IOException {
        fos.write(figuraBytowa(figura));
    }

    private byte[] figuraBytowa(Figura figura) {

        byte[] figuraB = new byte[2];
        boolean[] figuraBool = figuraBooleanowo(figura);

        figuraB[1] = boolToByte(figuraBool, 0);
        figuraB[0] = boolToByte(figuraBool, 8);

        return figuraB;
    }

    private byte boolToByte(boolean[] figuraBool, int start) {
        return (byte)(boolToInt(figuraBool, start, start+8));
    }

    private boolean[] figuraBooleanowo(Figura figura)
    {
        boolean[] figuraB = new boolean[16];
        boolean[] typFiguryB = liczbaNaBoolean(figura.getType().getNrFigury(), 3);
        boolean[] pozX = liczbaNaBoolean(figura.getPozycja().getX(), 4);
        boolean[] pozY = liczbaNaBoolean(figura.getPozycja().getY(), 4);

        for(int i = 0; i < 3; i ++)
        {
            figuraB[i] = typFiguryB[i];
        }
        for(int i = 3; i < 7; i ++)
        {
            figuraB[i] = pozX[i-3];
        }
        for(int i = 7; i < 11; i ++)
        {
            figuraB[i] = pozY[i-7];
        }
        figuraB[11] = figura.jestBiala();

        for(int i = 12; i < 16; i ++)
        {
            figuraB[i] = false;
        }

        return figuraB;

    }

    private boolean[] byteNaBoolean(byte b, int l)
    {
        boolean[] liczbaB = new boolean[l];
        for(int i = 0; i < l; i++)
        {
            liczbaB[i] = (b&1) > 0;
            b = (byte)(b>>1);
        }
        return liczbaB;
    }
    private boolean[] liczbaNaBoolean(int liczba, int l) {

        boolean[] liczbaB = new boolean[l];
        for(int i = 0; i < l; i ++)
        {
            liczbaB[i] = liczba % 2 != 0;
            liczba = liczba/2;
        }
        return liczbaB;
    }

    @Override
    public void wczytaj() throws IOException {
        System.out.println("podaj sciezke:");
        Scanner input = new Scanner(System.in);
        String sciezka = input.nextLine();

        int max_l_figur = 64;
        FileInputStream fis = new FileInputStream(sciezka);

        byte[] bajtyFigur = new byte[max_l_figur*2];
        int l_bytow = fis.read(bajtyFigur);
        fis.close();

        int l_figur = l_bytow/2;
        Figura[] figuryWczyt = new Figura[l_figur];

        for(int i = 0; i < l_bytow; i += 2)
        {
            figuryWczyt[i/2] = wczytajFigure(bajtyFigur[i+1], bajtyFigur[i]);
        }
        List<Figura> figury_biale = getBialePoWczytaniu(figuryWczyt, true);
        List<Figura> figury_czarne = getBialePoWczytaniu(figuryWczyt, false);

        gracze[0] = new GraczImp(Kolor.BIALY, figury_biale);
        gracze[1] = new GraczImp(Kolor.CZARNY, figury_czarne);
        updatePlanszy();
    }

    private List<Figura> getBialePoWczytaniu(Figura[] figuryWczyt, boolean Czy_biale) {
        List<Figura> figury_kolor = new ArrayList<>();
        for(Figura f : figuryWczyt)
        {
            if(f.jestBiala() == Czy_biale)
            {
                figury_kolor.add(f);
            }
        }
        return figury_kolor;
    }

    private Figura wczytajFigure(byte b1, byte b2) {

        boolean[] bajt1 = byteNaBoolean(b1, 8);
        boolean[] bajt2 = byteNaBoolean(b2, 8);

        boolean[] figuraBool = new boolean[16];
        for(int i = 0; i < 16; i ++)
        {
            if(i < 8) {
                figuraBool[i] = bajt1[i];
            }
            else
            {
                figuraBool[i] = bajt2[i-8];
            }
        }
        return BoolToFigura(figuraBool);
    }

    private Figura BoolToFigura(boolean[] figuraBool) {

        //0-3 typ, 4-7 x, 8-11 y, 12 kolor
        Figura figura = null;
        TypFigury typ = TypFigury.getFigura(boolToInt(figuraBool, 0, 3));
        int x = boolToInt(figuraBool, 3, 7);
        int y = boolToInt(figuraBool, 7, 11);
        boolean kolor = figuraBool[11];
        switch(typ) {
            case PIONEK -> figura = new Pionek(new Pozycja(x, y));
            case KROL -> figura = new Krol(new Pozycja(x, y));
            case KROLOWA -> figura = new Krolowa(new Pozycja(x, y));
            case WIEZA -> figura = new Wieza(new Pozycja(x, y));
            case GONIEC -> figura = new Goniec(new Pozycja(x, y));
            case SKOCZEK -> figura = new Skoczek(new Pozycja(x, y));
            default -> throw new RuntimeException("Nieprawidlowy typ figury!" + typ);
        }
            if(!kolor)
        {
            figura.setCzarna();
        }
        return figura;
    }

    private int boolToInt(boolean[] figuraBool, int start, int end) {
        int sum = 0;
        int st = 0;
        for(int i = start; i < end; i ++)
        {
            int cyfra = figuraBool[i] ? 1 : 0;
            sum += cyfra*pow(2, st);
            st++;
        }
        return sum;
    }
}
