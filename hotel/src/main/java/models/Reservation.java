package models;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Reservation {
    private int id;
    private Chambre chambre;
    private Client client;
    private LocalDate dateDebut;
    private LocalDate dateFin;

    public Reservation(int id, Chambre chambre, Client client, String dateDebut, String dateFin) {
        this.id = id;
        this.chambre = chambre;
        this.client = client;
        this.dateDebut = LocalDate.parse(dateDebut);
        this.dateFin = LocalDate.parse(dateFin);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Chambre getChambre() {
        return chambre;
    }

    public void setChambre(Chambre chambre) {
        this.chambre = chambre;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }


    public String getDateDebut() {
        return dateDebut.toString();
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = LocalDate.parse(dateDebut);
    }

    public String getDateFin() {
        return dateFin.toString();
    }

    public void setDateFin(String dateFin) {
        this.dateFin = LocalDate.parse(dateFin);
    }

    public Long getPrice(){
        return this.chambre.getPrice()* ChronoUnit.DAYS.between(this.dateDebut, this.dateFin);
    }
}
