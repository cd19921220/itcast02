package cn.itcast.home.action;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;

import cn.itcast.core.constant.Constant;
import cn.itcast.core.util.QueryHelper;
import cn.itcast.nsfw.complain.entity.Complain;
import cn.itcast.nsfw.complain.service.ComplainService;
import cn.itcast.nsfw.info.entity.Info;
import cn.itcast.nsfw.info.service.InfoService;
import cn.itcast.nsfw.user.entity.User;
import cn.itcast.nsfw.user.service.UserService;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class HomeAction extends ActionSupport {
	
	@Resource
	private UserService userService;
	@Resource
	private InfoService infoService;
	
	@Resource
	private ComplainService complainService;
	
	private Map<String, Object> return_map;
	private Complain comp;
	private Info info;
	//��ת����ҳ
	public String execute(){
		//������Ϣ�б�
		//���ط��༯��
		ActionContext.getContext().getContextMap().put("infoTypeMap", Info.INFO_TYPE_MAP);
		QueryHelper queryHelper1 = new QueryHelper(Info.class, "i");
		queryHelper1.addOrderByProperty("i.createTime", QueryHelper.ORDER_BY_DESC);
		List<Info> infoList = infoService.getPageResult(queryHelper1, 1, 5).getItems();
		ActionContext.getContext().getContextMap().put("infoList", infoList);
		
		User user = (User) ServletActionContext.getRequest().getSession().getAttribute(Constant.USER);
		//�����ҵ�Ͷ����Ϣ�б�
		//����״̬����
		ActionContext.getContext().getContextMap().put("complainStateMap", Complain.COMPLAIN_STATE_MAP);
		QueryHelper queryHelper2 = new QueryHelper(Complain.class, "c");
		queryHelper2.addCondition("c.compName = ?", user.getName());
		//����Ͷ��ʱ����������
		queryHelper2.addOrderByProperty("c.compTime", QueryHelper.ORDER_BY_ASC);
		//����Ͷ��״̬��������
		queryHelper2.addOrderByProperty("c.state", QueryHelper.ORDER_BY_DESC);
		List<Complain> complainList = complainService.getPageResult(queryHelper2, 1, 6).getItems();
		ActionContext.getContext().getContextMap().put("complainList", complainList);
		return "home";
	}
	
	//��ת����ҪͶ��
	public String complainAddUI(){
		return "complainAddUI";
	}
	
	public void getUserJson(){
		try {
			//1����ȡ����
			String dept = ServletActionContext.getRequest().getParameter("dept");
			if(StringUtils.isNotBlank(dept)){
				QueryHelper queryHelper = new QueryHelper(User.class, "u");
				queryHelper.addCondition("u.dept like ?", "%" + dept);
				//2�����ݲ��Ų�ѯ�û��б�
				List<User> userList = userService.findObjects(queryHelper);
				//����Json����
				JSONObject jso = new JSONObject();
				jso.put("msg", "success");
				jso.accumulate("userList", userList);
				
				//3������û��б���json��ʽ�ַ�����ʽ���
				HttpServletResponse response = ServletActionContext.getResponse();
				response.setContentType("text/html");
				ServletOutputStream outputStream = response.getOutputStream();
				outputStream.write(jso.toString().getBytes("utf-8"));
				outputStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getUserJson2(){
		try {
			//1����ȡ����
			String dept = ServletActionContext.getRequest().getParameter("dept");
			if(StringUtils.isNotBlank(dept)){
				QueryHelper queryHelper = new QueryHelper(User.class, "u");
				queryHelper.addCondition("u.dept like ?", "%" +dept);
				//2�����ݲ��Ų�ѯ�û��б�
				return_map = new HashMap<String, Object>();
				return_map.put("msg", "success");
				return_map.put("userList", userService.findObjects(queryHelper));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	//����Ͷ��
	public void complainAdd(){
		try {
			if(comp != null){
				//����ĬдͶ��״̬Ϊ ������
				comp.setState(Complain.COMPLAIN_STATE_UNDONE);
				comp.setCompTime(new Timestamp(new Date().getTime()));
				complainService.save(comp);
				
				//���Ͷ�߽��
				HttpServletResponse response = ServletActionContext.getResponse();
				response.setContentType("text/html");
				ServletOutputStream outputStream = response.getOutputStream();
				outputStream.write("success".getBytes("utf-8"));
				outputStream.close();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//�鿴��Ϣ
	public String infoViewUI(){
		//���ط��༯��
		ActionContext.getContext().getContextMap().put("infoTypeMap", Info.INFO_TYPE_MAP);
		if(info != null){
			info = infoService.findObjectById(info.getInfoId());
		}
		return "infoViewUI";
	}
	
	//�鿴Ͷ����Ϣ
	public String complainViewUI(){
		//����״̬����
		ActionContext.getContext().getContextMap().put("complainStateMap", Complain.COMPLAIN_STATE_MAP);
		if(comp != null){
			comp = complainService.findObjectById(comp.getCompId());
		}
		return "complainViewUI";
	}

	public Map<String, Object> getReturn_map() {
		return return_map;
	}

	public void setReturn_map(Map<String, Object> return_map) {
		this.return_map = return_map;
	}

	public Complain getComp() {
		return comp;
	}

	public void setComp(Complain comp) {
		this.comp = comp;
	}

	public Info getInfo() {
		return info;
	}

	public void setInfo(Info info) {
		this.info = info;
	}
	
}
