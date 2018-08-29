package cn.itcast.nsfw.user.action;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.print.attribute.standard.PageRanges;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.aspectj.util.FileUtil;

import cn.itcast.core.action.BaseAction;
import cn.itcast.core.page.PageResult;
import cn.itcast.core.util.QueryHelper;
import cn.itcast.nsfw.role.service.RoleService;
import cn.itcast.nsfw.user.entity.User;
import cn.itcast.nsfw.user.entity.UserRole;
import cn.itcast.nsfw.user.service.UserService;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class UserAction extends BaseAction {
	
	@Resource
	private UserService userService;
	@Resource
	private RoleService roleService;
	private List<User> userList;
	private User user;
	private String[] selectedRow;
	private File headImg;
	private String headImgContentType;
	private String headImgFileName;
	
	private File userExcel;
	private String userExcelContentType;
	private String userExcelFileName;
	private String[] userRoleIds;
	private String strName;
	
	
	//�б�ҳ��
	public String listUI() throws Exception{
		QueryHelper queryHelper = new QueryHelper(User.class, "u");
		try {
			if(user != null){
				if(StringUtils.isNoneBlank(user.getName())){
					user.setName(URLDecoder.decode(user.getName(),"utf-8"));
					queryHelper.addCondition("u.name like ?", "%" + user.getName() + "%");
				}
			}
			pageResult = userService.getPageResult(queryHelper, getPageNo(), getPageSize());
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		
		return "listUI";
	}
	//��ת������ҳ��
	public String addUI(){
		ActionContext.getContext().getContextMap().put("roleList", roleService.findObjects());
		strName = user.getName();
		return "addUI";
	}
	//��������
	public String add(){
		try {
			if(user != null){
				if(headImg != null){
					//1������ͷ��upload/user
					//��ȡ����·���ľ��Ե�ַ
					String filePath = ServletActionContext.getServletContext().getRealPath("upload/user");
					String fileName = UUID.randomUUID().toString().replace("-", "")+headImgFileName.substring(headImgFileName.lastIndexOf("."));
					//�����ļ�
					FileUtil.copyFile(headImg, new File(filePath,fileName));
					
					//�����û�ͷ��·��
					user.setHeadImg("user/" + fileName);
				}
				userService.saveUserAndRole(user,userRoleIds);
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return "list";
	}
	
	//��ת���༭ҳ��
	public String editUI() {
		//���ؽ�ɫ�б�
		ActionContext.getContext().getContextMap().put("roleList", roleService.findObjects());
		if(user != null && user.getId() != null){
			strName = user.getName();
			user = userService.findObjectById(user.getId());
			//�����ɫ����
			List<UserRole> list = userService.getUserRolesByUserId(user.getId());
			if(list != null && list.size() > 0){
				userRoleIds = new String[list.size()];
				for(int i = 0; i < list.size(); i++){
					userRoleIds[i] = list.get(i).getId().getRole().getRoleId();
				}
			}
		}
		return "editUI";
	}
	
	//����༭
	public String edit() {
		try {
			if(user != null){
				if(headImg != null){
					//1������ͷ��upload/user
					//��ȡ����·���ľ��Ե�ַ
					String filePath = ServletActionContext.getServletContext().getRealPath("upload/user");
					String fileName = UUID.randomUUID().toString().replace("-", "")+headImgFileName.substring(headImgFileName.lastIndexOf("."));
					//�����ļ�
					FileUtil.copyFile(headImg, new File(filePath,fileName));
					
					//�����û�ͷ��·��
					user.setHeadImg("user/" + fileName);
				}
				userService.updateUserAndRole(user, userRoleIds);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "list";
	}
	
	//ɾ��
	public String delete() {
		if(user != null && user.getId() != null){
			strName = user.getName();
			userService.delete(user.getId());
		}
		return "list";
	}
	
	//����ɾ��
	public String deleteSelected() {
		if(selectedRow != null){
			for(String id: selectedRow){
				userService.delete(id);
			}
		}
		return "list";
	}
	
	//�����û��б�
	public void exportExcel() {
		try {
			//1�������û��б�
			userList = userService.findObjects();
			//2.����
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("application/x-execl");
			response.setHeader("Content-Disposition", "attachment;filename=" + new String("�û��б�.xls".getBytes(), "ISO-8859-1"));
			ServletOutputStream outputStream = response.getOutputStream();
			userService.exportExcel(userList, outputStream);
			if(outputStream != null){
				outputStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//�����û��б�
	public String importExcel(){
		//1����ȡexcel�ļ�
		if(userExcel != null){
			//�Ƿ���excel
			if(userExcelFileName.matches("^.+\\.(?i)((xls)|(xlsx))$")){
				//2������
				userService.importExcel(userExcel, userExcelFileName);
			}
		}
		return "list";
	}
	
	//У���û��˺�Ψһ
	public void verifyAccount(){
		try {
			//1����ȡ�˺�
			if(user != null &&  StringUtils.isNotBlank(user.getAccount())){
				//2�������˺ŵ����ݿ���У���Ƿ���ڸ��˺Ŷ�Ӧ���û�
				List<User> list = userService.findUserByAccountAndId(user.getId(), user.getAccount());
				String strResult = "true";
				if(list != null && list.size() >0 ){
					strResult = "false";
				}
				
				//���
				HttpServletResponse response = ServletActionContext.getResponse();
				response.setContentType("text/html");
				ServletOutputStream outputStream = response.getOutputStream();
				outputStream.write(strResult.getBytes());
				outputStream.close();
				
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	
	
	public List<User> getUserList() {
		return userList;
	}
	
	public void setUserList(List<User> userList) {
		this.userList = userList;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user){
		this.user = user;
	}
	
	public String[] getSelectedRow() {
		return selectedRow;
	}
	
	public void setSelectedRow(String[] selectedRow){
		this.selectedRow = selectedRow;
	}
	public File getHeadImg() {
		return headImg;
	}
	public void setHeadImg(File headImg) {
		this.headImg = headImg;
	}
	public String getHeadImgContentType() {
		return headImgContentType;
	}
	public void setHeadImgContentType(String headImgContentType) {
		this.headImgContentType = headImgContentType;
	}
	public String getHeadImgFileName() {
		return headImgFileName;
	}
	public void setHeadImgFileName(String headImgFileName) {
		this.headImgFileName = headImgFileName;
	}
	public File getUserExcel() {
		return userExcel;
	}
	public void setUserExcel(File userExcel) {
		this.userExcel = userExcel;
	}
	public String getUserExcelContentType() {
		return userExcelContentType;
	}
	public void setUserExcelContentType(String userExcelContentType) {
		this.userExcelContentType = userExcelContentType;
	}
	public String getUserExcelFileName() {
		return userExcelFileName;
	}
	public void setUserExcelFileName(String userExcelFileName) {
		this.userExcelFileName = userExcelFileName;
	}
	public String[] getUserRoleIds() {
		return userRoleIds;
	}
	public void setUserRoleIds(String[] userRoleIds) {
		this.userRoleIds = userRoleIds;
	}
	public String getStrName() {
		return strName;
	}
	public void setStrName(String strName) {
		this.strName = strName;
	}
	
	
}
