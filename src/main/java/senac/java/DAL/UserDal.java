package senac.java.DAL;


import senac.java.Domain.Users;

import senac.java.Services.ResponseEndPoints;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class UserDal {

    public static List<Users> usersList = new ArrayList<>();
    static ResponseEndPoints res = new ResponseEndPoints();


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


    //    Inserir - CREATE
    public int inserirUsuario(String name, String lastName, String email, String cpf) throws SQLException {
        String sql = "INSERT INTO Users(name, lastName, email, cpf) VALUES (?, ?, ?, ?)";

        int linhasAfetadas = 0;
        Connection conexao = conectar();


        try (PreparedStatement statement = conexao.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setString(3, email);
            statement.setString(4, cpf);

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

    public List listarUsuario() throws SQLException, IOException {
        String sql = "SELECT * FROM Users";
        ResultSet result = null;

        List<Users> userArray = new ArrayList<>();

        try (PreparedStatement statement = conectar().prepareStatement(sql)) {

            result = statement.executeQuery();

            System.out.println("Listagem dos usuarios: ");

            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");
                String lastName = result.getString("lastName");
                String email = result.getString("email");
                String cpf = result.getString("cpf");

                Users currentUser = new Users(id, name, lastName, email, cpf);
                userArray.add(currentUser);


                System.out.println("id: " + id);
                System.out.println("name: " + name);
                System.out.println("lastName: " + lastName);
                System.out.println("email: " + email);
                System.out.println("cpf: " + cpf);
                System.out.println(" ");
            }

            result.close();
            return userArray;

        } catch (SQLException e) {
            System.out.println("O erro na listagem de dados foi: " + e);
        }
        return userArray;
    }

    public int atualizarUsuario(int id, String name, String lastName, String email, String cpf) throws SQLException {
        String sql = "UPDATE Users SET name = ?, lastName =  ?, email = ?, cpf = ? WHERE id = ?";
        int linhasAfetadas = 0;

        try (PreparedStatement statement = conectar().prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setString(3, email);
            statement.setString(4, cpf);
            statement.setInt(5, id);

            linhasAfetadas = statement.executeUpdate();
            System.out.println("Foram afetadas " + linhasAfetadas + " no banco de dados");
            return linhasAfetadas;

        } catch (SQLException e) {
            System.out.println("O erro na listagem de dados foi: " + e);
        }
        return linhasAfetadas;
    }


    public int excluirUsuario(int id) throws SQLException {
        String sql = "DELETE FROM Users WHERE id = ?";
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
