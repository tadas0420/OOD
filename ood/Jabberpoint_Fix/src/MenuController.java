import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;

public class MenuController extends MenuBar {

	private final Frame parent;
	private final Presentation presentation;
	private final Accessor xmlAccessor;

	// Constants for menu item names and messages
	private static final String ABOUT = "About";
	private static final String FILE = "File";
	private static final String EXIT = "Exit";
	private static final String GOTO = "Go to slide";
	private static final String HELP = "Help";
	private static final String NEW = "New";
	private static final String NEXT = "Next";
	private static final String OPEN = "Open";
	private static final String PREV = "Prev";
	private static final String SAVE = "Save";
	private static final String VIEW = "View";
	private static final String IOEX = "IO Exception: ";
	private static final String LOADERR = "Load Error";
	private static final String SAVEERR = "Save Error";

	public MenuController(Frame frame, Presentation pres, Accessor accessor) {
		this.parent = frame;
		this.presentation = pres;
		this.xmlAccessor = accessor;

		setupFileMenu();
		setupViewMenu();
		setupHelpMenu();
	}

	private void setupFileMenu() {
		Menu fileMenu = new Menu(FILE);
		fileMenu.add(createMenuItem(OPEN, e -> openPresentation()));
		fileMenu.add(createMenuItem(NEW, e -> newPresentation()));
		fileMenu.add(createMenuItem(SAVE, e -> savePresentation()));
		fileMenu.addSeparator();
		fileMenu.add(createMenuItem(EXIT, e -> presentation.exit(0)));
		add(fileMenu);
	}

	private void setupViewMenu() {
		Menu viewMenu = new Menu(VIEW);
		viewMenu.add(createMenuItem(NEXT, e -> presentation.nextSlide()));
		viewMenu.add(createMenuItem(PREV, e -> presentation.prevSlide()));
		viewMenu.add(createMenuItem(GOTO, e -> goToSlide()));
		add(viewMenu);
	}

	private void setupHelpMenu() {
		Menu helpMenu = new Menu(HELP);
		helpMenu.add(createMenuItem(ABOUT, e -> showAbout()));
		setHelpMenu(helpMenu);
	}

	private MenuItem createMenuItem(String name, java.awt.event.ActionListener listener) {
		MenuItem menuItem = new MenuItem(name);
		menuItem.addActionListener(listener);
		return menuItem;
	}

	public void openPresentation() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Open Presentation File");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("XML Files", "xml");
		fileChooser.setFileFilter(filter);

		int result = fileChooser.showOpenDialog(parent);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			presentation.clear();

			try {
				xmlAccessor.loadFile(presentation, selectedFile.getAbsolutePath());
				presentation.setSlideNumber(0);
			} catch (IOException exc) {
				JOptionPane.showMessageDialog(parent, IOEX + exc, LOADERR, JOptionPane.ERROR_MESSAGE);
			}
			parent.repaint();
		}
	}

	private void newPresentation() {
		presentation.clear();
		parent.repaint();
	}

	private void savePresentation() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Save Presentation");
		fileChooser.setSelectedFile(new File("savedPresentation.xml"));
		FileNameExtensionFilter filter = new FileNameExtensionFilter("XML Files", "xml");
		fileChooser.setFileFilter(filter);

		int userSelection = fileChooser.showSaveDialog(null);

		if (userSelection == JFileChooser.APPROVE_OPTION) {
			File fileToSave = fileChooser.getSelectedFile();
			// Ensure the file has the correct extension
			if (!fileToSave.getName().toLowerCase().endsWith(".xml")) {
				fileToSave = new File(fileToSave.getAbsolutePath() + ".xml");
			}
			try {
				xmlAccessor.saveFile(presentation, fileToSave.getAbsolutePath());
			} catch (IOException exc) {
				JOptionPane.showMessageDialog(null, IOEX + exc, SAVEERR, JOptionPane.ERROR_MESSAGE);
			}
		}
	}


	private void goToSlide() {
		int slideCount = presentation.getSize();
		String pageNumberStr = JOptionPane.showInputDialog(parent, "Enter slide number (1-" + slideCount + ")");
		try {
			int pageNumber = Integer.parseInt(pageNumberStr);
			if (pageNumber >= 1 && pageNumber <= slideCount) {
				presentation.setSlideNumber(pageNumber - 1);
			} else {
				JOptionPane.showMessageDialog(parent, "Invalid slide number", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(parent, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void showAbout() {
		JOptionPane.showMessageDialog(parent,
				"JabberPoint is a primitive slide-show program in Java(tm). It is freely copyable as long as you keep this notice and the splash screen intact.\n" +
						"Copyright (c) 1995-1997 by Ian F. Darwin, ian@darwinsys.com. Adapted by Gert Florijn (version 1.1) and Sylvia Stuurman (version 1.2 and higher) for the OpenUniversity of the Netherlands, 2002 -- now.\n" +
						"Author's version available from http://www.darwinsys.com/",
					"About JabberPoint", JOptionPane.INFORMATION_MESSAGE);
	}

	public MenuItem mkMenuItem(String name) {
		return new MenuItem(name, new MenuShortcut(name.charAt(0)));
	}
}
