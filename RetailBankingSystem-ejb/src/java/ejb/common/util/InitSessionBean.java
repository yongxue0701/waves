package ejb.common.util;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;

@Singleton
@LocalBean
@Startup
public class InitSessionBean {

    @EJB
    private EjbTimerSessionBeanLocal ejbTimerSessionLocal;

    @PostConstruct
    public void init() {
//        ejbTimerSessionLocal.createTimer10000MS();
//        ejbTimerSessionLocal.createTimer300000MS();
//        ejbTimerSessionLocal.createTimer15000MS();
//        ejbTimerSessionLocal.createTimer70000MS();
//        ejbTimerSessionLocal.createTimer2000MS();
//        ejbTimerSessionLocal.createTimer5000MS();
//        ejbTimerSessionLocal.createTimer30000MS();
//        ejbTimerSessionLocal.cancelTimer300000MSDashboard();
    }
}
