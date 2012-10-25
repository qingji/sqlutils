package im.qingji.sqlutil.convert;


/**
 * 
 * @author qingji
 *
 */
public class ConvertException extends RuntimeException {

	private static final long serialVersionUID = -4792464840677638293L;

	public ConvertException() {
		super();
	}

	public ConvertException(String message) {
		super(message);
	}

	public ConvertException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConvertException(Throwable cause) {
		super(cause);
	}


}