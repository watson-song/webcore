package cn.watsontech.webhelper.impl;

import cn.watsontech.webhelper.UploadProperties;
import cn.watsontech.webhelper.UploadService;
import cn.watsontech.webhelper.vo.UploadFile;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 使用腾讯云cos上传文件，需添加以下腾讯云依赖
 * <dependency>
 *    <groupId>com.qcloud</groupId>
 *    <artifactId>cos_api</artifactId>
 *    <version>${qcloud-version}</version>
 * </dependency>
 * Copyright to watsontech
 * Created by Watson on 2020/2/3.
 */
@Service
@ConditionalOnClass(COSClient.class)
public class TencentUploadServiceImpl extends UploadService {
    Log log = LogFactory.getLog(TencentUploadServiceImpl.class);

    //缓存已经初始化的tencentcos客户端
    COSClient tencentCosClient;

    public TencentUploadServiceImpl(UploadProperties uploadProperties) {
        super(uploadProperties);
    }

    @PostConstruct
    void initTencentCosClient() {
        Assert.isTrue(UploadVendor.tencent==properties.getVendor(), "当前配置非腾讯云存储服务");

        COSCredentials cred = new BasicCOSCredentials(properties.getAccessKey(), properties.getAccessSecret());// 1 初始化用户身份信息(secretId, secretKey)
        ClientConfig clientConfig = new ClientConfig(new Region(properties.getRegionName()));// 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        tencentCosClient = new COSClient(cred, clientConfig);
    }

    /**
     * 上传文件到腾讯云并返回url
     */
    private ObjectMetadata uploadTencentFile(String bucketName, InputStream inputStream, String contentType, long contentLength, String key, Map<String, Object> userMetadata, StorageClass storageClass) {
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
        objectMetadata.setUserMetadata(convertMap(userMetadata));

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, inputStream, objectMetadata);
        putObjectRequest.withStorageClass(storageClass);
        PutObjectResult putObjectResult = tencentCosClient.putObject(putObjectRequest);
        log.info(String.format("上传文件到腾讯云成功，%s", putObjectResult));
        return putObjectResult.getMetadata();
    }

    private Map<String, String> convertMap(Map<String, Object> metadata) {
        Map<String, String> result = new HashMap<>();
        if (metadata!=null) {
            Set<String> keys = metadata.keySet();
            Iterator<String> iterable = keys.iterator();
            while (iterable.hasNext()) {
                String key = iterable.next();
                result.put(key, (String)metadata.get(key));
            }
        }
        return result;
    }

    /**
     * 列表显示bucket所有文件
     * @param bucketName
     */
    @Override
    public List<UploadFile> listFiles(String bucketName) {
        ObjectListing objectListing = tencentCosClient.listObjects(bucketName);
        List<COSObjectSummary> cosObjectSummaries = objectListing.getObjectSummaries();
        return cosObjectSummaries.stream().map(cosObjectSummary -> {
            String objectKey = cosObjectSummary.getKey();
            String[] paths = objectKey.split("/");
            UploadFile uploadFile = new UploadFile();
            uploadFile.setVendor(UploadVendor.tencent);
            uploadFile.setUrl(cosObjectSummary.getKey());
//            uploadFile.setType();
            uploadFile.setBucket(cosObjectSummary.getBucketName());
            uploadFile.setName(paths[paths.length-1]);
            uploadFile.setPath(objectKey);
            uploadFile.setSize(cosObjectSummary.getSize());
            uploadFile.setTags(cosObjectSummary.getETag());
            return uploadFile;
        }).collect(Collectors.toList());
    }

    /**
     * 生成授权访问链接
     */
    @Override
    public URL generatePresignedUrl(String bucketName, String key, Date expiration) {
        return tencentCosClient.generatePresignedUrl(bucketName, key, expiration);
    }

    /**
     * 删除tencent文件
     */
    @Override
    public void deleteFile(String bucketName, String key) {
        tencentCosClient.deleteObject(bucketName, key);
    }

    @Override
    protected UploadFile uploadFile(MultipartFile file, UploadProperties uploadProperties, String fileObjectKey, Long userId, Map<String, Object> userMetadata, Integer storageLevel/*0 归档存储，1 低频存储，2 标准存储*/, Boolean isImage) {
        // 判断文件是否为空
        if (!file.isEmpty()) {
            UploadFile model = new UploadFile();
            if(isImage==null) isImage = false;

            try {
                return uploadInternal(model, file.getContentType(), uploadProperties, file.getInputStream(), file.getOriginalFilename(), file.getSize(), userId, userMetadata, storageLevel, isImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected UploadFile uploadFile(File tmpFile, String contentType, UploadProperties uploadProperties, String fileObjectKey, Long userId, Map<String, Object> userMetadata, Integer storageLevel/*0 归档存储，1 低频存储，2 标准存储*/, Boolean isImage) {
        // 判断文件是否为空
        if (tmpFile!=null) {
            UploadFile model = new UploadFile();
            if(isImage==null) isImage = false;

            try {
                model = uploadInternal(model, contentType, uploadProperties, new FileInputStream(tmpFile), tmpFile.getName(), tmpFile.length(), userId, userMetadata, storageLevel, isImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (model!=null) {
                //删除本地临时文件
                tmpFile.delete();
            }
            return model;
        }

        return null;
    }

    private UploadFile uploadInternal(UploadFile model, String contentType, UploadProperties uploadProperties, InputStream fileInputStream, String realFileName, long fileLength, Long userId, Map<String, Object> userMetadata, Integer storageLevel/*0 归档存储，1 低频存储，2 标准存储*/, Boolean isImage) {
        try {
            String remoteFileDirectory = "/" + userId + "/" + DateFormatUtils.format(new Date(), "yyyyMMdd")+"/";
            model.setPath(remoteFileDirectory +  URLEncoder.encode(realFileName, "utf-8").replace("+", "%20"));//encode后替换%20解决空格问题
            model.setName(realFileName);
            model.setSize(fileLength);
            model.setVendor(uploadProperties.getVendor());
            model.setBucket(uploadProperties.getBucket());
            model.setType(contentType);

            switch (uploadProperties.getVendor()) {
                case tencent:
                    ObjectMetadata uploadResult = uploadTencentFile(uploadProperties.getBucket(), fileInputStream, contentType, fileLength, remoteFileDirectory + realFileName, userMetadata, convertTencentStorageClass(storageLevel));
                    model.setTags(uploadResult.getETag());
                    break;
                default:
                    throw new IllegalArgumentException("尚不支持的文件存储类型："+uploadProperties.getVendor());
            }
            model.setUrl((isImage&&uploadProperties.getImageRootUrl()!=null?uploadProperties.getImageRootUrl():uploadProperties.getWebRootUrl()) + model.getPath());

            return model;
        } catch (Exception e) {
            e.printStackTrace();
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

    private StorageClass convertTencentStorageClass(Integer storageLevel) {
        if (storageLevel==null) storageLevel = 2;

        switch (storageLevel) {
            case 0: return StorageClass.fromValue("Archive");
            case 1: return StorageClass.Standard_IA;
            default: return StorageClass.Standard;
        }
    }
}
