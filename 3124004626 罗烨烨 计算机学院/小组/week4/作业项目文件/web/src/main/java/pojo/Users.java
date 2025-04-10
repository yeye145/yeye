package pojo;

public class Users {

    private int id;
    private String phoneNumber;
    private String password;
    private Integer isAdmin;
    private String email;


    public Users() {
    }

    public Users(int id, String phoneNumber, String password, Integer isAdmin, String email) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.isAdmin = isAdmin;
        this.email = email;
    }

    /**
     * 获取
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * 设置
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * 获取
     * @return phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * 设置
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * 获取
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取
     * @return isAdmin
     */
    public Integer getIsAdmin() {
        return isAdmin;
    }

    /**
     * 设置
     * @param isAdmin
     */
    public void setIsAdmin(Integer isAdmin) {
        this.isAdmin = isAdmin;
    }

    /**
     * 获取
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    public String toString() {
        return "Users{id = " + id + ", phoneNumber = " + phoneNumber + ", password = " + password + ", isAdmin = " + isAdmin + ", email = " + email + "}";
    }
}
