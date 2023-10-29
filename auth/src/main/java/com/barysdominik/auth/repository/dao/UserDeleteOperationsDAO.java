package com.barysdominik.auth.repository.dao;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

@Component
public class UserDeleteOperationsDAO {


    public void deleteUserOperations(long userId) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
            PreparedStatement preparedStatement = null;

            String selectTutorialsSql = "SELECT id FROM tutorial WHERE author_id = ?";
            preparedStatement = connection.prepareStatement(selectTutorialsSql);
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();


            //kucharz ma id 4
            while (resultSet.next()) {
                long tutorialId = resultSet.getLong("id");
                String deleteTutorialMainIngredients = "DELETE FROM tutorials_ingredients WHERE tutorial_id = ?"; //poradnik kucharza to ma
                preparedStatement = connection.prepareStatement(deleteTutorialMainIngredients);
                preparedStatement.setLong(1, tutorialId);
                preparedStatement.execute();
            }

            String deleteTutorialSql = "DELETE FROM tutorial WHERE author_id = ?"; //kucharz to ma
            preparedStatement = connection.prepareStatement(deleteTutorialSql);
            preparedStatement.setLong(1, userId);
            preparedStatement.execute();

            String deleteUserIngredientsSql = "DELETE FROM user_ingredients WHERE user_id = ?"; //kucharz to ma
            preparedStatement = connection.prepareStatement(deleteUserIngredientsSql);
            preparedStatement.setLong(1, userId);
            preparedStatement.execute();

            String deleteUserNotificationsSql = "DELETE FROM notification WHERE receiver_id = ?";
            preparedStatement = connection.prepareStatement(deleteUserNotificationsSql);
            preparedStatement.setLong(1, userId);
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
