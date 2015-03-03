package de.germanspacebuild.plugins.fasttravel.util;

import de.germanspacebuild.plugins.fasttravel.FastTravel;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by oneill011990 on 03.03.15.
 * Copyright
 *
 * @author oneill011990
 */
public class UpdateChecker {

    private FastTravel plugin;
    private URL url;

    private String version;
    private String link;

    public UpdateChecker(FastTravel plugin, String url) {
        this.plugin = plugin;

        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    public boolean updateFound(){
        try {
            InputStream instream = this.url.openConnection().getInputStream();
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(instream);

            Node latestFile = document.getElementsByTagName("item").item(0);
            NodeList children = latestFile.getChildNodes();

            this.version = children.item(1).getTextContent().replaceAll("[a-zA-Z ]", "");
            this.link = children.item(3).getTextContent();

            if (plugin.getDescription().getVersion() != this.version){
                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        return false;
    }

    public String getVersion() {
        return version;
    }

    public String getLink() {
        return link;
    }
}
