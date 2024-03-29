/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.security.boundary;

import gui.security.boundary.InvitationBean;
import business.security.help.EventCreation;
import business.security.boundary.EventManager;
import business.security.boundary.UserEventManager;
import business.security.boundary.UserManager;
import business.security.entity.Event;
import business.security.entity.Place;
import business.security.entity.Users;
import business.security.entity.UserEvent;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author Alessandro
 */
public class InvitationBeanTest {

    Query query = mock(Query.class);

    private EventCreation init(String title) {

        EventCreation e1 = new EventCreation();
        e1.setTitle(title);
        e1.setPlace("posto");
        return e1;
    }

    private Event initEvent(String title) {
         Place p = new Place();
        p.setCity("posto");
        Event e1 = new Event();
        e1.setPlace(p);
        e1.setTitle(title);
        return e1;
    }

    @Test
    public void invitationInList() {
        EventManager em = mock(EventManager.class);
        UserManager um = mock(UserManager.class);
        InvitationBean ib = new InvitationBean();

        Users userTest = new Users();
        userTest.setEmail("gigi@mail.it");
        userTest.setGroupName("USERS");
        userTest.setPassword("pippo");
        userTest.setPublicCalendar(true);

        List<Event> list2 = new ArrayList<>();
        Event eventtemp1 = this.initEvent("prova1");
        Event eventtemp2 = this.initEvent("prova2");

        EventCreation e1 = this.init("prova1");
        EventCreation e2 = this.init("prova3");

        list2.add(eventtemp1);
        list2.add(eventtemp2);
        when(um.getLoggedUser()).thenReturn(userTest);
        when(em.findInvitatedEvent(um.getLoggedUser())).thenReturn(list2);

        ib.um = um;
        ib.em = em;
        ib.setInvites(new ArrayList<EventCreation>());
        ib.loadInvites();
        List<EventCreation> temp = ib.getInvites();

        assertTrue(temp.get(0).getTitle().equals(e1.getTitle()));
        assertTrue(!temp.get(1).getTitle().equals(e1.getTitle()));
    }

    @Test
    public void noInvitationInList() {
        EventManager em = mock(EventManager.class);
        UserManager um = mock(UserManager.class);
        InvitationBean ib = new InvitationBean();

        Users userTest = new Users();
        userTest.setEmail("gigi@mail.it");
        userTest.setGroupName("USERS");
        userTest.setPassword("pippo");
        userTest.setPublicCalendar(true);

        List<Event> list2 = new ArrayList<>();

        EventCreation e1 = this.init("No Invitation");

        when(um.getLoggedUser()).thenReturn(userTest);

        when(em.findInvitatedEvent(um.getLoggedUser())).thenReturn(list2);

        ib.um = um;
        ib.em = em;
        ib.setInvites(new ArrayList<EventCreation>());
        ib.loadInvites();
        List<EventCreation> temp = ib.getInvites();

        assertTrue(temp.get(0).getTitle().equals(e1.getTitle()));
        assertTrue(temp.size() == 1);
    }

    @Test
    public void testAcceptInvite() {

        EventManager em = mock(EventManager.class);
        UserManager um = mock(UserManager.class);
        UserEventManager uem = mock(UserEventManager.class);
        InvitationBean ib = new InvitationBean();

        Users userTest = new Users();
        userTest.setEmail("gigi@mail.it");
        userTest.setGroupName("USERS");
        userTest.setPassword("pippo");
        userTest.setPublicCalendar(true);

        UserEvent userEvent = new UserEvent();

        List<Event> list2 = new ArrayList<>();



        Event eventtemp1 = this.initEvent("prova1");
        Event eventtemp2 = this.initEvent("prova2");


        EventCreation e1 = this.init("prova1");
        EventCreation e2 = this.init("prova2");

        list2.add(eventtemp1);
        list2.add(eventtemp2);

        when(em.findInvitatedEvent(userTest)).thenReturn(list2);
        when(um.getLoggedUser()).thenReturn(userTest);
        when(em.searchOverlapping(eventtemp1, um.getLoggedUser())).thenReturn(false);
        when(uem.getUserEventofUser(eventtemp1, um.getLoggedUser())).thenReturn(userEvent);

        ib.um = um;
        ib.em = em;
        ib.uem = uem;
        ib.setInvites(new ArrayList<EventCreation>());
        ib.loadInvites();
        String ret;
        ret = ib.acceptInvite(e1);

        assertTrue(ret.equalsIgnoreCase("calendar?faces-redirect=true"));
    }

}
