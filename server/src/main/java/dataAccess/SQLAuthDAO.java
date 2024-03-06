package dataAccess;

import model.AuthData;
import model.UserData;

import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLAuthDAO implements AuthDAO{

    public SQLAuthDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public String createAuth(AuthData authData) throws DataAccessException {
        var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
        SQLHelper.executeUpdate(statement, authData.authToken(), authData.username());
        return authData.authToken();
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, username FROM auth WHERE authToken=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    while(rs.next()){
                        return new AuthData(rs.getString(1), rs.getString(2));
                    }
                    return null;
                }
            }
        } catch (Exception e) {
            throw new DataAccessException( String.format("Unable to read data: %s", e.getMessage()));
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        var statement = "DELETE FROM auth WHERE authToken=?";
        SQLHelper.executeUpdate(statement,authToken);
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE auth";
        SQLHelper.executeUpdate(statement);
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS auth (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws DataAccessException {
//        try (var conn = DatabaseManager.getConnection()) {
//            var statement = "SELECT COUNT(*) AS count FROM information_schema.SCHEMATA WHERE SCHEMA_NAME = ?";
//            try (var ps = conn.prepareStatement(statement)) {
//                ps.setString(1, "chess");
//                try (var rs = ps.executeQuery()) {
//                    if(rs.getInt(1) == 0){
//                        DatabaseManager.createDatabase();
//                    }
//                }
//            }
//        } catch (Exception e) {
//            throw new DataAccessException( String.format("Unable to read data: %s", e.getMessage()));
//        }
        SQLHelper.configureDatabase(createStatements);
    }
}

