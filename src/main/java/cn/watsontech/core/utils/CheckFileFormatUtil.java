/*
 * Copyright (c) 2019. 串普网络科技有限公司版权所有.
 */

package cn.watsontech.core.utils;

/**
 * Created by Watson on 2019/10/8.
 */
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;


/**
 * @author wlx
 * @version 创建时间 2018年7月3日 下午16:37:26
 * <p>
 * 类描述：获取和判断文件头信息
 * |--文件头是位于文件开头的一段承担一定任务的数据，一般都在开头的部分。
 * |--头文件作为一种包含功能函数、数据接口声明的载体文件，用于保存程序的声明(declaration),而定义文件用于保存程序的实现(implementation)。
 * |--为了解决在用户上传文件的时候在服务器端判断文件类型的问题，故用获取文件头的方式，直接读取文件的前几个字节，来判断上传文件是否符合格式。
 */
public class CheckFileFormatUtil {

    public static final class FileType {
        String type;
        String contentType;

        public FileType(String type, String contentType) {
            this.type = type;
            this.contentType = contentType;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        @Override
        public String toString() {
            return "FileType{" +
                    "type='" + type + '\'' +
                    ", contentType='" + contentType + '\'' +
                    '}';
        }
    }

    // 缓存文件头信息-文件头信息
    private static final HashMap<String, FileType> mFileTypes = new HashMap<String, FileType>();
    static {
        // images
        mFileTypes.put("FFD8FF", new FileType("jpg", "image/jpeg"));
        mFileTypes.put("89504E47", new FileType("png", "image/png"));
        mFileTypes.put("47494638", new FileType("gif", "image/gif"));
        mFileTypes.put("49492A00", new FileType("tif", "image/tiff"));
        mFileTypes.put("424D", new FileType("bmp", "application/x-bmp"));
        //
        mFileTypes.put("41433130", new FileType("dwg", "application/x-dwg")); // CAD
        mFileTypes.put("38425053", new FileType("psd", "application/octet-stream"));
        mFileTypes.put("7B5C727466", new FileType("rtf", "application/x-rtf")); // 日记本
        mFileTypes.put("3C3F786D6C", new FileType("xml", "text/xml"));
        mFileTypes.put("68746D6C3E", new FileType("html", "text/html"));

        mFileTypes.put("44656C69766572792D646174653A", new FileType("eml", "message/rfc822")); // 邮件
        mFileTypes.put("D0CF11E0", new FileType("doc", "application/msword"));
        mFileTypes.put("D0CF11E0", new FileType("xls", "application/x-xls"));//excel2003版本文件
        mFileTypes.put("5374616E64617264204A", new FileType("mdb", "application/x-mdb"));
        mFileTypes.put("252150532D41646F6265", new FileType("ps", "application/x-ps"));
        mFileTypes.put("255044462D312E", new FileType("pdf", "application/pdf"));
        mFileTypes.put("504B0304", new FileType("docx", "application/vnd.ms-word.document.12"));
        mFileTypes.put("504B0304", new FileType("xlsx", "application/vnd.ms-excel.12"));//excel2007以上版本文件
        mFileTypes.put("52617221", new FileType("rar", "application/octet-stream"));
        mFileTypes.put("57415645", new FileType("wav", "audio/wav"));
        mFileTypes.put("41564920", new FileType("avi", "video/avi"));
        mFileTypes.put("2E524D46", new FileType("rm", "application/vnd.rn-realmedia"));
        mFileTypes.put("000001BA", new FileType("mpg", "video/mpg"));
        mFileTypes.put("000001B3", new FileType("mpg", "video/mpg"));
        mFileTypes.put("6D6F6F76", new FileType("mov", "video/quicktime"));
        mFileTypes.put("3026B2758E66CF11", new FileType("asf", "video/x-ms-asf"));
        mFileTypes.put("4D546864", new FileType("mid", "audio/mid"));
        mFileTypes.put("1F8B08", new FileType("gz", "application/x-gzip"));
    }

    /**
     * @param filePath 文件路径
     * @return 文件头信息
     * @author wlx
     * <p>
     * 方法描述：根据文件路径获取文件头信息
     */
    public static FileType getFileType(String filePath) {
        FileType fileType = filePath!=null?mFileTypes.get(getFileHeader(filePath)):null;
        if (fileType==null) {
            fileType = new FileType("tmp", "application/octet-stream");
        }
        return fileType;
    }

    /**
     * @param filePath 文件路径
     * @return 文件头信息
     * @author wlx
     * <p>
     * 方法描述：根据文件路径获取文件头信息
     */
    public static String getFileHeader(String filePath) {
        FileInputStream is = null;
        String value = null;
        try {
            is = new FileInputStream(filePath);
            byte[] b = new byte[4];
            /*
			 * int read() 从此输入流中读取一个数据字节。int read(byte[] b) 从此输入流中将最多 b.length
			 * 个字节的数据读入一个 byte 数组中。 int read(byte[] b, int off, int len)
			 * 从此输入流中将最多 len 个字节的数据读入一个 byte 数组中。
			 */
            is.read(b, 0, b.length);
            value = bytesToHexString(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }

    /**
     * @param src 要读取文件头信息的文件的byte数组
     * @return 文件头信息
     * @author wlx
     * <p>
     * 方法描述：将要读取文件头信息的文件的byte数组转换成string类型表示
     */
    private static String bytesToHexString(byte[] src) {
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        String hv;
        for (byte aSrc : src) {
            // 以十六进制（基数 16）无符号整数形式返回一个整数参数的字符串表示形式，并转换为大写
            hv = Integer.toHexString(aSrc & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        return builder.toString();
    }

    /**
     * @param args
     * @throws Exception
     * @author wlx
     * <p>
     * 方法描述：测试
     */
    public static void main(String[] args) throws Exception {
        final FileType fileType = getFileType("/Users/watson/tmp/2019-10-08/e23cd09cd0384707901b79821e4fa150.png");
        System.out.println(fileType);
        System.out.println(getFileHeader("/Users/watson/tmp/2019-10-08/e23cd09cd0384707901b79821e4fa150.png"));
    }
}
