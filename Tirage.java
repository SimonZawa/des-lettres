import java.util.ArrayList;
import java.security.SecureRandom;

public class Tirage {
    private ArrayList<String> lettresTirees;
    private ArrayList<String> voyelles;
    private ArrayList<String> consonnes;

    public Tirage(ArrayList<String> lettresTirees,
                  ArrayList<String> voyelles,
                  ArrayList<String> consonnes) {

        this.lettresTirees = lettresTirees;
        this.voyelles = voyelles;
        this.consonnes = consonnes;
    }
    public ArrayList<String> tirerLettres(ArrayList<String> voyelles,
                                          ArrayList<String> consonnes,
                                          int nbreVoyelles) {

        SecureRandom random = new SecureRandom();

        for (int i = 0; i < nbreVoyelles; i++) { //Tirage des voyelles

            this.lettresTirees.add(voyelles.get(((random.nextInt(voyelles.size())))));
        }
        for (int i = 0; i < (10 - nbreVoyelles); i++) { //Tirage des consonnes

            this.lettresTirees.add(consonnes.get(((random.nextInt(consonnes.size())))));
        }

        return lettresTirees;
    }

    public String getLettres(ArrayList<String> lettresTirees) {

        String lettres = "";

        for (String i: lettresTirees) {

            lettres += i; //On stocke le tirage dans une variable, pour trouver le mot le plus long
        }
        return lettres;
    }
}
