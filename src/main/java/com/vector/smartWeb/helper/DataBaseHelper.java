package com.vector.smartWeb.helper;

import com.vector.smartWeb.util.CollectionUtil;
import com.vector.smartWeb.util.PropsUtil;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by vector01.yao on 2017/7/30.
 * 数据库操作助手类
 */
public final class DataBaseHelper {
    private static final Logger LOGGER= LoggerFactory.getLogger(DataBaseHelper.class);

    private static final ThreadLocal<Connection> CONNECTION_HOLDER;

    private static final QueryRunner QUERY_RUNNER;

    private static final BasicDataSource DATA_SOURCE;

    static {
        CONNECTION_HOLDER=new ThreadLocal<Connection>();

        QUERY_RUNNER=new QueryRunner();

        Properties dbConfig= PropsUtil.loadProps("db.properties");
        DATA_SOURCE=new BasicDataSource();
        DATA_SOURCE.setDriverClassName(dbConfig.getProperty("jdbc.driver"));
        DATA_SOURCE.setUrl(dbConfig.getProperty("jdbc.url"));
        DATA_SOURCE.setUsername(dbConfig.getProperty("jdbc.username"));
        DATA_SOURCE.setPassword(dbConfig.getProperty("jdbc.password"));
    }

    /**
     * 获取数据库连接
     */
    public static Connection getConnection(){
        Connection conn=CONNECTION_HOLDER.get();
        if (conn==null){
            try {
                //conn= DriverManager.getConnection(URL,USERNAME,PASSWORD);
                conn=DATA_SOURCE.getConnection();
            } catch (SQLException e) {
                LOGGER.error("get connection failure",e);
            } finally {
                CONNECTION_HOLDER.set(conn);
            }
        }
        return conn;
    }

    public static void closeConnection(){
        Connection conn=CONNECTION_HOLDER.get();
        if (conn!=null){
            try {
                conn.close();
            }catch (SQLException e) {
                LOGGER.error("close connection failure",e);
                throw new RuntimeException(e);
            }finally {
                CONNECTION_HOLDER.remove();
            }
        }
    }

    /**
     * 查询实体列表
     * @param entityClass 实体类
     * @param sql sql语句
     * @param params 参数
     * @return
     */
    public static <T> List<T> queryEntityList(Class<T> entityClass,String sql,Object... params){
        List<T> entityList;
        Connection connection=getConnection();
        try {
            entityList=QUERY_RUNNER.query(connection,sql,new BeanListHandler<T>(entityClass),params);
        } catch (SQLException e) {
            LOGGER.error("query entity list failure",e);
            throw new RuntimeException(e);
        }
        return entityList;
    }


    /**
     * 查询单个实体
     * @param entityClass 实体类
     * @param sql sql语句
     * @param params 参数
     * @return
     */
    public static <T> T queryEntity(Class<T> entityClass,String sql,Object... params){
        T entity;
        Connection connection=getConnection();
        try {
            entity=QUERY_RUNNER.query(connection,sql,new BeanHandler<T>(entityClass),params);
        } catch (SQLException e) {
            LOGGER.error("query entity list failure",e);
            throw new RuntimeException(e);
        }
        return entity;
    }

    /**
     * 执行更新语句（包括update、insert、delete）
     * @param sql
     * @param params
     * @return
     */
    public static int executeUpdate(String sql,Object... params){
        int rows=0;
        Connection connection=getConnection();
        try {
            rows=QUERY_RUNNER.update(connection,sql,params);
        } catch (SQLException e) {
            LOGGER.error("execute update failure",e);
            throw new RuntimeException(e);
        }
        return rows;
    }

    /**
     * 插入实体
     */
    public static <T> boolean insertEntity(Class<T> entityClass,Map<String,Object> fieldMap){
        if (CollectionUtil.isEmpty(fieldMap)){
            LOGGER.error("can not insert entity:fieldMap is empty");
            return false;
        }

        String sql="insert into "+getTableName(entityClass);
        StringBuilder columns=new StringBuilder("(");
        StringBuilder values=new StringBuilder("(");
        for (String fieldName:fieldMap.keySet()){
            columns.append(fieldName).append(", ");
            values.append("?, ");
        }
        columns.replace(columns.lastIndexOf(", "),columns.length(),")");
        values.replace(values.lastIndexOf(", "),values.length(),")");
        sql+=columns+" VALUES "+values;

        Object[] params=fieldMap.values().toArray();
        return executeUpdate(sql,params)==1;
    }

    /**
     * 更新实体
     * @param entityClass 实体类
     * @param id id
     * @param fieldMap 属性map
     * @param <T>
     * @return
     */
    public static <T> boolean updateEntity(Class<T> entityClass,long id,Map<String,Object> fieldMap){
        if (CollectionUtil.isEmpty(fieldMap)){
            LOGGER.error("can not update entity:fieldMap is empty");
            return false;
        }

        String sql="update "+getTableName(entityClass)+" set";
        StringBuilder columns=new StringBuilder();
        for (String fieldName:fieldMap.keySet()){
            columns.append(fieldName).append("=?, ");
        }
        sql+=columns.substring(0,columns.lastIndexOf(", "))+" where id=?";

        List<Object> paramList=new ArrayList<Object>();
        paramList.addAll(fieldMap.values());
        paramList.add(id);
        Object[] params=paramList.toArray();

        return executeUpdate(sql,params)==1;
    }

    public static <T> boolean deleteEntity(Class<T> entityClass,long id){
        String sql="delete from "+ getTableName(entityClass)+" where id=?";
        return executeUpdate(sql,id)==1;
    }

    private static String getTableName(Class<?> entityClass){
        return entityClass.getSimpleName();
    }
}
