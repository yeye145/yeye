package regist;

import myHandWriteTool.MyUpdate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class InformationInput {

    Scanner sc = new Scanner(System.in);

    //编写sql语句
    String sql = "INSERT INTO `student`.`students` " +
            "(`students_phoneNumber`," +
            " `students_classHadSelected`, `students_classNumber`," +
            " `students_idNumber`, `students_gender`, `students_birthday`, " +
            "`students_name`) " +
            "VALUES (?, '还没有选课哦', '0', ?, ?, ?, ?);";


    /*
     *
     *
     * 学生表信息注册
     *
     *
     * */

    public InformationInput(Connection connection, String isAdmin) throws Exception {

        //如果是管理员注册

        if (isAdmin.equals("2")) {

            adminInput(connection);

            //返回
            return;

        }


        //获取pstmt对象
        PreparedStatement pstmt = connection.prepareStatement(sql);


        while(true){
            //录入姓名
            System.out.printf("===== 学生信息完善 =====\n" + "请输入姓名：\n> ");

            String name = sc.next();

            if(name.length()>=30){
                System.out.println("你的名字太长了！");
                continue;
            }

            pstmt.setString(5, sc.next());
            break;

        }

        //录入手机号
        phoneInput(pstmt);

        //录入身份证号
        inputIdNumber(pstmt);


    }

    /*
     *
     *
     * 录入手机号码
     *
     *
     * */

    private void phoneInput(PreparedStatement pstmt) throws SQLException {
        while (true) {

            //从键盘获取手机号
            System.out.printf("输入手机号:\n>");
            String phoneNumber = sc.next();

            //长度为11位
            //第一位必须是1
            //只能数字
            //构建正确规格手机号码的正则表达式
            String regexphoneNumber = "1[3-9][0-9]{9}";
            if (phoneNumber.matches(regexphoneNumber)) {
                //如果满足，则录入并退出循环
                pstmt.setString(1, phoneNumber);
                break;
            } else {
                System.out.println("\n您输入的手机号码不符合要求，请重新输入");
            }
        }
    }

    /*
     *
     *
     * 录入管理员信息
     *
     *
     * */

    private static void adminInput(Connection connection) throws Exception {

        String sqlAdmin = "INSERT INTO `student`.`students` " +
                "(`students_phoneNumber`," +
                " `students_classHadSelected`, `students_classNumber`," +
                " `students_idNumber`, `students_gender`, `students_birthday`, " +
                "`students_name`) " +
                "VALUES ('-1', '管理员', '-1', '-1', '-1', '-1', '-1');";

        MyUpdate.update(sqlAdmin, connection);

    }

    /*
     *
     *
     *
     * 录入身份证号，
     * 同时完善性别和出生日期
     * 最后上传到数据库
     *
     *
     * */

    private void inputIdNumber(PreparedStatement pstmt) throws SQLException {
        while (true) {

            //从键盘获取身份证号
            System.out.printf("输入身份证号:\n>");
            String idNumber = sc.next();

            /*长度18位
              不能以0开头
              前17位为数字
              最后一位可以是数字或者X或者
              123456  1234 12 12  1234*/

            String regexIdNumber = "[1-9]\\d{5}20[0-1]\\d(0[1-9]|1[0-2])(0[1-9]|[12]\\d|30|31)[0-9]{3}[\\dXx]";

            if (idNumber.matches(regexIdNumber)) {

                pstmt.setString(2, idNumber);

                //获取月份和日期
                int year = Integer.parseInt(idNumber.substring(6, 10));
                int moon = Integer.parseInt(idNumber.substring(10, 12));
                int day = Integer.parseInt(idNumber.substring(12, 14));

                pstmt.setString(4, year + "-" + moon + "-" + day);

                if (Integer.parseInt(idNumber.substring(16, 17)) % 2 == 0)
                    pstmt.setString(3, "女");
                if (Integer.parseInt(idNumber.substring(16, 17)) % 2 == 1)
                    pstmt.setString(3, "男");

                break;

            } else {
                System.out.println("您的身份证号不符合要求");
            }

        }

        //更新学生表信息
        pstmt.executeUpdate();

        //释放
        pstmt.close();

    }


}
