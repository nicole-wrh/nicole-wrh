package com.demo.labs.config;

import com.demo.labs.constant.DBConstants;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.demo.labs.mapper.users", sqlSessionTemplateRef = "usersSqlSessionTemplate")
public class MyBatisUsersConfig {

    @Bean(name = "usersDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.users")
    public DataSource usersDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "usersSqlSessionFactory")
    public SqlSessionFactory usersSqlSessionFactory(@Qualifier("usersDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setTypeAliasesPackage("com.demo.labs.dataobject");
        bean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:mybatis-config.xml"));
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/users/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "usersSqlSessionTemplate")
    public SqlSessionTemplate usersSqlSessionTemplate(@Qualifier("usersSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(name = DBConstants.TX_MANAGER_USERS)
    public PlatformTransactionManager usersTransactionManager(@Qualifier("usersDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
