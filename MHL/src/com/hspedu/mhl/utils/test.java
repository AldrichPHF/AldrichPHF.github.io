package com.hspedu.mhl.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLOutput;

/**
 * @author 庞化峰
 * @version 1.0
 * 测试类
 */
public class test {
    public static void main(String[] args) throws SQLException {
        //测试Utility工具类
        System.out.println("请输入一个整数：");
        int i = Utility.readInt();
        System.out.println("i = "+ i);

        //测试JDBCUtilsByDruid
        Connection connection = JDBCUtilsByDruid.getConnection();
        System.out.println(connection);
    }

}
