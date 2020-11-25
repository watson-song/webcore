package cn.watsontech.webhelper.vendor.impl;

import cn.watsontech.webhelper.vendor.UploadService;
import cn.watsontech.webhelper.web.spring.config.UploadProperties;
import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import com.obs.services.model.*;
import com.obs.services.model.fs.DropFolderRequest;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 使用腾讯云cos上传文件，需添加以下腾讯云依赖
 * <!--华为obs对象存储服务-->
 * <dependency>
 *    <groupId>com.huaweicloud</groupId>
 *    <artifactId>esdk-obs-java</artifactId>
 *    <version>3.19.7</version>
 * </dependency>
 * Created by Watson on 2020/3/9.
 */
@Log4j2
@ConditionalOnClass(ObsClient.class)
public class HuaweiUploadServiceImpl extends UploadService {
    private ObsClient obsClient;

    public HuaweiUploadServiceImpl(UploadProperties uploadProperties) {
        super(uploadProperties);
    }

    @PostConstruct
    void initObsClient() {
        Assert.isTrue(UploadVendor.huaweicloud==properties.getVendor(), "当前配置非华为云存储");

        String endPoint = "https://obs."+properties.getRegionName()+".myhuaweicloud.com";
        // 创建ObsClient实例
        obsClient = new ObsClient(properties.getAccessKey(), properties.getAccessSecret(), endPoint);
    }

    /**
     * 关闭obs服务
     */
    public void closeObsService() throws IOException {
        // 关闭obsClient
        obsClient.close();
    }

    public DownloadFileResult downloadFile(String key, String savePath) {
        DownloadFileRequest downloadFileRequest = new DownloadFileRequest(properties.getBucket(), key);
        downloadFileRequest.setDownloadFile(savePath);
        downloadFileRequest.setProgressListener(new ProgressListener() {
            @Override
            public void progressChanged(ProgressStatus status) {
                status.getTransferPercentage();
            }
        });
        DownloadFileResult downloadFileResult = obsClient.downloadFile(downloadFileRequest);
        return downloadFileResult;
    }

    @Override
    public List<UploadFile> listFiles(String bucketName) {
        ObjectListing objectListing = obsClient.listObjects(bucketName);
        List<ObsObject> cosObjectSummaries = objectListing.getObjects();
        return cosObjectSummaries.stream().map(cosObjectSummary -> {
            String objectKey = cosObjectSummary.getObjectKey();
            ObjectMetadata objectMetadata = cosObjectSummary.getMetadata();
            String[] paths = objectKey.split("/");
            UploadFile uploadFile = new UploadFile();
            uploadFile.setVendor(UploadVendor.huaweicloud);
            uploadFile.setUrl(cosObjectSummary.getObjectKey());
            uploadFile.setType(objectMetadata.getContentType());
            uploadFile.setBucket(cosObjectSummary.getBucketName());
            uploadFile.setName(paths[paths.length-1]);
            uploadFile.setPath(objectKey);
            uploadFile.setSize(objectMetadata.getContentLength());
            uploadFile.setTags(objectMetadata.getEtag());
            return uploadFile;
        }).collect(Collectors.toList());
    }

