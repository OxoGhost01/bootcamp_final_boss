/* FOURNI — NE PAS MODIFIER. Produit le fichier destiné au maillon JavaScript. */
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        List<Ligne> lignes = Chargeur.chargerCourses("courses_propres.csv");
        System.out.println(lignes.size() + " lignes chargées");

        List<Resultat> pilotes = Classement.classementPilotes(lignes);
        List<Resultat> ecuries = Classement.classementEcuries(pilotes);

        System.out.println("\nClassement pilotes :");
        for (int i = 0; i < pilotes.size(); i++) {
            Resultat r = pilotes.get(i);
            System.out.printf("%2d. %-12s %-14s %3d pts  (%dV, position moyenne %.2f)%n",
                    i + 1, r.nom, r.ecurie, r.points, r.victoires,
                    Classement.positionMoyenne(lignes, r.nom));
        }

        System.out.println("\nClassement écuries :");
        for (int i = 0; i < ecuries.size(); i++) {
            System.out.printf("%d. %-14s %3d pts%n", i + 1, ecuries.get(i).nom, ecuries.get(i).points);
        }

        Chargeur.ecrireDonneesJs("../03-js/donnees.js", pilotes, ecuries);
        System.out.println("\nFichier écrit : ../03-js/donnees.js");
    }
}
