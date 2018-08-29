package cn.itcast.login.action;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import cn.itcast.core.constant.Constant;
import cn.itcast.nsfw.user.entity.User;
import cn.itcast.nsfw.user.service.UserService;

import com.opensymphony.xwork2.ActionSupport;

public class LoginAction extends ActionSupport{
	
	@Resource
	private UserService userService;
	
	private User user;
	private String loginResult;
	
	//��ת����¼ҳ��
	public String toLoginUI() {
		return "loginUI";
	}
	
	//��¼
	public String login(){
		if(user != null){
			if(StringUtils.isNotBlank(user.getAccount()) && StringUtils.isNotBlank(user.getPassword())){
				//�����û����˺ź������ѯ�û��б�
				List<User> list = userService.findUserByAccountAndPass(user.getAccount(),user.getPassword());
				if(list != null && list.size() > 0){
					//2.1 ��¼�ɹ�
					User user = list.get(0);
					//2.1.1 ...
					//2.1.2���û���Ϣ���浽session��
					ServletActionContext.getRequest().getSession().setAttribute(Constant.USER, user);
					//2.1.3���û���¼��¼����־�ļ�
					Log log = LogFactory.getLog(getClass());
					log.info("�û�����Ϊ��" + user.getName() + " ���û���¼��ϵͳ��");
					//2.1.4���ض�����ת����ҳ
					return "home";
				} else {//��¼ʧ��
					loginResult = "�ʺŻ����벻��ȷ��";
				}
			} else {
				loginResult = "�ʺŻ����벻��Ϊ�գ�";
			}
		}else {
			loginResult = "�������ʺź����룡";
		}
		return toLoginUI();
	}

	//�˳���ע��
	public String logout(){
		//���session�б�����û���Ϣ
		ServletActionContext.getRequest().getSession().removeAttribute(Constant.USER);
		return toLoginUI();
	}
	//��ת��û��Ȩ����ʾҳ��
	public String toNoPermissionUI(){
		return "noPermissionUI";
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public String getLoginResult() {
		return loginResult;
	}

	public void setLoginResult(String loginResult) {
		this.loginResult = loginResult;
	}
	
}
