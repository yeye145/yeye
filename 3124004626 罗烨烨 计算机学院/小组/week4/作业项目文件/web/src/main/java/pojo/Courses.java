package pojo;

public class Courses {

    private Integer key;
    private String courseName;
    private String information;
    private Integer score;
    private Integer ifCanChoose;
    private Integer numberCanChoose;
    private Integer numberChoose;


    public Courses() {
    }

    public Courses(String courseName, String information, Integer score, Integer key, Integer ifCanChoose, Integer numberCanChoose, Integer numberChoose) {
        this.courseName = courseName;
        this.information = information;
        this.score = score;
        this.key = key;
        this.ifCanChoose = ifCanChoose;
        this.numberCanChoose = numberCanChoose;
        this.numberChoose = numberChoose;
    }

    /**
     * 获取
     *
     * @return courseName
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * 设置
     *
     * @param courseName
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    /**
     * 获取
     *
     * @return information
     */
    public String getInformation() {
        return information;
    }

    /**
     * 设置
     *
     * @param information
     */
    public void setInformation(String information) {
        this.information = information;
    }

    /**
     * 获取
     *
     * @return score
     */
    public Integer getScore() {
        return score;
    }

    /**
     * 设置
     *
     * @param score
     */
    public void setScore(Integer score) {
        this.score = score;
    }

    /**
     * 获取
     *
     * @return key
     */
    public Integer getKey() {
        return key;
    }

    /**
     * 设置
     *
     * @param key
     */
    public void setKey(Integer key) {
        this.key = key;
    }

    /**
     * 获取
     *
     * @return ifCanChoose
     */
    public Integer getIfCanChoose() {
        return ifCanChoose;
    }

    /**
     * 设置
     *
     * @param ifCanChoose
     */
    public void setIfCanChoose(Integer ifCanChoose) {
        this.ifCanChoose = ifCanChoose;
    }

    /**
     * 获取
     *
     * @return numberCanChoose
     */
    public Integer getNumberCanChoose() {
        return numberCanChoose;
    }

    /**
     * 设置
     *
     * @param numberCanChoose
     */
    public void setNumberCanChoose(Integer numberCanChoose) {
        this.numberCanChoose = numberCanChoose;
    }

    /**
     * 获取
     *
     * @return numberChoose
     */
    public Integer getNumberChoose() {
        return numberChoose;
    }

    /**
     * 设置
     *
     * @param numberChoose
     */
    public void setNumberChoose(Integer numberChoose) {
        this.numberChoose = numberChoose;
    }

    public String toString() {
        return "Courses{courseName = " + courseName + ", information = " + information + ", score = " + score + ", key = " + key + ", ifCanChoose = " + ifCanChoose + ", numberCanChoose = " + numberCanChoose + ", numberChoose = " + numberChoose + "}";
    }
}
