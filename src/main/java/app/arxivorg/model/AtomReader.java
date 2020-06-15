package app.arxivorg.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public class AtomReader {
    /*
        ATTRIBUTES
    */

    private String content;
    private Articles articles;

    /*
        CONSTRUCTOR
    */

    public AtomReader(String content) {
        this.content = content;

        try {
            articles=read();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
        FUNCTIONS
    */

    public Articles read() throws ParserConfigurationException, IOException {
        ArrayList<Article> myArticles= new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = convertStringToXMLDocument( this.content );
        Element rootElement = doc.getDocumentElement();
        for (int i = 0; i < rootElement.getChildNodes().getLength(); i++) {
            boolean lever= false;
            if(rootElement.getChildNodes().item(i).getNodeName().equals("entry")){
                NodeList entry = rootElement.getChildNodes().item(i).getChildNodes();
                String id = entry.item(1).getTextContent().replace("http", "https");
                String published = entry.item(3).getTextContent();
                String updated = entry.item(5).getTextContent();
                String title = entry.item(7).getTextContent().replaceAll("\n", " ");
                String summary = entry.item(9).getTextContent().replaceAll("\n", " ");
                String principalCat ="not find" ;
                ArrayList<String> myCategories = new ArrayList<>(0);
                ArrayList<String> myAuthors = new ArrayList<>(0);
                String linkPdf = "";
                String commentary ="";
                for (int j = 10; j <entry.getLength()  ; j++) {
                    Node myChild = entry.item(j);
                    if(myChild.getNodeName().equals("category")){
                        if(lever){
                            myCategories.add(myChild.getAttributes().item(1).getTextContent());
                        }else{
                            principalCat = myChild.getAttributes().item(1).getTextContent();
                            lever = true;
                        }
                    }else if(myChild.getNodeName().equals("author")){
                        myAuthors.add(myChild.getChildNodes().item(1).getTextContent());
                    }else if(myChild.getNodeName().equals("link") && myChild.getAttributes().getNamedItem("title")!=null ){
                        linkPdf = myChild.getAttributes().getNamedItem("href").getTextContent().replace("http", "https") + ".pdf";
                    }else if(myChild.getNodeName().equals("arxiv:comment")){
                        commentary = myChild.getTextContent();
                    }
                }
                 myArticles.add(new Article(
                         id,
                         published,
                         updated,
                         title,
                         summary,
                         new Authors(myAuthors),
                         commentary,
                         linkPdf,
                         myCategories,
                         principalCat
                         ));
            }
        }

        return new Articles(myArticles);
    }

    private static Document convertStringToXMLDocument(String xmlString)
    {
        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try
        {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();

            //Parse the content to Document object
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /*
        GETTERS
    */

    public String getContent() {
        return content;
    }

    public Articles getArticles() {
        return articles;
    }
}