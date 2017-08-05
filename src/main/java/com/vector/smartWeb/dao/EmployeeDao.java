package com.vector.smartWeb.dao;

import com.vector.smartWeb.model.Employee;

/**
 * Created by vector01.yao on 2017/8/4.
 */
public interface EmployeeDao {
    Employee selectByPrimaryKey(String employeeId);
}
