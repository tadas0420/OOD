import java.util.ArrayList;


/**
 * <p>Presentations keeps track of the slides in a presentation.</p>
 * <p>Only one instance of this class is available.</p>
 *
 * @author Ian F. Darwin, ian@darwinsys.com, Gert Florijn, Sylvia Stuurman
 * @version 1.6 2014/05/16 Sylvia Stuurman
 */

public class Presentation implements Subject {
    private String showTitle; //The title of the presentation
    private ArrayList<Slide> showList = null; //An ArrayList with slides
    private int currentSlideNumber = 0; //The number of the current slide
    private SlideViewerComponent slideViewComponent; //The view component of the slides

    private ArrayList<Observ> observers = new ArrayList<>();

    public Presentation() {
        slideViewComponent = null;
        clear();
    }

    public Presentation(SlideViewerComponent slideViewerComponent) {
        this.slideViewComponent = slideViewerComponent;
        registerObserver(slideViewerComponent);
        clear();
    }

    public int getSize() {
        return showList.size();
    }

    public String getTitle() {
        return showTitle;
    }

    public void setTitle(String nt) {
        showTitle = nt;
    }

    public void setShowView(SlideViewerComponent slideViewerComponent) {
        this.slideViewComponent = slideViewerComponent;
    }

    //Returns the number of the current slide
    public int getSlideNumber() {
        return currentSlideNumber;
    }

    //Change the current slide number and report it the the window
    public void setSlideNumber(int number) {
        currentSlideNumber = number;
        if (slideViewComponent != null) {
            slideViewComponent.update(this, getCurrentSlide());
            notifyObservers();
        }
    }

    //Navigate to the previous slide unless we are at the first slide
    public void prevSlide() {
        if (currentSlideNumber > 0) {
            setSlideNumber(currentSlideNumber - 1);
        }
    }

    //Navigate to the next slide unless we are at the last slide
    public void nextSlide() {
        if (currentSlideNumber < (showList.size() - 1)) {
            setSlideNumber(currentSlideNumber + 1);
        }
    }

    //Remove the presentation
    void clear() {
        showList = new ArrayList<>();
        setSlideNumber(-1);
    }

    //Add a slide to the presentation
    public void append(Slide slide) {
        showList.add(slide);
    }

    //Return a slide with a specific number
    public Slide getSlide(int number) {
        if (number < 0 || number >= getSize()) {
            return null;
        }
        return showList.get(number);
    }

    //Return the current slide
    public Slide getCurrentSlide() {
        return getSlide(currentSlideNumber);
    }

    public void exit(int n) {
        for (Observ observer : new ArrayList<>(observers)) { // Create a copy to avoid ConcurrentModificationException
            removeObserver(observer);
        }
        System.exit(n);
    }

    @Override
    public void registerObserver(Observ observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(Observ observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observ observer : observers) {
            observer.update(this, slideViewComponent.getSlide());
        }
    }
}
