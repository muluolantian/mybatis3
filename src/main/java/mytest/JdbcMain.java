/**
 *    Copyright ${license.git.copyrightYears} the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package mytest;

import org.apache.ibatis.annotations.Result;

import java.sql.*;

/**
 * Created by Yong.Wang50 on 2019/7/25.
 */
public class JdbcMain {
    public static void main(String args[]) throws Exception{

        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection(
                "jdbc:mysql://10.38.164.94:3306/browser?characterEncoding=UTF-8",
                "root", "miuiservermysql");

        PreparedStatement preparedStatement = conn.prepareStatement(
                "SELECT id, mi_id from reader2_user where mi_id = ?");
        preparedStatement.setLong(1,3150044223L);
        preparedStatement.execute();
        ResultSet rs = preparedStatement.getResultSet();
        while (rs.next()){
         long id =    rs.getLong("id");
          long miId =   rs.getLong("mi_id");
          System.out.print("id="+id+" ,miId="+miId);
        }
        // 5. 释放资源
        rs.close();
        preparedStatement.close();
        conn.close();
    }
}