    @Override
    public URL generatePresignedUrl(String bucketName, String key, Date expiration) {
        //String bucketName, String objectKey, String prefix, Date expiryDate, Map<String, String> headers, Map<String, Object> queryParams
        TemporarySignatureResponse response = obsClient.createGetTemporarySignature(bucketName, key, "", expiration, null, null);
        try {
            return new URL(response.getSignedUrl());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deleteFile(String bucketName, String key) {
        obsClient.deleteObject(bucketName, key);
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

                //如果未提供存储位置则给出默认key
                if(StringUtils.isEmpty(fileObjectKey)) {
                    fileObjectKey = userId + "/" + DateFormatUtils.format(new Date(), "yyyyMMdd") + "/" + URLEncoder.encode(realFileName, "utf-8");
                }
                model.setPath(fileObjectKey);
                model.setSize(file.getSize());
                model.setType(file.getContentType());

                model.setVendor(uploadProperties.getVendor());
                model.setBucket(uploadProperties.getBucket());

                switch (uploadProperties.getVendor()) {
                    case huaweicloud:
                        PutObjectResult uploadResult = uploadHuaweiFile(uploadProperties.getBucket(), file.getInputStream(), file.getContentType(), file.getSize(), fileObjectKey, userMetadata, convertStorageClass(storageLevel));
                        model.setTags(uploadResult.getEtag());
                        break;
                    default:
                        throw new IllegalArgumentException("不支持的文件存储类型：" + uploadProperties.getVendor());
                }

                model.setUrl((isImage&&uploadProperties.getImageRootUrl()!=null ? uploadProperties.getImageRootUrl():uploadProperties.getWebRootUrl()) + "/"+model.getPath());
                return model;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public void uploadFolder(String bucketName, String localFolder, String objectKeyPrefix) {
        uploadHuaweiFolder(bucketName, localFolder, objectKeyPrefix);
    }

    @Override
    public void uploadFolderFiles(String bucketName, String localFolder, String objectKeyPrefix) {
       File folder = new File(localFolder);
       if (folder.exists()) {
           File[] folderFiles = folder.listFiles();
           if (folderFiles!=null) {
               for (int i = 0; i < folderFiles.length; i++) {
                   File folderFile = folderFiles[i];
                   if (folderFile.isDirectory()) {
                       String subFolderName = folderFile.getName();
                       if(objectKeyPrefix!=null) {
                           uploadFolderFiles(bucketName, folderFile.getAbsolutePath(), objectKeyPrefix+"/"+subFolderName);
                       }else {
                           uploadFolderFiles(bucketName, folderFile.getAbsolutePath(), subFolderName);
                       }
                   }else {
                       PutObjectRequest putObjectRequest = null;
                       if(objectKeyPrefix!=null) {
                           putObjectRequest = new PutObjectRequest(bucketName, objectKeyPrefix + "/" + folderFile.getName(), folderFile);
                       }else {
                           putObjectRequest = new PutObjectRequest(bucketName, folderFile.getName(), folderFile);
                       }

                       putObjectRequest.setAcl(AccessControlList.REST_CANNED_PUBLIC_READ);
                       obsClient.putObject(putObjectRequest);
                   }
               }
           }
       }
    }

    @Override
    public void dropFolder(String bucketName, String dropFolder) {
        dropHuaweiFolder(bucketName, dropFolder);
    }

    @Override
    public void dropBucketFiles(String bucketName) {
        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName);
        deleteObjectsRequest.setQuiet(true);
        ObjectListing objectListing = obsClient.listObjects(bucketName);
        objectListing.getObjects().forEach(obsObject -> {
            deleteObjectsRequest.addKeyAndVersion(obsObject.getObjectKey());
        });
        obsClient.deleteObjects(deleteObjectsRequest);
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

                //如果未提供存储位置则给出默认key
                if(StringUtils.isEmpty(fileObjectKey)) {
                    fileObjectKey = userId + "/" + DateFormatUtils.format(new Date(), "yyyyMMdd") + "/" + URLEncoder.encode(realFileName, "utf-8");
                }
                model.setPath(fileObjectKey);

//                String remoteFileDirectory = userId + File.separator + DateFormatUtils.format(new Date(), "yyyyMMdd");
//                model.setPath(remoteFileDirectory + File.separator + URLEncoder.encode(realFileName, "utf-8"));
                model.setSize(tmpFile.length());
                model.setVendor(uploadProperties.getVendor());
                model.setBucket(uploadProperties.getBucket());
                model.setType(contentType);

                switch (uploadProperties.getVendor()) {
                    case huaweicloud:
                        PutObjectResult putObjectResult = uploadHuaweiFile(uploadProperties.getBucket(), new FileInputStream(tmpFile), contentType, tmpFile.length(), fileObjectKey, userMetadata, convertStorageClass(storageLevel));
                        model.setTags(putObjectResult.getEtag());

                        //删除本地临时文件
                        tmpFile.delete();
                        break;
                    default:
                        throw new IllegalArgumentException("不支持的文件存储类型："+uploadProperties.getVendor());
                }
                model.setUrl((isImage&&uploadProperties.getImageRootUrl()!=null?uploadProperties.getImageRootUrl():uploadProperties.getWebRootUrl()) +"/"+ model.getPath());

                return model;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * 上传文件到腾讯云并返回url
     */
    private PutObjectResult uploadHuaweiFile(String bucketName, InputStream inputStream, String contentType, long contentLength, String key, Map<String, Object> userMetadata, StorageClassEnum storageClass) {
        // 简单文件上传, 最大支持 5 GB, 适用于小文件上传, 建议 20M以下的文件使用该接口
        // 大文件上传请参照 API 文档高级 API 上传
        // File localFile = new File("src/test/resources/len5M.txt");
        // 指定要上传到 COS 上对象键
        // 对象键（Key）是对象在存储桶中的唯一标识。例如，在对象的访问域名 `bucket1-1250000000.cos.ap-guangzhou.myqcloud.com/doc1/pic1.jpg` 中，对象键为 doc1/pic1.jpg, 详情参考 [对象键](https://cloud.tencent.com/document/product/436/13324)
        // String key = "upload_single_demo.txt";

        ObjectMetadata objectMetadata = new ObjectMetadata();
        // 设置输入流长度为 500
        objectMetadata.setContentLength(contentLength);
        // 设置 Content type, 默认是 application/octet-stream
        objectMetadata.setContentType(contentType);
        objectMetadata.setMetadata(userMetadata);
        objectMetadata.setObjectStorageClass(storageClass);

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, inputStream);
        putObjectRequest.setMetadata(objectMetadata);
        putObjectRequest.setAcl(AccessControlList.REST_CANNED_PUBLIC_READ);

        PutObjectResult putObjectResult = obsClient.putObject(putObjectRequest);
        log.info("上传文件到华为云成功，{}", putObjectResult);
        return putObjectResult;
    }

    /**
     * 上传文件到华为云并返回url
     * @param bucketName bucket名称
     * @param localFolder 本地文件夹
     * @param objectKeyPrefix cos文件夹前缀
     */
    private UploadProgressStatus uploadHuaweiFolder(String bucketName, String localFolder, String objectKeyPrefix) {
        PutObjectsRequest request = new PutObjectsRequest(bucketName, localFolder);
        request.setAcl(AccessControlList.REST_CANNED_PUBLIC_READ);
        request.setPrefix(objectKeyPrefix);
        request.setCallback(new TaskCallback<PutObjectResult, PutObjectBasicRequest>() {
            @Override
            public void onSuccess(PutObjectResult result) {
                log.info("文件上传成功：{}", result.getObjectKey());
            }

            @Override
            public void onException(ObsException exception, PutObjectBasicRequest singleRequest) {
                log.error("文件上传失败：{}，错误：{}", singleRequest.getObjectKey(), exception.getMessage());
            }
        });
        UploadProgressStatus uploadProgressStatus = obsClient.putObjects(request);
        log.info("上传文件到华为云成功，{}", uploadProgressStatus);
        return uploadProgressStatus;
    }

    /**
     * 删除文件夹
     * @param bucketName
     * @param dropFolder
     * @return
     */
    private TaskProgressStatus dropHuaweiFolder(String bucketName, String dropFolder) {
        DropFolderRequest dropFolderRequest = new DropFolderRequest(bucketName, dropFolder);
        dropFolderRequest.setCallback(new TaskCallback<DeleteObjectResult, String>() {
            @Override
            public void onSuccess(DeleteObjectResult result) {
                log.info("删除文件夹成功：{}", result.getObjectKey());
            }

            @Override
            public void onException(ObsException exception, String singleRequest) {
                log.error("删除文件夹失败：{}，错误：{}", singleRequest, exception.getMessage());
            }
        });

        return obsClient.dropFolder(dropFolderRequest);
    }

    private StorageClassEnum convertStorageClass(Integer storageLevel) {
        if (storageLevel==null) storageLevel = 2;

        switch (storageLevel) {
            /*0 归档存储，1 低频存储，2 标准存储*/
            case 0: return StorageClassEnum.COLD;
            case 1: return StorageClassEnum.WARM;
            default: return StorageClassEnum.STANDARD;
        }
    }
}
