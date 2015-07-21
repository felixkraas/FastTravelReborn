/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2011-2015 CraftyCreeper, minebot.net, oneill011990
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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

            int versionInt = Integer.parseInt(this.version.replaceAll("[a-zA-Z., ]-", ""));
            int oldInt = Integer.parseInt(plugin.getDescription().getVersion().replaceAll("[a-zA-Z., ]", ""));

            if (this.version.replaceAll("[a-zA-Z., ]", "").length() < 3) {
                versionInt = versionInt * 10;
            } else if (plugin.getDescription().getVersion().replaceAll("[a-zA-Z., ]-", "").length() < 3) {
                oldInt = oldInt * 10;
            }

            if (oldInt < versionInt){
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
