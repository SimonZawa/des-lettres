import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Dictionnaire {

    private Set<String> ensembleMot;

    public Dictionnaire(ArrayList<String> mots) {

        this.ensembleMot = new HashSet<>();//On rajoute les mots dans le HashSet

        for (String mot : mots) {

            this.ensembleMot.add(mot);
        }
    }

    public boolean estValide(String mot) {

        return this.ensembleMot.contains(mot.toUpperCase());
    }

    public String getMotLePlusLong(String lettres) {

        int[] frequenceLettres = tableauFrequence(lettres.toUpperCase());

        String motLePlusLong = null;

        for (String mot : this.ensembleMot) {

            if (peutFaireMot(mot, frequenceLettres)) {

                if (motLePlusLong == null || mot.length() > motLePlusLong.length()) {
                    motLePlusLong = mot;
                }
            }
        }

        return motLePlusLong;
    }

    private int[] tableauFrequence(String lettres) {

        int[] compte = new int[26];

        for (char lettre : lettres.toCharArray()) {

            if (Character.isLetter(lettre)) {

                compte[lettre - 'A']++;
            }
        }
        return compte;
    }

    private boolean peutFaireMot(String mot, int[] frequenceLettres) {

        int[] copieFrequenceLettres = Arrays.copyOf(frequenceLettres, frequenceLettres.length);

        for (char lettre : mot.toCharArray()) {

            if (lettre < 'A' || lettre > 'Z') { //On trie pour ignorer les caractères spéciaux
                return false;
            }
            int index = lettre - 'A'; //On récupère la position de la lettre dans le tableau

            if (copieFrequenceLettres[index] == 0) {
                return false;
            }
            copieFrequenceLettres[index]--;
        }
        return true;
    }
}