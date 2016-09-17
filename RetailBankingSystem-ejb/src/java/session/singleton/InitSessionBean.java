package session.singleton;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import session.stateless.EjbTimerSessionLocal;

@Singleton
@LocalBean
@Startup
public class InitSessionBean {
    @EJB
    private EjbTimerSessionLocal ejbTimerSessionLocal;
    
    @PostConstruct
    public void init()
    {
        ejbTimerSessionLocal.createTimer10000MS();
        ejbTimerSessionLocal.createTimer300000MS();
    }
}
