package pojo;

public class Students {
    private Integer id;
    private String name;
    private String phoneNumber;
    //已选课程名称
    private String classHadSelected;
    //已选课程总数量
    private Integer classNumber;
    private String idNumber;
    private String birthday;
    private String gender;
    private String email;


    public Students() {
    }

    public Students(Integer id, String name, String phoneNumber, String classHadSelected, Integer classNumber, String idNumber, String birthday, String gender, String email) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.classHadSelected = classHadSelected;
        this.classNumber = classNumber;
        this.idNumber = idNumber;
        this.birthday = birthday;
        this.gender = gender;
        this.email = email;
    }

    /**
     * 获取
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置
     * @param id
     */
    public void setId(Integer id) {
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
     * @return classHadSelected
     */
    public String getClassHadSelected() {
        return classHadSelected;
    }

    /**
     * 设置
     * @param classHadSelected
     */
    public void setClassHadSelected(String classHadSelected) {
        this.classHadSelected = classHadSelected;
    }

    /**
     * 获取
     * @return classNumber
     */
    public Integer getClassNumber() {
        return classNumber;
    }

    /**
     * 设置
     * @param classNumber
     */
    public void setClassNumber(Integer classNumber) {
        this.classNumber = classNumber;
    }

    /**
     * 获取
     * @return idNumber
     */
    public String getIdNumber() {
        return idNumber;
    }

    /**
     * 设置
     * @param idNumber
     */
    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    /**
     * 获取
     * @return birthday
     */
    public String getBirthday() {
        return birthday;
    }

    /**
     * 设置
     * @param birthday
     */
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    /**
     * 获取
     * @return gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * 设置
     * @param gender
     */
    public void setGender(String gender) {
        this.gender = gender;
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
        return "Students{id = " + id + ", name = " + name + ", phoneNumber = " + phoneNumber + ", classHadSelected = " + classHadSelected + ", classNumber = " + classNumber + ", idNumber = " + idNumber + ", birthday = " + birthday + ", gender = " + gender + ", email = " + email + "}";
    }
}
