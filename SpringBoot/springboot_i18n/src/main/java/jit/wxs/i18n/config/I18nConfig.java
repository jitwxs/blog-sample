package jit.wxs.i18n.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;

/**
 * 国际化配置类
 * @author jitwxs
 * @since 2018/12/2 16:37
 */
@Configuration
public class I18nConfig {

    @Bean
    public LocaleResolver localeResolver() {
        return new CustomSessionLocaleResolver();
    }
}
