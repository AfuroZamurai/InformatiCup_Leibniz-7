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

	private boolean shouldStop = false;
	private boolean isRunning = false;
	private IEvaluator evaluator;
	private IModuleIterate module;
	private Controller controller;

	public ModuleFramework(Controller controller) {
		evaluator = new TrasiWebEvaluator();
		this.controller = controller;
	}

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

	public void stopModule() {
		if (isRunning)
			shouldStop = true;
	}

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
