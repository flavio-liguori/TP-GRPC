package models;

// Classe représentant un couple identifiant/password
public class IdentifiantPassword {
    private String identifiant;
    private String password;

    public IdentifiantPassword(String identifiant, String password) {
        this.identifiant = identifiant;
        this.password = password;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public String getPass() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


