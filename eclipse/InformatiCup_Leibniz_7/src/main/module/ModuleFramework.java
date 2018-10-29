package main.module;

import java.awt.image.BufferedImage;

import javafx.application.Platform;
import main.evaluate.EvaluationResult;
import main.evaluate.EvaluationResult.Sign;
import main.evaluate.IEvaluator;
import main.evaluate.TrasiWebEvaluator;
import main.gui.Controller;

/**
 * A Framework that handles the running of modules in the background.
 * 
 * @author Fredo
 *
 */
public class ModuleFramework implements Runnable {

	/**
	 * A boolean indicating whether the currently running module should stop. It is
	 * set to true when the stopModule() method is called
	 */
	private boolean shouldStop = false;

	/** A boolean indicating whether a module is currently running */
	private boolean isRunning = false;

	/** The evaluator that is used to evaluate the generated Images */
	private IEvaluator evaluator;

	/** The module that is run by this framework */
	private IModuleIterate module;

	/**
	 * The controller of the GUI, which is called to update its interface when a new
	 * image is generated
	 */
	private Controller controller;

	/**
	 * Creates a new ModuleFramework and initializes the evaluator using the
	 * TrasiWebEvaluator.
	 * 
	 * @param controller
	 *            The controller of the GUI that is updated when an image is
	 *            generated
	 */
	public ModuleFramework(Controller controller) {
		evaluator = new TrasiWebEvaluator();
		this.controller = controller;
	}

	/**
	 * Starts the module by initializing it and by creating a thread that runs the
	 * module in the background.
	 * 
	 * @param module
	 *            The module that is run
	 * @param initImage
	 *            The initial image that is given to the module
	 * @param sign
	 *            The Sign of the initial image
	 */
	public void startModule(IModuleIterate module, BufferedImage initImage, Sign sign) {
		if (!isRunning) {
			module.setInitImage(initImage, sign);
			this.module = module;
			this.isRunning = true;

			Thread thread = new Thread(this);
			thread.setDaemon(true);
			thread.start();
		}
	}

	/**
	 * Stops the currently running module.
	 */
	public void stopModule() {
		if (isRunning)
			shouldStop = true;
	}

	/**
	 * The run method that is run by the thread when calling startModule. It runs in
	 * a loop until the stopModule method is called. The loop runs the following
	 * steps: generating a new image, evaluating it, giving the result to the
	 * module, updating the GUI.
	 */
	@Override
	public void run() {
		while (!shouldStop) {
			BufferedImage newImg = module.generateNextImage();

			try {
				EvaluationResult evalResult = evaluator.evaluateImage(newImg);
				module.setEvalResult(evalResult);

				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						controller.updateResultImage(newImg, evalResult);
					}
				});

				Thread.sleep(800);
			} catch (Exception e) {
				e.printStackTrace();
				stopModule();
				System.out.println("The image could not be evaluated!");
			}
		}
		shouldStop = false;
		isRunning = false;
	}
}
