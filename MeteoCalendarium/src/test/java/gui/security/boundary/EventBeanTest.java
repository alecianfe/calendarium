/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.security.boundary;

import gui.security.boundary.EventBean;
import business.security.help.EventCreation;
import business.security.exception.OverlappingException;
import business.security.boundary.EventManager;
import business.security.boundary.IDEventManager;
import business.security.control.MailSenderManager;
import business.security.control.OwmClientInterface;
import business.security.boundary.PreferenceManager;
import business.security.boundary.UserEventManager;
import business.security.boundary.UserManager;
import business.security.entity.Event;
import business.security.entity.IDEvent;
import business.security.entity.Place;
import business.security.entity.Users;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import org.junit.Test;
import org.mockito.Matchers;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.primefaces.context.RequestContext;

/**
 *
 * @author home
 */
public class EventBeanTest {

    private Users u = new Users();
    private Long now = new Long(String.valueOf(new java.util.Date().getTime() + 100000000));
    private Long now2 = new Long(String.valueOf(now + 111111111));
    private Timestamp sd = new Timestamp(now);
    private Timestamp ed = new Timestamp(now2);
    private EventManager em;
    private EntityManager entityManager;
    private String t = "Test";
    private Place p = new Place();
    private EventCreation beanEvent = new EventCreation();
    private EventBean eb = new EventBean();

    @Test
    public void createEventTest() throws OverlappingException {

        EventBean eb = new EventBean();
        EventCreation beanEvent = new EventCreation();
        beanEvent.setPlace("Ciao");

        initB(beanEvent);

        List<Event> listEvent = new ArrayList<Event>();

        Event e1 = new Event();

        OwmClientInterface weather = mock(OwmClientInterface.class);
        eb.setWeather(weather);

        FacesContext fc = mock(FacesContext.class);
        RequestContext rc = mock(RequestContext.class);
        EventManager eM = mock(EventManager.class);
        PreferenceManager pm = mock(PreferenceManager.class);

        IDEventManager idm = mock(IDEventManager.class);
        when(idm.findFirstFreeID()).thenReturn(new Long(-1));

        UserManager um = mock(UserManager.class);
        when(um.getLoggedUser()).thenReturn(u);
        when(um.findByMail(t)).thenReturn(u);

        MailSenderManager mailSender = mock(MailSenderManager.class);

        UserEventManager uem = mock(UserEventManager.class);

        eb.setIdm(idm);
        eb.c = fc;
        eb.r = rc;
        eb.setBeanEvent(beanEvent);
        eb.setUm(um);
        eb.setStartDate(new Date(beanEvent.getStartDate().getTime()));
        eb.setEndDate(new Date(beanEvent.getEndDate().getTime()));
        eb.setEm(eM);
        eb.setPm(pm);
        eb.setMailSender(mailSender);
        eb.setUem(uem);

        //Creazione semplice
        eb.create();

        List<String> p = new ArrayList();
        p.add("Clear");
        List<String> u = new ArrayList();
        u.add("pippo");

        eb.setSelectedPreferences(p);
        eb.setSelectedUsers(u);

        EventCreation beanEvent2 = new EventCreation();
        beanEvent2.setPlace("Ciao");
        eb.setBeanEvent(beanEvent2);
        //Creazione con preferenze ed invitati
        eb.create();

    }

    @Test
    public void modifyEventTest() {

        EventBean eb = new EventBean();
        EventCreation beanEvent = new EventCreation();
        EventCreation tempEvent = new EventCreation();
        beanEvent.setPlace("Ciao");
        tempEvent.setPlace("Ciao");

        initB(beanEvent);

        List<Event> listEvent = new ArrayList<Event>();

        Event e1 = new Event();

        OwmClientInterface weather = mock(OwmClientInterface.class);
        eb.setWeather(weather);

        FacesContext fc = mock(FacesContext.class);
        RequestContext rc = mock(RequestContext.class);

        EventManager eM = mock(EventManager.class);
        when(eM.loadSpecificEvent((String) Matchers.anyObject())).thenReturn(e1);

        PreferenceManager pm = mock(PreferenceManager.class);

        IDEventManager idm = mock(IDEventManager.class);
        when(idm.findFirstFreeID()).thenReturn(new Long(-1));

        UserManager um = mock(UserManager.class);
        when(um.getLoggedUser()).thenReturn(u);
        when(um.findByMail(t)).thenReturn(u);

        MailSenderManager mailSender = mock(MailSenderManager.class);

        UserEventManager uem = mock(UserEventManager.class);

        eb.setIdm(idm);
        eb.c = fc;
        eb.r = rc;
        eb.setBeanEvent(beanEvent);
        eb.setTempEvent(tempEvent);
        eb.setUm(um);
        eb.setStartDate(new Date(beanEvent.getStartDate().getTime()));
        eb.setEndDate(new Date(beanEvent.getEndDate().getTime()));
        eb.setEm(eM);
        eb.setPm(pm);
        eb.setMailSender(mailSender);
        eb.setUem(uem);

        //Simple
        eb.modify();

        List<String> p = new ArrayList();
        p.add("Clear");
        List<String> u = new ArrayList();
        u.add("pippo");

        initB(beanEvent);
        eb.setBeanEvent(beanEvent);

        //With Preference
        eb.modify();

    }

    @Test
    public void deleteEventTest() {

        EventBean eb = new EventBean();
        EventCreation e =new EventCreation();
        e.setPlace("Ciao");
        eb.setBeanEvent(e);

        List<Event> listEvent = new ArrayList<Event>();

        Event e1 = new Event();
        Event e2 = new Event();

        listEvent.add(e1);
        listEvent.add(e2);

        OwmClientInterface weather = mock(OwmClientInterface.class);
        eb.setWeather(weather);


        FacesContext fc = mock(FacesContext.class);
        RequestContext rc = mock(RequestContext.class);
        EventManager eM = mock(EventManager.class);

        eb.c = fc;
        eb.r = rc;
        eb.setEm(eM);

        eb.cancel();

    }

    private EventBean init(EventBean eb) {


        eb.setEndDate(ed);
        eb.setStartDate(sd);
        eb.setIsOwnEvent(true);
        eb.setCreating(true);

        return eb;
    }

    private EventCreation initB(EventCreation beanEvent) {
        u.setEmail("test@test.test");
        u.setGroupName("USERS");
        u.setPassword("test");
        u.setPublicCalendar(true);
        p.setCity("Test");
        beanEvent.setCreator(u);
        beanEvent.setDescription("Test");
        beanEvent.setEndDate(ed);
        IDEvent id = new IDEvent("-1");
        long l = 111;
        id.setId(l);
        beanEvent.setIdEvent(id);
        beanEvent.setOutdoor(false);
        beanEvent.setPlace("posto");
        beanEvent.setPublicEvent(true);
        beanEvent.setStartDate(sd);
        beanEvent.setTitle(t);

        return beanEvent;
    }
}
