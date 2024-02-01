import java.io.*;
import java.util.*;
import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class XMLAccessor extends Accessor {

    private static final String SHOWTITLE = "showtitle";
    private static final String SLIDETITLE = "title";
    private static final String SLIDE = "slide";
    private static final String ITEM = "item";
    private static final String LEVEL = "level";
    private static final String KIND = "kind";
    private static final String TEXT = "text";
    private static final String IMAGE = "image";

    private String getTitle(Element element, String tagName) {
        NodeList titles = element.getElementsByTagName(tagName);
        return titles.item(0).getTextContent();
    }

    public void loadFile(Presentation presentation, String filename) throws IOException {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(new File(filename));
            Element doc = document.getDocumentElement();

            loadPresentationTitle(presentation, doc);
            loadSlides(presentation, doc);
        } catch (IOException | SAXException | ParserConfigurationException ex) {
            handleLoadFileError(ex);
        }
    }

    private void loadPresentationTitle(Presentation presentation, Element doc) {
        String showTitle = getTitle(doc, SHOWTITLE);
        presentation.setTitle(showTitle);
    }

    private void loadSlides(Presentation presentation, Element doc) {
        NodeList slides = doc.getElementsByTagName(SLIDE);
        for (int slideNumber = 0; slideNumber < slides.getLength(); slideNumber++) {
            Element xmlSlide = (Element) slides.item(slideNumber);
            Slide slide = createSlide(xmlSlide);
            presentation.append(slide);
        }
    }

    private Slide createSlide(Element xmlSlide) {
        String slideTitle = getTitle(xmlSlide, SLIDETITLE);
        Slide slide = new Slide();
        slide.setTitle(slideTitle);

        NodeList slideItems = xmlSlide.getElementsByTagName(ITEM);
        for (int itemNumber = 0; itemNumber < slideItems.getLength(); itemNumber++) {
            Element item = (Element) slideItems.item(itemNumber);
            loadSlideItem(slide, item);
        }

        return slide;
    }

    private void loadSlideItem(Slide slide, Element item) {
        int level = 1;
        NamedNodeMap attributes = item.getAttributes();
        String levelText = attributes.getNamedItem(LEVEL).getTextContent();
        if (levelText != null) {
            try {
                level = Integer.parseInt(levelText);
            } catch (NumberFormatException e) {
                handleInvalidLevelError();
            }
        }

        String type = attributes.getNamedItem(KIND).getTextContent();
        if (TEXT.equals(type)) {
            slide.append(new TextItem(level, item.getTextContent()));
        } else if (IMAGE.equals(type)) {
            slide.append(new BitmapItem(level, item.getTextContent()));
        } else {
            handleUnknownTypeError();
        }
    }

    public void saveFile(Presentation presentation, String filename) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(filename))) {
            out.println("<?xml version=\"1.0\"?>");
            out.println("<!DOCTYPE presentation SYSTEM \"jabberpoint.dtd\">");
            out.println("<presentation>");
            out.print("<showtitle>");
            out.print(presentation.getTitle());
            out.println("</showtitle>");

            for (int slideNumber = 0; slideNumber < presentation.getSize(); slideNumber++) {
                Slide slide = presentation.getSlide(slideNumber);
                out.println("<slide>");
                out.println("<title>" + slide.getTitle() + "</title>");
                Vector<SlideItem> slideItems = slide.getSlideItems();

                for (int itemNumber = 0; itemNumber < slideItems.size(); itemNumber++) {
                    SlideItem slideItem = slideItems.elementAt(itemNumber);
                    saveSlideItem(out, slideItem);
                }

                out.println("</slide>");
            }

            out.println("</presentation>");
        }
    }

    private void saveSlideItem(PrintWriter out, SlideItem slideItem) {
        out.print("<item kind=");
        if (slideItem instanceof TextItem) {
            out.print("\"text\" level=\"" + slideItem.getLevel() + "\">");
            out.print(((TextItem) slideItem).getText());
        } else if (slideItem instanceof BitmapItem) {
            out.print("\"image\" level=\"" + slideItem.getLevel() + "\">");
            out.print(((BitmapItem) slideItem).getName());
        } else {
            handleUnknownTypeError();
        }
        out.println("</item>");
    }

    private void handleLoadFileError(Exception ex) {
        System.err.println("Error loading XML file: " + ex.getMessage());
    }

    private void handleInvalidLevelError() {
        System.err.println("Invalid level format in XML file.");
    }

    private void handleUnknownTypeError() {
        System.err.println("Unknown element type in XML file.");
    }

    public void loadDemoPresentation(Presentation presentation) {
        presentation.setTitle("Demo Presentation");

        Slide slide = new Slide();
        slide.setTitle("JabberPoint");
        slide.append(1, "The Java presentation tool");
        slide.append(2, "Copyright (c) 1996-2000: Ian Darwin");
        slide.append(2, "Copyright (c) 2000-now: Gert Florijn and Sylvia Stuurman");
        slide.append(4, "Calling Jabberpoint without a filename will show this presentation");
        slide.append(1, "Navigate:");
        slide.append(3, "Next slide: PgDn or Enter");
        slide.append(3, "Previous slide: PgUp or up-arrow");
        slide.append(3, "Quit: q or Q");
        presentation.append(slide);

        slide = new Slide();
        slide.setTitle("Demonstration of levels and styles");
        slide.append(1, "Level 1");
        slide.append(2, "Level 2");
        slide.append(1, "Again level 1");
        slide.append(1, "Level 1 has style number 1");
        slide.append(2, "Level 2 has style number 2");
        slide.append(3, "This is how level 3 looks like");
        slide.append(4, "And this is level 4");
        presentation.append(slide);

        slide = new Slide();
        slide.setTitle("The third slide");
        slide.append(1, "To open a new presentation,");
        slide.append(2, "use File->Open from the menu.");
        slide.append(1, " ");
        slide.append(1, "This is the end of the presentation.");
        slide.append(new BitmapItem(1, "JabberPoint.jpg"));
        presentation.append(slide);
    }
}
