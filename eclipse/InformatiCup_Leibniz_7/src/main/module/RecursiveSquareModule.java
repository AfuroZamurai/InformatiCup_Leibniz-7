package main.module;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ThreadLocalRandom;

import javafx.util.Pair;
import main.evaluate.EvaluationResult;
import main.evaluate.IClassification;

public class RecursiveSquareModule implements IModuleIterate {

	private BufferedImage originalImage;
	private BufferedImage workingImage;
	private IClassification imageClass;
	private Color color1 = Color.GREEN;
	private Color color2 = Color.WHITE;
	private int x = 0, y = 0;
	private int blockSize = 32;
	private int width, height;
	private int i;
	private WorkingBlock currentBlock;
	private boolean isFirst, isFinished = false, isDone = false;
	private boolean discoveryPhase;
	private float overallConfidence;
	private float confidenceColor1;
	private Graphics2D g;
	private int showResIn = 4;

	private PriorityQueue<WorkingBlock> queue = new PriorityQueue<>();

	private class WorkingBlock implements Comparable<WorkingBlock> {
		private int x, y, size;
		public float rating;
		private Color color;
		private WorkingBlock[] children = new WorkingBlock[4];
		private Pair<WorkingBlock, Integer> lastAddedChild;

		public WorkingBlock(int x, int y, int size, float rating, Color color) {
			this.x = x;
			this.y = y;
			this.size = size;
			this.rating = rating;
			this.color = color;
		}

		public WorkingBlock addChild(int position) {
			if (position >= 0 && position < 4) {
				int childSize = size / 2;
				int childX = (position % 2) * childSize + x;
				int childY = position / 2 * childSize + y;
				Color childColor = color == color1 ? color2 : color1;
				WorkingBlock child = new WorkingBlock(childX, childY, childSize, rating, childColor);
				children[position] = child;
				return child;
			}
			return null;
		}

		public void addRandomChild() {
			LinkedList<Pair<WorkingBlock, Integer>> list = new LinkedList<>();
			getAllChildren(list);
			int rndIndex = ThreadLocalRandom.current().nextInt(list.size());
			Pair<WorkingBlock, Integer> pair = list.get(rndIndex);
			pair.getKey().addChild(pair.getValue());
			lastAddedChild = pair;
		}

		public void removeLastAddedChild() {
			if (lastAddedChild != null) {
				lastAddedChild.getKey().children[lastAddedChild.getValue()] = null;
				lastAddedChild = null;
			}
		}

		private LinkedList<Pair<WorkingBlock, Integer>> getAllChildren(LinkedList<Pair<WorkingBlock, Integer>> list) {
			if (size == 2) {
				return list;
			} else {
				for (int i = 0; i < 4; i++) {
					WorkingBlock block = children[i];
					if (block == null) {
						list.add(new Pair<WorkingBlock, Integer>(this, i));
					} else {
						block.getAllChildren(list);
					}
				}
				return list;
			}
		}

		public void paint(Graphics2D g) {
			g.setColor(color);
			g.fillRect(x, y, size, size);
			g.setColor(Color.DARK_GRAY);
			g.drawRect(x, y, size, size);

			for (WorkingBlock block : children) {
				if (block != null) {
					block.paint(g);
				}
			}
		}

		@Override
		public int compareTo(WorkingBlock o) {
			if (rating < o.rating)
				return -1;
			if (rating > o.rating)
				return 1;
			return 0;
		}

		@Override
		public String toString() {
			return "(" + x + ", " + y + ", " + rating + ")";
		}
	}

	@Override
	public BufferedImage generateNextImage() {
		if (isFirst) {
			return originalImage;
		} else if (isDone) {
			// isFinished = true;
			return workingImage;
		} else if (discoveryPhase) {
			BufferedImage res = copyImage(originalImage);
			Graphics2D g = res.createGraphics();
			if (i % 2 == 0) {
				g.setColor(color1);
			} else {
				g.setColor(color2);
			}
			g.fillRect(x, y, blockSize, blockSize);

			return res;
		} else {
			BufferedImage res = copyImage(workingImage);
			Graphics2D g = res.createGraphics();
			if (ThreadLocalRandom.current().nextBoolean()) {
				int pos = ThreadLocalRandom.current().nextInt(2, 5);
				Iterator<WorkingBlock> iter = queue.iterator();
				for (int i = 0; i < pos; i++) {
					currentBlock = iter.next();
				}
			} else {
				currentBlock = queue.peek();
			}
			currentBlock.addRandomChild();
			currentBlock.paint(g);
			g.setColor(Color.DARK_GRAY);
			g.drawLine(0, height - 1, width, height - 1);
			g.drawLine(width - 1, 0, width - 1, height);
			
			return res;
		}
	}

	@Override
	public void setEvalResult(EvaluationResult<IClassification> result) {
		float res = result.getConfidenceForClass(imageClass);

		if (isFirst) {
			isFirst = false;

		} else {
			if (discoveryPhase) {
				WorkingBlock block;
				if (i % 2 == 0) {
					confidenceColor1 = res;
				} else if (confidenceColor1 < res) {
					block = new WorkingBlock(x, y, blockSize, res, color2);
					queue.add(block);
					block.paint(g);
				} else {
					block = new WorkingBlock(x, y, blockSize, confidenceColor1, color1);
					queue.add(block);
					block.paint(g);
				}

				i++;
				if (i % 2 == 0) {
					x += blockSize;
					if (x >= width) {
						x = 0;
						y += blockSize;
						if (y >= height) {
							y = 0;
							isDone = true;
							i = 0;
							discoveryPhase = false;
						}
					}
				}

			} else if (isDone) {
				for (WorkingBlock block : queue) {
					block.rating = res;
				}
				overallConfidence = res;
				isDone = false;
			} else {
				if (res > overallConfidence)
					overallConfidence = res;
				if (res >= overallConfidence) {					
					currentBlock.rating = res;
				} else {
					currentBlock.removeLastAddedChild();
				}
				currentBlock.paint(g);
				i++;
			}
			System.out.println("Best confidence: " + overallConfidence);
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
		queue.clear();
		currentBlock = null;
		x = 0;
		y = 0;
		i = 0;

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
