Ce programme est une simulation basée sur un modèle probabiliste qui permet de simuler la propagation d'un feu de forêt. 
Les paramètres de la simulation sont lus à partir d'un fichier de configuration JSON et sont utilisés pour initialiser la grille de simulation.

Au départ, une propagation initiale est appliquée à partir des positions spécifiées, puis la méthode simuler() est appelée. 
Cette méthode est conçue pour poursuivre la propagation du feu à chaque étape tant qu'il y a encore des arbres en feu sur la grille.

La méthode propagerFeu() est utilisée pour propager le feu à partir des arbres en feu voisins et pour mettre à jour la grille. 
Pour déterminer si la simulation est terminée, la méthode estEnFeu() est utilisée. Enfin, à chaque étape, la méthode afficherGrille() est utilisée pour afficher la grille de simulation.

Pour lire le fichier de configuration JSON et initialiser les paramètres de la simulation, le programme utilise la bibliothèque Gson.
