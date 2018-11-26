package main.generate;

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

public class RecursiveSquareGenerator implements IGenerator {

	private BufferedImage originalImage;
	private BufferedImage workingImage;
	private BufferedImage bestImage;
	private IClassification imageClass;
	private Color[] colors = new Color[3];
	private int x = 0, y = 0;
	private int blockSize = 32;
	private int width, height;
	private int i, blockCounter;
	private WorkingBlock currentBlock;
	private WorkingBlock[] blocks = new WorkingBlock[4];
	private boolean isFirst, isFinished = false, isDone = false;
	private boolean discoveryPhase;
	private float overallConfidence;
	private float[] confidenceColors = new float[colors.length];
	private Graphics2D g;

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
				Color childColor = pickNewColor();
				WorkingBlock child = new WorkingBlock(childX, childY, childSize, rating, childColor);
				children[position] = child;
				return child;
			}
			return null;
		}

		public WorkingBlock addChild(int position, Color color) {
			if (position >= 0 && position < 4) {
				int childSize = size / 2;
				int childX = (position % 2) * childSize + x;
				int childY = position / 2 * childSize + y;
				Color childColor = color;
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

		/**
		 * Paints the block using the given graphics object.
		 * 
		 * @param g
		 *            The graphics object used to draw the block
		 */
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

		private Color pickNewColor() {
			int rnd = ThreadLocalRandom.current().nextInt(colors.length - 1);
			int counter = 0;
			for (int i = 0; i < colors.length; i++) {
				if (color == colors[i])
					continue;
				if (counter == rnd)
					return colors[i];
				counter++;
			}
			return color;
		}
	}

	@Override
	public BufferedImage generateNextImage() {
		if (isFirst) {
			return originalImage;
		} else if (isDone) {
			return workingImage;
		} else if (discoveryPhase) {
			BufferedImage res = copyImage(originalImage);
			Graphics2D g = res.createGraphics();
			for (int j = 0; j < colors.length; j++) {
				if (i % colors.length == j) {
					g.setColor(colors[j]);
				}
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
				for (int j = 0; j < colors.length; j++) {
					if (i % colors.length == j) {
						confidenceColors[j] = res;
					}
				}
				int indexMax = 0;
				int indexMax2 = 0;
				float max = 0f;
				if (i % colors.length == colors.length - 1) {
					for (int j = 0; j < colors.length; j++) {// selecting the color with max confidence
						if (confidenceColors[j] >= max) {
							max = confidenceColors[j];
							indexMax2 = indexMax;
							indexMax = j;
						}
					}

					if (blockCounter < 4) {
						block = new WorkingBlock(x, y, blockSize, max, colors[indexMax]);
						queue.add(block);
						blocks[blockCounter] = block;
						block.paint(g);
					} else {
						block = blocks[blockCounter % 4];
						int position = 0;
						if (blockCounter % 4 == 0) {
							position = 3;
						} else if (blockCounter % 4 == 1) {
							position = 2;
						} else if (blockCounter % 4 == 2) {
							position = 1;
						} else if (blockCounter % 4 == 3) {
							position = 0;
						}
						if (blockCounter < 8) {
							Color childColor = colors[indexMax];
							if (colors[indexMax] == block.color)
								childColor = colors[indexMax2];
							block.addChild(position, childColor);
							block.paint(g);
						} else {
							block = block.children[position];
							Color blockColor = colors[indexMax];
							if (colors[indexMax] == block.color)
								blockColor = colors[indexMax2];
							if (blockCounter >= 16) {
								position = ThreadLocalRandom.current().nextInt(4);
								int rnd = ThreadLocalRandom.current().nextInt(colors.length);
								blockColor = colors[rnd];
								//TODO: here should be a new random phase + alternating colors
							}
							block.addChild(position, blockColor);
							block.paint(g);
						}
					}
					blockCounter++;

				}

				// advancing the block further: left -> right, top -> bottom
				i++;
				if (i % colors.length == 0) {
					x += blockSize;
					int endX = width;
					// quick try out! this has to be changed if it works out
					
					if (blockSize == 16)
						endX = 48;
					if (blockSize == 8)
						endX = 40;
					
					if (x >= endX) {
						int startX = 0;
						
						if (blockSize == 16)
							startX = 16;
						if (blockSize == 8)
							startX = 24;
						
						x = startX;
						y += blockSize;
						int endY = height;
						
						if (blockSize == 16)
							endY = 48;
						if (blockSize == 8)
							endY = 40;
						
						if (y >= endY) {
							int startY = 0;
							
							if (blockSize == 32) {
								startY = 16;
								startX = 16;
							}

							if (blockSize == 16) {
								startY = 24;
								startX = 24;
							}
							
							y = startY;
							x = startX;
							isDone = true;
							i = 0;
							discoveryPhase = false;
						}
					}
				}

			} else if (isDone) {
				for (WorkingBlock block : queue) {
					block.rating = 1 - res;
				}
				overallConfidence = res;
				isDone = false;
				bestImage = copyImage(workingImage);
				if (overallConfidence <= 0.01f) {
					discoveryPhase = true;
					blockSize = blockSize / 2;
					//queue.clear();
				}
			} else {
				if (overallConfidence >= 0.9f)
					isFinished = true;
				if (res > overallConfidence)
					overallConfidence = res;
				if (res >= overallConfidence) {
					currentBlock.rating = 1 - res;
					currentBlock.paint(g);
					g.setColor(Color.DARK_GRAY);
					g.drawLine(0, height - 1, width, height - 1);
					g.drawLine(width - 1, 0, width - 1, height);
					bestImage = copyImage(workingImage);
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
		colors[0] = Color.WHITE;
		colors[1] = Color.RED;
		colors[2] = Color.BLUE;
		queue.clear();
		currentBlock = null;
		blockSize = 32;
		overallConfidence = 0f;
		bestImage = null;
		x = 0;
		y = 0;
		i = 0;
		blockCounter = 0;

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
		return bestImage;
	}
}
