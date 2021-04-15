package io.gamefreak.pixelmonextension.storage;

import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import io.gamefreak.pixelmonextension.Pixelmonextension;
import io.gamefreak.pixelmonextension.token.TokenTypes;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.sql.SqlService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

public class Database {


    private Connection getConnection() {
        DataSource dbSource = null;
        try {
            dbSource = Sponge.getServiceManager().provide(SqlService.class).get().getDataSource("jdbc:h2:" + Pixelmonextension.INSTANCE.configDir.resolve("data"));
            Connection connection = dbSource.getConnection();
            //TODO: TABLE PREFIX
            boolean pc = connection.getMetaData().getTables(null, null, "UNCLAIMEDPIXELMONTOKENS", null).next();

            if (!pc) {
                connection.prepareStatement("CREATE TABLE UNCLAIMEDPIXELMONTOKENS(ID INTEGER NOT NULL AUTO_INCREMENT,PRIMARY KEY(ID), UUID VARCHAR(50), TokenType VARCHAR(30) ,amount INTEGER)").executeUpdate();
            }

            boolean CR = connection.getMetaData().getTables(null, null, "CATCHRATES", null).next();

            if (!CR) {
                connection.prepareStatement("CREATE TABLE CATCHRATES(ID INTEGER NOT NULL AUTO_INCREMENT,PRIMARY KEY(ID), species VARCHAR(50), rate INTEGER)").executeUpdate();
            }
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void UpdateToken() {
        Map<UUID, Map<TokenTypes.TokenName, Integer>> tokens = Pixelmonextension.registry.getPixelmonTokens();

        Connection connection = getConnection();
        if (getConnection() != null) {
            for (Map.Entry<UUID, Map<TokenTypes.TokenName, Integer>> entry : tokens.entrySet()) {
                UUID uuid = entry.getKey();
                for (Map.Entry<TokenTypes.TokenName, Integer> entry1 : entry.getValue().entrySet()) {
                    try {
                        PreparedStatement statement = connection.prepareStatement("SELECT * FROM UNCLAIMEDPIXELMONTOKENS WHERE UUID = ? AND TokenType = ?");
                        statement.setString(1, uuid.toString());
                        statement.setString(2, entry1.getKey().name());
                        statement.setInt(3, entry1.getValue());
                        ResultSet results = statement.executeQuery();
                        boolean exists = results.next();
                        if (!exists) {
                            PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO UNCLAIMEDPIXELMONTOKENS(UUID,TokenType,amount) VALUES(?,?,?)");
                            insertStatement.setString(1, uuid.toString());
                            insertStatement.setString(2, entry1.getKey().name());
                            insertStatement.setInt(3, entry1.getValue());

                            insertStatement.executeUpdate();
                        } else {
                            PreparedStatement uState = connection.prepareStatement("UPDATE UNCLAIMEDPIXELMONTOKENS SET amount = ? WHERE UUID = ? AND TokenType = ?");
                            uState.setInt(1, entry1.getValue());
                            uState.setString(2, uuid.toString());
                            uState.setString(3, entry1.getKey().name());
                            uState.executeUpdate();

                        }


                    } catch (SQLException ex) {
                        try {
                            connection.close();
                        } catch (SQLException throwables) {
                            Pixelmonextension.INSTANCE.logger.error("Could not close connection");
                        }
                    }
                    //create or update

                }
            }
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else {
            Pixelmonextension.INSTANCE.logger.error("Could not insert data, connection could not be created");
        }

    }


    public void UpdateTokenOfUuid(UUID uuid) {
        Map<TokenTypes.TokenName, Integer> tokens = Pixelmonextension.registry.getUnclaimedTokens(uuid);
        Connection connection = getConnection();
        if (connection != null) {
            for (Map.Entry<TokenTypes.TokenName, Integer> entry1 : tokens.entrySet()) {
                try {

                    PreparedStatement statement = connection.prepareStatement("SELECT * FROM UNCLAIMEDPIXELMONTOKENS WHERE UUID = ? AND TokenType = ?");
                    statement.setString(1, uuid.toString());
                    statement.setString(2, entry1.getKey().name());
                    ResultSet results = statement.executeQuery();
                    boolean exists = results.next();
                    if (!exists) {
                        PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO UNCLAIMEDPIXELMONTOKENS(UUID,TokenType,amount) VALUES(?,?,?)");
                        insertStatement.setString(1, uuid.toString());
                        insertStatement.setString(2, entry1.getKey().name());
                        insertStatement.setInt(3, entry1.getValue());
                        insertStatement.executeUpdate();
                    } else {
                        PreparedStatement uState = connection.prepareStatement("UPDATE UNCLAIMEDPIXELMONTOKENS SET amount = ? WHERE UUID = ? AND TokenType = ?");
                        uState.setInt(1, entry1.getValue());
                        uState.setString(2, uuid.toString());
                        uState.setString(3, entry1.getKey().name());
                        uState.executeUpdate();
                    }

                } catch (SQLException ex) {
                    try {
                        ex.printStackTrace();
                        connection.close();
                    } catch (SQLException throwables) {
                        Pixelmonextension.INSTANCE.logger.error("Could not close connection");
                    }
                }
                //create or update

            }

            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        } else {
            Pixelmonextension.INSTANCE.logger.error("Could not insert data, connection could not be created");
        }


    }

    public void ReadTokens() {

        try {

            Connection connection = getConnection();
            ResultSet tokens = connection.prepareStatement("SELECT * FROM UNCLAIMEDPIXELMONTOKENS").executeQuery();

            while (tokens.next()) {
                TokenTypes.TokenName name = TokenTypes.getTokenNameFromString(tokens.getString("TokenType"));
                int amount = tokens.getInt("amount");
                UUID uuid = UUID.fromString(tokens.getString("UUID"));
                Pixelmonextension.registry.addToken(uuid, name, amount);
            }
            connection.close();
        } catch (SQLException throwables) {
            Pixelmonextension.INSTANCE.logger.error("Could not create connection");
        }


    }


    public void UpdateCatchrate() {
        Map<EnumSpecies, Integer> catchrates = Pixelmonextension.registry.getCatchrates();
        Connection connection = getConnection();
        if (connection != null) {
            for (Map.Entry<EnumSpecies, Integer> entry : catchrates.entrySet()) {
                try {

                    PreparedStatement statement = connection.prepareStatement("SELECT * FROM CATCHRATES WHERE species = ? AND rate = ?");
                    statement.setString(1, entry.getKey().name);
                    statement.setInt(2, entry.getValue());
                    ResultSet results = statement.executeQuery();
                    boolean exists = results.next();
                    if (!exists) {
                        PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO CATCHRATES(species,rate) VALUES(?,?)");
                        insertStatement.setString(1, entry.getKey().name);
                        insertStatement.setInt(2, entry.getValue());
                        insertStatement.executeUpdate();
                    } else {
                        PreparedStatement uState = connection.prepareStatement("UPDATE CATCHRATES SET rate = ? WHERE species = ?");
                        uState.setInt(1, entry.getValue());
                        uState.setString(2, entry.getKey().name);
                        uState.executeUpdate();
                    }

                } catch (SQLException ex) {
                    try {
                        ex.printStackTrace();
                        connection.close();
                    } catch (SQLException throwables) {
                        Pixelmonextension.INSTANCE.logger.error("Could not close connection");
                    }
                }
                //create or update

            }

            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        } else {
            Pixelmonextension.INSTANCE.logger.error("Could not insert data, connection could not be created");
        }

    }


    public void ReadCatchrates() {

        try {

            Connection connection = getConnection();
            ResultSet rates = connection.prepareStatement("SELECT * FROM CATCHRATES").executeQuery();

            while (rates.next()) {
                EnumSpecies species = EnumSpecies.getFromName(rates.getString("species")).get();
                int rate = rates.getInt("rate");
                if(species != null){
                    Pixelmonextension.registry.addCatchrate(species,rate);
                }
            }
            connection.close();
        } catch (SQLException throwables) {

            Pixelmonextension.INSTANCE.logger.error("Could not create connection");
        }


    }

}
