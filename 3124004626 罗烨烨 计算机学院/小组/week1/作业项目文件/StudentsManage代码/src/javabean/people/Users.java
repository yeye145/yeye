package javabean.people;

public class Users {

    private int id;
    private String name;
    private String password;
    private Integer isAdmin;


    public Users() {
    }

    public Users(int id, String name, String password, Integer isAdmin) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.isAdmin = isAdmin;
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
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 设置
     * @param name
     */
    public void setName(String name) {
        this.name = name;
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

    public String toString() {
        return "Users{id = " + id + ", name = " + name + ", password = " + password + ", isAdmin = " + isAdmin + "}";
    }
}
