package cn.itcast.core.exception;

public class ServiceException extends SysException {

	public ServiceException(){
		super("ҵ���������");
	}
	
	public ServiceException(String message){
		super(message);
	}
}
