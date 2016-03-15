package cz.metacentrum.perun.wui.registrar.pages.steps;

import cz.metacentrum.perun.wui.model.GeneralObject;
import cz.metacentrum.perun.wui.model.PerunException;

/**
 * Data about step. How it ends, exception, ...
 * Primary used for Final step to show summary.
 * Feel free to add more data.
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class Result {

    private FormStep.Type type;
    private String registeredMail;
    private boolean autoApproval;
    private GeneralObject bean;
    private PerunException exception;

    public Result(FormStep.Type type, PerunException exception, GeneralObject bean, boolean autoApproval) {
        this.type = type;
        this.registeredMail = null;
        this.exception = exception;
        this.bean = bean;
        this.autoApproval = autoApproval;
    }

    public Result(FormStep.Type type, GeneralObject bean, boolean autoApproval) {
        this(type, null, bean, autoApproval);
    }

    public boolean hasAutoApproval() {
        return autoApproval;
    }

    public GeneralObject getBean() {
        return bean;
    }

    public boolean isOk() {
        return (exception == null);
    }

    public FormStep.Type getType() {
        return type;
    }

    public String getRegisteredMail() {
        return registeredMail;
    }

    public void setRegisteredMail(String mail) {
        this.registeredMail = mail;
    }

    public PerunException getException() {
        return exception;
    }

    public void setException(PerunException exception) {
        this.exception = exception;
    }

}
