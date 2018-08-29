package cn.itcast.core.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.itcast.core.constant.Constant;
import cn.itcast.core.permission.PermissionCheck;
import cn.itcast.nsfw.user.entity.User;

public class LoginFilter implements Filter{

	public void destroy() {
		
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String uri = request.getRequestURI();
		//�жϵ�ǰ�����ַ�Ƿ��ǵ�¼�������ַ
		if(!uri.contains("sys/login")){
			//�ǵ�¼����
			if(request.getSession().getAttribute(Constant.USER) != null){
				//˵���Ѿ���¼����
				//�ж��Ƿ������˰����ϵͳ
				if(uri.contains("/nsfw/")){
					//������˰������ϵͳ
					User user = (User) request.getSession().getAttribute(Constant.USER);
					//��ȡspring����
					WebApplicationContext applicationContext = 
						WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
					PermissionCheck pc = (PermissionCheck)applicationContext.getBean("permissionCheck");
					if(pc.isAccessible(user, "nsfw")){
						//˵����Ȩ�ޣ�����
						chain.doFilter(request, response);
					}else{
						//û��Ȩ�ޣ���ת��û��Ȩ����ʾҳ��
						response.sendRedirect(request.getContextPath() + "/sys/login_toNoPermissionUI.action");
					}
				}else{
					//�Ƿ�����˰������ϵͳ����ֱ�ӷ���
					chain.doFilter(request, response);
				}
			}else{
				//û�е�¼����ת����¼ҳ��
				response.sendRedirect(request.getContextPath() + "/sys/login_toLoginUI.action");
			}
		}else{
			//��¼����ֱ�ӷ���
			chain.doFilter(request, response);
		}
	}

	public void init(FilterConfig arg0) throws ServletException {
		
	}

}
