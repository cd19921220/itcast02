package cn.itcast.nsfw.user.service;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import javax.servlet.ServletOutputStream;

import cn.itcast.core.exception.ServiceException;
import cn.itcast.core.service.BaseService;
import cn.itcast.nsfw.user.entity.User;
import cn.itcast.nsfw.user.entity.UserRole;

public interface UserService extends BaseService<User> {

	// �����û��б�
	public void exportExcel(List<User> userList,
			ServletOutputStream outputStream);

	// �����û��б�
	public void importExcel(File userExcel, String userExcelFileName);

	/**
	 * �����ʺź��û�id��ѯ�û�
	 * 
	 * @param id
	 *            �û�ID
	 * @param account
	 *            �û��ʺ�
	 * @return �û��б�
	 */
	public List<User> findUserByAccountAndId(String id, String account);

	// �����û������Ӧ�Ľ�ɫ
	public void saveUserAndRole(User user, String... roleIds);

	// �����û������Ӧ�Ľ�ɫ
	public void updateUserAndRole(User user, String... roleIds);

	// �����û�id��ȡ���û���Ӧ�������û���ɫ
	public List<UserRole> getUserRolesByUserId(String id);

	// �����û����ʺź������ѯ�û��б�
	public List<User> findUserByAccountAndPass(String account, String password);

	// �����û����ʺŲ�ѯ�û��б�
	public List<User> findUserByAccount(String account);

}
