import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;

public class Simulation {
    private int hauteur;
    private int largeur;
    private double probabilitePropagation;
    private boolean[][] grille;
    private boolean[][] arbreBrule;

    public static void main(String[] args) {
        lireConfiguration("config.json");
    }

    private static void lireConfiguration(String fichier) {
        Gson gson = new Gson();
        try {
            Scanner scanner = new Scanner(new File(fichier));
            String json = scanner.useDelimiter("\\Z").next();
            scanner.close();

            // Extraire les paramètres à partir du fichier JSON
            Parametres parametres = gson.fromJson(json, Parametres.class);

            // Création d'une nouvelle instance de la classe Simulation avc les paramètres extraits du fichier JSON
            Simulation simulation = new Simulation(parametres.hauteur, parametres.longueur, parametres.probabilitePropagation, parametres.positionsInitiales);

            // Lancer la simulation
            simulation.simuler();

        } catch (FileNotFoundException e) {
            System.out.println("Fichier de configuration introuvable : " + fichier);
        }
    }


    public Simulation(int hauteur, int largeur, double probabilitePropagation, List<Integer> positionsInitiales) {
        this.hauteur = hauteur; // Hauteur de la grille
        this.largeur = largeur; // Longueur de la grille
        this.probabilitePropagation = probabilitePropagation; // Probabilité qu'un feu se propage à une case adjacente
        this.grille = new boolean[hauteur][largeur]; // Initialiser une grille vide
        this.arbreBrule = new boolean[hauteur][largeur];// Initialiser une grille vide pour les arbres brulés

        // On initialise les grilles avec les positions de depart des incendies
        //On avance de 2 en 2 sur cette boucle for car la liste positionsInitiales contient les positions initiales des arbres en deux dimensions,
        // avec chaque paire de valeurs représentant les coordonnées x et y respectivement.
        // Ainsi, en avançant de 2 en 2, nous récupérons les valeurs x et y correspondantes à chaque arbre, et elles sont utilisé pour initialiser la grille et l'arbre brûlé.
        for (int i = 0; i < positionsInitiales.size(); i += 2) {// On avance de 2 en 2 pour avoir les coordonnées x et y
            int x = positionsInitiales.get(i);
            int y = positionsInitiales.get(i + 1);
            grille[x][y] = true;
            arbreBrule[x][y] = true;
        }

        /*
        La grille principale grille[][] est utilisée pour stocker l'état actuel de la forêt, où chaque élément de la grille représente un emplacement dans la forêt.
        Lorsqu'un arbre est en feu, l'élément de la grille correspondant est mis à true.

        La grille auxiliaire arbreBrule[][] est utilisée pour stocker les nouveaux arbres qui prennent feu lors d'une étape de simulation.
        Lorsqu'un arbre est allumé, il n'est pas brûlé immédiatement, mais doit attendre l'étape suivante de la simulation pour se propager aux arbres adjacents.
        Pour éviter de brûler les mêmes arbres plusieurs fois pendant la même étape de simulation, nous stockons les nouveaux arbres en feu dans la grille arbreBrule[][]
        et les transférons dans la grille principale grille[][] à la fin de chaque étape de simulation.

        Les deux grilles sont nécessaires pour gérer la propagation du feu dans la forêt et éviter de brûler plusieurs fois les mêmes arbres pendant la même étape de simulation.
        */

    }
    public void simuler() {
        int t = 0;
        while (estEnFeu()) {
            System.out.println("Étape " + t + " :");
            afficherGrille();

            // Mettre à jour la grille
            boolean[][] nouvelleGrille = new boolean[hauteur][largeur];
            for (int ligne = 0; ligne < hauteur; ligne++) {
                for (int colonne = 0; colonne < largeur; colonne++) {
                    if (grille[ligne][colonne]) {
                        propagerFeu(arbreBrule,nouvelleGrille, ligne, colonne);
                    } else {
                        // La case n'est pas en feu, elle reste comme elle est
                        nouvelleGrille[ligne][colonne] = grille[ligne][colonne];
                    }
                }
            }
            grille = nouvelleGrille;

            t++;
        }
        System.out.println("Terminé après " + t + " étapes.");
    }



    /*
Cette méthode est utilisée pour propager le feu à partir d'une cellule en feu vers les cellules adjacentes dans la grille.
La première ligne nouvelleGrille[ligne][colonne] = false; permet de stocker les arbres qui ne sont pas en feu dans la grille mise à jour.
La deuxième ligne arbreBrule[ligne][colonne] = true; sert à stocker les arbres qui sont en feu dans une grille distincte. Cela permet d'éviter de propager le feu plusieurs fois sur le même arbre.
Les conditions if qui suivent permettent de propager le feu aux cases adjacentes avec une certaine probabilité (probabilitePropagation). Elles vérifient que la case adjacente n'est pas déjà en feu (!arbreBrule[ligne -1][colonne],
par exemple) et que la probabilité de propagation est respectée (Math.random() < probabilitePropagation). Si les deux conditions sont remplies, la case adjacente est mise en feu dans la grille mise à jour (nouvelleGrille[ligne -1][colonne] = true;) et un message est affiché dans la console pour indiquer que le feu a été propagé en cette position (System.out.println("Feu propagé en (" + (ligne -1) + ", " + colonne + ")");).
Le code vérifie les cases adjacentes dans les quatre directions possibles (en haut, en bas, à gauche et à droite).*/




