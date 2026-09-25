/* CORRIGÉ E2 — les deux méthodes à ajouter à Classement.java */

    public static String auteurMeilleurTour(List<Ligne> lignes, String course) {
        String auteur = null;
        double meilleur = Double.MAX_VALUE;
        for (Ligne ligne : lignes) {
            if (ligne.course().equals(course) && ligne.tempsTour() > 0 && ligne.tempsTour() < meilleur) {
                meilleur = ligne.tempsTour();
                auteur = ligne.pilote();
            }
        }
        return auteur;
    }

    public static List<Resultat> classementAvecMeilleurTour(List<Ligne> lignes) {
        List<Resultat> classement = classementPilotes(lignes);

        List<String> coursesVues = new ArrayList<>();
        for (Ligne ligne : lignes) {
            if (coursesVues.contains(ligne.course())) {
                continue;
            }
            coursesVues.add(ligne.course());

            String auteur = auteurMeilleurTour(lignes, ligne.course());
            if (auteur == null) {
                continue;
            }
            for (Ligne autre : lignes) {
                if (autre.course().equals(ligne.course()) && autre.pilote().equals(auteur)
                        && autre.position() >= 1 && autre.position() <= 10) {
                    for (Resultat r : classement) {
                        if (r.nom.equals(auteur)) {
                            r.points++;
                        }
                    }
                }
            }
        }
        classement.sort((a, b) -> {
            if (a.points != b.points) return b.points - a.points;
            if (a.victoires != b.victoires) return b.victoires - a.victoires;
            if (a.deuxiemes != b.deuxiemes) return b.deuxiemes - a.deuxiemes;
            return a.nom.compareTo(b.nom);
        });
        return classement;
    }
