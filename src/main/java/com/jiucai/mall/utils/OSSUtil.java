package com.jiucai.mall.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;

@Component
public class OSSUtil{

    @Value("${spring.cloud.alicloud.oss.endpoint}")
    private String endpoint;

    @Value("${spring.cloud.alicloud.oss.bucket}")
    private String bucket;

    @Value("${spring.cloud.alicloud.access-key}")
    private String accessId;

    @Value("${spring.cloud.alicloud.secret-key}")
    private String secretId;

    public String upLoadImage(String image) throws IOException {

        System.out.println(this.endpoint);
        System.out.println(this.accessId);
        System.out.println(this.secretId);

        OSS ossClient = new OSSClientBuilder().build("http://" + endpoint, accessId, secretId);
        String imageName;

        // 获取图片格式
        String suffix = image.substring(11, image.indexOf(";"));
        // 使用插件传输产生的前缀
        String prefix = image.substring(0, image.indexOf(",") + 1);
        // 替换前缀为空
        image = image.replace(prefix,"");

        long currentTime = System.currentTimeMillis();
        imageName = String.valueOf(currentTime) + "." + suffix;

        BASE64Decoder decoder = new BASE64Decoder();
        byte[] imageByte = decoder.decodeBuffer(image);

        try {
            // 判断Bucket是否存在
            if (ossClient.doesBucketExist(bucket)) {
                System.out.println("您已经创建Bucket：" + bucket + "。");
            } else {
                System.out.println("您的Bucket不存在，创建Bucket：" + bucket + "。");
                // 创建Bucket
                ossClient.createBucket(bucket);
            }

            // 把二进制图片字符串存入OSS，picture的名称为fileName
            InputStream pictureStream = new ByteArrayInputStream(imageByte);
            ossClient.putObject(bucket, imageName, pictureStream);
            System.out.println("Picture：" + imageName + "存入OSS成功。");

        } catch (OSSException oe) {
            oe.printStackTrace();
        } catch (ClientException ce) {
            ce.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }

        return imageName;
    }
}
