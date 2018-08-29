package cn.itcast.core.exception;

public class ServiceException extends SysException {

	public ServiceException(){
		super("ÒµÎñ²Ù×÷´íÎó");
	}
	
	public ServiceException(String message){
		super(message);
	}
}
