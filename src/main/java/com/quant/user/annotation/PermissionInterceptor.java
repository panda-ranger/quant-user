package com.quant.user.annotation;

import com.quant.user.utils.Result;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.annotation.Resource;
import java.util.List;

@Component
public class PermissionInterceptor implements HandlerInterceptor {

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod method = (HandlerMethod) handler;

        RequirePermission permission =
                method.getMethodAnnotation(RequirePermission.class);

        if (permission == null) {
            return true;
        }

        String header = request.getHeader("X-Permissions");

        if (header == null || header.isEmpty()) {
            return forbidden(response);
        }

        try {
            List<String> permissions =
                    objectMapper.readValue(
                            header,
                            new TypeReference<List<String>>() {}
                    );

            if (!permissions.contains(permission.value())) {
                return forbidden(response);
            }

            return true;

        } catch (Exception e) {
            return forbidden(response);
        }
    }

    private boolean forbidden(HttpServletResponse response) {
        try {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(
                    objectMapper.writeValueAsString(
                            Result.fail(403, "无权限")
                    )
            );
        } catch (Exception ignored) {
        }
        return false;
    }
}