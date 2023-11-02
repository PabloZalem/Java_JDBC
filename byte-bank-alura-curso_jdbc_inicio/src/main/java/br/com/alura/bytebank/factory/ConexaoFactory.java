package br.com.alura.bytebank.factory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoFactory {
    public Connection recuperarConexao(){
        try {
            //return DriverManager.getConnection("jdbc:mysql://localhost:3306/byte_bank?user=root&password=pablo1764");
            return createDataSource().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private HikariDataSource createDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/byte_bank");
        config.setUsername("root");
        config.setPassword("pablo1764");
        config.setMaximumPoolSize(10);

        return new HikariDataSource(config);
    }
}
