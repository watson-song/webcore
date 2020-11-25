package cn.watsontech.webhelper.vendor.impl;

import cn.watsontech.webhelper.vendor.UploadService;
import cn.watsontech.webhelper.web.spring.config.UploadProperties;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Copyright to watsontech
 * Created by Watson on 2020/2/3.
 */
@Log4j2
public class LocalUploadServiceImpl extends UploadService {
    //本地存储文件夹
    File localDir;

    public LocalUploadServiceImpl(UploadProperties uploadProperties) {
        super(uploadProperties);
    }

    @PostConstruct
    void initLocalDir() {
        Assert.isTrue(UploadVendor.local==properties.getVendor(), "当前配置非本地磁盘存储服务");

        localDir = new File(properties.getLocalPath());
        if (!localDir.exists()) {
            localDir.mkdirs();
        }
    }

    /**
     * 列表显示bucket所有文件
     * @param bucketName
     */
    @Override
    public List<UploadFile> listFiles(String bucketName) {
        File childDir = new File(localDir, bucketName);
        File[] files = childDir.listFiles();
        List<UploadFile> uploadFiles = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            UploadFile uploadFile = new UploadFile();
            uploadFile.setSize(file.length());
            uploadFile.setName(file.getName());
            uploadFile.setBucket(bucketName);
            uploadFile.setVendor(UploadVendor.local);
            uploadFile.setPath(file.getPath());
            uploadFiles.add(uploadFile);
        }
        return uploadFiles;
    }

    /**
     * 生成授权访问链接
     */
    @Override
    public URL generatePresignedUrl(String bucketName, String key, Date expiration) {
        try {
            return new URL("file:"+properties.getLocalPath()+key);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除tencent文件
     */
    @Override
    public void deleteFile(String bucketName, String key) {
        File folder = new File(localDir, bucketName);
        new File(folder, key).delete();
    }

    @Override
    protected UploadFile uploadFile(MultipartFile file, UploadProperties uploadProperties, String fileObjectKey, Long userId, Map<String, Object> userMetadata, Integer storageLevel/*0 归档存储，1 低频存储，2 标准存储*/, Boolean isImage) {
        // 判断文件是否为空
        if (!file.isEmpty()) {
            UploadFile model = new UploadFile();
            if(isImage==null) isImage = false;

            try {
                String realFileName = file.getOriginalFilename();
                model.setName(realFileName);

                String remoteFileDirectory = "/" + userId + "/" + DateFormatUtils.format(new Date(), "yyyyMMdd") + "/";
                model.setPath(remoteFileDirectory + URLEncoder.encode(realFileName, "utf-8"));
                model.setSize(file.getSize());
                model.setType(file.getContentType());

                model.setVendor(uploadProperties.getVendor());
                model.setBucket(uploadProperties.getBucket());

                switch (uploadProperties.getVendor()) {
                    case local:
                        // 判断路径是否存在，不存在则新创建一个
                        File filepath = new File(uploadProperties.getLocalPath() + remoteFileDirectory, realFileName);
                        String targetLocalPath = uploadProperties.getLocalPath();
                        if (!filepath.isAbsolute()) {
                            targetLocalPath = getClass().getClassLoader().getResource("").getPath() + targetLocalPath;
                            filepath = new File(targetLocalPath + remoteFileDirectory, realFileName);
                        }

                        if (!filepath.getParentFile().exists()) {
                            filepath.getParentFile().mkdirs();
                        }

                        file.transferTo(new File(targetLocalPath + remoteFileDirectory + realFileName));
                        break;
                    default:
                        throw new IllegalArgumentException("尚不支持的文件存储类型：" + uploadProperties.getVendor());
                }

                model.setUrl((isImage&&uploadProperties.getImageRootUrl()!=null ? uploadProperties.getImageRootUrl():uploadProperties.getWebRootUrl()) + model.getPath());
                return model;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public void uploadFolder(String bucketName, String localFolder, String objectKeyPrefix) {
        throw new IllegalArgumentException("当前文件服务不支持上传文件夹");
    }

    @Override
    public void dropFolder(String bucketName, String dropFolder) {
        throw new IllegalArgumentException("当前文件服务不支持删除文件夹");
    }

    @Override
    protected UploadFile uploadFile(File tmpFile, String contentType, UploadProperties uploadProperties, String fileObjectKey, Long userId, Map<String, Object> userMetadata, Integer storageLevel/*0 归档存储，1 低频存储，2 标准存储*/, Boolean isImage) {
        // 判断文件是否为空
        if (tmpFile!=null) {
            UploadFile model = new UploadFile();
            if(isImage==null) isImage = false;

            try {
                String realFileName = tmpFile.getName();
                model.setName(realFileName);

                String remoteFileDirectory = File.separator + userId + File.separator + DateFormatUtils.format(new Date(), "yyyyMMdd");
                model.setPath(remoteFileDirectory + File.separator + URLEncoder.encode(realFileName, "utf-8"));
                model.setSize(tmpFile.length());
                model.setVendor(uploadProperties.getVendor());
                model.setBucket(uploadProperties.getBucket());
                model.setType(contentType);

                switch (uploadProperties.getVendor()) {
                    case local:
                        // 转存文件

                        // 判断路径是否存在，不存在则新创建一个
                        File filepath = new File(uploadProperties.getLocalPath() + remoteFileDirectory, realFileName);
                        if (!filepath.getParentFile().exists()) {
                            filepath.getParentFile().mkdirs();
                        }

                        FileUtils.moveFile(tmpFile, new File(uploadProperties.getLocalPath() + remoteFileDirectory + File.separator + realFileName));
                        break;
                    default:
                        throw new IllegalArgumentException("尚不支持的文件存储类型："+uploadProperties.getVendor());
                }
                model.setUrl((isImage&&uploadProperties.getImageRootUrl()!=null?uploadProperties.getImageRootUrl():uploadProperties.getWebRootUrl()) + model.getPath());

                return model;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

}
