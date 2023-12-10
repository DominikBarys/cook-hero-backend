package com.barysdominik.notificationservice.repository.dao;

import com.barysdominik.notificationservice.exception.IngredientNotFoundException;
import com.barysdominik.notificationservice.exception.UserNotFoundException;
import com.barysdominik.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class UserIngredientDao {

    private final NotificationService notificationService;
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String user;
    @Value("${spring.datasource.password}")
    private String password;


    public void checkUsersIngredientsExpirationDates() {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            checkExpiredIngredients(connection);
            checkIngredientsNearExpiration(connection);
        } catch (SQLException | IngredientNotFoundException | UserNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void checkExpiredIngredients(Connection connection)
            throws SQLException, IngredientNotFoundException, UserNotFoundException {
        String sql = "SELECT ingredient_id, quantity, user_id, expiration_date FROM user_ingredients WHERE expiration_date < ?";
        LocalDate currentDate = LocalDate.now();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDate(1, java.sql.Date.valueOf(currentDate));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    long ingredientId = resultSet.getLong("ingredient_id");
                    int quantity = resultSet.getInt("quantity");
                    long userId = resultSet.getLong("user_id");
                    LocalDate expirationDate = resultSet.getDate("expiration_date").toLocalDate();
                    String ingredientName = getIngredientName(connection, ingredientId);
                    if(ingredientName == null) {
                        throw new IngredientNotFoundException("Ingredient with id: '" + ingredientId + "' does not exist in database");
                    }
                    notificationService.createExpiredNotification(userId, quantity, ingredientName, expirationDate);
                }
            }
        }
    }

    private void checkIngredientsNearExpiration(Connection connection) throws SQLException {
        String sql = "SELECT ingredient_id, quantity, user_id, expiration_date FROM user_ingredients WHERE expiration_date <= ?";
        LocalDate twoDaysFromNow = LocalDate.now().plusDays(2);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDate(1, java.sql.Date.valueOf(twoDaysFromNow));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    long ingredientId = resultSet.getLong("ingredient_id");
                    int quantity = resultSet.getInt("quantity");
                    long userId = resultSet.getLong("user_id");
                    LocalDate expirationDate = resultSet.getDate("expiration_date").toLocalDate();
                    String ingredientName = getIngredientName(connection, ingredientId);

                    if(!(LocalDate.now().isAfter(expirationDate))) {
                        notificationService.createCloseToExpirationNotification(
                                userId,
                                quantity,
                                ingredientName,
                                expirationDate
                        );
                    }
                }
            }
        }
    }

    private String getIngredientName(Connection connection, long ingredientId) throws SQLException{
        String selectIngredientNameSql = "SELECT name FROM ingredient WHERE id = ?";
        try(PreparedStatement ingredientPreparedStatement = connection.prepareStatement(selectIngredientNameSql)) {
            ingredientPreparedStatement.setLong(1, ingredientId);
            try(ResultSet ingredientResultSet = ingredientPreparedStatement.executeQuery()) {
                if(ingredientResultSet.next()) {
                    return ingredientResultSet.getString("name");
                }
                return null;
            }
        }
    }

}
