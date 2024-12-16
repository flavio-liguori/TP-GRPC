package models;

import java.io.IOException;
import java.time.LocalDate;

public class Offre {
    private int id;
    private int idHotel;
    Chambre chambre;
    LocalDate dateDispoDebut;
    LocalDate dateDispoFin;
    long prix;
    private byte[] image;
    private String image64;
    private String Agence;

    // Encodage de l'image
    public Offre(int idHotel, int id, Chambre chambre, String dateDispoDebut, String dateDispoFin, long prix) throws IOException {
        this.idHotel = idHotel;
        this.id = id;
        this.chambre = chambre;
        this.dateDispoDebut = LocalDate.parse(dateDispoDebut);
        this.dateDispoFin = LocalDate.parse(dateDispoFin);
        this.prix = prix;
        this.image = chambre.getImage();
    }
    public int getIdHotel(){
        return idHotel;
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

    public String getDateDispoDebut() {
        return dateDispoDebut.toString();
    }

    public void setDateDispoDebut(String dateDispoDebut) {
        this.dateDispoDebut = LocalDate.parse(dateDispoDebut);
    }

    public String getDateDispoFin() {
        return dateDispoFin.toString();
    }

    public void setDateDispoFin(String dateDispoFin) {
        this.dateDispoFin = LocalDate.parse(dateDispoFin);
    }
    public long getPrix() {
        return prix;
    }
    public byte[] getImage() {
        return this.image;
    }
    public void setImage(String image) {
        this.image64 = image;
    }
    public String getImage64() {
        return image64;
    }
    public void setAgence(String Agence) {
        this.Agence = Agence;
    }
}
