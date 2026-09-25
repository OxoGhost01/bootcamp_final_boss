/* =========================================================================
   MAILLON 2 — JAVA : le moteur de calcul
   Complétez les quatre méthodes. Les classes Ligne, Resultat et Chargeur
   sont fournies : ne les modifiez pas.
       javac -encoding UTF-8 -d out src/*.java
       java -Dstdout.encoding=UTF-8 -cp out Tests     (les tests)
       java -Dstdout.encoding=UTF-8 -cp out Main      (la production)
   ========================================================================= */

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Classement {

    /** Barème officiel des dix premiers. FOURNI — NE PAS MODIFIER. */
    public static final int[] BAREME = {25, 18, 15, 12, 10, 8, 6, 4, 2, 1};

    // réduction de code : 2 fonctions ont la même implémentation
    private static final Comparator<Resultat> ORDRE = Comparator
            .comparingInt((Resultat r) -> -r.points)
            .thenComparingInt(r -> -r.victoires)
            .thenComparingInt(r -> -r.deuxiemes)
            .thenComparing(r -> r.nom);

    // 1. pointsPourPosition(position) : points marqués pour cette position.
    //    1 -> 25, 2 -> 18, ..., 10 -> 1. Au-delà de la 10e place : 0.
    //    Un abandon vaut la position 0, donc 0 point.
    public static int pointsPourPosition(int position) {
        return (position < 1 || position > 10) ? 0 : BAREME[position - 1];
    }

    // 2. classementPilotes(lignes) : un Resultat par pilote, avec ses points,
    //    ses victoires (position 1) et ses 2e places, trié par :
    //    points décroissants, puis victoires, puis 2e places, puis nom (A→Z).
    public static List<Resultat> classementPilotes(List<Ligne> lignes) {
        Map<String, Resultat> pilotes = new HashMap<>();
        for (Ligne ligne : lignes) {
            Resultat resultat = pilotes.get(ligne.pilote());
            if (resultat == null) {
                resultat = new Resultat(ligne.pilote(), ligne.ecurie());
                pilotes.put(ligne.pilote(), resultat);
            }
            resultat.points += pointsPourPosition(ligne.position());
            if (ligne.position() == 1) resultat.victoires++;
            if (ligne.position() == 2) resultat.deuxiemes++;
        }
        return pilotes.values().stream().sorted(ORDRE).toList();
    }

    // 3. classementEcuries(pilotes) : additionne les points, victoires et
    //    2e places des pilotes de chaque écurie. Même ordre de tri.
    public static List<Resultat> classementEcuries(List<Resultat> pilotes) {
        Map<String, Resultat> ecuries = new HashMap<>();
        for (Resultat pilote : pilotes) {
            Resultat ecurie = ecuries.get(pilote.ecurie);
            if (ecurie == null) {
                ecurie = new Resultat(pilote.ecurie, "");
                ecuries.put(pilote.ecurie, ecurie);
            }
            ecurie.points += pilote.points;
            ecurie.victoires += pilote.victoires;
            ecurie.deuxiemes += pilote.deuxiemes;
        }
        return ecuries.values().stream().sorted(ORDRE).toList();
    }

    // 4. positionMoyenne(lignes, pilote) : moyenne des positions de ce pilote,
    //    ABANDONS EXCLUS, arrondie à 2 décimales. 0 s'il n'a jamais terminé.
    //    Ex. positions 1, 2 et un abandon -> 1.5
    public static double positionMoyenne(List<Ligne> lignes, String pilote) {
        int nombre = 0;
        double somme = 0;
        for (Ligne ligne : lignes) {
            if (ligne.pilote().equals(pilote) && ligne.position() != 0) {
                somme += ligne.position();
                nombre++;
            }
        }
        return nombre == 0 ? 0 : Math.round(somme / nombre * 100) / 100.0;
    }
}
