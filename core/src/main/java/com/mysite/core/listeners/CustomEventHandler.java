package com.mysite.core.listeners;

import org.apache.sling.api.SlingConstants;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true,service = EventHandler.class,property = {
        Constants.SERVICE_DESCRIPTION+"=This event handler listens the events on Page Activation",
        EventConstants.EVENT_TOPIC+"=org/apache/sling/api/resource/Resource/ADDED",
        EventConstants.EVENT_TOPIC+"=org/apache/sling/api/resource/Resource/CHANGED",
        EventConstants.EVENT_FILTER+ "(&" + "(path=/content/we-retail/us/en/*/jcr:content) (|("+ SlingConstants.PROPERTY_CHANGED_ATTRIBUTES+
                "=*jcr:title)" + "(" + ResourceChangeListener.CHANGES+"=*jcr:title)))"})
public class CustomEventHandler implements EventHandler {

    private  static final Logger LOG = LoggerFactory.getLogger(CustomEventHandler.class);
    @Override
    public void handleEvent(Event event) {

        LOG.info("Event is: {} "+event.getTopic());

    }
}
