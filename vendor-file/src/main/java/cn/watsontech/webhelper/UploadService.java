package cn.watsontech.webhelper;

import cn.watsontech.webhelper.param.FileRefFormParams;
import cn.watsontech.webhelper.vo.UploadFile;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URL;
import java.util.*;

/**
 * Copyright to watsontech
 * Created by Watson on 2020/2/3.
 */
public abstract class UploadService {
    public enum UploadVendor {aliyun, tencent, huaweicloud, local}

    protected UploadProperties properties;

    public UploadService(UploadProperties uploadProperties) {
        this.properties = uploadProperties;
    }

    /**
     * 列表显示bucket所有文件
     * @param bucketName
     */
    public abstract List<UploadFile> listFiles(String bucketName);

    /**
     * 生成授权访问链接
     */
    public abstract URL generatePresignedUrl(String bucketName, String key, Date expiration);

    /**
     * 删除tencent文件
     */
    public abstract void deleteFile(String bucketName, String key);

    /**
     * 保存文件到远程仓库或本地
     */
    public UploadFile uploadFile(MultipartFile file, FileRefFormParams fileParam, String fileObjectKey, Long userId) {
        Assert.isTrue(!file.isEmpty(), "错误，当前上传文件为空文件");
        if (fileParam==null) fileParam = new FileRefFormParams();

        return uploadFile(file, properties, fileObjectKey, userId, getUserMetadata(fileParam), fileParam.getStorageLeve(), fileParam.isImage());
    }

    /**
     * 保存文件到远程仓库或本地
     */
    public UploadFile uploadFile(File file, String contentType, FileRefFormParams fileParam, String fileObjectKey, Long userId) {
        Assert.notNull(file, "错误，当前上传文件为空文件");
        if (fileParam==null) fileParam = new FileRefFormParams();

        //加载appid相对应的配置信息
        return uploadFile(file, contentType, properties, fileObjectKey, userId, getUserMetadata(fileParam), fileParam.getStorageLeve(), fileParam.isImage());
    }

    /**
     * 上传本地文件夹到bucket
     * @param bucketName
     * @param localFolder 本地文件夹名称
     * @param objectKeyPrefix cos文件夹前缀
     */
    public abstract void uploadFolder(String bucketName, String localFolder, String objectKeyPrefix);
    public void uploadFolderFiles(String bucketName, String localFolder, String objectKeyPrefix){}
    public abstract void dropFolder(String bucketName, String dropFolder);
    public void dropBucketFiles(String bucketName) {}

    /**
     * 实际执行上传操作方法
     */
    protected abstract UploadFile uploadFile(File tmpFile, String contentType, UploadProperties uploadProperties, String fileObjectKey, Long userId, Map<String, Object> userMetadata, Integer storageLevel/*0 归档存储，1 低频存储，2 标准存储*/, Boolean isImage);
    protected abstract UploadFile uploadFile(MultipartFile file, UploadProperties uploadProperties, String fileObjectKey, Long userId, Map<String, Object> userMetadata, Integer storageLevel/*0 归档存储，1 低频存储，2 标准存储*/, Boolean isImage);

    /**
     * 保存文件到远程仓库或本地
     */
    public List<UploadFile> uploadFiles(MultipartFile[] files, List<FileRefFormParams> fileParams, List<String> fileObjectKeys, Long userId) {
        Assert.isTrue(files!=null&&files.length>0, "错误，当前上传文件为空文件");

        if (fileParams==null) {
            fileParams = new ArrayList<>();
        }

        // 判断文件是否为空
        if (files!=null&&files.length>0) {
            List<UploadFile> fileInfoList = new ArrayList<>();
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                FileRefFormParams fileParam = fileParams.get(i);
                if (fileParam==null) fileParam = new FileRefFormParams();
                String fileObjectKey = fileObjectKeys!=null&&fileObjectKeys.size()>i?fileObjectKeys.get(i):null;

                UploadFile model = uploadFile(file, properties, fileObjectKey, userId, getUserMetadata(fileParam), fileParam.getStorageLeve(), fileParam.isImage());
                //不判断空 因为需要跟前台对应
                fileInfoList.add(model);
            }

            return fileInfoList;
        }

        return Collections.EMPTY_LIST;
    }

    /**
     * 保存文件到远程仓库或本地
     */
    public List<UploadFile> uploadFiles(File[] files, String[] contentTypes, List<FileRefFormParams> fileParams, List<String> fileObjectKeys, Long userId) {
        Assert.isTrue(files!=null&&files.length>0, "错误，当前上传文件为空文件");

        if (fileParams==null) {
            fileParams = new ArrayList<>();
        }
        // 判断文件是否为空
        if (files!=null&&files.length>0) {
            List<UploadFile> fileInfoList = new ArrayList<>();
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                String contentType = contentTypes[i];
                FileRefFormParams fileParam = fileParams.get(i);
                if (fileParam==null) fileParam = new FileRefFormParams();
                String fileObjectKey = fileObjectKeys!=null&&fileObjectKeys.size()>i?fileObjectKeys.get(i):null;

                UploadFile model = uploadFile(file, contentType, properties, fileObjectKey, userId, getUserMetadata(fileParam), fileParam.getStorageLeve(), fileParam.isImage());
                //不判断空 因为需要跟前台对应
                fileInfoList.add(model);
            }

            return fileInfoList;
        }

        return Collections.EMPTY_LIST;
    }

    protected Map<String, Object> getUserMetadata(FileRefFormParams fileParam) {
        Map<String, Object> map = new HashMap<>();
        if (fileParam!=null) {
            map.put("refObjectId", fileParam.getRefObjectId());
            map.put("refObjectType", fileParam.getRefObjectType());
        }
        return map;
    }
}
