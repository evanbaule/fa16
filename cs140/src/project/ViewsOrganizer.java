package project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.Observable;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ViewsOrganizer extends Observable {

	private MachineModel model;
	private CodeViewPanel codeViewPanel;
	private MemoryViewPanel memoryViewPanel1;
	private MemoryViewPanel memoryViewPanel2;
	private MemoryViewPanel memoryViewPanel3;
	private ControlPanel controlPanel;
	private ProcessorViewPanel processorPanel;
	private MenuBarBuilder menuBuilder;
	private JFrame frame;
	private FilesManager filesManager;
	private Animator animator;

	public void step() {
		System.out.println("Confirmed stepping once per; added for testing purposes");
		if (model.getCurrentState() != States.PROGRAM_HALTED && model.getCurrentState() != States.NOTHING_LOADED) {
			try {
				model.step();
			} catch (CodeAccessException e) {
				JOptionPane.showMessageDialog(frame, "Illegal access to code from line " + model.getInstructionPointer()
						+ "\n" + "Exception message: " + e.getMessage(), "Run time error", JOptionPane.OK_OPTION);
			} catch (ArrayIndexOutOfBoundsException e) {
				JOptionPane.showMessageDialog(frame, "Index out of Bounds from line " + model.getInstructionPointer()
						+ "\n" + "Exception message: " + e.getMessage(), "Run time error", JOptionPane.OK_OPTION);
			} catch (NullPointerException e) {
				JOptionPane.showMessageDialog(frame, "Null pointer from line " + model.getInstructionPointer()
						+ "\n" + "Exception message: " + e.getMessage(), "Run time error", JOptionPane.OK_OPTION);
			} catch (IllegalArgumentException e) {
				JOptionPane.showMessageDialog(frame, "Illegal Argument from line " + model.getInstructionPointer()
						+ "\n" + "Exception message: " + e.getMessage(), "Run time error", JOptionPane.OK_OPTION);
			} catch (DivideByZeroException e) {
				JOptionPane.showMessageDialog(frame, "Divide by Zero Exception from line " + model.getInstructionPointer()
						+ "\n" + "Exception message: " + e.getMessage(), "Run time error", JOptionPane.OK_OPTION);
			}
			setChanged();
			notifyObservers();
		}
	}

	public void execute() {
		while (model.getCurrentState() != States.PROGRAM_HALTED && model.getCurrentState() != States.NOTHING_LOADED) {
			try {
				model.step();
			} catch (CodeAccessException e) {
				JOptionPane.showMessageDialog(frame, "Illegal access to code from line " + model.getInstructionPointer()
						+ "\n" + "Exception message: " + e.getMessage(), "Run time error", JOptionPane.OK_OPTION);
			} catch (ArrayIndexOutOfBoundsException e) {
				JOptionPane.showMessageDialog(frame, "Index out of bounds from line " + model.getInstructionPointer()
						+ "\n" + "Exception message: " + e.getMessage(), "Run time error", JOptionPane.OK_OPTION);
			} catch (NullPointerException e) {
				JOptionPane.showMessageDialog(frame, "Null Pointer from line " + model.getInstructionPointer()
						+ "\n" + "Exception message: " + e.getMessage(), "Run time error", JOptionPane.OK_OPTION);
			} catch (IllegalArgumentException e) {
				JOptionPane.showMessageDialog(frame, "Illegal Argument from line " + model.getInstructionPointer()
						+ "\n" + "Exception message: " + e.getMessage(), "Run time error", JOptionPane.OK_OPTION);
			} catch (DivideByZeroException e) {
				JOptionPane.showMessageDialog(frame, "Divide by Zero Exceptionfrom line " + model.getInstructionPointer()
						+ "\n" + "Exception message: " + e.getMessage(), "Run time error", JOptionPane.OK_OPTION);
			}

		}
		setChanged();
		notifyObservers();
	}

	public MachineModel getModel() {
		return model;
	}

	public void setModel(MachineModel model) {
		this.model = model;
	}

	private void createAndShowGUI() {
		animator = new Animator(this);
		filesManager = new FilesManager(this);
		filesManager.initialize();

		codeViewPanel = new CodeViewPanel(this, model);

		memoryViewPanel1 = new MemoryViewPanel(this, model, 0, 240);
		memoryViewPanel2 = new MemoryViewPanel(this, model, 240, Memory.DATA_SIZE / 2);
		memoryViewPanel3 = new MemoryViewPanel(this, model, Memory.DATA_SIZE / 2, Memory.DATA_SIZE);

		controlPanel = new ControlPanel(this);
		processorPanel = new ProcessorViewPanel(this, model);
		menuBuilder = new MenuBarBuilder(this);

		frame = new JFrame("Simulator");

		Container content = frame.getContentPane();

		content.setLayout(new BorderLayout(1, 1));
		content.setBackground(Color.BLACK);

		content.setSize(1200, 600);

		JPanel center = new JPanel();
		center.setLayout(new GridLayout(1, 3));

		frame.add(codeViewPanel.createCodeDisplay(), BorderLayout.LINE_START);
		center.add(memoryViewPanel1.createMemoryDisplay(), BorderLayout.LINE_START);
		center.add(memoryViewPanel2.createMemoryDisplay(), BorderLayout.LINE_START);
		center.add(memoryViewPanel3.createMemoryDisplay(), BorderLayout.LINE_START);

		frame.add(center, BorderLayout.CENTER);
		// Put in a comment that we have to "return HERE for the other GUI
		// components."

		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(WindowListenerFactory.windowClosingFactory(e -> exit()));
		model.setCurrentState(States.NOTHING_LOADED);

		frame.add(controlPanel.createControlDisplay(), BorderLayout.PAGE_END);
		frame.add(processorPanel.createProcessorDisplay(), BorderLayout.PAGE_START);

		model.getCurrentState().enter();
		setChanged();
		notifyObservers();
		animator.start();
		// return HERE for other setup details
		frame.setVisible(true);

		JMenuBar bar = new JMenuBar();
		frame.setJMenuBar(bar);
		bar.add(menuBuilder.createFileMenu());
		bar.add(menuBuilder.createExecuteMenu());
		bar.add(menuBuilder.createJobsMenu());
		
		frame.pack();

	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ViewsOrganizer organizer = new ViewsOrganizer();
				MachineModel model = new MachineModel(() -> organizer.setCurrentState(States.PROGRAM_HALTED));
				organizer.setModel(model);
				organizer.createAndShowGUI();
			}
		});
	}

	public States getCurrentState() {
		return model.getCurrentState();
	}

	public void setCurrentState(States currentState) {
		
		if (currentState == States.PROGRAM_HALTED) {
			animator.setAutoStepOn(false);
		}
		model.setCurrentState(currentState);
		model.getCurrentState().enter();
		setChanged();
		notifyObservers();
	}

	public void exit() { // method executed when user exits the program
		int decision = JOptionPane.showConfirmDialog(frame, "Do you really wish to exit?", "Confirmation",
				JOptionPane.YES_NO_OPTION);
		if (decision == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

	public Frame getFrame() {
		return frame;
	}

	public void clearJob() {
		model.clearJob();
		model.setCurrentState(States.NOTHING_LOADED);
		model.getCurrentState().enter();
		setChanged();
		notifyObservers("Clear");
	}

	public void toggleAutoStep() {
		animator.toggleAutoStep();
		if (animator.isAutoStepOn()) {
			model.setCurrentState(States.AUTO_STEPPING);
		} else {
			model.setCurrentState(States.PROGRAM_LOADED_NOT_AUTOSTEPPING);

		}
		model.getCurrentState().enter();
		setChanged();
		notifyObservers();

	}

	public void reload() {
		animator.setAutoStepOn(false);
		clearJob();
		filesManager.finalLoad_ReloadStep(model.getCurrentJob());
	}

	public void assembleFile() {
		filesManager.assembleFile();
	}

	public void loadFile(Job j) {
		j = model.getCurrentJob();
		filesManager.loadFile(j); //
	}

	public void setPeriod(int period) {
		animator.setPeriod(period);
	}

	public void setJob(int i) {
		model.setJob(i);
		if (model.getCurrentState() != null) {
			model.getCurrentState().enter();
			setChanged();
			notifyObservers();
		}

	}

	public void loadFile() {
		filesManager.loadFile(model.getCurrentJob());
	}

	public void makeReady(String string) {
		animator.setAutoStepOn(false);
		model.setCurrentState(States.PROGRAM_LOADED_NOT_AUTOSTEPPING);

		model.getCurrentState().enter();
		setChanged();
		notifyObservers(string);
	}

}
