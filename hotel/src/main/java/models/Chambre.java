package models;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Chambre {

    private int id ;
    private int capacite;
    private int price;
    private String description;
    private byte[] image;

    public Chambre(int id, int capacite, int price, String description, String imagePath) throws IOException {
        this.id = id;
        this.capacite = capacite;
        this.price = price;
        this.description = description;
        if (imagePath != null && !imagePath.isEmpty()) {
            Path path = Paths.get(imagePath);
            if (Files.exists(path) && Files.isRegularFile(path)) {
                this.image = Files.readAllBytes(path);
            } else {
                System.out.println("Chemin d'image invalide : " + imagePath);
                this.image = null; // Aucune image chargée
            }
        } else {
            this.image = null; // Aucune image chargée
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public int getPrice() {
        return price;
    }


    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return image;
    }
}
