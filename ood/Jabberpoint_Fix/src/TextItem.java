import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>A text item.</p>
 * <p>A text item has drawing capabilities.</p>
 *
 * @author Ian F. Darwin, ian@darwinsys.com, Gert Florijn, Sylvia Stuurman
 * @version 1.6 2014/05/16 Sylvia Stuurman
 */

public class TextItem extends SlideItem {
    private final String text;

    private static final String EMPTYTEXT = "No Text Given";

    //A textitem of int level with text string
    public TextItem(int level, String string) {
        super(level);
        text = string;
    }

    //An empty textitem
    public TextItem() {
        this(0, EMPTYTEXT);
    }

    //Returns the text
    public String getText() {
        return text == null ? "" : text;
    }

    //Returns the AttributedString for the Item
    public AttributedString getAttributedString(Style style, float scale) {
        AttributedString attrStr = new AttributedString(getText());
        attrStr.addAttribute(TextAttribute.FONT, style.getFont(scale), 0, text.length());
        return attrStr;
    }

    //Returns the bounding box of an Item
    private Dimension calculateSize(List<TextLayout> layouts, Style style, float scale) {
        int width = 0, height = (int) (style.leading * scale);
        for (TextLayout layout : layouts) {
            Rectangle2D bounds = layout.getBounds();
            width = Math.max(width, (int) bounds.getWidth());
            height += bounds.getHeight() + layout.getLeading() + layout.getDescent();
        }
        return new Dimension(width, height);
    }

    public Rectangle getBoundingBox(Graphics g, ImageObserver observer, float scale, Style myStyle) {
        List<TextLayout> layouts = getLayouts(g, myStyle, scale);
        Dimension size = calculateSize(layouts, myStyle, scale);
        return new Rectangle((int) (myStyle.indent * scale), 0, size.width, size.height);
    }

    //Draws the item
    public void draw(int x, int y, float scale, Graphics g, Style myStyle, ImageObserver o) {
        if (text == null || text.length() == 0) {
            return;
        }
        List<TextLayout> layouts = getLayouts(g, myStyle, scale);
        Point pen = new Point(x + (int) (myStyle.indent * scale), y + (int) (myStyle.leading * scale));
        prepareGraphics(g, myStyle);
        drawLayouts(layouts, pen, g);
    }

    // Prepare the graphics object for drawing
    private void prepareGraphics(Graphics g, Style style) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(style.color);
    }

    // Draw the list of text layouts
    private void drawLayouts(List<TextLayout> layouts, Point pen, Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        for (TextLayout layout : layouts) {
            pen.y += layout.getAscent();
            layout.draw(g2d, pen.x, pen.y);
            pen.y += layout.getDescent();
        }
    }

    private List<TextLayout> getLayouts(Graphics g, Style s, float scale) {
        List<TextLayout> layouts = new ArrayList<>();
        AttributedString attrStr = getAttributedString(s, scale);
        Graphics2D g2d = (Graphics2D) g;
        FontRenderContext frc = g2d.getFontRenderContext();
        LineBreakMeasurer measurer = new LineBreakMeasurer(attrStr.getIterator(), frc);
        float wrappingWidth = (Slide.WIDTH - s.indent) * scale;
        while (measurer.getPosition() < getText().length()) {
            TextLayout layout = measurer.nextLayout(wrappingWidth);
            layouts.add(layout);
        }
        return layouts;
    }

    public String toString() {
        return "TextItem[" + getLevel() + "," + getText() + "]";
    }
}
