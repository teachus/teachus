package dk.teachus.frontend.pages;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LazyInitProxy implements InvocationHandler, Serializable {
	private static final long serialVersionUID = 1L;
	
	private transient Object target;
	
	public LazyInitProxy(Object target) {
		this.target = target;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		return method.invoke(target, args);
	}

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

}
