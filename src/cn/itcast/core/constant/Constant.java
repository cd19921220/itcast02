package cn.itcast.core.constant;

import java.util.HashMap;
import java.util.Map;

public class Constant {
	
	//ϵͳ���û���session�еı�ʶ��
	public static String USER = "SYS_USER";
	
	/*----------------------ϵͳȨ�޼���--------------------------*/
	public static String PRIVILEGE_XZGL = "xzgl"; 
	public static String PRIVILEGE_HQFW = "hqfw"; 
	public static String PRIVILEGE_ZXXX = "zxxx"; 
	public static String PRIVILEGE_NSFW = "nsfw"; 
	public static String PRIVILEGE_SPACE = "spaces"; 

	public static Map<String, String> PRIVILEGE_MAP;
	static {
		PRIVILEGE_MAP = new HashMap<String, String>();
		PRIVILEGE_MAP.put(PRIVILEGE_XZGL, "��������");
		PRIVILEGE_MAP.put(PRIVILEGE_HQFW, "���ڷ���");
		PRIVILEGE_MAP.put(PRIVILEGE_ZXXX, "����ѧϰ");
		PRIVILEGE_MAP.put(PRIVILEGE_NSFW, "��˰����");
		PRIVILEGE_MAP.put(PRIVILEGE_SPACE, "�ҵĿռ�");
	}
	
}
