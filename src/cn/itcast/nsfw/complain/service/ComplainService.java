package cn.itcast.nsfw.complain.service;

import java.util.List;
import java.util.Map;

import cn.itcast.core.service.BaseService;
import cn.itcast.nsfw.complain.entity.Complain;

public interface ComplainService extends BaseService<Complain> {

	//�Զ�����Ͷ��
	public void autoDeal();
	
	/**
	 * ������ݻ�ȡͳ����ȵ�ÿ���µ�Ͷ����
	 * @param year Ҫͳ�Ƶ����
	 * @return ͳ������
	 */
	public List<Map> getAnnualStatisticDataByYear(int year);
}
