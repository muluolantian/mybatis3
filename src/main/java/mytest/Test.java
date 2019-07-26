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
/**
 * 
 */
package mytest;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Proxy;
import java.util.Date;
import java.util.List;

/**
 * 描述：
 * 
 * @author wangyong
 * @since 2018年11月19日 下午1:00:24
 */
public class Test {

    public static void main(String args[]) throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = sqlSessionFactory.openSession();
        User query = new User();
        query.setId(100030419L);
        query.setMiId(3150044223L);
        //query.setMiId(3150044223L);
        try {
            Date date = new Date(1563453526954L);
            Date date1 = new Date(1563453526000L);
            List<User> list = session.selectList("mytest.UserDao.findUserByCondition", query);
            System.out.println(list);

/*            UserDao userMapper = session.getMapper(UserDao.class);
            List<User> list2 = userMapper.findUserByCondition(query);
            System.out.println(list2);*/
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            session.close();
        }


    }
}
