/* FOURNI — NE PAS MODIFIER
   Une ligne du contrat 1 : un pilote dans une course.
   position vaut 0 en cas d'abandon ; tempsTour vaut -1 si le temps est absent. */
public record Ligne(String course, String pilote, String ecurie, int position, double tempsTour) {
}
