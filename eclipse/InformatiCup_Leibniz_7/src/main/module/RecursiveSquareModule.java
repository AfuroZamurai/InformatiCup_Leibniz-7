package main.module;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.PriorityQueue;

import main.evaluate.EvaluationResult;
import main.evaluate.IClassification;

public class RecursiveSquareModule implements IModuleIterate {

	private BufferedImage originalImage;
	private BufferedImage workingImage;
	private IClassification imageClass;
	private Color color = Color.GREEN;
	private int x = 0, y = 0;
	private int blockSize = 16;
	private int width, height;
	private int i;
	private WorkingBlock currentBlock;
	private boolean isFirst, isFinished = false, isDone = false;
	private boolean discoveryPhase;
	private float originalConfidence;
	private Graphics2D g;

	private PriorityQueue<WorkingBlock> queue = new PriorityQueue<>();

	private class WorkingBlock implements Comparable<WorkingBlock> {
		private int x, y, size;
		private float rating;

		public WorkingBlock(int x, int y, int size, float rating) {
			this.x = x;
			this.y = y;
			this.size = size;
			this.rating = rating;
		}

		@Override
		public int compareTo(WorkingBlock o) {
			if (rating < o.rating)
				return -1;
			if (rating > o.rating)
				return 1;
			return 0;
		}
	}

	@Override
	public BufferedImage generateNextImage() {
		if (isFirst) {
			return originalImage;
		} else if (isDone) {
			isFinished = true;
			return workingImage;
		} else if (discoveryPhase) {
			BufferedImage res = copyImage(originalImage);
			Graphics2D g = res.createGraphics();
			g.setColor(color);
			g.fillRect(x, y, blockSize, blockSize);

			return res;
		} else {
			BufferedImage res = copyImage(workingImage);
			Graphics2D g = res.createGraphics();
			g.setColor(color);
			
			g.fillRect(x, y, width, height);
			
			return res;
		}
	}

	@Override
	public void setEvalResult(EvaluationResult<IClassification> result) {
		float res = result.getConfidenceForClass(imageClass);

		if (isFirst) {
			originalConfidence = res;
			isFirst = false;
		} else {
			queue.add(new WorkingBlock(x, y, blockSize, res));

			if (res >= originalConfidence) {
				g.setColor(color);
				g.fillRect(x, y, blockSize, blockSize);
			}

			x += blockSize;
			if (x >= width) {
				x = 0;
				y += blockSize;
				if (y >= height) {
					y = 0;
					isDone = true;
				}
			}
		}
	}

	@Override
	public void setInitImage(BufferedImage img, IClassification imageClass) {
		originalImage = img;
		this.imageClass = imageClass;
		this.width = img.getWidth();
		this.height = img.getHeight();
		isFirst = true;
		isFinished = false;
		isDone = false;
		discoveryPhase = true;

		workingImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		g = workingImage.createGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
	}

	@Override
	public List<Parameter> getParameterList() {
		return null;
	}

	@Override
	public boolean isFinished() {
		return isFinished;
	}

	@Override
	public String getModuleDescription() {
		return "Blockrekursion: ";
	}

	/**
	 * Copies the content of a BufferedImage into a new BufferedImage object.
	 * 
	 * @param source
	 *            The BufferedImage whose content is copied
	 * @return The copy of the source image
	 */
	private BufferedImage copyImage(BufferedImage source) {
		BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
		Graphics g = b.getGraphics();
		g.drawImage(source, 0, 0, null);
		g.dispose();
		return b;
	}

	@Override
	public BufferedImage getResult() {
		return null;
	}
}
