package com.zhu.rimxia.biz.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandHelper {
    public static boolean execCommand(String cmd,File dir) {
        Process process = null;
        boolean re = true;
        try {
            process = Runtime.getRuntime().exec(cmd,null,dir);
            if(process.waitFor()==-1){
                re = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            re = false;
        } finally {
            try {
                process.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return re;
    }

    public static String execCommandRe(String cmd,File dir) {
        Process process = null;
        BufferedReader br = null;
        StringBuilder result = new StringBuilder();
        try {
            process = Runtime.getRuntime().exec(cmd,null,dir);
            process.waitFor();

// 获取命令执行结果, 有两个结果: 正常的输出 和 错误的输出（PS: 子进程的输出就是主进程的输入）
            BufferedReader bufrIn = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
            BufferedReader bufrError = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));

// 读取输出
            String line = null;
            while ((line = bufrIn.readLine()) != null) {
                System.out.println(line);
                result.append(line).append('\n');
            }
            while ((line = bufrError.readLine()) != null) {
                System.out.println(line);
                result.append(line).append('\n');
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "-1";
        } finally {
            try {
                process.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result.toString();
    }

    public static void main(String[] args) {

        boolean re =  CommandHelper.execCommand("ffmpeg -i BladeRunner2049.flv -c:a aac -vcodec libx264 out2.mp4",
                new File("C:\\Users\\bigSheller\\Desktop\\" +
        "red5-server-1.0.10\\red5-server\\webapps\\oflaDemo\\streams"));
       // System.out.println(re);
//        Pattern pattern = Pattern.compile(" Video: ([a-z0-9]+) ");
////        Matcher matcher = pattern.matcher(re);
////        while(matcher.find()){
////            System.out.println("Found value: " + matcher.group(1) );
////        }
////        System.out.println(re.contains("h264"));;

    }
}
