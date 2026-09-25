/* Tests EXTENSION E2 — copiez ce fichier dans 02-java/src/ puis :
   javac -encoding UTF-8 -d out src/*.java
   java -Dstdout.encoding=UTF-8 -cp out TestsE2                              */

import java.util.ArrayList;
import java.util.List;

public class TestsE2 {

    private static int reussis = 0;
    private static int total = 0;

    private static final List<Ligne> MINI = List.of(
            // course A : le meilleur tour est signé par GAMMA, non classé -> pas de point
            new Ligne("A", "ALPHA", "E1", 1, 90.500),
            new Ligne("A", "BETA", "E1", 2, 90.800),
            new Ligne("A", "GAMMA", "E2", 0, 89.900),
            // course B : le meilleur tour est signé par BETA, 3e -> point accordé
            new Ligne("B", "ALPHA", "E1", 1, 91.000),
            new Ligne("B", "GAMMA", "E2", 2, 90.900),
            new Ligne("B", "BETA", "E1", 3, 90.100));

    public static void main(String[] args) {
        verifier("E2-1. auteurMeilleurTour", () -> {
            egal(Classement.auteurMeilleurTour(MINI, "A"), "GAMMA");
            egal(Classement.auteurMeilleurTour(MINI, "B"), "BETA");
            egal(Classement.auteurMeilleurTour(MINI, "INCONNUE"), null);
        });

        verifier("E2-2. classementAvecMeilleurTour (petit jeu)", () -> {
            List<Resultat> c = Classement.classementAvecMeilleurTour(MINI);
            egal(noms(c), "[ALPHA, BETA, GAMMA]");
            egal(c.get(0).points, 50);      // 25 + 25, pas de meilleur tour
            egal(c.get(1).points, 34);      // 18 + 15 + 1 (meilleur tour course B, 3e)
            egal(c.get(2).points, 18);      // 0 (abandon) + 18, aucun point malgré le meilleur tour
        });

        verifier("E2-3. saison complète", () -> {
            List<Ligne> lignes;
            try {
                lignes = Chargeur.chargerCourses("courses_propres.csv");
            } catch (Exception e) {
                throw new AssertionError("courses_propres.csv illisible : " + e.getMessage());
            }
            List<Resultat> avec = Classement.classementAvecMeilleurTour(lignes);
            List<Resultat> sans = Classement.classementPilotes(lignes);
            egal(avec.get(0).nom, "VERSTAPPEN");
            egal(avec.get(0).points, 102);
            egal(sans.get(0).points, 101);
            int totalAvec = 0;
            int totalSans = 0;
            for (int i = 0; i < avec.size(); i++) {
                totalAvec += avec.get(i).points;
                totalSans += sans.get(i).points;
            }
            egal(totalAvec - totalSans, 5);   // un point par course
        });

        System.out.println();
        System.out.println(reussis + " / " + total + " tests réussis"
                + (reussis == total ? " — extension E2 validée 🎉" : ""));
        if (reussis != total) {
            System.exit(1);
        }
    }

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
