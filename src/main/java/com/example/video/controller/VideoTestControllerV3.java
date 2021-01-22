package com.example.video.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

@RestController
public class VideoTestControllerV3 {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @GetMapping("video3/test/video{index}.mp4")
    public String testVideo(@PathVariable("index")  int index) {
        String fileUri = "C:\\Users\\USER\\Desktop\\learn\\c语言\\2021-01-22-17-19-22\\video" + index + ".mp4";
        //创建文件对象
        File f = new File(fileUri);
        //获取文件名称
        String fileName = f.getName();
        //导出文件
        InputStream fis = null;
        OutputStream os = null;
        try {
            fis = new BufferedInputStream(new FileInputStream(f.getPath()));
            byte[] buffer;
            buffer = new byte[fis.available()];
            fis.read(buffer);
            response.reset();
            //由于火狐和其他浏览器显示名称的方式不相同，需要进行不同的编码处理
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            //设置response编码
            response.setCharacterEncoding("UTF-8");
            response.addHeader("Content-Length", "" + f.length());
            //设置输出文件类型
            response.setContentType("video/mp4");
            response.setHeader("Access-Control-Allow-Origin", "*");
            //获取response输出流
            os = response.getOutputStream();
            // 输出文件
            os.write(buffer);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            //关闭流
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } finally {
                try {
                    if (os != null) {
                        os.flush();
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                } finally {
                    try {
                        if (os != null) {
                            os.close();
                        }
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
        return "success";
    }
}
