package cn.watsontech.webhelper.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;

/**
 * Created by Watson on 31/01/2018.
 */
public class AES {
    public static boolean initialized = false;

    public class BadDecryptKeyException extends IllegalArgumentException {
        public BadDecryptKeyException(Throwable cause) {
            super("解密秘钥错误", cause);
        }
    }
    /**
     * AES解密
     * @param content 密文
     * @return
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchProviderException
     */
    public byte[] decrypt(byte[] content, byte[] keyByte, byte[] ivByte) throws BadDecryptKeyException {
        initialize();
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            Key sKeySpec = new SecretKeySpec(keyByte, "AES");
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, generateIV(ivByte));// 初始化
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new BadDecryptKeyException(e);
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            throw new BadDecryptKeyException(e);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new BadDecryptKeyException(e);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            throw new BadDecryptKeyException(e);
        } catch (BadPaddingException e) {
            e.printStackTrace();
            throw new BadDecryptKeyException(e);
        } catch (NoSuchProviderException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new BadDecryptKeyException(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new BadDecryptKeyException(e);
        }
    }
    public static void initialize(){
        if (initialized) return;
        Security.addProvider(new BouncyCastleProvider());
        initialized = true;
    }
    //生成iv
    public static AlgorithmParameters generateIV(byte[] iv) throws Exception {
        AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
        params.init(new IvParameterSpec(iv));
        return params;
    }
}
