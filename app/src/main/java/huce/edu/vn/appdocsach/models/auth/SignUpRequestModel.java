package huce.edu.vn.appdocsach.models.auth;

import java.io.File;

public class SignUpRequestModel {
    private String username;

    private String fullName;

    private String email;

    private String password;
    private File avatarFile;

    public SignUpRequestModel(String username, String fullName, String email, String password) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.avatarFile = avatarFile;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public File getAvatarFile(){
        return avatarFile;
    }
    public void setAvatarFile(File avatarFile){
        this.avatarFile = avatarFile;
    }
}
