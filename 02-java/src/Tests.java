/* Tests automatiques du maillon Java — NE PAS MODIFIER.
   javac -encoding UTF-8 -d out src/*.java
   java -Dstdout.encoding=UTF-8 -cp out Tests                                */

import java.util.ArrayList;
import java.util.List;

public class Tests {

    private static int reussis = 0;
    private static int total = 0;

    private static final List<Ligne> MINI = List.of(
            new Ligne("A", "ALPHA", "Ecurie1", 1, 90.0),
            new Ligne("A", "BETA", "Ecurie1", 2, 91.0),
            new Ligne("A", "GAMMA", "Ecurie2", 0, -1),
            new Ligne("B", "GAMMA", "Ecurie2", 1, 89.5),
            new Ligne("B", "ALPHA", "Ecurie1", 2, 90.2),
            new Ligne("B", "BETA", "Ecurie1", 11, 92.0));

    public static void main(String[] args) throws Exception {
        verifier("1. pointsPourPosition", () -> {
            egal(Classement.pointsPourPosition(1), 25);
            egal(Classement.pointsPourPosition(2), 18);
            egal(Classement.pointsPourPosition(10), 1);
            egal(Classement.pointsPourPosition(11), 0);
            egal(Classement.pointsPourPosition(0), 0);
        });

        verifier("2. classementPilotes (petit jeu)", () -> {
            List<Resultat> c = Classement.classementPilotes(MINI);
            egal(c.size(), 3);
            egal(noms(c), "[ALPHA, GAMMA, BETA]");
            egal(c.get(0).points, 43);     // 25 + 18
            egal(c.get(1).points, 25);     // 25 + 0 (abandon)
            egal(c.get(2).points, 18);     // 18 + 0 (11e)
            egal(c.get(0).victoires, 1);
            egal(c.get(0).deuxiemes, 1);
            egal(c.get(0).ecurie, "Ecurie1");
        });

        verifier("3. classementEcuries", () -> {
            List<Resultat> e = Classement.classementEcuries(Classement.classementPilotes(MINI));
            egal(e.size(), 2);
            egal(noms(e), "[Ecurie1, Ecurie2]");
            egal(e.get(0).points, 61);
            egal(e.get(1).points, 25);
            egal(e.get(0).victoires, 1);
        });

        verifier("4. positionMoyenne (abandons exclus)", () -> {
            egal(Classement.positionMoyenne(MINI, "ALPHA"), 1.5);
            egal(Classement.positionMoyenne(MINI, "GAMMA"), 1.0);
            egal(Classement.positionMoyenne(MINI, "BETA"), 6.5);
            egal(Classement.positionMoyenne(MINI, "INCONNU"), 0.0);
        });

        verifier("5. saison complète (courses_propres.csv)", () -> {
            List<Ligne> lignes;
            try {
                lignes = Chargeur.chargerCourses("courses_propres.csv");
            } catch (Exception e) {
                throw new AssertionError("courses_propres.csv illisible — le maillon Python "
                        + "a-t-il produit le fichier ? (" + e.getMessage() + ")");
            }
            egal(lignes.size(), 50);
            List<Resultat> pilotes = Classement.classementPilotes(lignes);
            egal(pilotes.size(), 10);
            egal(pilotes.get(0).nom, "VERSTAPPEN");
            egal(pilotes.get(0).points, 101);
            egal(pilotes.get(1).nom, "LECLERC");
            egal(pilotes.get(1).points, 95);
            egal(pilotes.get(9).nom, "STROLL");
            // PEREZ et PIASTRI sont à égalité de points : départage aux 2e places
            egal(pilotes.get(5).nom, "PEREZ");
            egal(pilotes.get(6).nom, "PIASTRI");

            List<Resultat> ecuries = Classement.classementEcuries(pilotes);
            egal(noms(ecuries), "[Red Bull, Ferrari, McLaren, Mercedes, Aston Martin]");
            egal(ecuries.get(0).points, 139);
            egal(ecuries.get(1).points, 134);
            egal(Classement.positionMoyenne(lignes, "VERSTAPPEN"), 1.8);
        });

        System.out.println();
        System.out.println(reussis + " / " + total + " tests réussis"
                + (reussis == total ? " — maillon Java validé 🎉" : ""));
        if (reussis != total) {
            System.exit(1);
        }
    }

    // ---------------------------------------------------------------- outils
    private static String noms(List<Resultat> classement) {
        List<String> noms = new ArrayList<>();
        for (Resultat r : classement) {
            noms.add(r.nom);
        }
        return noms.toString();
    }

    private static void verifier(String nom, Runnable controle) {
        total++;
        try {
            controle.run();
            reussis++;
            System.out.println("✅ " + nom);
        } catch (AssertionError e) {
            System.out.println("❌ " + nom + "  →  " + e.getMessage());
        } catch (Throwable e) {
            System.out.println("❌ " + nom + "  →  " + e.getClass().getSimpleName()
                    + (e.getMessage() == null ? "" : " : " + e.getMessage()));
        }
    }

    private static void egal(Object obtenu, Object attendu) {
        if (!String.valueOf(attendu).equals(String.valueOf(obtenu))) {
            throw new AssertionError("attendu " + attendu + ", obtenu " + obtenu);
        }
    }
}
