package com.phfjava.utils;



/**
 * @author 庞化峰
 * @version 1.0
 */
public class WebUtils {
    /**
     * 将一个字符串类型的数字，转成int，如果转换失败，就返回传入的defaultVal
     *
     * @param strNum
     * @param defaultVal
     * @return
     */
    public static int parseInt(String strNum, int defaultVal) {
        try {
            return Integer.parseInt(strNum);
        } catch (NumberFormatException e) {
            System.out.println(strNum + "格式不正确，转换失败");
        }
        return defaultVal;
    }



}
