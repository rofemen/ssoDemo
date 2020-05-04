package com.rofe.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.io.IOException;
import java.util.List;

@Slf4j
public class OutConfigFileLoader implements EnvironmentPostProcessor {

    private final ResourcePatternResolver ppr = new PathMatchingResourcePatternResolver();

    private final List<PropertySourceLoader> propertySourceLoaderList;

    public OutConfigFileLoader() {
        super();
        //通过SpringFactoriesLoader.loadFactorie加载默认配置文件中的property加载器和yaml加载器
        this.propertySourceLoaderList = SpringFactoriesLoader.loadFactories(PropertySourceLoader.class, getClass().getClassLoader());
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        //加载application.properties的spring.profiles.active属性，遍历加载环境
        String[] activeProfiles = environment.getActiveProfiles();
        for(String activeProfile : activeProfiles){
            for(PropertySourceLoader loader : this.propertySourceLoaderList){
                for(String fileExtension : loader.getFileExtensions()){
                    String location = "classpath:" + activeProfile + "/sso-*." + fileExtension;
                    try{
                        //通过*号匹配该路径下的文件，并加载配置文件的属性
                        Resource[] resources = this.ppr.getResources(location);
                        for(Resource resource : resources){
                            List<PropertySource<?>> propertySources = loader.load(resource.getFilename(), resource);
                            if(propertySources != null && !propertySources.isEmpty()){
                                propertySources.stream().forEach(environment.getPropertySources()::addLast);
                            }
                        }
                    }catch(IOException e){
                        e.printStackTrace();
                        log.error("load config file error message=%s", e.getMessage(), e);
                    }
                }
            }
        }
    }
}
