public class Livre {
    private String description;
    private int points;
    private boolean obtenu;
    private int exemplaire;
    private int prix;

    public Livre(String description) {

        this.description = description;
        this.obtenu = false;
        this.exemplaire = 1;
    }

    public String getDescription() {

        return this.description;
    }

    public int getExemplaire() {

        return this.exemplaire;
    }

    public void plusUnExemplaire() {

        this.exemplaire++;
    }

    public void setObtenu() {

        this.obtenu = true;
    }

    public void setPoints(int points) {

        this.points = points;
    }

    public void setPrix(int prix) {

        this.prix = prix;
    }

    public int getPoints () {

        if (this.obtenu) {

            return this.points;

        } else {

            return 0;
        }
    }

    public int getPrix() {

        return this.prix;
    }

    public boolean getObtenu() {

        return this.obtenu;
    }
}
