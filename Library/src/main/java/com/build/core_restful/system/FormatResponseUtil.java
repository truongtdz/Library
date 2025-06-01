package com.build.core_restful.system;

import com.build.core_restful.domain.response.FormatResponse;
import com.build.core_restful.util.annotation.AddMessage;
import com.nimbusds.jose.util.Resource;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class FormatResponseUtil implements ResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response) {

        HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        int status = servletResponse.getStatus();

        FormatResponse<Object> res = new FormatResponse<Object>();
        res.setCode(status);

       if (body instanceof String || body instanceof Resource) {
           return body;
       }

        String path = request.getURI().getPath();
        if (path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui")) {
            return body;
        }

        if(status >= 400){
            return body;
        } else {
            res.setData(body);
            AddMessage message = returnType.getMethodAnnotation(AddMessage.class);
            res.setMessage(message != null ? message.value() : "Call api success");
        }
        return res;
    }
}
