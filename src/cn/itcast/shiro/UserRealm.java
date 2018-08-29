package cn.itcast.shiro;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.nsfw.user.entity.User;
import cn.itcast.nsfw.user.service.UserService;



/**
 * �Զ���Realm
 * 
 * @author lenovo
 *
 */
public class UserRealm extends AuthorizingRealm {

	@Autowired
	private UserService userSerivce;

	/**
	 * ִ����Ȩ�߼�
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
		System.out.println("ִ����Ȩ�߼�");

		/*// ����Դ������Ȩ
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

		// �����Դ����Ȩ�ַ���
		// info.addStringPermission("user:add");

		// �����ݿ��ѯ��ǰ��¼�û�����Ȩ�ַ���
		// ��ȡ��ǰ��¼�û�
		Subject subject = SecurityUtils.getSubject();
		//subject.getPrincipal()���Եõ���֤���ĵ�¼����Ϣ
		User user = (User) subject.getPrincipal();
		User dbUser = userSerivce.getUserRolesByUserId(user.getId());

		info.addStringPermission(dbUser.getPerms());*/

		return null;
	}

	/**
	 * ִ����֤�߼�
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken arg0) throws AuthenticationException {
		System.out.println("ִ����֤�߼�");

		// �������ݿ���û���������
		/*
		 * String name = "dong"; String password = "123456";
		 */

		// ��дshiro�ж��߼����ж��û���������
		// 1.�ж��û���
		UsernamePasswordToken token = (UsernamePasswordToken) arg0;

		List<User> users = userSerivce.findUserByAccount(token.getUsername());
		
		User user = users.get(0);

		if (user == null) {
			// �û���������
			return null;// shiro�ײ���׳�UnKnowAccountException
		}

		// 2.�ж�����
		return new SimpleAuthenticationInfo(user, user.getPassword(), "");
	}

}
