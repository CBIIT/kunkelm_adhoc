/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.controllers;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@SessionScoped
public class IdleController implements Serializable {

    private static final Logger lgr = Logger.getLogger("GLOBAL");

    private static final long serialVersionUID = 7816871787968798179L;

    private static final Integer sessionTimeLimitMin = 2;
    private static final Integer idleWarningTimeMin = 1;

    private Integer secondsRemaining;
    private String mostRecentView;

    public IdleController() {
        init();
    }

    @PostConstruct
    public void init() {
    }

    private class IncrementRemaining extends TimerTask {

        public void run() {
            secondsRemaining--;
        }
    }

    public void onIdle() {

        this.secondsRemaining = (this.sessionTimeLimitMin - this.idleWarningTimeMin) * 60;

        Timer timer = new Timer();
        timer.schedule(new IncrementRemaining(), 0, 1000);

        // where the user was when idle kicked in
        this.mostRecentView = FacesContext.getCurrentInstance().getViewRoot().getViewId();

        try {
            ExternalContext extCtx = FacesContext.getCurrentInstance().getExternalContext();
            extCtx.redirect(extCtx.getRequestContextPath() + "/idleWarning.xhtml?faces-redirect=true");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void checkEndSession() {

        if (this.secondsRemaining < 1) {

            try {
                ExternalContext extCtx = FacesContext.getCurrentInstance().getExternalContext();
                HttpSession httpSession = (HttpSession) extCtx.getSession(false);
                httpSession.invalidate();
                extCtx.redirect(extCtx.getRequestContextPath() + "/sessionEnded.xhtml?faces-redirect=true");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public void onActive() {

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                "Welcome back!", "Welcome back!"));

//        try {
//            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
//            System.out.println("In onActive ec.getRequestContextPath(): " + ec.getRequestContextPath());
//            ec.redirect(ec.getRequestContextPath() + this.mostRecentPage);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public Integer getSessionTimeLimitMin() {
        return sessionTimeLimitMin;
    }

    public Integer getIdleWarningTimeMin() {
        return idleWarningTimeMin;
    }

    public Integer getSecondsRemaining() {
        return secondsRemaining;
    }

    public String getMostRecentView() {
        return mostRecentView;
    }

}
