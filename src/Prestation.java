package src;

import java.time.LocalDate;
import java.time.LocalTime;
import java.math.BigDecimal;

public class Prestation {
    private int id;
    private String type; // "formation" ou "consultation"
    private LocalDate datePrestation;
    private LocalTime heureDebut; // formation
    private LocalTime heureFin;   // formation
    private String classe;        // formation
    private String module;        // formation
    private String description;   // consultation
    private BigDecimal tjm;       // consultation
    private BigDecimal tarifHoraire; // formation
    private Integer nbJours;         // consultation
    private Client client;
    private String entreprise;

    public Prestation(int id, String type, LocalDate datePrestation, LocalTime heureDebut, LocalTime heureFin, String classe, String module, String description, BigDecimal tjm, Client client, String entreprise, BigDecimal tarifHoraire, Integer nbJours) {
        this.id = id;
        this.type = type;
        this.datePrestation = datePrestation;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.classe = classe;
        this.module = module;
        this.description = description;
        this.tjm = tjm;
        this.client = client;
        this.entreprise = entreprise;
        this.tarifHoraire = tarifHoraire;
        this.nbJours = nbJours;
    }

    // Getters et setters pour chaque champ
    public String getType() { return type; }
    public LocalDate getDatePrestation() { return datePrestation; }
    public Client getClient() { return client; }
    public String getEntreprise() { return entreprise; }
    public String getModule() { return module; }
    public String getDescription() { return description; }
    public BigDecimal getTjm() { return tjm; }
    public BigDecimal getTarifHoraire() { return tarifHoraire; }
    public void setTarifHoraire(BigDecimal tarifHoraire) { this.tarifHoraire = tarifHoraire; }
    public Integer getNbJours() { return nbJours; }
    public void setNbJours(Integer nbJours) { this.nbJours = nbJours; }
    public LocalTime getHeureDebut() { return heureDebut; }
    public LocalTime getHeureFin() { return heureFin; }
} 