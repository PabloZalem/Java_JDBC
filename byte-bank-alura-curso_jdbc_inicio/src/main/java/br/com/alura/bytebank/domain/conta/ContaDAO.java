package br.com.alura.bytebank.domain.conta;

import br.com.alura.bytebank.domain.cliente.Cliente;
import br.com.alura.bytebank.domain.cliente.DadosCadastroCliente;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ContaDAO {
    private Connection connection;

    public ContaDAO(Connection connection) {
        this.connection = connection;
    }

    public void salvar(DadosAberturaConta dadosDaConta){
        var cliente = new Cliente(dadosDaConta.dadosCliente());
        var conta = new Conta(dadosDaConta.numero(), BigDecimal.ZERO, cliente, true);
        String sql = "INSERT INTO conta(numero, saldo, cliente_nome, cliente_cpf, cliente_email)" +
                " VALUES (?,?,?,?,?)";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, conta.getNumero());
            preparedStatement.setBigDecimal(2, BigDecimal.ZERO);
            preparedStatement.setString(3,dadosDaConta.dadosCliente().nome());
            preparedStatement.setString(4,dadosDaConta.dadosCliente().cpf());
            preparedStatement.setString(5,dadosDaConta.dadosCliente().email());
            preparedStatement.setBoolean(6,true);

            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<Conta> listar() {
        PreparedStatement ps;
        ResultSet resultSet;
        Set<Conta> contas = new HashSet<>();

        String sql = "SELECT * FROM conta WHERE esta_ativa = true";

        try {
            ps = connection.prepareStatement(sql);
            resultSet = ps.executeQuery();

            while (resultSet.next()) {
                Integer numero = resultSet.getInt(1);
                BigDecimal saldo = resultSet.getBigDecimal(2);
                String nome = resultSet.getString(3);
                String cpf = resultSet.getString(4);
                String email = resultSet.getString(5);
                Boolean estaAtiva = resultSet.getBoolean(6);

                DadosCadastroCliente dadosCadastroCliente =
                        new DadosCadastroCliente(nome, cpf, email);
                Cliente cliente = new Cliente(dadosCadastroCliente);

                contas.add(new Conta(numero, saldo, cliente, estaAtiva));
            }
            resultSet.close();
            ps.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return contas;
    }

    public Conta listarPorNumero(Integer numero) {
        String sql = "SELECT * FROM conta WHERE numero = ? AND esta_ativa = true";

        PreparedStatement ps;
        ResultSet resultSet;
        Conta conta = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, numero);  // Defina o valor do parâmetro aqui
            resultSet = ps.executeQuery();

            while (resultSet.next()) {
                Integer numeroRecuperado = resultSet.getInt(1);
                BigDecimal saldo = resultSet.getBigDecimal(2);
                String nome = resultSet.getString(3);
                String cpf = resultSet.getString(4);
                String email = resultSet.getString(5);
                Boolean estaAtiva = resultSet.getBoolean(6);

                DadosCadastroCliente dadosCadastroCliente =
                        new DadosCadastroCliente(nome, cpf, email);
                Cliente cliente = new Cliente(dadosCadastroCliente);

                conta = new Conta(numeroRecuperado, saldo, cliente, estaAtiva);
            }
            resultSet.close();
            ps.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return conta;
    }


    public void alterarLogico(Integer numeroDaConta){
        PreparedStatement ps;
        String sql = "UPDATE conta SET esta_ativa = false WHERE numero = ?";

        try {
            connection.setAutoCommit(false);

            ps = connection.prepareStatement(sql);

            ps.setInt(1, numeroDaConta);

            ps.execute();
            ps.close();
            connection.close();
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    public void alterar(Integer numero, BigDecimal valor) {
        PreparedStatement ps;
        String sql = "UPDATE conta SET saldo = ? WHERE numero = ?";

        try {
            connection.setAutoCommit(false);

            ps = connection.prepareStatement(sql);

            ps.setBigDecimal(1, valor);
            ps.setInt(2, numero);

            ps.execute();
            ps.close();
            connection.close();
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    public void deletar(Integer numeroDaConta){
        String sql = "DELETE FROM conta WHERE numero = ?";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,numeroDaConta);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
