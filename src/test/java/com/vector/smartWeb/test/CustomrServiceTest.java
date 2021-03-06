package com.vector.smartWeb.test;

import com.vector.smartWeb.model.Employee;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vector01.yao on 2017/7/30.
 */
public class CustomrServiceTest {

    @Test
    public void testMybatis() throws IOException {
         /*
         * 1.加载mybatis的配置文件，初始化mybatis，创建出SqlSessionFactory，是创建SqlSession的工厂
         * 这里只是为了演示的需要，SqlSessionFactory临时创建出来，在实际的使用中，SqlSessionFactory只需要创建一次，当作单例来使用
         */
        InputStream inputStream = Resources.getResourceAsStream("MybatisConfig.xml");
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        SqlSessionFactory factory = builder.build(inputStream);

        //2. 从SqlSession工厂 SqlSessionFactory中创建一个SqlSession，进行数据库操作
        SqlSession sqlSession = factory.openSession();

        //3.使用SqlSession查询
        Map<String,Object> params = new HashMap<String,Object>();

        params.put("employeeId",100);
        //a.查询工资低于10000的员工
        List<Employee> result = sqlSession.selectList("com.vector.smartWeb.dao.EmployeeDao.selectByPrimaryKey",params);

    }
}
