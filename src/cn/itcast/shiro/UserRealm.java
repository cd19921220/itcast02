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
 * 自定义Realm
 * 
 * @author lenovo
 *
 */
public class UserRealm extends AuthorizingRealm {

	@Autowired
	private UserService userSerivce;

	/**
	 * 执行授权逻辑
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
		System.out.println("执行授权逻辑");

		/*// 给资源进行授权
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

		// 添加资源的授权字符串
		// info.addStringPermission("user:add");

		// 到数据库查询当前登录用户的授权字符串
		// 获取当前登录用户
		Subject subject = SecurityUtils.getSubject();
		//subject.getPrincipal()可以得到认证过的登录者信息
		User user = (User) subject.getPrincipal();
		User dbUser = userSerivce.getUserRolesByUserId(user.getId());

		info.addStringPermission(dbUser.getPerms());*/

		return null;
	}

	/**
	 * 执行认证逻辑
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken arg0) throws AuthenticationException {
		System.out.println("执行认证逻辑");

		// 假设数据库的用户名和密码
		/*
		 * String name = "dong"; String password = "123456";
		 */

		// 编写shiro判断逻辑，判断用户名和密码
		// 1.判断用户名
		UsernamePasswordToken token = (UsernamePasswordToken) arg0;

		List<User> users = userSerivce.findUserByAccount(token.getUsername());
		
		User user = users.get(0);

		if (user == null) {
			// 用户名不存在
			return null;// shiro底层会抛出UnKnowAccountException
		}

		// 2.判断密码
		return new SimpleAuthenticationInfo(user, user.getPassword(), "");
	}

}
