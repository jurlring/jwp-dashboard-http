package org.apache.coyote.http11.response;

import static nextstep.jwp.utils.FileReader.readFile;

import java.io.IOException;
import java.util.Map.Entry;
import org.apache.coyote.http11.request.ContentType;

public class HttpResponse {

    private final HttpStatus httpStatus;
    private final ResponseHeader headers;
    private final String body;

    public HttpResponse(HttpStatus httpStatus, ResponseHeader headers, String body) {
        this.httpStatus = httpStatus;
        this.headers = headers;
        this.body = body;
    }

    public void addCookie(String sessionId) {
        headers.addHeader("Set-Cookie", "JSESSIONID=" + sessionId);
    }

    public static HttpResponse ok(String url) throws IOException {
        String responseBody = readFile(url);
        return ok(ContentType.findByUrl(url), responseBody);
    }

    public static HttpResponse ok(ContentType contentType, String responseBody) {
        ResponseHeader header = new ResponseHeader();
        header.addHeader("Content-Type", contentType.getContent());
        header.addHeader("Content-Length", String.valueOf(responseBody.getBytes().length));

        return new HttpResponse(HttpStatus.OK, header, responseBody);
    }

    public static HttpResponse found(String url) {
        ResponseHeader header = new ResponseHeader();
        header.addHeader("Location", url);

        return new HttpResponse(HttpStatus.FOUND, header, "");
    }

    public static HttpResponse unAuthorized() throws IOException {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return clientExceptionResponse(status);
    }

    public static HttpResponse notFound() throws IOException {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return clientExceptionResponse(status);
    }

    private static HttpResponse clientExceptionResponse(HttpStatus status) throws IOException {
        String responseBody = readFile(status.getFilePath());
        ResponseHeader header = new ResponseHeader();
        header.addHeader("Content-Type", ContentType.findByUrl(status.getFilePath()).getContent());
        header.addHeader("Content-Length", String.valueOf(responseBody.getBytes().length));

        return new HttpResponse(status, header, responseBody);
    }

    public String getHttpResponse() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("HTTP/1.1 " + httpStatus.getCode() + " " + httpStatus.getMessage() + " ")
                .append("\r\n");
        for (Entry<String, String> header : headers.getHeaders().entrySet()) {
            stringBuilder.append(header.getKey() + ": " + header.getValue() + " ")
                    .append("\r\n");
        }
        stringBuilder.append("\r\n")
                .append(body);

        return stringBuilder.toString();
    }
}
