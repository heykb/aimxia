package com.zhu.rimxia.biz.common;

import java.util.ArrayList;
import java.util.List;

public class CommandUtil {

    public static  class PrintStream extends Thread
    {
        java.io.InputStream __is = null;
        public PrintStream(java.io.InputStream is)
        {
            __is = is;
        }

        public void run()
        {
            try
            {
                while(this != null)
                {
                    int _ch = __is.read();
                    if(_ch != -1)
                        System.out.print((char)_ch);
                    else break;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    /**
     * 视频转码 (PC端MP4)
     * @param ffmpegPath    转码工具的存放路径
     * @param upFilePath    用于指定要转换格式的文件,要截图的视频源文件
     * @param codcFilePath    格式转换后的的文件保存路径
     * @return
     * @throws Exception
     */
    public static  boolean exchangeToMp4(String upFilePath, String codcFilePath) throws Exception {
        // 创建List集合来保存转换视频文件为flv格式的命令
        List<String> convert = new ArrayList<String>();
        convert.add("D:\\Program Files (x86)\\ffmpeg\\bin\\ffmpeg.exe"); // 添加转换工具路径
        convert.add("-y"); // 该参数指定将覆盖已存在的文件
        convert.add("-i");
        convert.add(upFilePath);
        convert.add("-c:v");
        convert.add("libx264");
        convert.add("-c:a");
        convert.add("aac");
        convert.add("-strict");
        convert.add("-2");
//        convert.add("-pix_fmt");
//        convert.add("yuv420p");
//        convert.add("-movflags");
//        convert.add("faststart");
        convert.add("-hls_list_size");
        convert.add("0");
        //convert.add("-vf");   // 添加水印
        //convert.add("movie=watermark.gif[wm];[in][wm]overlay=20:20[out]");
        convert.add(codcFilePath);

        boolean mark = true;

        try {
            Process videoProcess = new ProcessBuilder(convert).redirectErrorStream(true).start();
            new PrintStream(videoProcess.getInputStream()).start();
            //videoProcess.waitFor();  // 加上这句，系统会等待转换完成。不加，就会在服务器后台自行转换。

        } catch (Exception e) {
            mark = false;
            System.out.println(e);
            e.printStackTrace();
        }
        return mark;
    }

    public static void main(String[] args) {
        try {
            CommandUtil.exchangeToMp4(
                    "C:\\Users\\bigSheller\\Desktop\\red5-server-1.0.10\\red5-server\\webapps\\oflaDemo\\streams\\BladeRunner2049.flv",
                    "C:\\Users\\bigSheller\\Desktop\\red5-server-1.0.10\\red5-server\\webapps\\oflaDemo\\streams\\out3.m3u8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
