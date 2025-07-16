package src;

public class Client {
    private int id;
    private String nom;
    private String entreprise;

    public Client(int id, String nom, String entreprise) {
        this.id = id;
        this.nom = nom;
        this.entreprise = entreprise;
    }

    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getEntreprise() { return entreprise; }

    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setEntreprise(String entreprise) { this.entreprise = entreprise; }

    @Override
    public String toString() {
        return nom + (entreprise != null && !entreprise.isEmpty() ? " (" + entreprise + ")" : "");
    }
} 