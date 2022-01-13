package com.ecommerce.newshop1.config;

import org.hibernate.collection.spi.PersistentCollection;
import org.modelmapper.Condition;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper mapper(){

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setPropertyCondition(new Condition<Object, Object>() {
            public boolean applies(MappingContext<Object, Object> context) {
                return !(context.getSource() instanceof PersistentCollection);
            }
        });
        return mapper;
    }



}
