package models;

import java.util.ArrayList;

public class Hotel {
    private int id;
    private String name;
    private String address;
    private String city;
    private String country;
    private int stars;
    private ArrayList<Chambre> chambres;
    private ArrayList<Reservation> reservations;
    private ArrayList<Offre> offres;
    private ArrayList<IdentifiantPassword> identifiantPasswords;



    public Hotel(int id,String name ,String address , String city, String country,  int stars) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.country = country;
        this.stars = stars;
        this.chambres = new ArrayList<>();
        this.reservations = new ArrayList<>();
        this.offres = new ArrayList<>();
        this.identifiantPasswords = new ArrayList<>();

    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }


    public ArrayList<Chambre> getChambres() {
        return chambres;
    }

    public void setChambres(ArrayList<Chambre> chambres) {
        this.chambres = chambres;
    }

    public ArrayList<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(ArrayList<Reservation> reservations) {
        this.reservations = reservations;
    }

    public ArrayList<Offre> getOffres() {
        return offres;
    }
    public void setOffres(ArrayList<Offre> offres) {
        this.offres = offres;
    }
    public void addOffre(Offre offre) {
        this.offres.add(offre);
    }

    public ArrayList<IdentifiantPassword> getIdentifiantPasswords() {
        return identifiantPasswords;
    }

    public void setIdentifiantPasswords(ArrayList<IdentifiantPassword> identifiantPasswords) {
        this.identifiantPasswords = identifiantPasswords;
    }
    public void addReservation(Reservation reserv){
        this.reservations.add(reserv);
    }
}
