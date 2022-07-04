package com.edw;

import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "MySqlServlet", urlPatterns = "/mysql")
public class MySqlServlet extends HttpServlet {

    private DataSource dataSource;

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:/datasources/MysqlDS");
            connection = dataSource.getConnection();

            statement = connection.prepareStatement("select * from this_table");
            statement.execute();
            resultSet = statement.getResultSet();

            while (resultSet.next()) {
                out.println(resultSet.getString(1));
                out.println("<br />");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if(connection != null)
                    connection.close();
                if(statement != null)
                    statement.close();
                if(resultSet != null)
                    resultSet.close();
            }catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}