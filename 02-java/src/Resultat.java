/* FOURNI — NE PAS MODIFIER
   Une ligne de classement (pilote ou écurie). */
public class Resultat {
    public final String nom;
    public final String ecurie;   // vide pour une écurie
    public int points;
    public int victoires;
    public int deuxiemes;

    public Resultat(String nom, String ecurie) {
        this.nom = nom;
        this.ecurie = ecurie;
    }

    @Override
    public String toString() {
        return nom + " (" + points + " pts, " + victoires + "V)";
    }
}
