package com.vector.smartWeb.service;

import com.vector.smartWeb.annonation.Service;
import com.vector.smartWeb.model.Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by vector01.yao on 2017/7/30.
 */
@Service
public class CustomerService {

    public List<Customer> getCustomerList(){
        List<Customer> customerList=new ArrayList<>();
        Customer customer=new Customer();
        customer.setName("Tom");
        customer.setContact("kim");
        customer.setEmail("yyh6352@126.com");
        customer.setId(1);
        customer.setRemark("abc");
        customer.setTelephone("13127728317");
        customerList.add(customer);
        return customerList;
    }

    public Customer getCustomer(long id){
        return null;
    }

    public boolean createCustomer(long id, Map<String,Object> fieldMap){
        return true;
    }

    public boolean updateCustomer(long id,Map<String,Object> fieldMap){
        return false;
    }

    public boolean delecteCustomer(long id){
        return true;
    }
}
