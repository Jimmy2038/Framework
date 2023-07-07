package etu2038.framework;

public class UploadFile {
    String path;
    String nom;
    byte[] bytes;
    
    public void setPath(String path) {
        this.path = path;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
    public String getPath() {
        return path;
    }
    public String getNom() {
        return nom;
    }
    public byte[] getBytes() {
        return bytes;
    }
}
