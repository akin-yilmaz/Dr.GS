package com.dreamgames.backendengineeringcasestudy.config;
/*
import com.dreamgames.backendengineeringcasestudy.converter.TestGroupReadingConverter;
import com.dreamgames.backendengineeringcasestudy.converter.TestGroupWritingConverter;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;

import java.util.ArrayList;
import java.util.List;

// https://docs.spring.io/spring-data/relational/reference/r2dbc/mapping.html

@Configuration
public class R2dbcConfig{

    @Bean
    public R2dbcCustomConversions r2dbcCustomConversionsTestGroup() {

        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new TestGroupWritingConverter());
        converters.add(new TestGroupReadingConverter());
        return new R2dbcCustomConversions(R2dbcCustomConversions.StoreConversions.NONE, converters);

    }

}
*/