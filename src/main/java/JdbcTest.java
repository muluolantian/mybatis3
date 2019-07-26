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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/*
 * 文 件 名:  JdbcTest.java
 * 版    权:  Copyright 2016 咪咕互动娱乐有限公司,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  WangYong
 * 创建时间:  2018年3月3日
 
 */

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  WangYong
 * @version  [版本号, 2018年3月3日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class JdbcTest
{
    private static final String URL = "jdbc:oracle:thin:@192.168.129.147:1521:orcl";
    
    private static final String NAME = "cloudgame";
    
    private static final String PASSWORD = "cloudgame";
    
    public static void main(String[] args)
        throws Exception
    {
        //1.加载驱动程序
        Class.forName("oracle.jdbc.driver.OracleDriver");
        //2.获得数据库的连接
        Connection conn = DriverManager.getConnection(URL, NAME, PASSWORD);
        //3.通过数据库的连接操作数据库，实现增删改查
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select id from t_user_account_info where rownum =1");//选择import java.sql.ResultSet;
        while (rs.next())
        {//如果对象中有数据，就会循环打印出来
            System.out.println(rs.getLong("id"));
        }
    }
}
