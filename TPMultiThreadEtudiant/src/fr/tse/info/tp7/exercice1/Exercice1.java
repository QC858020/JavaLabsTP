package fr.tse.info.tp7.exercice1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Exercice 1 : Synchronization d'un compteur
 * 
 * Dans cet exercice, le compteur (Class compteur) n'est pas synchronis�
 * correctement. 10 threads tentent de l'incr�menter 10 fois chacun, le r�sultat
 * attendu est donc 100.
 * 
 * Cependant si vous ex�cutez ce programme vous n'obtiendrez pas ce r�sultat.
 * Vous allez synchroniser ce programme de 3 mani�res diff�rentes
 * <ol>
 * <li>Utiliser le mot cl� synchronized pour r�soudre le probl�me</li>
 * <li>Dans la classe compteur, utiliser des locks pour synchroniser</li>
 * <li>Rechercher dans le package java.util.concurrent.atomic une classe
 * permettant de r�aliser la synchonisation</li>
 * </ol>
 * 
 * @author Julien Subercaze
 * 
 */
public class Exercice1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Utilisation d'un threadpool, regardez la doc pour en savoir plus.
		// Permet de g�rer l'ex�cution d'un ensemble de thread
		ExecutorService service = Executors.newFixedThreadPool(10);
		Compteur compteur = new Compteur();
		// Lance 10 threads qui modifient le m�me compteur
		for (int i = 0; i < 10; i++) {
			service.execute(new ThreadModifieur(compteur));
		}
		// Attendre la fin de l'ex�cution
		shutdownAndAwaitTermination(service);
		System.out.println("Valeur du compteur :" + compteur.getCompteur());
	}

	/**
	 * Bonne pratique : m�thode reprise de la Javadoc
	 * 
	 * @param pool
	 */
	static void shutdownAndAwaitTermination(ExecutorService pool) {
		pool.shutdown(); // Disable new tasks from being submitted
		try {
			// Wait a while for existing tasks to terminate
			if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
				pool.shutdownNow(); // Cancel currently executing tasks
				// Wait a while for tasks to respond to being cancelled
				if (!pool.awaitTermination(60, TimeUnit.SECONDS))
					System.err.println("Pool did not terminate");
			}
		} catch (InterruptedException ie) {
			// (Re-)Cancel if current thread also interrupted
			pool.shutdownNow();
			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}
	}

}
