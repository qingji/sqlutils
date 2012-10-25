package im.qingji.sqlutil.convert;


/**
 * 不支持类型异常
 * @author qingji
 *
 */
public class UnsupportedTypeException extends RuntimeException {


	private static final long serialVersionUID = 7705133471102426380L;

	public UnsupportedTypeException() {
		super();
	}

	public UnsupportedTypeException(String message) {
		super(message);
	}

	public UnsupportedTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnsupportedTypeException(Throwable cause) {
		super(cause);
	}


}