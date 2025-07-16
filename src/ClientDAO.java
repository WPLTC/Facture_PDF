package src;

import java.sql.*;
import java.util.*;
import java.io.FileInputStream;
import java.util.Properties;

public class ClientDAO {
    private Connection connect() throws Exception {
        Properties props = new Properties();
        props.load(new FileInputStream("db.properties"));
        String url = props.getProperty("url");
        String user = props.getProperty("user");
        String password = props.getProperty("password");
        return DriverManager.getConnection(url, user, password);
    }

    public List<Client> getAllClients() throws Exception {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM client ORDER BY nom";
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                clients.add(new Client(rs.getInt("id"), rs.getString("nom"), rs.getString("entreprise")));
            }
        }
        return clients;
    }

    public void ajouterClient(String nom, String entreprise) throws Exception {
        String sql = "INSERT INTO client (nom, entreprise) VALUES (?, ?)";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nom);
            stmt.setString(2, entreprise);
            stmt.executeUpdate();
        }
    }

    public void modifierClient(int id, String nom, String entreprise) throws Exception {
        String sql = "UPDATE client SET nom = ?, entreprise = ? WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nom);
            stmt.setString(2, entreprise);
            stmt.setInt(3, id);
            stmt.executeUpdate();
        }
    }

    public void supprimerClient(int id) throws Exception {
        String sql = "DELETE FROM client WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
} 