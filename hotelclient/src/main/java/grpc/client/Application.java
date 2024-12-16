package grpc.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import grpc.hotel.HotelServiceGrpc;
import grpc.hotel.GetOffresRequest;
import grpc.hotel.GetOffresResponse;
import grpc.hotel.GetConfirmationRequest;
import grpc.hotel.GetConfirmationResponse;
import grpc.hotel.Reservation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class Application {

	private ManagedChannel channel;
	private HotelServiceGrpc.HotelServiceBlockingStub stub;
	private String clientName;
	private String clientEmail;
	private String clientPassword;

	public static void main(String[] args) {
		new Application().start();
	}

	public void start() {
		// Configuration du canal vers le serveur gRPC
		channel = ManagedChannelBuilder.forAddress("localhost", 9091)
				.usePlaintext() // Connexion sans TLS
				.build();

		// Création d'un stub pour appeler le service
		stub = HotelServiceGrpc.newBlockingStub(channel);

		// Demander à l'utilisateur de se connecter
		login();

		// Démarrage du menu CLI
		runCLI();

		// Fermeture du canal
		channel.shutdown();
	}

	private void login() {
		// Demander à l'utilisateur de se connecter (nom, email et mot de passe)
		Scanner scanner = new Scanner(System.in);

		System.out.print("Entrez votre nom : ");
		clientName = scanner.nextLine();

		System.out.print("Entrez votre id : ");
		clientEmail = scanner.nextLine();

		System.out.print("Entrez votre mot de passe : ");
		clientPassword = scanner.nextLine();

		System.out.println("Connexion réussie ! Bienvenue, " + clientName + " !");
	}

	private void runCLI() {
		Scanner scanner = new Scanner(System.in);
		boolean running = true;

		while (running) {
			// Affichage du menu
			System.out.println("\n--- Menu Principal ---");
			System.out.println("1. Afficher les offres");
			System.out.println("2. Faire une réservation");
			System.out.println("3. Quitter");

			System.out.print("Choisissez une option : ");
			int choice = scanner.nextInt();

			switch (choice) {
				case 1:
					showOffres();
					break;
				case 2:
					makeReservation(scanner);
					break;
				case 3:
					running = false;
					System.out.println("Merci d'avoir utilisé l'application !");
					break;
				default:
					System.out.println("Option invalide. Essayez à nouveau.");
			}
		}

		scanner.close();
	}

	private void showOffres() {
		Scanner scanner = new Scanner(System.in);

		try {
			// Demander les dates à l'utilisateur
			System.out.print("Entrez la date de début (format AAAA-MM-JJ) : ");
			String dateDebut = scanner.nextLine();

			System.out.print("Entrez la date de fin (format AAAA-MM-JJ) : ");
			String dateFin = scanner.nextLine();

			System.out.print("Entrez le nombre de personnes : ");
			int nbPersonnes = scanner.nextInt();

			// Appel à la méthode GetOffres
			System.out.println("\n--- Appel de GetOffres ---");
			GetOffresRequest offresRequest = GetOffresRequest.newBuilder()
					.setValid(true)
					.setIdentifiant(clientEmail) // Envoi de l'email comme identifiant
					.setPassword(clientPassword) // Envoi du mot de passe
					.setDateDebut(dateDebut)
					.setDateFin(dateFin)
					.setNbPersonnes(nbPersonnes)
					.build();

			GetOffresResponse offresResponse = stub.getOffres(offresRequest);

			// Créer un dossier pour enregistrer les images (si ce n'est pas déjà fait)
			File imagesDirectory = new File("images_chambres");
			if (!imagesDirectory.exists()) {
				imagesDirectory.mkdir();
			}

			System.out.println("Offres reçues :");
			offresResponse.getOffresList().forEach(offre -> {
				try {
					System.out.println("  - Offre ID: " + offre.getId());
					System.out.println("    Prix: " + offre.getPrice());
					System.out.println("    Chambre ID: " + offre.getChambre().getId());

					// Sauvegarder l'image de la chambre
					byte[] imageBytes = offre.getChambre().getImage().toByteArray();
					String imagePath = "images_chambres/chambre_" + offre.getChambre().getId() + ".jpg";
					saveImage(imageBytes, imagePath);
					System.out.println("    Image sauvegardée dans : " + imagePath);
				} catch (IOException e) {
					System.err.println("Erreur lors de la sauvegarde de l'image : " + e.getMessage());
				}
			});
		} catch (StatusRuntimeException e) {
			System.err.println("Erreur gRPC : " + e.getStatus() + ", message : " + e.getMessage());
		}
	}

	private void makeReservation(Scanner scanner) {
		try {
			// Demander à l'utilisateur de saisir l'ID de l'offre
			System.out.print("Entrez l'ID de l'offre à réserver : ");
			int idOffre = scanner.nextInt();
			scanner.nextLine(); // Consommer la nouvelle ligne après nextInt()

			// Utiliser les informations de l'utilisateur (nom, email et mot de passe) pour la réservation
			GetConfirmationRequest confirmationRequest = GetConfirmationRequest.newBuilder()
					.setIdOffre(idOffre)
					.setEmail(clientEmail)
					.setPassword(clientPassword)
					.setName(clientName)
					.build();

			GetConfirmationResponse confirmationResponse = stub.getConfirmation(confirmationRequest);
			Reservation reservation = confirmationResponse.getReservation();
			System.out.println("Réservation confirmée :");
			System.out.println("  - ID Réservation: " + reservation.getId());
			System.out.println("  - Nom du client: " + reservation.getClient().getName());
			System.out.println("  - Email du client: " + reservation.getClient().getEmail());
			System.out.println("  - Chambre ID: " + reservation.getChambre().getId());
		} catch (StatusRuntimeException e) {
			System.err.println("Erreur gRPC : " + e.getStatus() + ", message : " + e.getMessage());
		}
	}

	private void saveImage(byte[] imageBytes, String imagePath) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(imagePath)) {
			fos.write(imageBytes);
			fos.flush();
		}
	}
}
