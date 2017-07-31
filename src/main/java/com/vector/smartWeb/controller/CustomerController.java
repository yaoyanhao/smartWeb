package com.vector.smartWeb.controller;

import com.vector.smartWeb.annonation.Action;
import com.vector.smartWeb.annonation.Controller;
import com.vector.smartWeb.annonation.Inject;
import com.vector.smartWeb.bean.View;
import com.vector.smartWeb.model.Customer;
import com.vector.smartWeb.service.CustomerService;

import java.util.Date;
import java.util.List;

/**
 * Created by vector01.yao on 2017/7/30.
 * 客户管理
 */
@Controller
public class CustomerController {

    @Inject
    private CustomerService customerService;

    @Action("get:/customer")
    public View customer(){
        List<Customer> customerList=customerService.getCustomerList();
        return new View("customer.jsp").addModel("customerList",customerList);
    }

    @Action("get:/")
    public View index(){
        List<Customer> customerList=customerService.getCustomerList();
        return new View("hello.jsp").addModel("currentTime",new Date());
    }
}
