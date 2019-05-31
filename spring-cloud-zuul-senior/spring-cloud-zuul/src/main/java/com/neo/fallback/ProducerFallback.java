package com.neo.fallback;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

/**
 * spring-cloud-producer 断路返回
 */
@Component
public class ProducerFallback implements FallbackProvider {
    private final Logger logger = LoggerFactory.getLogger(FallbackProvider.class);

    /**
     * 指定负责的服务
     * @return
     */
    @Override
    public String getRoute() {
        return "spring-cloud-producer";
    }

    /**
     * 定制返回内容
     * @param service
     * @param throwable
     * @return
     */
    @Override
    public ClientHttpResponse fallbackResponse(String service, Throwable throwable) {
        String msg = "The service " + service + " is unavailable !";
        if (throwable != null && throwable.getCause() != null) {
            logger.error("Throwable {}", throwable.getCause().getMessage());
            msg = "The service " + service + " has an error !";
        }

        return fallbackResponse(msg);
    }

    private ClientHttpResponse fallbackResponse(String msg) {
        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.OK;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return 200;
            }

            @Override
            public String getStatusText() throws IOException {
                return "OK";
            }

            @Override
            public void close() {
            }

            @Override
            public InputStream getBody() throws IOException {
                return new ByteArrayInputStream(msg.getBytes());
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                return headers;
            }
        };
    }
}
