package services;

import grpc.hotel.GetConfirmationRequest;
import grpc.hotel.GetConfirmationResponse;
import grpc.hotel.GetOffresRequest;
import grpc.hotel.GetOffresResponse;
import io.grpc.stub.StreamObserver;
import models.*;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@GrpcService
public class HotelServiceImpl extends grpc.hotel.HotelServiceGrpc.HotelServiceImplBase {

    @Autowired
    private final Hotel hotel; // Référence à l'objet métier `Hotel`
    @Autowired
    private final ArrayList<IdentifiantPassword> ids;

    public HotelServiceImpl(Hotel hotel, ArrayList<IdentifiantPassword> ids) {
        this.hotel = hotel;
        this.ids = ids;
    }

    @Override
    public void getOffres(GetOffresRequest request, StreamObserver<GetOffresResponse> responseObserver) {
        try {
            // Validation des identifiants
            boolean IsEligible = false;
            for(IdentifiantPassword id: ids) {
                if(id.getIdentifiant().equals(request.getIdentifiant())&&id.getPass().equals(request.getPassword())){
                    IsEligible = true;
                }
            }

            // Validation des dates
            LocalDate dateDebut = LocalDate.parse(request.getDateDebut());
            LocalDate dateFin = LocalDate.parse(request.getDateFin());
            if (dateDebut.isAfter(dateFin)) {
                responseObserver.onError(new IllegalArgumentException("La date de début ne peut pas être après la date de fin."));
                return;
            }

            // Recherche des chambres disponibles
            List<Offre> offres = new ArrayList<>();
            List<Chambre> chambres = getChambresByNbPersAndDate(
                    request.getNbPersonnes(),
                    request.getDateDebut(),
                    request.getDateFin()
            );

            int compteur = hotel.getOffres().size();
            for (Chambre chambre : chambres) {
                // Calcul du prix avec ou sans réduction
                long prixBase = (long) calculatePrice(chambre.getPrice(), request.getDateDebut(), request.getDateFin(), request.getValid());
                long prixFinal = IsEligible ? prixBase - 10 : prixBase;

                // Création de l'offre
                Offre offre = new Offre(
                        hotel.getId(),
                        compteur,
                        chambre,
                        request.getDateDebut(),
                        request.getDateFin(),
                        prixFinal
                );
                offres.add(offre);
                hotel.addOffre(offre);
                compteur++;
            }

            // Construire la réponse gRPC
            GetOffresResponse.Builder responseBuilder = GetOffresResponse.newBuilder();
            for (Offre offre : offres) {
                responseBuilder.addOffres(mapOffreToProto(offre));
            }

            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }




    @Override
    public void getConfirmation(GetConfirmationRequest request, StreamObserver<GetConfirmationResponse> responseObserver) {
        try {
            // Rechercher l'offre concernée
            Offre offreConcerne = hotel.getOffres().stream()
                    .filter(o -> o.getId() == request.getIdOffre())
                    .findFirst()
                    .orElse(null);

            if (offreConcerne == null) {
                responseObserver.onError(new IllegalArgumentException("Offre non trouvée."));
                return;
            }

            // Vérifier si la chambre est déjà réservée
            for (Reservation reservation : hotel.getReservations()) {
                if (reservation.getChambre().equals(offreConcerne.getChambre()) &&
                        overlapDates(reservation.getDateDebut(), reservation.getDateFin(),
                                offreConcerne.getDateDispoDebut(), offreConcerne.getDateDispoFin())) {
                    responseObserver.onError(new IllegalArgumentException("Chambre déjà réservée pour cette période."));
                    return;
                }
            }

            // Créer une nouvelle réservation
            Random random = new Random();
            Client client = new Client(
                    10000000 + random.nextInt(90000000),
                    request.getName(),
                    request.getEmail()
            );
            Reservation reservation = new Reservation(
                    10000000 + random.nextInt(90000000),
                    offreConcerne.getChambre(),
                    client,
                    offreConcerne.getDateDispoDebut(),
                    offreConcerne.getDateDispoFin()
            );
            hotel.addReservation(reservation);

            // Construire la réponse gRPC
            GetConfirmationResponse response = GetConfirmationResponse.newBuilder()
                    .setReservation(mapReservationToProto(reservation))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    // Helpers

    private List<Chambre> getChambresByNbPersAndDate(int nbPers, String dateDebut, String dateFin) {
        List<Chambre> chambresDisponibles = new ArrayList<>();
        for (Chambre chambre : hotel.getChambres()) {
            if (chambre.getCapacite() == nbPers) {
                boolean isAvailable = hotel.getReservations().stream()
                        .noneMatch(reservation -> reservation.getChambre().equals(chambre) &&
                                overlapDates(reservation.getDateDebut(), reservation.getDateFin(), dateDebut, dateFin));
                if (isAvailable) {
                    chambresDisponibles.add(chambre);
                }
            }
        }
        return chambresDisponibles;
    }

    private boolean overlapDates(String start1, String end1, String start2, String end2) {
        LocalDate s1 = LocalDate.parse(start1);
        LocalDate e1 = LocalDate.parse(end1);
        LocalDate s2 = LocalDate.parse(start2);
        LocalDate e2 = LocalDate.parse(end2);
        return !e1.isBefore(s2) && !s1.isAfter(e2);
    }

    private double calculatePrice(double basePrice, String dateDebut, String dateFin, boolean isValid) {
        long days = ChronoUnit.DAYS.between(LocalDate.parse(dateDebut), LocalDate.parse(dateFin));
        double price = basePrice * days;
        return isValid ? price - 10 : price;
    }

    private grpc.hotel.Offre mapOffreToProto(Offre offre) {
        return grpc.hotel.Offre.newBuilder()
                .setHotelId(offre.getIdHotel())
                .setId(offre.getId())
                .setChambre(mapChambreToProto(offre.getChambre()))
                .setDateDebut(offre.getDateDispoDebut())
                .setDateFin(offre.getDateDispoFin())
                .setPrice(offre.getPrix())
                .build();
    }

    private grpc.hotel.Chambre mapChambreToProto(Chambre chambre) {
        grpc.hotel.Chambre.Builder chambreBuilder = grpc.hotel.Chambre.newBuilder()
                .setId(chambre.getId())
                .setCapacite(chambre.getCapacite())
                .setPrice(chambre.getPrice());

        // Ajouter l'image si elle est disponible
        if (chambre.getImage() != null) {
            chambreBuilder.setImage(com.google.protobuf.ByteString.copyFrom(chambre.getImage()));
        }

        return chambreBuilder.build();
    }


    private grpc.hotel.Reservation mapReservationToProto(Reservation reservation) {
        return grpc.hotel.Reservation.newBuilder()
                .setId(reservation.getId())
                .setChambre(mapChambreToProto(reservation.getChambre()))
                .setClient(mapClientToProto(reservation.getClient()))
                .setDateDebut(reservation.getDateDebut())
                .setDateFin(reservation.getDateFin())
                .build();
    }

    private grpc.hotel.Client mapClientToProto(Client client) {
        return grpc.hotel.Client.newBuilder()
                .setId(client.getId())
                .setName(client.getName())
                .setEmail(client.getEmail())
                .build();
    }
}
