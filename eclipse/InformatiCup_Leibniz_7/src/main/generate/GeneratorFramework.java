package main.generate;

import java.awt.image.BufferedImage;

import javafx.application.Platform;
import main.evaluate.EvaluationResult;
import main.evaluate.IClassification;
import main.evaluate.IEvaluator;
import main.evaluate.TrasiWebEvaluator;
import main.gui.Controller;

/**
 * A Framework that handles the running of modules in the background.
 * 
 * @author Fredo
 *
 */
public class GeneratorFramework implements Runnable {

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
	private IGenerator module;

	/**
	 * The controller of the GUI, which is called to update its interface when a new
	 * image is generated
	 */
	private Controller controller;

	/**
	 * The time the framework waits between iterations, if it is -1, the default
	 * implementation is chosen (one iteration takes exactly one second)
	 */
	private int delayTime;

	/**
	 * The maximum number of iterations for one generator, if it is -1, there is no
	 * limit
	 */
	private int maxIterations;

	/** Counter of the current iteration */
	private int iterationCounter;

	/**
	 * Creates a new ModuleFramework and initializes the evaluator using the
	 * TrasiWebEvaluator.
	 * 
	 * @param controller
	 *            The controller of the GUI that is updated when an image is
	 *            generated
	 */
	public GeneratorFramework(Controller controller) {
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
	 * @param imageClass
	 *            The Image Class of the initial image
	 */
	public void startModule(IGenerator module, BufferedImage initImage, IClassification imageClass, int delayTime,
			int maxIterations) {
		if (!isRunning) {
			module.setInitImage(initImage, imageClass);
			this.module = module;
			this.isRunning = true;
			this.delayTime = delayTime;
			this.maxIterations = maxIterations;
			iterationCounter = 1;

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
			long startTime = System.currentTimeMillis();
			BufferedImage newImg;
			if (module.isFinished()) {
				newImg = module.getResult();
			} else {
				newImg = module.generateNextImage();
			}

			try {
				EvaluationResult<IClassification> evalResult = evaluator.evaluateImage(newImg);

				if (!module.isFinished())
					module.setEvalResult(evalResult);

				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						controller.updateResultImage(newImg, evalResult);
					}
				});

				long sleeptime = 0;
				if (delayTime >= 0) {
					sleeptime = delayTime - System.currentTimeMillis() + startTime;
				} else {
					sleeptime = 1000 - System.currentTimeMillis() + startTime;
				}
				if (sleeptime > 0)
					Thread.sleep(sleeptime);

				if (maxIterations >= 0)
					iterationCounter++;
			} catch (Exception e) {
				e.printStackTrace();
				stopModule();
				System.out.println("The image could not be evaluated!");
			}

			if (module.isFinished() || iterationCounter == maxIterations) {
				// this may not be the most elegant way to stop the module
				// (this is just simulating the stop button)
				controller.cancellation(null);
			}
		}
		shouldStop = false;
		isRunning = false;

		BufferedImage resultImage = module.getResult();
		if (resultImage != null) {
			try {
				EvaluationResult<IClassification> evalResult = evaluator.evaluateImage(resultImage);

				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						controller.updateResultImage(resultImage, evalResult);
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("The image could not be evaluated");
			}
		}
	}
}
