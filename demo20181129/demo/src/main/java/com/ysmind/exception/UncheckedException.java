package com.ysmind.exception;

/**
 * @Description:通用的UncheckedException异常，继承RuntimeException，为运行期异常。
 * 每种业务自定义一个异常类的弊端是，如果业务线比较多，自定义的异常子类也会比较多，完全可以定义为一个通用的UncheckedException异常
 * 【这里要注意，如果用spring在业务层管理异常，一定要配置好异常回滚类型，因为spring默认只回滚RuntiomeException类型的】
 * 一、异常相关知识：
 * 非运行时异常(Checked Exception) 
 * Java中凡是继承自Exception但不是继承自RuntimeException的类都是非运行时异常。
 * 运行时异常（Runtime Exception/Unchecked Exception） 
 * RuntimeException类直接继承自Exception类，称为运行时异常。Java中所有的运行时异常都直接或间接的继承自RuntimeException。
 * 3、Java中所有的异常类都直接或间接的继承自Exception。
 * 4、spring事务回滚机制只处理运行时异常，发生非运行时异常则不会回滚操作。
 * 
 * springmvc 通过异常增强返回给客户端统一格式：http://www.mamicode.com/info-detail-1378775.html
 */
public class UncheckedException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    /** 错误Key，用于唯一标识错误类型 */
    private String errorCode = null;
    /** 错误信息 */
    private String errorMessage;
    /** 传递给变量的错误值 */
    private Object[] errorParam = null;

    /**
     * 构造函数
     * @param errorCode 异常编码
     */
    public UncheckedException(String errorCode) {
        this.errorCode = errorCode;
    }
    
    /**
     * 构造函数
     * @param errorCode 异常编码
     * @param message 异常信息
     */
    public UncheckedException(String message,String errorCode) {
        this.errorCode = errorCode;
        setErrorMessage(message);
    }

    /**
     * 构造函数
     * @param errorCode 异常编码
     * @param errorParam Object[] 异常信息用到的参数
     */
    public UncheckedException(String errorCode, Object[] errorParam) {
        this.errorCode = errorCode;
        this.errorParam = errorParam;
    }

    /**
     * 重载构造函数
     * @param errorCode 异常编码
     * @param errorParam 异常信息用到的参数
     * @param t 异常实例
     */
    public UncheckedException(String errorCode, Object[] errorParam, Throwable t) {
        super(t);
        this.errorCode = errorCode;
        this.errorParam = errorParam;
    }

    /**
     * 重载构造函数
     * @param message 异常信息
     * @param t 异常实例
     */
    public UncheckedException(String message, Throwable t) {
        super(message, t);
        setErrorMessage(message);
    }



    /**
     * 异常编码
     * @return String
     */
    public String getErrorCode() {
        return this.errorCode;
    }

    /**
     * 异常信息用到的参数
     * @return Object[]
     */
    public Object[] getErrorParam() {
        return this.errorParam;
    }

    /**
     * 错误信息
     * 
     * @return
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * 错误信息
     * 
     * @param errorMessage
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * 覆盖方法：getMessage
     * @return String
     */
    @Override
    public String getMessage() {
        if (errorMessage != null) {
            return errorMessage;
        }

        //异常信息以资源文件的形式保存，并且支持国际化，此处通过errorCode去读取国际化异常信息
        /*if (errorCode != null && !errorCode.trim().equals("")) {
            setErrorMessage(AppLang.getLU().getMessage(errorCode, errorParam,Locale.SIMPLIFIED_CHINESE));
        }*/
        
        setErrorMessage(errorMessage);//这里就先不用国际化了

        return getErrorMessage();
    }
}