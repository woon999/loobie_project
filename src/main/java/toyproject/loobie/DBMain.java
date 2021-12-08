package toyproject.loobie;


import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBMain {
    static {
        try {
            Class<?> aClass = Class.forName("org.mariadb.jdbc.Driver");
            System.out.println(aClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
//        final HikariDataSource ds = new HikariDataSource();
//        ds.setMaximumPoolSize(20);
//        ds.setDriverClassName("org.mariadb.jdbc.Driver");
//        ds.setJdbcUrl("jdbc:mariadb://dotnews-rds.cnyobjdzyehq.ap-northeast-2.rds.amazonaws.com:3306/dotnews");
//        ds.addDataSourceProperty("user", "admin");
//        ds.addDataSourceProperty("password", "-");
//        ds.setAutoCommit(false);
//        System.out.println(ds);

//        try(Connection con =
//                    DriverManager.getConnection("jdbc:mariadb://dotnews-rds.cnyobjdzyehq.ap-northeast-2.rds.amazonaws.com:3306/" +
//                            "dotnews?user=admin&password=--")){
//            System.out.println(con);
//            if(con != null) {
//                System.out.println("DB Connection Success!");
//            }
//        }catch (SQLException e){
//            e.printStackTrace();
//        }
//
//        System.out.println(con);

    }
}
