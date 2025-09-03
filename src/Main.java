import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Array;
import java.util.*;
import java.security.SecureRandom;


public class Main {
    public static void main(String[] args) throws IOException {
        SecureRandom random = new SecureRandom();

        //On transforme les consonnes et voyelles (string) en listes
        String cons = "BCDFGHJKLMNPQRSTVWXZ";
        String[] consSplit = cons.split("");
        ArrayList<String> consonnes = new ArrayList<String>(Arrays.asList(consSplit));
        String voy = "AEIOUY";
        String[] voySplit = voy.split("");
        ArrayList<String> voyelles = new ArrayList<String>(Arrays.asList(voySplit));

        ArrayList<String> lettresTirees = new ArrayList<>();
        String[] mots = new String[336531];
        mots = motsVersArray(mots);
        int nbreVoyelles = 0;
        Scanner scanner = new Scanner(System.in);
        String lettres = "";
        String motLePlusLong = "";
        int taille = 0; //taille du mot le plus long
        int tailleMotChoisi = 0;
        String motChoisi = "";
        boolean motCorrect = false;
        int scoreDemande = 1000;
        double scoreTotal = 0;
        double score = 0;
        short motsJouables = 4;
        short manche = 0;

        while (motsJouables > 0){
            motCorrect = false; //On réinitialise les variables
            score = 0;
            scoreTotal = 0;
            manche ++;

            while (scoreTotal < scoreDemande){
                motCorrect = false; //On réinitialise les variables
                nbreVoyelles = 0;
                lettres = "";
                lettresTirees.clear();

                System.out.println("Manche "+ manche);


                //On demande combien de voyelles sont tirées, et on vérifie qu'il y a un minimum de 2 voyelles et 2 consonnes
                while (nbreVoyelles < 2 || nbreVoyelles > 8) {
                    System.out.println("Combien de voyelles ?");
                    nbreVoyelles = Integer.parseInt(scanner.nextLine());
                    if (nbreVoyelles < 2 || nbreVoyelles > 8) {
                        System.out.println("Il faut au minimum 2 voyelles et 2 consonnes");
                    }
                }

                for (int i = 0; i < nbreVoyelles; i++) { //Tirage des voyelles
                    lettresTirees.add(voyelles.get(((random.nextInt(voyelles.size())))));
                }
                for (int i = 0; i < (10 - nbreVoyelles); i++) { //Tirage des consonnes
                    lettresTirees.add(consonnes.get(((random.nextInt(consonnes.size())))));
                }
                for (String i: lettresTirees){
                    lettres += i; //On stocke le tirage dans une variable, pour trouver le mot le plus long
                }
                while (!motCorrect) {
                    Collections.shuffle(lettresTirees); //On mélange les lettres tirées
                    for (String i : lettresTirees) {
                        System.out.print(i + " ");
                    }
                    System.out.println("");
                    Dictionary dict = new Dictionary(mots);
                    motLePlusLong = dict.getLongestWord(lettres);
                    taille = motLePlusLong.length();
                    System.out.println(motLePlusLong);
                    System.out.println("Trouvez le mot le plus long possible: (si rien n'est envoyé, mélange le tirage)");
                    System.out.println("Mot le plus long : "+ taille + " lettres");
                    System.out.println("Vous avez " + motsJouables+ " mots jouables.");
                    System.out.println("Il faut marquer encore " + (scoreDemande-scoreTotal) + " points.");
                    motChoisi = scanner.nextLine();
                    if (motChoisi.isEmpty()){
                        continue;
                    } else {
                        for (String i: mots){
                            // On vérifie que le mot choisi est dans le dictionnaire ET qu'il n'utilise que les lettres tirées
                            if (motChoisi.equals(i) && estDansLettresTirees(lettresTirees, motChoisi)){
                                tailleMotChoisi = motChoisi.length();
                                score = calculScore(taille, tailleMotChoisi);
                                scoreTotal += score;
                                System.out.println("Vous avez marqué "+ score+" points.");
                                motsJouables--;
                                motCorrect = true;
                                break;
                            }
                            else {
                                motCorrect = false;
                            }

                        }
                        if (!motCorrect){
                            System.out.println("Mot incorrect");
                        }
                    }
                }
            }
            motsJouables += 4;

        }
        System.out.println("GAME OVER");




    }
    public static String[] motsVersArray(String[] array) throws IOException {
        List<String> listOfStrings = new ArrayList<String>();

        //On charge les données
        BufferedReader bf = new BufferedReader(new FileReader("/home/deck/Documents/Java/Des lettres/src/mots.txt"));
        String line = bf.readLine();

        while (line != null) {
            line = line.trim().replaceAll("[^A-Z]", ""); // supprime tout sauf A-Z
            if (!line.isEmpty()) {                       // ignore les lignes vides
                listOfStrings.add(line);
            }
            line = bf.readLine();
        }
        bf.close();

        //On met les données dans un array
        array = listOfStrings.toArray(new String[0]);

        return array;

    }

    public static class Dictionary {
        private Set<String> wordSet;

        public Dictionary(String[] words) {
            // Stockage insensible à la casse
            wordSet = new HashSet<>();
            for (String word : words) {
                wordSet.add(word.toUpperCase());
            }
        }

        public boolean isValidWord(String word) {
            return wordSet.contains(word.toUpperCase());
        }

        public String getLongestWord(String letters) {

            int[] letterCounts = buildFrequencyArray(letters.toUpperCase());

            String longestWord = null;

            for (String word : wordSet) {
                if (canFormWord(word, letterCounts)) {
                    if (longestWord == null || word.length() > longestWord.length()) {
                        longestWord = word;
                    }
                }
            }


            return longestWord;
        }

        private int[] buildFrequencyArray(String letters) {
            int[] counts = new int[26];
            for (char c : letters.toCharArray()) {
                if (Character.isLetter(c)) {
                    counts[c - 'A']++;
                }
            }
            return counts;
        }

        private boolean canFormWord(String word, int[] letterCounts) {
            int[] tempCounts = Arrays.copyOf(letterCounts, letterCounts.length);

            for (char c : word.toCharArray()) {
                int idx = c - 'A';
                if (tempCounts[idx] == 0) {
                    return false;
                }
                tempCounts[idx]--;
            }
            return true;
        }
    }
    public static boolean estDansLettresTirees(ArrayList<String> lettresTirees, String motChoisi){
        String[] motChoisiVersArray = motChoisi.split("");
        ArrayList<String> motChoisiListe = new ArrayList<>(Arrays.asList(motChoisiVersArray));
        int i = 0;
        for (String lettre: motChoisiListe){
            if (lettresTirees.contains(lettre)){
                i++;
            }
        }
        if (i==motChoisi.length()){
            return true;
        } else {
            return false;
        }
    }
    public static double calculScore(int taille, int tailleMotChoisi){
        double score = Math.round(((double)10 / 2) * (10 - taille + tailleMotChoisi) * ((double)Math.pow(10, ((double) (10 + tailleMotChoisi) /(10 + taille)))));
        return score;
    }
}