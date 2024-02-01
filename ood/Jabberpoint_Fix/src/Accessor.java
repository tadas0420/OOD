import java.io.IOException;

/**
 * <p>An Accessor makes it possible to read and write data
 * for a presentation.</p>
 * <p>Non-abstract subclasses should implement the load and save methods.</p>
 *
 * @author Ian F. Darwin, ian@darwinsys.com, Gert Florijn, Sylvia Stuurman
 * @version 1.6 2014/05/16 Sylvia Stuurman
 */

public abstract class Accessor {
    public static final String DEMO_NAME = "Demo presentation";
    public static final String DEFAULT_EXTENSION = ".xml";

    public static Accessor getDemoAccessor() {
        return new XMLAccessor() {
            @Override
            public void loadFile(Presentation presentation, String unusedFilename) {
                this.loadDemoPresentation(presentation);
            }

            @Override
            public void saveFile(Presentation presentation, String filename) throws IOException {
                throw new UnsupportedOperationException("Saving a demo presentation is not supported.");
            }
        };
    }

    abstract public void loadFile(Presentation presentation, String filename) throws IOException;

    abstract public void saveFile(Presentation presentation, String filename) throws IOException;

}
