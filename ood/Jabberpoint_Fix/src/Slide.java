import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.util.Vector;

/** <p>A slide. This class has drawing functionality.</p>
 * @author Ian F. Darwin, ian@darwinsys.com, Gert Florijn, Sylvia Stuurman
 * @version 1.1 2002/12/17 Gert Florijn
 * @version 1.2 2003/11/19 Sylvia Stuurman
 * @version 1.3 2004/08/17 Sylvia Stuurman
 * @version 1.4 2007/07/16 Sylvia Stuurman
 * @version 1.5 2010/03/03 Sylvia Stuurman
 * @version 1.6 2014/05/16 Sylvia Stuurman
 */

public class Slide {
	public final static int WIDTH = 1200;
	public final static int HEIGHT = 800;
	protected String title; //The title is kept separately
	protected Vector<SlideItem> items; //The SlideItems are kept in a vector

	public Slide() {
		items = new Vector<>();
	}

	//Add a SlideItem
	public void append(SlideItem anItem) {
		items.addElement(anItem);
	}

	//Return the title of a slide
	public String getTitle() {
		return title;
	}

	//Change the title of a slide
	public void setTitle(String newTitle) {
		title = newTitle;
	}

	//Create a TextItem out of a String and add the TextItem
	public void append(int level, String message) {
		append(new TextItem(level, message));
	}

	//Returns the SlideItem
	public SlideItem getSlideItem(int number) {
		return items.elementAt(number);
	}

	//Return all the SlideItems in a vector
	public Vector<SlideItem> getSlideItems() {
		return items;
	}

	//Returns the size of a slide
	public int getSize() {
		return items.size();
	}

	//Draws the slide
	public void draw(Graphics g, Rectangle area, ImageObserver view) {
		float scale = getScale(area);
		int y = drawTitle(g, area, view, scale);
		drawItems(g, area, view, scale, y);
	}

	// Draws the title of the slide and returns the Y position for the next item
	private int drawTitle(Graphics g, Rectangle area, ImageObserver view, float scale) {
		SlideItem titleItem = new TextItem(0, getTitle());
		Style titleStyle = Style.getStyle(titleItem.getLevel());
		titleItem.draw(area.x, area.y, scale, g, titleStyle, view);
		return area.y + titleItem.getBoundingBox(g, view, scale, titleStyle).height;
	}

	// Draws all slide items starting from a specific Y position
	private void drawItems(Graphics g, Rectangle area, ImageObserver view, float scale, int startY) {
		int y = startY;
		for (SlideItem item : items) {
			Style itemStyle = Style.getStyle(item.getLevel());
			item.draw(area.x, y, scale, g, itemStyle, view);
			y += item.getBoundingBox(g, view, scale, itemStyle).height;
		}
	}

	//Returns the scale to draw a slide
	private float getScale(Rectangle area) {
		return Math.min(((float)area.width) / ((float)WIDTH), ((float)area.height) / ((float)HEIGHT));
	}
}
