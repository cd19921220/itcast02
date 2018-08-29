package cn.itcast.nsfw.complain.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.itcast.core.service.impl.BaseServiceImpl;
import cn.itcast.core.util.QueryHelper;
import cn.itcast.nsfw.complain.dao.ComplainDao;
import cn.itcast.nsfw.complain.entity.Complain;
import cn.itcast.nsfw.complain.service.ComplainService;

@Service("complainService")
public class ComplainServiceImpl extends BaseServiceImpl<Complain> implements ComplainService {
	
	private ComplainDao complainDao;
	
	@Resource
	public void setComplainDao(ComplainDao complainDao) {
		super.setBaseDao(complainDao);
		this.complainDao = complainDao;
	}


	public void autoDeal() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);//���õ�ǰʱ�������Ϊ1��
		cal.set(Calendar.HOUR_OF_DAY, 0);//���õ�ǰʱ�������Ϊ1��,0ʱ
		cal.set(Calendar.MINUTE, 0);//���õ�ǰʱ�������Ϊ1��,0��
		cal.set(Calendar.SECOND, 0);//���õ�ǰʱ�������Ϊ1��,0��
		
		//1����ѯ����֮ǰ�Ĵ������Ͷ���б�
		QueryHelper queryHelper = new QueryHelper(Complain.class, "c");
		queryHelper.addCondition("c.state=?", Complain.COMPLAIN_STATE_UNDONE);
		queryHelper.addCondition("c.compTime < ?", cal.getTime());//����1��0ʱ0��0��
		
		List<Complain> list = findObjects(queryHelper);
		if(list != null && list.size() > 0){
			//2������Ͷ����Ϣ��״̬Ϊ ��ʧЧ
			for(Complain comp: list){
				comp.setState(Complain.COMPLAIN_STATE_INVALID);
				update(comp);
			}
		}
	}

	public List<Map> getAnnualStatisticDataByYear(int year) {
		List<Map> resList = new ArrayList<Map>();
		//1����ȡͳ������
		List<Object[]> list = complainDao.getAnnualStatisticDataByYear(year);
		if(list != null && list.size()>0){
			Calendar cal = Calendar.getInstance();
			//�Ƿ�ǰ���
			boolean isCurYear = (cal.get(Calendar.YEAR) == year);
			int curMonth = cal.get(Calendar.MONTH)+1;//��ǰ�·�
			//2����ʽ��ͳ�ƽ��
			int temMonth = 0;
			Map<String, Object> map = null;
			for(Object[] obj: list){
				temMonth = Integer.valueOf((obj[0])+"");
				map = new HashMap<String, Object>();
				map.put("label", temMonth+ " ��");
				if(isCurYear){//��ǰ���
					//��ǰ��ݣ�����·��ѹ�����ֱ��ȡͶ��������ֵΪ�ջ�nullʱ����Ϊ0������·�δ������ȫ��Ͷ�����ÿ�
					if(temMonth > curMonth){//δ���·ݣ���Ͷ����Ϊ��
						map.put("value", "");
					} else {//�ѹ��·�
						map.put("value", obj[1]==null?"0":obj[1]);
					}
				} else {//�ǵ�ǰ�����ֱ��ȡͶ��������ֵΪ�ջ�nullʱ����Ϊ0
					map.put("value", obj[1]==null?"0":obj[1]);
				}
				resList.add(map);
			}
		}
		
		return resList;
	}

}
