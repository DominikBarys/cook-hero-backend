package com.barysdominik.notificationservice.repository.dao;

import com.barysdominik.notificationservice.entity.notification.Notification;
import com.barysdominik.notificationservice.repository.NotificationRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class UserIngredientDao {

    private final NotificationRepository notificationRepository;
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void checkExpiredIngredients(Connection connection) throws SQLException {
        String sql = "SELECT short_id, user_id, expiration_date FROM user_ingredients WHERE expirationDate < ?";
        LocalDate currentDate = LocalDate.now();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDate(1, java.sql.Date.valueOf(currentDate));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String shortId = resultSet.getString("short_id");
                    String userId = resultSet.getString("user_id");
                    LocalDate expirationDate = resultSet.getDate("expiration_date").toLocalDate();

                    System.out.println("Składnik o short id: " + shortId + " należący do usera o id: " + userId + " już się przeterminował");
                }
            }
        }
    }

    private void checkIngredientsNearExpiration(Connection connection) throws SQLException {
        String sql = "SELECT short_id, user_id, expiration_date FROM user_ingredients WHERE expirationDate <= ?";
        LocalDate twoDaysFromNow = LocalDate.now().plusDays(2);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDate(1, java.sql.Date.valueOf(twoDaysFromNow));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String shortId = resultSet.getString("short_id");
                    String userId = resultSet.getString("user_id");
                    LocalDate expirationDate = resultSet.getDate("expiration_date").toLocalDate();

                    if(!(LocalDate.now().isAfter(expirationDate))) {
                        System.out.println("Składnik o short id: " + shortId + " należący do usera o id: " + userId + " za niedługo się przeterminuje");
                    }

                }
            }
        }
    }

}
