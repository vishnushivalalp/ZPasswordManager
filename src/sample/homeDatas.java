package sample;

public class homeDatas {
    private int id;
    private String username, email, password, url, remark;

    Encrypt_Decrypt ed = new Encrypt_Decrypt();

    public homeDatas(int id, String username, String email, String password, String url, String remark) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.url = url;
        this.remark = remark;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        String decrypt_pass = null;
        try {
            decrypt_pass = ed.decrypt(password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decrypt_pass;
    }

    public String getUrl() {
        return url;
    }

    public String getRemark() {
        return remark;
    }
}
