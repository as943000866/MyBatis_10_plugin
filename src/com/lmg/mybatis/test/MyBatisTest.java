package com.lmg.mybatis.test;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import com.lmg.mybatis.bean.Employee;
import com.lmg.mybatis.dao.EmployeeMapper;


public class MyBatisTest {
	
	private SqlSessionFactory getSqlSessionFactory() throws IOException {
		String resource = "mybatis-conf.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		return sqlSessionFactory;
	}
	/**
	 * 1.获取sqlSessionFactory对象
	 * 		解析文件的每个信息保存在 Configuration中,返回包含 Cofiguration 的 DefaultSqlSessionFactory
	 * 		注意:【MappedStatement】: 代表一个增删改查的详细信息
	 * 2.获取sqlSession对象
	 * 		返回一个DefaultSQLSession对象,包含Executor和Configuration;
	 * 		这一步会创建Executor对象
	 * 3.获取接口的代理对象(MapperProxy)
	 * 		getMapper,使用MapperProxyFactory创建一个MapperProxy的代理对象
	 * 		代理对象里面包含了,DefaultSqlSession(Executor)
	 * 4.执行增删改查方法
	 * 
	 * 总结:
	 * 	 1.根据配置文件 (全局,sql映射) 初始化Configuration对象
	 * 	 2.创建一个DefaultSqlSession对象
	 * 		他里面包含Configuration以及
	 * 		Executor (根据全局配置文件中的defaultExecutorType创建对应的Executor)
	 * 	 3.DefaultSqlSession.getMapper(): 拿到Mapper接口对应的MapperProxy
	 * 	 4.MapperProxy里面有(DefaultSqlSession)
	 * 	 5.执行增删改查
	 * 			1).调用DefaultSqlSession的增删改查(Executor)
	 * 			2).会创建一个StatementHandle对象
	 * 				(同时也会创建出ParameterHandler和ResultSetHandler)
	 * 			3).调用StatementHandler预编译参数以及设置参数值;
	 * 				使用ParameterHandler来给Sql设置参数
	 * 			4).调用StatementHandle的增删改查方法
	 * 			5).ResultSetHandler封装结果
	 *   注意:
	 *   	四大对象每个创建的时候都有一个interceptorChain.pluginAll(parameterHandler);
	 *
	 * @throws IOException
	 */
	@Test
	public void test01() throws IOException{
		//1. 获取sqlSessionFactory对象
		SqlSessionFactory sessionFactory = getSqlSessionFactory();
		
		//2. 获取sqlSession对象
		SqlSession session = sessionFactory.openSession();
		
		try {
			
			//3.获取接口的实现类对象
			EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
			
			Employee employee = mapper.getEmpById(1);
			System.out.println(mapper.getClass());
			System.out.println(employee);
		} finally {
			
			session.close();
		}
		
	}
	/**
	 * 插件原理
	 * 在四大对象创建的时候
	 * 1.每个创建出来的对象不是直接返回的,而是
	 * 		interceptorChain.pluginAll(parameterHandler)
	 * 2.获取到所有的Interceptor(拦截器) (插件需要实现的接口)
	 * 		调用 interceptor.plugin(target); 返回 target 包装后的
	 * 3.插件机制,我们可以使用插件为目标对象创建一个代理对象;AOP (面向切面)
	 * 		我们的插件可以为四大对象创建出代理对象
	 * 		代理对象就可以拦截到四大代理的每个执行
	 * public Object pluginAll(Object target) {
	    for (Interceptor interceptor : interceptors) {
	      target = interceptor.plugin(target);
	    }
	    return target;
	   }
	 */
	/**
	 * 插件编写:
	 * 1.编写Interceptor 的实现类
	 * 2.使用@Intercepts 注解完成插件签名
	 * 3.将写好的插件注册到全局配置文件中
	 */
	@Test
	public void testPlugin(){
		
	}
}
