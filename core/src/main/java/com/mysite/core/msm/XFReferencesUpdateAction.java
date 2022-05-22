package com.mysite.core.msm;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;
import com.day.cq.wcm.commons.ReferenceSearch;
import com.day.cq.wcm.msm.api.LiveAction;
import com.day.cq.wcm.msm.api.LiveRelationship;
import com.day.cq.wcm.msm.commons.BaseAction;
import com.day.cq.wcm.msm.commons.BaseActionFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.*;
import java.util.Collections;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XFReferencesUpdateAction extends BaseAction {

    /** The log. */
    private Logger log = LoggerFactory.getLogger(XFReferencesUpdateAction.class);

    /** The Constant LIVE_ACTION_CLASS_NAME. */
    public static final String LIVE_ACTION_CLASS_NAME = "XFReferencesUpdateAction";

    /** The Constant LIVE_ACTION_NAME. */
    public static final String LIVE_ACTION_NAME = "linksUpdateXF";

    /** The Constant CONTENT_PATH_REGEXP. */
    private static final String CONTENT_PATH_REGEXP = "/content/(we-retail)[\\w\\-/]*";

    /** The Constant CONTENT_PATH_PATTERN. */
    private static final Pattern CONTENT_PATH_PATTERN = Pattern.compile(CONTENT_PATH_REGEXP);



    protected XFReferencesUpdateAction(ValueMap config, BaseActionFactory<? extends LiveAction> liveActionFactory) {
        super(config, liveActionFactory);
    }

    @Override
    protected boolean handles(Resource resource, Resource resource1, LiveRelationship liveRelationship, boolean b)
            throws RepositoryException, WCMException {
        return false;
    }

    @Override
    protected void doExecute(Resource source, Resource target, LiveRelationship liveRelationship, boolean b) throws
            RepositoryException, WCMException {
        ResourceResolver resolver = target.getResourceResolver();
        PageManager pageManager = resolver.adaptTo(PageManager.class);

        Page targetPage = pageManager.getPage(liveRelationship.getLiveCopy().getPath());
        String sourcePath = source.getPath();
        Resource sourceRoot = resolver.getResource(sourcePath);
        Node sourceNode = sourceRoot.adaptTo(Node.class);
        PropertyIterator pi = sourceNode.getProperties();
        while(pi.hasNext()){
            Property property = pi.nextProperty();
            if(property.isMultiple()){
                for(Value value : property.getValues()) {
                    processSingleValue(value, resolver, target, pageManager, targetPage);
                }
            }
        }
    }

    private void processSingleValue(Value value,ResourceResolver resolver,Resource target,PageManager pageManager,Page targetPage)
            throws RepositoryException {

        if(value.getType()!=PropertyType.STRING){
             return;
        }
        String ctaPath = value.getString();

        if(ctaPath==null || !ctaPath.startsWith("/content/we-retail")){
            return;
        }
        Matcher pathMatcher = CONTENT_PATH_PATTERN.matcher(ctaPath);
        while(pathMatcher.find()){
            Resource cta = resolver.getResource(pathMatcher.group());


        }

    }

    private void adjustReferences(PageManager pageManager, Resource cta, Resource target, Page targetPage)
            throws RepositoryException {
        if (Objects.nonNull(target) && !target.getPath().contains("countryselector") && Objects.nonNull(cta)) {
            String[] array = targetPage.getPath().replace("/content/experience-fragments/we-retail/", StringUtils.EMPTY)
                    .split("/");
            Page ctaPage = pageManager.getPage(cta.getPath());
            String targetCountryLanguage = array[0] + "/" + array[1];
            String ctaCountryLanguage = CommonUtils.getCountryCode(ctaPage) + "/"
                    + CommonUtils.getLanguageCode(ctaPage);
            String targetPagePath = cta.getPath().replace(ctaCountryLanguage, targetCountryLanguage);
            new ReferenceSearch().adjustReferences(target.adaptTo(Node.class), cta.getPath(), targetPagePath, false,
                    Collections.emptySet());
        }
    }



    }

