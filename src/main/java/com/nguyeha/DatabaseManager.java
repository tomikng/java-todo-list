package com.nguyeha;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static final String JDBC_URL = "jdbc:h2:./todolistdb";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "";

    public DatabaseManager() {
        initDatabase();
    }

    // Initialize the database and create tables if they don't exist
    private void initDatabase() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS tasks (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "title VARCHAR(255), " +
                    "dueDate DATE, " +
                    "priority VARCHAR(255), " +
                    "description TEXT, " +
                    "status BOOLEAN)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Insert a new task into the database
    public void insertTask(Task task) {
        String query = "INSERT INTO tasks (title, dueDate, priority, description, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, task.getTitle());
            preparedStatement.setDate(2, java.sql.Date.valueOf(task.getDueDate()));
            preparedStatement.setString(3, task.getPriority());
            preparedStatement.setString(4, task.getDescription());
            preparedStatement.setBoolean(5, task.isStatus());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Fetch all tasks from the database
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        String query = "SELECT * FROM tasks";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                LocalDate dueDate = resultSet.getDate("dueDate").toLocalDate();
                String priority = resultSet.getString("priority");
                String description = resultSet.getString("description");
                boolean status = resultSet.getBoolean("status");
                tasks.add(new Task(id, title, dueDate, priority, description, status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    // Edit an existing task
    public void editTask(Task task) {
        String query = "UPDATE tasks SET title = ?, dueDate = ?, priority = ?, description = ?, status = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, task.getTitle());
            preparedStatement.setDate(2, java.sql.Date.valueOf(task.getDueDate()));
            preparedStatement.setString(3, task.getPriority());
            preparedStatement.setString(4, task.getDescription());
            preparedStatement.setBoolean(5, task.isStatus());
            preparedStatement.setInt(6, task.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete a task by its ID
    public void deleteTask(int taskId) {
        String query = "DELETE FROM tasks WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, taskId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
