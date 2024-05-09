package com.BlogifyHub.configuration;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary getCloudinary(){
        Map config = new HashMap();
        config.put("cloud_name","dzwidv5zn");
        config.put("api_key","984936444345427");
        config.put("api_secret","AcFnz4uowYBVwMIkZ9aDVdw_XnE");
        config.put("secure",true);
        return new Cloudinary(config);
    }
}
