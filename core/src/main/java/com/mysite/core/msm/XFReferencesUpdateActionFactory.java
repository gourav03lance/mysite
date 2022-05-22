package com.mysite.core.msm;

import com.adobe.cq.xf.ExperienceFragmentsServiceFactory;
import com.day.cq.wcm.api.WCMException;
import com.day.cq.wcm.msm.api.LiveActionFactory;
import com.day.cq.wcm.msm.api.LiveRelationshipManager;
import com.day.cq.wcm.msm.api.RolloutManager;
import com.day.cq.wcm.msm.commons.BaseAction;
import com.day.cq.wcm.msm.commons.BaseActionFactory;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.regex.Pattern;

@Component(service = LiveActionFactory.class, property={LiveActionFactory.LIVE_ACTION_NAME+"="
        +XFReferencesUpdateActionFactory.LIVE_ACTION_CLASS_NAME,
        LiveActionFactory.LIVE_ACTION_NAME+"="+XFReferencesUpdateActionFactory.LIVE_ACTION_CLASS_NAME})

public class XFReferencesUpdateActionFactory extends BaseActionFactory<BaseAction> {

    /** The Constant LIVE_ACTION_CLASS_NAME. */
    public static final String LIVE_ACTION_CLASS_NAME = "XFReferencesUpdateAction";

    /** The Constant LIVE_ACTION_NAME. */
    public static final String LIVE_ACTION_NAME = "linksUpdateXF";

    /** The Constant CONTENT_PATH_REGEXP. */
    private static final String CONTENT_PATH_REGEXP = "/content/(we-retail)[\\w\\-/]*";

    /** The Constant CONTENT_PATH_PATTERN. */
    private static final Pattern CONTENT_PATH_PATTERN = Pattern.compile(CONTENT_PATH_REGEXP);

    @Reference
    private RolloutManager rolloutManager;

    @Reference
    private LiveRelationshipManager relationshipManager;

    @Reference
    ExperienceFragmentsServiceFactory experienceFragmentsServiceFactory;



    @Override
    protected BaseAction newActionInstance(ValueMap valueMap) throws WCMException {
        return null;
    }

    @Override
    public String createsAction() {
        return null;
    }
}
