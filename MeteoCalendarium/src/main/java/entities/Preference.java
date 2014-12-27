/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import HelpClasses.PreferenceHelp;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author Alessandro
 */
/**
 *
 * @author home
 */@Entity
   @IdClass(PreferenceId.class)
public class Preference implements Serializable {
        
      private static final long serialVersionUID = 1L;

      @Id
      @ManyToOne
      private Event event= new Event();
      @Id
      @ManyToOne
      private MainCondition main= new MainCondition();

    public Preference() {
    }

    public Event getEvent() {
        return event;
    }
    
    public void setEvent(Event event) {
        this.event = event;
    }

    public MainCondition getMain() {
        return main;
    }

    public void setMain(MainCondition main) {
        this.main = main;
    }
      
    
    public Preference(Event ev,String pref)
    {
        main.setCondition(pref);
        setEvent(ev);
    }
    
     
     
     
     
     
     
     
     
}
