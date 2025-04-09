package pojo;

public class StudentCourses {
    private String courseName;
    private String information;
    private Integer score;
    private Integer key;


    public StudentCourses() {
    }

    public StudentCourses(String courseName, String information, Integer score, Integer key) {
        this.courseName = courseName;
        this.information = information;
        this.score = score;
        this.key = key;
    }

    /**
     * 获取
     * @return courseName
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * 设置
     * @param courseName
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    /**
     * 获取
     * @return information
     */
    public String getInformation() {
        return information;
    }

    /**
     * 设置
     * @param information
     */
    public void setInformation(String information) {
        this.information = information;
    }

    /**
     * 获取
     * @return score
     */
    public Integer getScore() {
        return score;
    }

    /**
     * 设置
     * @param score
     */
    public void setScore(Integer score) {
        this.score = score;
    }

    /**
     * 获取
     * @return key
     */
    public Integer getKey() {
        return key;
    }

    /**
     * 设置
     * @param key
     */
    public void setKey(Integer key) {
        this.key = key;
    }

    public String toString() {
        return "StudentCourses{courseName = " + courseName + ", information = " + information + ", score = " + score + ", key = " + key + "}";
    }
}
