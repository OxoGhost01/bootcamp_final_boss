/* FOURNI — NE PAS MODIFIER
   Lecture du contrat 1 et écriture du fichier destiné au maillon JavaScript. */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Chargeur {

    public static List<Ligne> chargerCourses(String chemin) throws IOException {
        List<Ligne> lignes = new ArrayList<>();
        try (BufferedReader lecteur = Files.newBufferedReader(Path.of(chemin), StandardCharsets.UTF_8)) {
            lecteur.readLine();                       // en-tête
            String ligne;
            while ((ligne = lecteur.readLine()) != null) {
                if (ligne.isBlank()) {
                    continue;
                }
                String[] champs = ligne.split(";", -1);
                double temps = champs[4].isBlank() ? -1 : Double.parseDouble(champs[4]);
                lignes.add(new Ligne(champs[0], champs[1], champs[2],
                        Integer.parseInt(champs[3]), temps));
            }
        }
        return lignes;
    }

    public static void ecrireDonneesJs(String chemin, List<Resultat> pilotes, List<Resultat> ecuries)
            throws IOException {
        try (BufferedWriter f = Files.newBufferedWriter(Path.of(chemin), StandardCharsets.UTF_8)) {
            f.write("const PILOTES = [");
            for (int i = 0; i < pilotes.size(); i++) {
                Resultat r = pilotes.get(i);
                f.write((i > 0 ? ", " : "") + "{\"nom\": \"" + r.nom + "\", \"ecurie\": \"" + r.ecurie
                        + "\", \"points\": " + r.points + ", \"victoires\": " + r.victoires + "}");
            }
            f.write("];\n");
            f.write("const ECURIES = [");
            for (int i = 0; i < ecuries.size(); i++) {
                Resultat r = ecuries.get(i);
                f.write((i > 0 ? ", " : "") + "{\"nom\": \"" + r.nom + "\", \"points\": " + r.points
                        + ", \"victoires\": " + r.victoires + "}");
            }
            f.write("];\n");
        }
    }
}
