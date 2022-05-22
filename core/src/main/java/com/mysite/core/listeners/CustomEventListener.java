package com.mysite.core.listeners;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;

@Component(service = EventListener.class,immediate = true)
public class CustomEventListener implements EventListener {

    private static final Logger LOG = LoggerFactory.getLogger(CustomEventListener.class);

    @Reference
    private ResourceResolverFactory resolverFactory;

    private ResourceResolver resolver;

    @Reference
    private SlingRepository respository;

    private Session session;

    /**Activate method for initializaing stuff **/
    @Activate
    protected void activate(ComponentContext componentContext){

        LOG.info("Activating the observation ");


    }



    @Override
    public void onEvent(EventIterator events) {

    }
}
