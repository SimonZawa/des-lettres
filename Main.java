import java.lang.reflect.Array;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.sql.SQLOutput;
import java.util.*;


public class Main {
    public static void main(String[] args) {

        //On transforme les consonnes et voyelles (string) en listes
        String cons = "BBCCCFFGGHHJKLLLLMMMNNNNPPPQQRRRRSSSSTTTTVVWXZ";
        String[] consSplit = cons.split("");
        ArrayList<String> consonnes = new ArrayList<String>(Arrays.asList(consSplit));
        String voy = "AAAEEEEIIIOOUUY";
        String[] voySplit = voy.split("");
        ArrayList<String> voyelles = new ArrayList<String>(Arrays.asList(voySplit));

        ArrayList<String> lettresTirees = new ArrayList<>();
        ArrayList<String> mots;

        ArrayList<Livre> livres = initialiserLivres();


        int nbreVoyelles;
        String lettres;
        String motLePlusLong;
        int taille; //taille du mot le plus long
        String motChoisi = "";
        double scoreDemande = 1000;
        double scoreTotal;
        double score;
        short motsJouables = 4;
        short manche = 0;
        int points;
        String livreAchete;

        mots = lireFichier(); //Stockage des mots français dans une liste
        Dictionnaire dico = new Dictionnaire(mots);

        while (motsJouables > 0) { //Représente une partie

            //On réinitialise le score et on incrémente la manche
            scoreTotal = 0;
            manche++;

            while (scoreTotal < scoreDemande) { //Représente une manche

                motChoisi = null; //On réinitialise les variables
                lettresTirees.clear();

                System.out.println("Manche " + manche);

                nbreVoyelles = entrerNombreVoyelles();
                //On demande au joueur d'entrer le nombre de voyelles

                Tirage tirage = new Tirage(lettresTirees, voyelles, consonnes);
                lettresTirees = tirage.tirerLettres(voyelles, consonnes, nbreVoyelles);
                //On tire les lettres que le joueur devra utiliser

                lettres = tirage.getLettres(lettresTirees);
                //On stocke le tirage dans une variable, pour trouver le mot le plus long

                while (motChoisi == null) { //Représente un mot joué dans la manche

                    Collections.shuffle(lettresTirees); //On mélange les lettres tirées

                    motLePlusLong = dico.getMotLePlusLong(lettres);
                    taille = motLePlusLong.length();

                    affichageMot(lettresTirees, taille, scoreDemande, scoreTotal, motsJouables);
                    //Affichage du tirage et de diverses informations
                    System.out.println(motLePlusLong);

                    motChoisi = motCorrect(lettresTirees, dico);
                    //On demande de choisir un mot, et on vérifie qu'il existe et qu'il soit écrit avec le tirage de lettres

                    if (motChoisi != null) {

                        points = calculPoints(motChoisi, livres);
                        score = calculScore(taille, motChoisi.length(), points);
                        scoreTotal += score;
                        System.out.println("Vous avez marqué " + score + " points.");
                        motsJouables--;

                    }
                }
            }

            System.out.println("Récompense : 4 mots jouables");
            motsJouables += 4;
            livreAchete = marchand(livres, motsJouables); //Le marchand propose un livre
            scoreDemande = scoreDemande * 1.3; //Le score demandé augmente

            if (livreAchete != null) { //Si le joueur a acheté un livre

                for (Livre livre : livres) {

                    if (livreAchete.equals(livre.getDescription())) { //On le marque comme "obtenu",
                                                                    //et on augmente de 1 l'exemplaire si on l'a déjà
                        if (livre.getObtenu()) {

                            livre.plusUnExemplaire();
                        }

                        livre.setObtenu();
                        motsJouables -= (short) livre.getPrix(); //On paye le prix du livre


                    }
                }
            }
        }
        System.out.println("GAME OVER");

    }
    public static ArrayList<String> lireFichier() {

        ArrayList<String> mots = new ArrayList<>();

        try (Scanner litFichier = new Scanner(Paths.get("src/mots.txt"))) {

            while (litFichier.hasNextLine()) {

                String ligne = litFichier.nextLine();

                if (ligne.isEmpty() || ligne.contains("-")) {

                    continue;
                }

                mots.add(ligne);
            }
        } catch (Exception e) {

            System.out.println("Erreur lecture fichier");
        }

        return mots;

    }
    public static int entrerNombreVoyelles() {

        Scanner scanner = new Scanner(System.in);
        int nbreVoyelles = 0;
        String nbreVoyellesString;

        //On demande combien de voyelles sont tirées, et on vérifie qu'il y ait un minimum de 2 voyelles et 2 consonnes
        while (nbreVoyelles < 2 || nbreVoyelles > 8) {

            System.out.println("Combien de voyelles ?");
            nbreVoyellesString = scanner.nextLine();

            if (!estUnNombre(nbreVoyellesString)) { //On vérifie que ce qui est entré est bien un nombre

                continue;
            }

            nbreVoyelles = Integer.parseInt(nbreVoyellesString);

            if (nbreVoyelles < 2 || nbreVoyelles > 8) {

                System.out.println("Il faut au minimum 2 voyelles et 2 consonnes");
            }
        }
        return nbreVoyelles;
    }
    public static boolean estUnNombre(String entree) {

        try { //On vérifie que ce qui est entré est bien un nombre

            Integer.parseInt(entree);
            return true;

        } catch (NumberFormatException e) {

            System.out.println("Doit être un nombre !");
            return false;
        }
    }

