package cn.itcast.nsfw.user.dao;

import java.io.Serializable;
import java.util.List;

import cn.itcast.core.dao.BaseDao;
import cn.itcast.nsfw.user.entity.User;
import cn.itcast.nsfw.user.entity.UserRole;

public interface UserDao extends BaseDao<User> {

	/**
	 * �����ʺź��û�id��ѯ�û�
	 * @param id �û�ID
	 * @param account �û��ʺ�
	 * @return �û��б�
	 */
	List<User> findUserByAccountAndId(String id, String account);

	//�����û���ɫ
	void saveUserRole(UserRole userRole);
	//�����û�idɾ�����û��������û���ɫ
	void deleteUserRoleByUserId(Serializable id);
	//�����û����ʺź������ѯ�û��б�
	public List<User> findUsersByAcccountAndPass(String account, String password);
	//�����û�id��ȡ���û���Ӧ�������û���ɫ
	List<UserRole> getUserRolesByUserId(String id);
	//�����û����ʺŲ�ѯ�û��б�
	List<User> findUsersByAcccount(String account);

}
