package com.lmg.mybatis.dao;

import com.lmg.mybatis.bean.Employee;

public interface EmployeeMapper {
	
	public Employee getEmpById(Integer id);
}
