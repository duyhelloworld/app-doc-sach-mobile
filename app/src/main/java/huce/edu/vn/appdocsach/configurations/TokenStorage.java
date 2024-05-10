package huce.edu.vn.appdocsach.configurations;

public interface TokenStorage {

    String getAccessToken();

    boolean save(String token);

    boolean clearAllTokens();
}
