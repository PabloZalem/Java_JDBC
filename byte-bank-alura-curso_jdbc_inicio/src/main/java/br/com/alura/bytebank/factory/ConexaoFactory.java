package br.com.alura.bytebank.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoFactory {
    public Connection recuperarConexao(){
        try {
            return DriverManager
                    .getConnection("jdbc:mysql://localhost:3306/byte_bank?user=root&password=pablo1764");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
