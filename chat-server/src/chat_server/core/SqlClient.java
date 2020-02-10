package chat_server.core;

import java.sql.*;

public class SqlClient {

    private static Connection connection;// соединение с базой
    private static Statement statement;  //позволяет отправлять запрос в базу

    public synchronized static void connect(){
        try{
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:chat-server/chat.db");
            statement = connection.createStatement();
        }catch (ClassNotFoundException | SQLException e){
            throw new RuntimeException(e);
        }
    }
    public synchronized static void disconnect() {

        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // throw new RuntimeException(e);
        }
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //метод который отправляет запрос о создании нового пользователя
    public synchronized static void addNewUser(String login, String password, String nickname){
        try {
            statement.executeUpdate(String.format("INSERT INTO users (login, password,nickname) VALUES ('%s','%s','%s')",login,password,nickname));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized static void changeNickName(String login, String password, String nickname){
        try {
            statement.executeUpdate(String.format("SELECT   VALUES ('%s','%s','%s')",login,password,nickname));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public synchronized static String getNickname(String login, String password){
        String query = String.format("select nickname from users where login = '%s' and password = '%s'",login,password);
        try(ResultSet set = statement.executeQuery(query)) { //при успехе вернет result set
          if (set.next()) //если у сета есть что-то, не пустой он, то
              return set.getString(1);//надо взять из SQL строковое значение
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
