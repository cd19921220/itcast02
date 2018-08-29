package cn.itcast.nsfw.user.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import cn.itcast.core.exception.ServiceException;
import cn.itcast.core.service.impl.BaseServiceImpl;
import cn.itcast.core.util.ExcelUtil;
import cn.itcast.nsfw.role.entity.Role;
import cn.itcast.nsfw.user.dao.UserDao;
import cn.itcast.nsfw.user.entity.User;
import cn.itcast.nsfw.user.entity.UserRole;
import cn.itcast.nsfw.user.entity.UserRoleId;
import cn.itcast.nsfw.user.service.UserService;

@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {
	
	private UserDao userDao;
	
	@Resource 
	public void setUserDao(UserDao userDao) {
		super.setBaseDao(userDao);
		this.userDao = userDao;
	}

	@Override
	public void delete(Serializable id) {
		userDao.delete(id);
		//ɾ���û���Ӧ������Ȩ��
		userDao.deleteUserRoleByUserId(id);
	}

	
	public void exportExcel(List<User> userList, ServletOutputStream outputStream) {
		ExcelUtil.exportUserExcel(userList, outputStream);
	}


	public void importExcel(File userExcel, String userExcelFileName) {
		try {
			FileInputStream fileInputStream = new FileInputStream(userExcel);
			boolean is03Excel = userExcelFileName.matches("^.+\\.(?i)(xls)$");
			//1����ȡ������
			Workbook workbook = is03Excel ? new HSSFWorkbook(fileInputStream):new XSSFWorkbook(fileInputStream);
			//2����ȡ������
			Sheet sheet = workbook.getSheetAt(0);
			//3����ȡ��
			if(sheet.getPhysicalNumberOfRows() > 2){
				User user = null;
				for(int k = 2; k < sheet.getPhysicalNumberOfRows(); k++){
					//4����ȡ��Ԫ��
					Row row = sheet.getRow(k);
					user = new User();
					//�û���
					Cell cell0 = row.getCell(0);
					user.setName(cell0.getStringCellValue());
					//�ʺ�
					Cell cell1 = row.getCell(1);
					user.setAccount(cell1.getStringCellValue());
					//��������
					Cell cell2 = row.getCell(2);
					user.setDept(cell2.getStringCellValue());
					//�Ա�
					Cell cell3 = row.getCell(3);
					user.setGender(cell3.getStringCellValue().equals("��"));
					//�ֻ���
					String mobile = "";
					Cell cell4 = row.getCell(4);
					try {
						mobile = cell4.getStringCellValue();
					} catch (Exception e) {
						double dMobile = cell4.getNumericCellValue();
						mobile = BigDecimal.valueOf(dMobile).toString();
					}
					user.setMobile(mobile);
					
					//��������
					Cell cell5 = row.getCell(5);
					user.setEmail(cell5.getStringCellValue());
					//����
					Cell cell6 = row.getCell(6);
					if(cell6.getDateCellValue() != null){
						user.setBirthday(cell6.getDateCellValue());
					}
					//Ĭ���û�����Ϊ 123456
					user.setPassword("123456");
					//Ĭ���û�״̬Ϊ ��Ч
					user.setState(User.USER_STATE_VALID);
					
					//5�������û�
					save(user);
				}
			}
			workbook.close();
			fileInputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public List<User> findUserByAccountAndId(String id, String account) {
		return userDao.findUserByAccountAndId(id, account);
	}

	
	public void saveUserAndRole(User user, String... roleIds) {
		//1�������û�
		save(user);
		//2�������û���Ӧ�Ľ�ɫ
		if(roleIds != null){
			for(String roleId: roleIds){
				userDao.saveUserRole(new UserRole(new UserRoleId(new Role(roleId), user.getId())));
			}
		}
	}

	
	public void updateUserAndRole(User user, String... roleIds) {
		//1�������û�ɾ�����û������н�ɫ
		userDao.deleteUserRoleByUserId(user.getId());
		//2�������û�
		update(user);
		//3�������û���Ӧ�Ľ�ɫ
		if(roleIds != null){
			for(String roleId: roleIds){
				userDao.saveUserRole(new UserRole(new UserRoleId(new Role(roleId), user.getId())));
			}
		}
	}

	
	public List<UserRole> getUserRolesByUserId(String id) {
		return userDao.getUserRolesByUserId(id);
	}

	
	public List<User> findUserByAccountAndPass(String account, String password) {
		return userDao.findUsersByAcccountAndPass(account, password);
	}

	public List<User> findUserByAccount(String account) {
		
		return userDao.findUsersByAcccount(account);
	}

}
