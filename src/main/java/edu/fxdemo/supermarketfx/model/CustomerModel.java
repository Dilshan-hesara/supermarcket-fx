
/*
 * Copyright (c) 2024 Dilshan Hesara
 * Author: Dilshan Hesara
 * GitHub: https://github.com/Dilshan-hesara
 *
 * All Rights Reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to use
 * the Software for **personal or educational purposes only**, subject to the following conditions:
 *
 * - The Software may **not be sold** for commercial purposes.
 * - The Software may **not be modified** or altered in any way.
 * - Redistribution of this Software is permitted, provided that the original
 *   copyright notice and this permission notice appear in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE, AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES, OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT, OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package edu.fxdemo.supermarketfx.model;

import edu.fxdemo.supermarketfx.db.DBConnection;
import edu.fxdemo.supermarketfx.dto.CustomerDto;
import edu.fxdemo.supermarketfx.dto.TM.CustomerTM;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

//import static edu.fxdemo.supermarketfx.controller.cust_con.CUSTOMER_MODEL;

public class CustomerModel {


    private static final CustomerModel CUSTOMER_MODEL = new CustomerModel();

    public String getNextCustomerId() throws SQLException {

        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "select customer_id from customer order by customer_id desc limit 1";
        PreparedStatement pst = connection.prepareStatement(sql);
        ResultSet rst = pst.executeQuery();

        if (rst.next()){
            String lastId = rst.getString(1); // C002
            String substring = lastId.substring(1); // 002
            int i = Integer.parseInt(substring); // 2
            int newIdIndex = i+1; // 3
//            String newId = ; // C003
            return String.format("C%03d",newIdIndex);
        }
        return  "C001";
    }
    public boolean saveCustomer(CustomerDto customerDTO) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "insert into customer values (?,?,?,?,?)";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setObject(1,customerDTO.getCustomerId());
        pst.setObject(2,customerDTO.getName());
        pst.setObject(3,customerDTO.getNic());
        pst.setObject(4,customerDTO.getEmail());
        pst.setObject(5,customerDTO.getPhone());
        int result = pst.executeUpdate();
        boolean isSaved = result>0;
        return isSaved;
    }
    public ArrayList<CustomerTM> getAllCustomer() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM customer";
        PreparedStatement statement = connection.prepareStatement(sql);

        ResultSet rst = statement.executeQuery();
        ArrayList<CustomerTM> customerTMs = new ArrayList<>(); // Use CustomerTM instead of CustomerDto

        while (rst.next()) {
            // Create CustomerTM object and set its fields based on ResultSet data
            CustomerTM customerTM = new CustomerTM();
            customerTM.setCustId(rst.getString("customer_id"));
            customerTM.setCustName(rst.getString("name"));
            customerTM.setCustNic(rst.getString("nic"));
            customerTM.setCustEmail(rst.getString("email"));
            customerTM.setCustPhone(rst.getString("phone"));

            // Add the CustomerTM object to the list
            customerTMs.add(customerTM);
        }
        return customerTMs;
    }



}
