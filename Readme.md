Ce code implémente une simulation de propagation d'incendie dans une forêt. Il lit les paramètres de la simulation à partir d'un fichier JSON, crée une grille 
pour la forêt, simule la propagation de l'incendie et affiche la grille après chaque étape de simulation.

La méthode lireConfiguration est appelée à partir de la méthode main pour lire les paramètres de la simulation à partir d'un fichier JSON, puis créer une nouvelle instance de la classe
Simulation avec ces paramètres et appeler la méthode simuler.

La classe Simulation a une méthode constructeur qui initialise les attributs de la classe hauteur, largeur, probabilitePropagation,
grille et arbreBrule. La grille est initialisée avec les positions initiales des arbres en feu. La méthode simuler implémente une
boucle while qui s'exécute tant qu'il reste des arbres en feu. Dans chaque itération de la boucle, elle affiche la grille, met à jour la
grille en propageant le feu à partir des cases en feu et incrémente le nombre d'étapes de la simulation.

La méthode propagerFeu propage le feu à partir d'une case en feu à des cases adjacentes avec une certaine probabilité.
Elle met également à jour les grilles nouvelleGrille et arbreBrule.

Le programme utilise la bibliothèque gson pour lire les paramètres à partir d'un fichier JSON.
