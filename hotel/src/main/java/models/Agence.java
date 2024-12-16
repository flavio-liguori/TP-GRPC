package models;

import java.util.ArrayList;

public class Agence {
    private int id;
    private String libelle;
    private String login;
    private String password;
    private ArrayList<Client> clients;
    public Agence(int id, String libelle, String login, String password) {
        this.id = id;
        this.libelle = libelle;
        this.clients = new ArrayList<>();
        this.login = login;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getLibelle() {
        return libelle;
    }
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
    public ArrayList<Client> getClients() {
        return clients;
    }
    public void setClients(ArrayList<Client> clients) {
        this.clients = clients;
    }
    public void addClient(Client client) {
        this.clients.add(client);
    }
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

}
