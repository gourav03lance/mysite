package com.mysite.core.servlets;

import java.io.*;
import java.net.URL;
import java.rmi.ServerException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.AssetManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.servlets.HttpConstants;
import com.day.cq.commons.jcr.JcrConstants;
import org.apache.sling.api.resource.ValueMap;


@Component(
        service = { Servlet.class }, property = { " service.description=Servlet", " service.vendor=Adobe",
        " process.label=Id Validator Service", "sling.servlet.paths=/bin/sling/uploadimage",
        "sling.servlet.methods=GET" })
public class UploadImageServlet extends SlingAllMethodsServlet
{
    private static final long serialVersionUID = -293773910749832945L;
    String text;
    String heading;
    private static final Logger LOG = LoggerFactory.getLogger(UploadImageServlet.class);
    @Reference
    private ResourceResolverFactory resolverFactory;
    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServerException, IOException {
        response.setHeader("Content-Type", "text/html");
        PrintWriter pw = response.getWriter();
        pw.write("<h1>Reading image from desktop and injecting image to JCR DAM</h1>");
        ResourceResolver resolver;
        InputStream is = null;
        try
        {
            final boolean isMultipart = org.apache.commons.fileupload.servlet.ServletFileUpload.isMultipartContent(request);
            PrintWriter out = null;
            Map<String, Object> serviceMap = new HashMap<String, Object>();
            serviceMap.put(ResourceResolverFactory.SUBSERVICE,"reverseReplicationService");
            resolver = resolverFactory.getServiceResourceResolver(serviceMap);
            LOG.info(resolver.getUserID());
            AssetManager assetMgr = resolver.adaptTo(AssetManager.class);
            out = response.getWriter();
            if (isMultipart) {
                final java.util.Map<String, org.apache.sling.api.request.RequestParameter[]> params = request.getRequestParameterMap();
                for (final java.util.Map.Entry<String, org.apache.sling.api.request.RequestParameter[]> pairs : params.entrySet()) {
                    final String k = pairs.getKey();
                    final org.apache.sling.api.request.RequestParameter[] pArr = pairs.getValue();
                    final org.apache.sling.api.request.RequestParameter param = pArr[0];
                    final InputStream stream = param.getInputStream();
                    if (param.isFormField()) {
                        out.println("Form field " + k + " with value " + org.apache.commons.fileupload.util.Streams.asString(stream) + " detected.");
                    } else {
                        out.println("File field " + k + " with file name " + param.getFileName() + " detected.");
                        String fileName = param.getFileName();
                        String mimeType = "image/png";
                        //File fi = new File( fileName);
                        //is = new FileInputStream(fi);
                        String newFile = "/content/dam/mysite/user-generated-content/" + fileName;
                        Asset asset = assetMgr.createAsset(newFile, stream, mimeType, true);
                       // saveCustomMetadataInfo(pw, asset);
                        resolver.commit();
                        pw.write("<p>File uploaded</p>");
                    }
                }
            }
        }
        catch (Exception e) {
            pw.write(e.getMessage());
        }

    }

    void saveCustomMetadataInfo(PrintWriter pw, final Asset asset) {
        Resource assetResource = asset.adaptTo(Resource.class);
        Calendar lastModified = Calendar.getInstance();
        lastModified.setTimeInMillis(lastModified.getTimeInMillis());
        if (assetResource != null) {
           // assetResource = assetResource.getChild(JcrConstants.JCR_CONTENT + "/metadata");
            assetResource = assetResource.getChild(JcrConstants.JCR_CONTENT);
        }
        if (assetResource != null) {
            Node node = assetResource.adaptTo(Node.class);
            try {
                node.setProperty("cq:distribute",true);
                node.setProperty("cq:lastModified",Calendar.getInstance());
                node.setProperty(Property.JCR_LAST_MODIFIED,lastModified);
                //node.getSession().save();
                //node.getSession().logout();
                pw.write("<p>Metadta is set</p>");
            } catch (RepositoryException e) {
                e.printStackTrace();
            }
            /*ValueMap assetProperties = assetResource.adaptTo(ModifiableValueMap.class);
            if (assetProperties != null) {
                assetProperties.put(Property.JCR_LAST_MODIFIED, lastModified);
                assetProperties.put("cq:distribute", String.valueOf(true));
                assetProperties.put("cq:lastModified", Calendar.getInstance().toString());
                pw.write("<p>Metadta is set</p>");
            }*/
        }
    }

}