    public static void affichageMot(ArrayList<String> lettresTirees,
                                    int taille,
                                    double scoreDemande,
                                    double scoreTotal,
                                    short motsJouables) {

        for (String lettre : lettresTirees) { //Affichage du tirage

            System.out.print(lettre + " ");
        }

        System.out.println("\nTrouvez le mot le plus long possible : (si rien n'est envoyé, mélange le tirage)");
        System.out.println("Mot le plus long : " + taille + " lettres.");
        System.out.println("Vous avez " + motsJouables + " mots jouables.");
        System.out.println("Il faut marquer encore " + (scoreDemande - scoreTotal) + " points.");
    }

    public static String motCorrect(ArrayList<String> lettresTirees,
                                    Dictionnaire dico) {

        Scanner scanner = new Scanner(System.in);
        String motChoisi = scanner.nextLine().toUpperCase();

        if (motChoisi.isEmpty()) {

            return null;
        }

        if (dico.estValide(motChoisi) && estDansLettresTirees(lettresTirees, motChoisi)) {

            return motChoisi;
        }

        System.out.println("Mot incorrect");
        return null;
    }

    public static boolean estDansLettresTirees(ArrayList<String> lettresTirees,
                                               String motChoisi) {

        String[] motChoisiVersArray = motChoisi.split("");
        ArrayList<String> motChoisiListe = new ArrayList<>(Arrays.asList(motChoisiVersArray));
        ArrayList<String> copieLettresTirees = new ArrayList<>(lettresTirees);

        int i = 0;

        for (String lettre : motChoisiListe) {

            if (copieLettresTirees.contains(lettre)) {

                i++;
                copieLettresTirees.remove(lettre);
            }
        }
        if (i == motChoisi.length()) {

            return true;

        } else {
            return false;
        }
    }
    public static double calculScore(int taille,
                                     int tailleMotChoisi,
                                     int points) {

        return Math.round(points + (double) 10 / 2) * (10 - taille + tailleMotChoisi)
                * (Math.pow(10, ((double) (10 + tailleMotChoisi) / (10 + taille))));
    }

    public static ArrayList<Livre> initialiserLivres() {

        ArrayList<Livre> livres = new ArrayList<>();

        livres.add(new Livre("+3 points"));
        livres.add(new Livre("+5 points si le mot choisi commence par une voyelle"));
        livres.add(new Livre("+3 points par voyelle jouée"));
        livres.add(new Livre("+3 points par consonne jouée"));

        for (Livre livre : livres) {

            if (livre.getDescription().equals("+3 points")) {

                livre.setPrix(1);
                livre.setPoints(3);

            }
            if (livre.getDescription().equals("+5 points si le mot choisi commence par une voyelle")) {

                livre.setPrix(1);
                livre.setPoints(5);

            }
            if (livre.getDescription().equals("+3 points par voyelle jouée")) {

                livre.setPrix(2);
                livre.setPoints(3);

            }
            if (livre.getDescription().equals("+3 points par consonne jouée")) {

                livre.setPrix(2);
                livre.setPoints(3);

            }
        }
        return livres;
    }

    public static int calculPoints(String motChoisi,
                                   ArrayList<Livre> livres) {

        int points = 0;
        String[] motChoisiListe = motChoisi.split("");

        for (Livre livre : livres) {

            if (livre.getDescription().equals("+3 points")) {

                points += livre.getPoints();
                points *= livre.getExemplaire();

            }
            if (livre.getDescription().equals("+5 points si le mot choisi commence par une voyelle")) {

                if ("AEIOUY".contains(String.valueOf(motChoisi.charAt(0)))) {

                    points += livre.getPoints();
                }

            }
            if (livre.getDescription().equals("+3 points par voyelle jouée")) {

                int compteur = 0;

                for (String lettre : motChoisiListe) {

                    if ("AEIOUY".contains(lettre)) {

                        compteur++;

                    }
                }
                points += livre.getPoints()*compteur;


            }
            if (livre.getDescription().equals("+3 points par consonne jouée")) {

                int compteur = 0;

                for (String lettre : motChoisiListe) {

                    if ("BCDFGHJKLMNPQRSTVWXZ".contains(lettre)) {

                        compteur++;

                    }
                }
                points += livre.getPoints() * compteur;

            }
        }
        return points;
    }

    public static String marchand(ArrayList<Livre> livres, int motsJouables) {

        SecureRandom random = new SecureRandom();
        Scanner scanner = new Scanner(System.in);
        String acheter = "";
        int livreAleatoire = random.nextInt(livres.size());
        int compteur = 0;

        for (Livre livre : livres) {

            if (livreAleatoire == compteur) {

                System.out.println("Marchand :");
                System.out.println("Livre proposé :");
                System.out.println(livre.getDescription());
                System.out.println("Vous avez " + motsJouables + " mots.");
                System.out.println("Prix : " + livre.getPrix() + " mots.");

                while (!acheter.equals("OUI") && !acheter.equals("NON")) {

                    System.out.println("Acheter ? (oui/non)");
                    acheter = scanner.nextLine().toUpperCase();

                }

                if (acheter.equals("OUI")) {

                    return livre.getDescription();

                } else {

                    return null;
                }
            }
            compteur++;
        }

        return null;
    }

}