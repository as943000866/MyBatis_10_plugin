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
	 * 1.��ȡsqlSessionFactory����
	 * 		�����ļ���ÿ����Ϣ������ Configuration��,���ذ��� Cofiguration �� DefaultSqlSessionFactory
	 * 		ע��:��MappedStatement��: ����һ����ɾ�Ĳ����ϸ��Ϣ
	 * 2.��ȡsqlSession����
	 * 		����һ��DefaultSQLSession����,����Executor��Configuration;
	 * 		��һ���ᴴ��Executor����
	 * 3.��ȡ�ӿڵĴ������(MapperProxy)
	 * 		getMapper,ʹ��MapperProxyFactory����һ��MapperProxy�Ĵ������
	 * 		����������������,DefaultSqlSession(Executor)
	 * 4.ִ����ɾ�Ĳ鷽��
	 * 
	 * �ܽ�:
	 * 	 1.���������ļ� (ȫ��,sqlӳ��) ��ʼ��Configuration����
	 * 	 2.����һ��DefaultSqlSession����
	 * 		���������Configuration�Լ�
	 * 		Executor (����ȫ�������ļ��е�defaultExecutorType������Ӧ��Executor)
	 * 	 3.DefaultSqlSession.getMapper(): �õ�Mapper�ӿڶ�Ӧ��MapperProxy
	 * 	 4.MapperProxy������(DefaultSqlSession)
	 * 	 5.ִ����ɾ�Ĳ�
	 * 			1).����DefaultSqlSession����ɾ�Ĳ�(Executor)
	 * 			2).�ᴴ��һ��StatementHandle����
	 * 				(ͬʱҲ�ᴴ����ParameterHandler��ResultSetHandler)
	 * 			3).����StatementHandlerԤ��������Լ����ò���ֵ;
	 * 				ʹ��ParameterHandler����Sql���ò���
	 * 			4).����StatementHandle����ɾ�Ĳ鷽��
	 * 			5).ResultSetHandler��װ���
	 *   ע��:
	 *   	�Ĵ����ÿ��������ʱ����һ��interceptorChain.pluginAll(parameterHandler);
	 *
	 * @throws IOException
	 */
	@Test
	public void test01() throws IOException{
		//1. ��ȡsqlSessionFactory����
		SqlSessionFactory sessionFactory = getSqlSessionFactory();
		
		//2. ��ȡsqlSession����
		SqlSession session = sessionFactory.openSession();
		
		try {
			
			//3.��ȡ�ӿڵ�ʵ�������
			EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
			
			Employee employee = mapper.getEmpById(1);
			System.out.println(mapper.getClass());
			System.out.println(employee);
		} finally {
			
			session.close();
		}
		
	}
	/**
	 * ���ԭ��
	 * ���Ĵ���󴴽���ʱ��
	 * 1.ÿ�����������Ķ�����ֱ�ӷ��ص�,����
	 * 		interceptorChain.pluginAll(parameterHandler)
	 * 2.��ȡ�����е�Interceptor(������) (�����Ҫʵ�ֵĽӿ�)
	 * 		���� interceptor.plugin(target); ���� target ��װ���
	 * 3.�������,���ǿ���ʹ�ò��ΪĿ����󴴽�һ���������;AOP (��������)
	 * 		���ǵĲ������Ϊ�Ĵ���󴴽����������
	 * 		�������Ϳ������ص��Ĵ�����ÿ��ִ��
	 * public Object pluginAll(Object target) {
	    for (Interceptor interceptor : interceptors) {
	      target = interceptor.plugin(target);
	    }
	    return target;
	   }
	 */
	/**
	 * �����д:
	 * 1.��дInterceptor ��ʵ����
	 * 2.ʹ��@Intercepts ע����ɲ��ǩ��
	 * 3.��д�õĲ��ע�ᵽȫ�������ļ���
	 */
	@Test
	public void testPlugin(){
		
	}
}
