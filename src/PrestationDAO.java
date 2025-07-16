package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.math.BigDecimal;
import java.sql.ResultSet;

public class PrestationDAO {
    private Connection connect() throws Exception {
        Properties props = new Properties();
        props.load(new FileInputStream("db.properties"));
        String url = props.getProperty("url");
        String user = props.getProperty("user");
        String password = props.getProperty("password");
        return DriverManager.getConnection(url, user, password);
    }

    public void ajouterPrestation(String type, LocalDate date, LocalTime heureDebut, LocalTime heureFin, String classe, String module, String description, BigDecimal tjm, String entreprise, String client, BigDecimal tarifHoraire, Integer nbJours) throws Exception {
        String sql = "INSERT INTO prestation (type, date_prestation, heure_debut, heure_fin, classe, module, description, tjm, entreprise, client_id, tarif_horaire, nb_jours) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, (SELECT id FROM client WHERE nom = ? LIMIT 1), ?, ?)";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, type);
            stmt.setDate(2, java.sql.Date.valueOf(date));
            stmt.setObject(3, heureDebut != null ? java.sql.Time.valueOf(heureDebut) : null);
            stmt.setObject(4, heureFin != null ? java.sql.Time.valueOf(heureFin) : null);
            stmt.setString(5, classe);
            stmt.setString(6, module);
            stmt.setString(7, description);
            stmt.setBigDecimal(8, tjm);
            stmt.setString(9, entreprise);
            stmt.setString(10, client);
            stmt.setBigDecimal(11, tarifHoraire);
            if (nbJours != null) stmt.setInt(12, nbJours); else stmt.setNull(12, java.sql.Types.INTEGER);
            stmt.executeUpdate();
        }
    }

    public java.util.List<Prestation> getAllPrestations() throws Exception {
        java.util.List<Prestation> list = new java.util.ArrayList<>();
        String sql = "SELECT p.*, c.id as client_id, c.nom as client_nom, c.entreprise as client_entreprise FROM prestation p LEFT JOIN client c ON p.client_id = c.id ORDER BY p.date_prestation DESC";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Client client = null;
                if (rs.getInt("client_id") != 0) {
                    client = new Client(rs.getInt("client_id"), rs.getString("client_nom"), rs.getString("client_entreprise"));
                }
                Prestation p = new Prestation(
                    rs.getInt("id"),
                    rs.getString("type"),
                    rs.getDate("date_prestation").toLocalDate(),
                    rs.getTime("heure_debut") != null ? rs.getTime("heure_debut").toLocalTime() : null,
                    rs.getTime("heure_fin") != null ? rs.getTime("heure_fin").toLocalTime() : null,
                    rs.getString("classe"),
                    rs.getString("module"),
                    rs.getString("description"),
                    rs.getBigDecimal("tjm"),
                    client,
                    rs.getString("entreprise"),
                    rs.getBigDecimal("tarif_horaire"),
                    rs.getObject("nb_jours") != null ? rs.getInt("nb_jours") : null
                );
                list.add(p);
            }
        }
        return list;
    }
} 