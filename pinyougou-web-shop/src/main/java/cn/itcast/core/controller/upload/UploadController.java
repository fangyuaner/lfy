package cn.itcast.core.controller.upload;

import cn.itcast.core.entity.Result;
import cn.itcast.core.utils.fdfs.FastDFSClient;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @Value("${FILE_SERVER_URL}")
    private String FILE_SERVER_URL;

    /**
     * 商品图片的上传
     * @param file
     * @return
     */
    @RequestMapping("/uploadFile.do")
    public Result uploadFile(MultipartFile file){
        try {
            String conf = "classpath:fastDFS/fdfs_client.conf";
            FastDFSClient fastDFSClient = new FastDFSClient(conf);
            // 获取到文件的扩展名
            // 获取原始附件名称：xxx.jpg
            String filename = file.getOriginalFilename();
            String extName = FilenameUtils.getExtension(filename);
            String path = fastDFSClient.uploadFile(file.getBytes(), extName, null);
            // 回显：拼接服务器地址http://192.168.200.128
            // 服务器地址：常量，java中维护常量方式：配置文件、枚举、接口（对扩展开放，对修改关闭）
            // 本次：通过配置文件维护服务器地址
            String url = FILE_SERVER_URL + path;
            return new Result(true, url);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "上传失败");
        }
    }
}
