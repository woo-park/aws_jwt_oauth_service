//package com.awsjwtservice.domain;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//
//import javax.sql.DataSource;
//
//import java.sql.Connection;
//import java.sql.DatabaseMetaData;
//import java.sql.SQLException;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@RunWith(SpringRunner.class)
//@DataJpaTest
//@TestPropertySource(properties = {
//        "spring.jpa.hibernate.ddl-auto=validate"
//})
//class UserRepositoryTest {
//
//
//    @Autowired
//    DataSource dataSource;
//
//    @Autowired
//    JdbcTemplate jdbcTemplate;
//4
//    @Autowired
//    UserRepository accountRespository;
//
//    @Test
//    public void di() throws SQLException {
//        try (Connection connection = dataSource.getConnection()){
//            DatabaseMetaData metaData = connection.getMetaData();
//            System.out.println(metaData.getURL());
//            System.out.println(metaData.getDriverName());
//            System.out.println(metaData.getUserName());
//        }
//    }
//}