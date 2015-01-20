/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import entity.Event;
import entity.User;
import entity.UserEvent;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * manager of entity UserEvent
 *
 * @author alessandro
 */
@Stateless
public class UserEventManager implements UserEventManagerInterface {

    @PersistenceContext
    private EntityManager em;

    @Inject
    Principal principal;

    @Override
    public void addUserEvent(UserEvent userEvent) {
        em.merge(userEvent);
    }

    /**
     * this funtion searches who is the creator of an event
     *
     * @param event
     * @return the creator
     */
    @Override
    public User findEventCreator(Event event) {
        Query query;
        query = em.createQuery("SELECT ue.user From UserEvent ue WHERE ue.event= :event and ue.creator=true").setParameter("event", event);
        List<User> user = query.getResultList();
        return user.get(0);

    }

    /**
     * this funtion return the tuple in UserEvent entity that match with the
     * passed parametes
     *
     * @param event
     * @param user
     * @return
     */
    @Override
    public UserEvent getUserEventofUser(Event event, User user) {
        Query query;
        query = em.createQuery("SELECT ue FROM UserEvent ue WHERE ue.event= :event and ue.creator=0 and ue.user = :user  ").setParameter("event", event).setParameter("user", user);
        List<UserEvent> result = query.getResultList();
        return result.get(0);
    }

    /**
     * this funtion modifies a tuple in UserEvent setting new values
     *
     * @param userEvent
     * @param decision
     * @param view
     */
    @Override
    public void modifyUserEvent(UserEvent userEvent, boolean decision, boolean view) {

        Query query = em
                .createQuery("UPDATE UserEvent ue SET ue.accepted = :decision , ue.view = :view"
                        + " WHERE ue= :userEvent");
        query.setParameter("userEvent", userEvent);
        query.setParameter("decision", decision);
        query.setParameter("view", view);
        query.executeUpdate();
    }

    /**
     * this function searches for invitation of an event
     *
     * @param event
     * @return a list of emails corresponding to invitees
     */
    @Override
    public List<String> invitedUsersOfEvent(Event event) {
        Query query;
        query = em.createQuery("SELECT ue.user.email FROM UserEvent ue WHERE ue.event= :event and ue.creator=0").setParameter("event", event);

        List<String> result = new ArrayList<>(query.getResultList());

        return result;
    }

    /**
     * this funcion deletes all tuples that have as event the passed parameter
     *
     * @param event
     */
    @Override
    public void deleteUserEvent(Event event) {
        Query query2 = em.createQuery("Delete From UserEvent ue Where ue.event= :event").setParameter(("event"), event);
        query2.executeUpdate();
    }

    /**
     *
     * @return all users that have created at least an event
     */
    @Override
    public List<User> getUsersCreator() {

        Query query = em.createQuery("Select distinct ue.user From UserEvent ue Where ue.creator=1");
        return query.getResultList();

    }

    /**
     *
     * @param event
     * @return a list of users that have accepted the invitation for the passed
     * event
     */
    @Override
    public List<User> getInvitedWhoAccepted(Event event) {

        Query query = em.createQuery("Select ue.user From UserEvent ue Where ue.event= :event and ue.accepted=1").setParameter("event", event);
        return query.getResultList();

    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }
}
