package senac.java.DAL;

import senac.java.Domain.Products;
import senac.java.Services.ConexaoSQLServer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductDal {

    public Connection conectar() {
        Connection conexao = null;

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://localhost:1433;databaseName=pi;trustServerCertificate=true";
            String usuario = "user";
            String senha = "123456";

            conexao = DriverManager.getConnection(url, usuario, senha);

            if (conexao != null) {
                System.out.println("Conexão com o banco feita com sucesso");
                return conexao;

            }

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("O erro foi: " + e);
        }
        return conexao;
    }


    public int inserirProdutos(String name, String factory, String quantity) throws SQLException {
//        Aqui eu estou criando a minha query para inserir os valores no banco
        String sql = "INSERT INTO Products(name, factory, quantity) VALUES (?, ?, ?)";

        int linhasAfetadas = 0;
        Connection conexao = conectar();

//      O PreparedStatement faz a troca dos meus numeros pela informações
        try (PreparedStatement statement = conexao.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, factory);
            statement.setString(3, quantity);

            linhasAfetadas = statement.executeUpdate();

            System.out.println("Foram afetadas " + linhasAfetadas + " no banco de dados");

            conexao.close();
            return linhasAfetadas;


        } catch (SQLException e) {
            System.out.println("O erro na inserção de dados foi: " + e);
            conexao.close();
        }

        conexao.close();
        return linhasAfetadas;
    }


    public List listarProdutos() throws SQLException {
        String sql = "SELECT * FROM Products";
        ResultSet result = null;

        List<Products> productsArray = new ArrayList<>();
        try (PreparedStatement statement = conectar().prepareStatement(sql)) {
            result = statement.executeQuery();

            System.out.println("Listagem dos usuarios: ");

            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");
                String factory = result.getString("factory");
                String quantity = result.getString("quantity");

                Products currentProducts = new Products(id, name, factory, quantity);
                productsArray.add(currentProducts);

                System.out.println("id: " + id);
                System.out.println("name: " + name);
                System.out.println("factory: " + factory);
                System.out.println("quantity: " + quantity);
                System.out.println(" ");

            }
            result.close();
            return productsArray;

        } catch (SQLException e) {
            System.out.println("O erro na listagem de dados foi: " + e);
        }
        return productsArray;
    }


    public int atualizarProdutos(int id, String name, String factory, String quantity) throws SQLException {
        String sql = "UPDATE Products SET name = ?, factory =  ?, quantity = ? WHERE id = ?";
        int linhasAfetadas = 0;

        try (PreparedStatement statement = conectar().prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, factory);
            statement.setString(3, quantity);
            statement.setInt(4, id);

            linhasAfetadas = statement.executeUpdate();
            System.out.println("Foram afetadas " + linhasAfetadas + " no banco de dados");
            return linhasAfetadas;

        } catch (SQLException e) {
            System.out.println("O erro na listagem de dados foi: " + e);
        }
        return linhasAfetadas;
    }


    public int excluirProdutos(int id) throws SQLException {
        String sql = "DELETE FROM Products WHERE id = ?";
        int linhasAfetadas = 0;

        try (PreparedStatement statement = conectar().prepareStatement(sql)) {
            statement.setInt(1, id);

            linhasAfetadas = statement.executeUpdate();
            System.out.println("Foram afetadas " + linhasAfetadas + " no banco de dados");

            return linhasAfetadas;

        } catch (SQLException e) {
            System.out.println("O erro na exclusão de dados foi: " + e);
        }

        return linhasAfetadas;
    }


}