    private void propagerFeu(boolean[][] arbreBrule, boolean[][] nouvelleGrille, int ligne, int colonne) {
// La case est en feu, elle s'éteint
        nouvelleGrille[ligne][colonne] = false; // Sert à stocker les arbres qui ne sont pas en feu
        arbreBrule[ligne][colonne] = true; // Sert à stocker les arbres qui sont en feu
        // Propager le feu aux cases adjacentes avec une certaine probabilité
        if (ligne > 0 && !arbreBrule[ligne -1][colonne] && Math.random() < probabilitePropagation) { // Si la ligne > 0  cela signifie que la case a une case adjacente en haut qui pourrait potentiellement propager le feu, si cette case n'est pas déjà en feu et que la probabilité de propagation est respectée
            nouvelleGrille[ligne -1][colonne] = true;// alors la casedu haut est mise en feu et la grille mise à jour.
            System.out.println("Feu propagé en (" + (ligne -1) + ", " + colonne + ")");
        }
        if (colonne > 0 && !arbreBrule[ligne][colonne -1] && Math.random() < probabilitePropagation) { // Case à gauche
            nouvelleGrille[ligne][colonne -1] = true;
            System.out.println("Feu propagé en (" + ligne + ", " + (colonne -1) + ")");
        }
        if (ligne < hauteur-1 && !arbreBrule[ligne +1][colonne] && Math.random() < probabilitePropagation) { // Case en bas
            nouvelleGrille[ligne +1][colonne] = true;
            System.out.println("Feu propagé en (" + (ligne +1) + ", " + colonne + ")");
        }
        if (colonne < largeur -1 && !arbreBrule[ligne][colonne +1] && Math.random() < probabilitePropagation) { // Case à droite
            nouvelleGrille[ligne][colonne +1] = true;
            System.out.println("Feu propagé en (" + ligne + ", " + (colonne +1) + ")");
        }
    }

    private boolean estEnFeu() {
        for (int ligne = 0; ligne < hauteur; ligne++) {
            for (int colonne = 0; colonne < largeur; colonne++) {
                if (grille[ligne][colonne]) {
                    return true;
                }
            }
        }
        return false;
    }

    private void afficherGrille() {
        for (int hauteurIndex = 0; hauteurIndex < hauteur; hauteurIndex++) {
            for (int largeurIndex = 0; largeurIndex < largeur; largeurIndex++) {
                if (grille[hauteurIndex][largeurIndex]) {
                    System.out.print("X "); // X représente un arbre en feu
                } else if (arbreBrule[hauteurIndex][largeurIndex]) {
                    System.out.print(". "); // . représente un arbre brulé
                } else {
                    System.out.print("O "); // O représente un arbre sain qui n'est pas en feu
                }
            }
            System.out.println();
        }
        System.out.println();
    }





}


/* ****************** JSON **************************
"hauteur" : la hauteur de la grille en nombre de cases
"longueur" : la longueur de la grille en nombre de cases
"probabilitePropagation" : la probabilité qu'un feu se propage à une case adjacente lorsqu'une case en feu est mise à jour
"positionsInitiales" : Coordonnées (x, y) des cases en feu. Les coordonnées sont stockées dans l'ordre x1, y1, x2, y2, x3, y3, etc.
*/


/*
* Ce code implémente une simulation de propagation d'incendie dans une forêt. Il lit les paramètres de la simulation à partir d'un fichier JSON, crée une grille pour la forêt,
* simule la propagation de l'incendie et affiche la grille après chaque étape de simulation.

La méthode lireConfiguration est appelée à partir de la méthode main pour lire les paramètres de la simulation à partir d'un fichier JSON, puis créer une nouvelle instance de la classe
 Simulation avec ces paramètres et appeler la méthode simuler.

La classe Simulation a une méthode constructeur qui initialise les attributs de la classe hauteur, largeur, probabilitePropagation,
grille et arbreBrule. La grille est initialisée avec les positions initiales des arbres en feu. La méthode simuler implémente une
boucle while qui s'exécute tant qu'il reste des arbres en feu. Dans chaque itération de la boucle, elle affiche la grille, met à jour la
grille en propageant le feu à partir des cases en feu et incrémente le nombre d'étapes de la simulation.

La méthode propagerFeu propage le feu à partir d'une case en feu à des cases adjacentes avec une certaine probabilité.
Elle met également à jour les grilles nouvelleGrille et arbreBrule.

Le programme utilise la bibliothèque gson pour lire les paramètres à partir d'un fichier JSON.
*
* */