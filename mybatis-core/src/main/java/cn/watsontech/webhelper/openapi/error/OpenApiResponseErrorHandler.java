package cn.watsontech.webhelper.openapi.error;

import cn.watsontech.webhelper.web.result.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.UnknownHttpStatusCodeException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Created by Watson on 2020/6/20.
 */
public class OpenApiResponseErrorHandler extends DefaultResponseErrorHandler {

    @Override
    protected void handleError(ClientHttpResponse response, HttpStatus statusCode) throws IOException {
        String statusText = response.getStatusText();
        HttpHeaders headers = response.getHeaders();
        byte[] body = getResponseBody(response);
        Charset charset = getCharset(response);
        Result result;
        if (ObjectUtils.isEmpty(body)) {
            result = new Result();
            result.setError("未知错误");
        }else {
            result = new ObjectMapper().readValue(body, Result.class);
        }
        String message = (String)result.getError();
        switch (statusCode.series()) {
            case CLIENT_ERROR:
                throw HttpClientErrorException.create(message, statusCode, statusText, headers, body, charset);
            case SERVER_ERROR:
                throw HttpServerErrorException.create(message, statusCode, statusText, headers, body, charset);
            default:
                throw new UnknownHttpStatusCodeException(message, statusCode.value(), statusText, headers, body, charset);
        }
    }

    /**
     * Return error message with details from the response body, possibly truncated:
     * <pre>
     * 404 Not Found: [{'id': 123, 'message': 'my very long... (500 bytes)]
     * </pre>
     */
    private String getErrorMessage(@Nullable byte[] responseBody, @Nullable Charset charset) {

        if (ObjectUtils.isEmpty(responseBody)) {
            return "";
        }

        charset = charset == null ? StandardCharsets.UTF_8 : charset;
        int maxChars = 200;

        if (responseBody.length < maxChars * 2) {
            return new String(responseBody, charset);
        }

        try {
            Reader reader = new InputStreamReader(new ByteArrayInputStream(responseBody), charset);
            CharBuffer buffer = CharBuffer.allocate(maxChars);
            reader.read(buffer);
            reader.close();
            buffer.flip();
            return buffer.toString();
        }
        catch (IOException ex) {
            // should never happen
            throw new IllegalStateException(ex);
        }
    }
}
