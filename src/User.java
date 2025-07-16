package src;

public class User {
    private int id;
    private String login;
    private String motDePasse;

    public User(int id, String login, String motDePasse) {
        this.id = id;
        this.login = login;
        this.motDePasse = motDePasse;
    }

    public int getId() { return id; }
    public String getLogin() { return login; }
    public String getMotDePasse() { return motDePasse; }

    public void setId(int id) { this.id = id; }
    public void setLogin(String login) { this.login = login; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
} 