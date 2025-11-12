package com.ecommerce.clothingstore.config;

import com.ecommerce.clothingstore.payload.ApiResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // Apply to all controller responses except ApiResponse or Exception
        return !returnType.getParameterType().equals(ApiResponse.class);
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        // If response is already an ApiResponse, return it as is
        if (body instanceof ApiResponse) return body;

        // Default message based on HTTP path (for convenience)
        String path = request.getURI().getPath();
        String message = "Request processed successfully";

        if (path.contains("order")) message = "Order operation successful";
        else if (path.contains("cart")) message = "Cart operation successful";

        return new ApiResponse<>(true, message, body);
    }
}
