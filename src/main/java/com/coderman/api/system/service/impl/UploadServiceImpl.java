package com.coderman.api.system.service.impl;


import com.coderman.api.system.service.UploadService;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;
import java.util.Random;

/**
 * @Author
 * @Date 2020/8/19 14:59
 * @Version 1.0
 **/
@Service
public class UploadServiceImpl implements UploadService {

    //图片存放根路径
    @Value("${file.rootPath}")
    private String ROOT_PATH;
    //图片存放根目录下的子目录
    @Value("${file.sonPath}")
    private String SON_PATH;

    @Value("${server.port}")
    //获取主机端口
    private String POST;
    //获取IP
    @Value("${file.host}")
    private String HOST;


    @Override
    public String getUploadFilePath(MultipartFile file) {
        //返回上传的文件是否为空，即没有选择任何文件，或者所选文件没有内容。
        //防止上传空文件导致奔溃
        if (file.isEmpty()) {
            throw new NullPointerException("文件为空");
        }
        // 设置文件上传后的路径
        String filePath = ROOT_PATH + SON_PATH;
        // 获取文件名后缀名
        String suffix = file.getOriginalFilename();
        String prefix = suffix.substring(suffix.lastIndexOf(".")+1);
        //为防止文件重名被覆盖，文件名取名为：当前日期 + 1-1000内随机数
        Random random = new Random();
        Integer randomFileName = random.nextInt(1000);
        String fileName = timestamp2string(System.currentTimeMillis(),"yyyyMMddHHmmss") + randomFileName +"." +  prefix;
        //创建文件路径
        File dest = new File(filePath + fileName);
        // 解决中文问题，liunx下中文路径，图片显示问题
        // fileName = UUID.randomUUID() + suffixName;
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            //假如文件不存在即重新创建新的文件已防止异常发生
            dest.getParentFile().mkdirs();
        }
        try {
            //transferTo（dest）方法将上传文件写到服务器上指定的文件
            file.transferTo(dest);
            //保存t_upload_file表中
            String filePathNew = SON_PATH + fileName;
            BufferedImage image = ImageIO.read(file.getInputStream());
            String profilePhoto = HOST + ":" + POST + filePathNew;
            if (image != null) {//如果image=null 表示上传的不是图片格式

            }
            return profilePhoto;
        } catch (Exception e) {
            return dest.toString();
        }
    }


    public static String timestamp2string(long time, String pattern) {
        Date d = new Date(time);

        if (pattern == null) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        return DateFormatUtils.format(d, pattern);
    }



    private static boolean running = false;
    public void deleteFileImg(String IMG_PATH){
        if (!running ) {
            boolean isRunning = true;
            File file = new File(IMG_PATH);
            //判断文件是否存在
            if (file.exists() == true){
                System.out.println("图片存在，可执行删除操作");
                Boolean flag = false;
                flag = file.delete();
                if (flag){
                    System.out.println("成功删除图片"+file.getName());
                }else {
                    System.out.println("删除失败");
                }
            }else {
                System.out.println("图片不存在，终止操作");
            }
        }
    }
}
