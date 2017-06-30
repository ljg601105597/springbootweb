package com.ljg;


import com.ljg.common.TokenManager;
import com.ljg.exception.TokenException;
import com.ljg.intercept.SinoInterceptorAdapter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 相关配置.
 *
 * @author
 * @create 2017-05-16 13:25
 **/
@Configuration
@EnableAutoConfiguration
public class ApplicationConfig extends WebMvcConfigurerAdapter {

    private String sinoddPath="";

    /**
     * 配置拦截器.
     * 多个拦截器组成一个拦截器链
     * addPathPatterns 用于添加拦截规则
     * excludePathPatterns 用户排除拦截
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // sino Token拦截器
        registry.addInterceptor(new SinoInterceptorAdapter() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//                response.setHeader("Access-Control-Allow-Origin","*");
//                response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, client_id, uuid, Authorization");
                // token 验证
                String token = request.getHeader("Authorization");
                if (StringUtils.isEmpty(token)) {
                    throw new TokenException("缺少token");
                }
                try {
                    TokenManager.parseJWT(token,"123");
                }catch (TokenException e){
                    throw e;
                }

                return true;
            }
        }).addPathPatterns(sinoddPath + "/**")
                .excludePathPatterns(sinoddPath)
                .excludePathPatterns(sinoddPath + "/login");
        super.addInterceptors(registry);
    }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }

}
