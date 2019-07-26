
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
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import mytest.User;

/*
 * 文 件 名:  MyTest.java
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
public class MyTest
{
    
    /**
     * <一句话功能简述>
     * <功能详细描述>
     * @param args
     * @throws IOException 
     * @throws ClassNotFoundException 
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("unused")
    public static void main(String[] args)
        throws IOException, ClassNotFoundException
    {
        //StaticSqlSource DynamicSqlSource  XMLScriptBuilder IfSqlNode DefaultResultSetHandler
        Class.forName("oracle.jdbc.driver.OracleDriver");
        /* 
         * 1.加载mybatis的配置文件，初始化mybatis，创建出SqlSessionFactory，是创建SqlSession的工厂 
         * 这里只是为了演示的需要，SqlSessionFactory临时创建出来，在实际的使用中，SqlSessionFactory只需要创建一次，当作单例来使用 
         */
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        SqlSessionFactory factory = builder.build(inputStream);
        
        //2. 从SqlSession工厂 SqlSessionFactory中创建一个SqlSession，进行数据库操作  
        SqlSession sqlSession = factory.openSession();
        Configuration configuration = sqlSession.getConfiguration();
        
        //SqlSource Interceptor Configuration
        //3.使用SqlSession查询  
        Map<String, Object> params = new HashMap<String, Object>();
        
        /*        params.put("userId", 103L);
        Long time1 = System.currentTimeMillis();
        List<Object> result = sqlSession.selectList("mytest.UserDao.findUserById2", params);
        System.out.println(result);
        Long time2 = System.currentTimeMillis();
        System.out.println(time2 - time1);*/
        
        User user = new User();
        //     user.setUserId(103L);
        //user.setNickname("");
        List<Object> result100 = sqlSession.selectList("mytest.UserDao.queryUserAccountInfos", user);
        System.out.println(result100);
        
        //   params.put("userId", 338L);
        /*        params.put("nickname", "test");
        int num = sqlSession.update("mytest.UserDao.updateUser", params);
        
        System.out.println("num=" + num);
              //  sqlSession.commit();*/
        List<Object> result2 = sqlSession.selectList("mytest.UserDao.findUserById", params);
        Long time3 = System.currentTimeMillis();
        //       System.out.println(time3 - time2);
        
        //  params.put("id", 339L);
        List<Object> result3 = sqlSession.selectList("mytest.UserDao.findUserById", params);
        //   System.out.println(result2);
        Long time4 = System.currentTimeMillis();
        System.out.println(time4 - time3);
        
        /*        Map<String, Object> params2 = new HashMap<String, Object>();
        
        User user = new User();
        user.setGroupId(134L);
        params2.put("user", user);
        List<Object> list = sqlSession.selectList("mytest.UserDao.queryUserAccountInfos", params2);
        for (int i = 0; i < list.size(); i++)
        {
            System.out.println(list.get(i));
        }*/
        
    }
    
}
