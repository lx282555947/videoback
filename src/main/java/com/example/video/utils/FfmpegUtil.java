package com.example.video.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FfmpegUtil {

        /**
         * 将视频分割为小段
         *
         * @param fileName    源文件名字(带路径)
         * @param outputPath  输出文件路径，会在该路径下根据系统时间创建目录，并在此目录下输出段视频
         * @param videoTime   总时间，单位 分钟
         * @param periodTime  小段视频时长 单位 分钟
         * @param merge       true合并，false单独分割 说明：是否将整个视频结尾部分不足一次分割时间的部分，合并到最后一次分割的视频中，即false会比true多生成一段视频
         *
         */
        public static void splitVideoFile(String fileName, String outputPath, float videoTime, int periodTime,  boolean merge) {
            final String TAG = "----------------------------";

            // 在outputPath路径下根据系统时间创建目录
            File file = createFileBySysTime(outputPath);
            if (file == null) {
                System.out.println("分割视频失败，创建目录失败");
                return;
            }
            outputPath = file.getPath() + File.separator; // 更新视频输出目录


            // 计算视频分割的个数
            int count;// 分割为几段
            float remain = 0; // 不足一次剪辑的剩余时间
            if (merge) {
                count = (int) (videoTime / periodTime);
                remain = videoTime % periodTime; // 不足一次剪辑的剩余时间
            } else {
                count = (int) (videoTime / periodTime) + 1;
            }
            System.out.println("将视频分割为" + count + "段，每段约" + periodTime + "分钟");

            String indexName; // 第 i 个视频，打印日志用
            final String FFMPEG = "ffmpeg";
            String startTime; // 每段视频的开始时间
            String periodVideoName; // 每段视频的名字，名字规则：视频i_时间段xx_yy
            float duration; // 每次分割的时长
            String command;// 执行的命令
            // 得到视频后缀 如.mp4
            String videoSuffix = fileName.substring(fileName.lastIndexOf("."));//得到点后面的后缀，包括点
            Runtime runtime = Runtime.getRuntime(); // 执行命令者

            // 将视频分割为count段
            for (int i = 0; i < count; i++) {
                indexName = "第" + i + "个视频";

                // 决定是否将整个视频结尾部分不足一次的时间，合并到最后一次分割的视频中
                if (merge) {
                    if (i == count - 1) {
                        duration = periodTime * 60 + remain * 60;// 将整个视频不足一次剪辑的时间，拼接在最后一次剪裁中
                        startTime = periodTime * i + ":00";
                        periodVideoName = "视频" + i + "_时间段" + periodTime * i + "_end" + videoSuffix;
                    } else {
                        duration = periodTime * 60;
                        startTime = periodTime * i + ":00";
                        periodVideoName = "视频" + i + "_时间段" + periodTime * i + "_" + periodTime * (i + 1) + videoSuffix;
                    }
                } else {
                    duration = periodTime * 60;
                    startTime = periodTime * i + ":00";
                    periodVideoName = "视频" + i + "_时间段" + periodTime * i + "_" + periodTime * (i + 1) + videoSuffix;
                }

                // 执行分割命令
                try {
                    // 创建命令
                    command = FFMPEG + " -ss " + startTime + " -i " + fileName + " -c copy -t " + duration + " " + outputPath + periodVideoName;

                    System.out.println(TAG);
                    System.out.println(indexName);
                    System.out.println("执行命令：" + command);
                    runtime.exec(command);
                    System.out.println(indexName + "分割成功");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(indexName + "分割失败!!!!!!");
                }
            }

        }

        /**
         * 在指定目录下根据系统时间创建文件夹
         * 文件名字eg：2019-07-02-23-56-31
         *
         * @param path 路径：eg： "/Users/amarao/业余/剪辑/output/";
         *             结果：创建成功/Users/amarao/业余/剪辑/output/2019-07-03-10-28-05
         *             <p>
         *             步骤：
         *             1. 读取系统时间
         *             2. 格式化系统时间
         *             3. 创建文件夹
         *             <p>
         *             参考：http://www.bubuko.com/infodetail-1685972.html
         */
        public static File createFileBySysTime(String path) {

            // 1. 读取系统时间
            Calendar calendar = Calendar.getInstance();
            Date time = calendar.getTime();

            // 2. 格式化系统时间
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            String fileName = format.format(time); //获取系统当前时间并将其转换为string类型,fileName即文件名

            // 3. 创建文件夹
            String newPath = path + fileName;
            File file = new File(newPath);
            //如果文件目录不存在则创建目录
            if (!file.exists()) {
                if (!file.mkdir()) {
                    System.out.println("当前路径不存在，创建失败");
                    return null;
                }
            }
            System.out.println("创建成功" + newPath);
            return file;
        }

    public static final String VIDEO_PATH = "C:\\Users\\USER\\Desktop\\learn\\c语言\\fragmented.mp4";
    public static final String OUTPUT_PATH = "C:\\Users\\USER\\Desktop\\learn\\c语言\\";

    public static void main(String[] args) throws IOException {

        // 将VIDEO_PATH分割为3分钟一段，VIDEO_PATH总共29分钟,如果结尾有不足3分钟的拼接的最后一段视频上
        splitVideoFile(VIDEO_PATH, OUTPUT_PATH, 9.45f, 2, false);

    }

}
