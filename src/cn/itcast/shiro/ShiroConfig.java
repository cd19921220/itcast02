package cn.itcast.shiro;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;




/**
 * Shiro��������
 * @author lenovo
 *
 */
@Configuration
public class ShiroConfig {

	/**
	 * ����ShiroFilterFactoryBean
	 */
	@Bean
	public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager")DefaultWebSecurityManager securityManager){
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		
		//���ð�ȫ������
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		
		//���Shiro���ù�����
				/**
				 * Shiro���ù�����������ʵ��Ȩ����ص�������
				 *    ���õĹ�������
				 *       anon: ������֤����¼�����Է���
				 *       authc: ������֤�ſ��Է���
				 *       user: ���ʹ��rememberMe�Ĺ��ܿ���ֱ�ӷ���
				 *       perms�� ����Դ����õ���ԴȨ�޲ſ��Է���
				 *       role: ����Դ����õ���ɫȨ�޲ſ��Է���
				 */
				Map<String,String> filterMap = new LinkedHashMap<String,String>();
				/*filterMap.put("/add", "authc");
				filterMap.put("/update", "authc");*/
				
				//��/user/*�µ�������Դ����������
				/*filterMap.put("/user/*", "authc");*/
				
				//����testThymeleaf����ҳ��
				filterMap.put("/testThymeleaf", "anon");
				
				//����login.htmlҳ��
				filterMap.put("/login", "anon");
				
				//��Ȩ������
				//ע�⣺��ǰ��Ȩ���غ�shiro���Զ���ת��δ��Ȩҳ��
				filterMap.put("/add", "perms[user:add]");
				filterMap.put("/update", "perms[user:update]");
				
				filterMap.put("/*", "authc");
				
				
				
				//�޸ĵ����ĵ�¼ҳ��
				shiroFilterFactoryBean.setLoginUrl("/toLogin");
				//����δ��Ȩ��ʾҳ��
				shiroFilterFactoryBean.setUnauthorizedUrl("/noAuth");
				
				shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
		
		return shiroFilterFactoryBean;
	}
	
	/**
	 * ����DefaultWebSecurityManager
	 */
	@Bean(name="securityManager")
	public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm")UserRealm userRealm){
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		//����realm
		securityManager.setRealm(userRealm);
		return securityManager;
	}
	
	/**
	 * ����Realm
	 */
	@Bean(name="userRealm")
	public UserRealm getRealm(){
		return new UserRealm();
	}
	
	
	
}
