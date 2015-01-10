/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Event;
import entities.MainCondition;
import entities.Place;
import entities.Preference;
import entities.User;
import entities.UserEvent;
import entities.iDEvent;
import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import managerBeans.EventManagerInterface;
import managerBeans.IDEventManagerInterface;
import managerBeans.PreferenceManagerInterface;
import managerBeans.UserEventManagerInterface;
import managerBeans.UserManagerInterface;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author alessandro
 */
@Named
@RequestScoped
public class ImportBean {
    
    @EJB
    private EventManagerInterface em;
    @EJB
    private UserManagerInterface um;
    @EJB
    private IDEventManagerInterface idm;
    @EJB
    private PreferenceManagerInterface pm;
    @EJB
    private UserEventManagerInterface uem;
    public ImportBean(){}
    
     public void imports() {
    
        removeEvents();
        File file;
        Event event = new Event();
        
       
        System.out.println("Import begin");
        
            file = new File("C:\\Users\\alessandro\\Documents\\NetBeansProjects\\MeteoCalendarium\\MeteoCalendarium\\src\\main\\import.xml");
          
        Timestamp help;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	    org.w3c.dom.Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            
            NodeList events = doc.getElementsByTagName("event");
            
            for (int i = 0; i < events.getLength(); i++) {
                Element element = (Element) events.item(i);
                NodeList titles = element.getElementsByTagName("title");
                event.setTitle(titles.item(0).getTextContent());
                
                NodeList descriptions = element.getElementsByTagName("description");
                event.setDescription(descriptions.item(0).getTextContent());
                
                NodeList startDates = element.getElementsByTagName("startDate");
                
                //next line gets error
                SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd hh:mm:ss");

    Date parsedTimeStamp = dateFormat.parse(startDates.item(0).getTextContent());

    Timestamp timestamp = new Timestamp(parsedTimeStamp.getTime());
                
                
                
                
                event.setStartDate(timestamp);
                System.out.println(event.getStartDate().toString());
                NodeList endDates = element.getElementsByTagName("endDate");
                

    parsedTimeStamp = dateFormat.parse(endDates.item(0).getTextContent());

     timestamp = new Timestamp(parsedTimeStamp.getTime());
                event.setEndDate(timestamp);  
               
                NodeList outdoors = element.getElementsByTagName("outdoor");
                if(outdoors.item(0).getTextContent().equalsIgnoreCase("1"))
                    event.setOutdoor(true);
                else
                    event.setOutdoor(false);
                
                NodeList publics = element.getElementsByTagName("publicEvent");
                if(publics.item(0).getTextContent().equalsIgnoreCase("1"))
                    event.setPublicEvent(true);
                else
                    event.setPublicEvent(false);
               
                
                NodeList places = element.getElementsByTagName("place");
                Place place = new Place();
                place.setCity(places.item(0).getTextContent());
                event.setPlace(place);
               
                NodeList users = element.getElementsByTagName("creator");
                User user = new User();
                
                user.setEmail(users.item(0).getTextContent());
              
                event.setCreator(user);
              
                
                long id;
            id=idm.findMax();
            iDEvent idEv = new iDEvent();
            idEv.setId(id);
            event.setIdEvent(idEv);
            em.addEvent(event);
            NodeList preferences = element.getElementsByTagName("preference");
            Preference pref;
            MainCondition main;
            for(int k=0;k<preferences.getLength();k++)
            {
                pref=new Preference();
                pref.setEvent(event);
                main=new MainCondition();
                main.setCondition(preferences.item(k).getTextContent());
                pref.setMain(main);
                pm.addPreference(pref);
            }
            NodeList invites = element.getElementsByTagName("invitated");
            UserEvent userEvent;
            userEvent=new UserEvent(event, um.getLoggedUser(), true);
                uem.addUserEvent(userEvent);
            for(int j=0;j<invites.getLength();j++)
            {
                user=new User();
                user=um.findByMail(invites.item(j).getTextContent());
                if(user!=null){
                userEvent=new UserEvent(event, user, false);
                uem.addUserEvent(userEvent);
                }
                
            }
            
            
                
              
                
                
                
                
            }
        } catch (Exception e) {
            System.out.println("Exception");
        }
        
    }
     public void removeEvents()
     {
         //prima elimini le preference e le userEvent, poi l'evento e il suo id
         em.removeAllEvent(um.getLoggedUser());
     
     }
    }

