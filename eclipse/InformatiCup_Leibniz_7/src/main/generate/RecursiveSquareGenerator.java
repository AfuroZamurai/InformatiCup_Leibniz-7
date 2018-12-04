package main.generate;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ThreadLocalRandom;

import javafx.util.Pair;
import main.evaluate.EvaluationResult;
import main.evaluate.IClassification;

/**
 * This Generator tries to create images consisting of many colored squared
 * blocks. These blocks are arranged in a recursive structure using the private
 * class WorkingBlock.
 * 
 * The Generator works in two main phases: exploration phase and convergence
 * phase. The goal of the exploration phase is to find a starting image of
 * confidence greater 0 using a starting image of very high confidence. Then in
 * the convergence phase the starting image is altered by adding blocks to find
 * an image of confidence greater 0.9 . During this phase the generator uses a
 * greedy approach: Changes that increase the confidence are stored while those
 * that decrease it are discarded.
 * 
 * 
 * @author Fredo
 */
public class RecursiveSquareGenerator implements IGenerator {

	/** The starting Image */
	private BufferedImage originalImage;

	/** Current working image */
	private BufferedImage workingImage;

	/** Best image found so far */
	private BufferedImage bestImage;

	/** Target class for the image */
	private IClassification imageClass;

	/** Array of available colors for the blocks */
	private Color[] colors = new Color[3];

	/** Current x coordinate of blocks */
	private int x = 0;

	/** Current y coordinate of blocks */
	private int y = 0;

	/** Current working block size */
	private int blockSize = 32;

	/** width of the images */
	private int width;

	/** Height of the images */
	private int height;

	/** Counter for iterations */
	private int i;

	/** Counter blocks in discovery phase */
	private int blockCounter;

	/** The current WorkingBlock - used after exploration phase */
	private WorkingBlock currentBlock;

	/** An array of the four main blocks in each corner */
	private WorkingBlock[] blocks = new WorkingBlock[4];

	/** Indicates the first iteration. */
	private boolean isFirst;

	/** Indicates whether the generator is finished. */
	private boolean isFinished = false; // true when confidence > 90%

	/** Indicates whether a step in the exploration phase is done. */
	private boolean isDone = false;

	/** Indicates whether the discovery phase in the exploration phase is active. */
	private boolean discoveryPhase;

	/** Indicates whether the random phase in the exploration phase is active. */
	private boolean randomPhase = false;

	/** The highest confidence found so far */
	private float overallConfidence;

	/**
	 * An array to store confidence values for the available colors when iterating
	 * through them
	 */
	private float[] confidenceColors = new float[colors.length];

	/** The graphics object of the workingImage */
	private Graphics2D g;

	/**
	 * The parameter that can be set by the user via the GUI; it enables automatic
	 * stopping of the generator
	 */
	private Parameter stoppingParameter = new Parameter("Automatisches Stoppen", "Ja: der Generator"
			+ " stoppt bei einem Bild über 90% - Nein: der Generator stoppt nur bei " + "Betätigung des Stop-Buttons",
			true);

	/** A priority queue used for ordering the four main blocks via their rating */
	private PriorityQueue<WorkingBlock> queue = new PriorityQueue<>();

	/**
	 * The WorkingBlock defines a block or square of a specific color. Each block
	 * can contain four smaller WorkingBlocks making this a recursive structure. The
	 * idea is that the generator creates images of four WorkingBlocks in each
	 * corner which each can have multiple sub blocks.
	 * 
	 * @author Fredo
	 */
	private class WorkingBlock implements Comparable<WorkingBlock> {
		/** The top left x coordinate of the block */
		private int x;

		/** The top left y coordinate of the block */
		private int y;

		/** The block size */
		private int size;

		/** The rating of the block given by the neural net */
		public float rating;

		/** The color of the block */
		private Color color;

		/**
		 * The children of this block: Index 0: top left child, Index 1: top right
		 * child, Index 2: bottom left child, Index 3: bottom right child
		 */
		private WorkingBlock[] children = new WorkingBlock[4];

		/**
		 * A Pair of Block and index, that indicate the child that has be added last.
		 * The WorkingBlock is the parent of the last added child and the Integer is the
		 * index of the child in the children array.
		 */
		private Pair<WorkingBlock, Integer> lastAddedChild;

		/**
		 * Creates a new WorkingBlock.
		 * 
		 * @param x
		 *            The top left x coordinate of the block
		 * @param y
		 *            The top left y coordinate of the block
		 * @param size
		 *            The block size
		 * @param rating
		 *            The rating of the block
		 * @param color
		 *            The color of the block
		 */
		public WorkingBlock(int x, int y, int size, float rating, Color color) {
			this.x = x;
			this.y = y;
			this.size = size;
			this.rating = rating;
			this.color = color;
		}

		/**
		 * Adds a child of random color, that is not the color of this block, at a given
		 * position.
		 * 
		 * @param position
		 *            The position the child is added to
		 * @return The child block
		 */
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

		/**
		 * Adds a child of given color to this block at a given position.
		 * 
		 * @param position
		 *            The position the child is added to
		 * @param color
		 *            The color of the child
		 * @return The child block
		 */
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

		/**
		 * Adds a child at a random free position in this block or any of its children.
		 */
		public void addRandomChild() {
			LinkedList<Pair<WorkingBlock, Integer>> list = new LinkedList<>();
			getAllChildren(list);
			int rndIndex = ThreadLocalRandom.current().nextInt(list.size());
			Pair<WorkingBlock, Integer> pair = list.get(rndIndex);
			pair.getKey().addChild(pair.getValue());
			lastAddedChild = pair;
		}

		/**
		 * Removes the child that has last been added to this block or one of its
		 * children.
		 */
		public void removeLastAddedChild() {
			if (lastAddedChild != null) {
				lastAddedChild.getKey().children[lastAddedChild.getValue()] = null;
				lastAddedChild = null;
			}
		}

		/**
		 * Returns a list of all free child positions of this block and its children.
		 * 
		 * @param list
		 *            The list of free child positions found so far
		 * @return The list of free child positions
		 */
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

		/**
		 * Returns a random Color of the color array that is different from the color of
		 * this block.
		 *
		 * @return A random Color
		 */
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
		} else if (randomPhase) {
			BufferedImage res = copyImage(workingImage);
			Graphics2D g = res.createGraphics();
			int position = ThreadLocalRandom.current().nextInt(4);
			WorkingBlock block = blocks[position];
			currentBlock = block;
			// Color childColor =
			// colors[ThreadLocalRandom.current().nextInt(colors.length)];
			if (position == 0) {
				block = block.children[3];
			} else if (position == 1) {
				block = block.children[2];
			} else if (position == 2) {
				block = block.children[1];
			} else if (position == 3) {
				block = block.children[0];
			}
			// block.addChild(ThreadLocalRandom.current().nextInt(4), childColor);
			block.addRandomChild();
			block.paint(g);

			g.setColor(Color.DARK_GRAY);
			g.drawLine(0, height - 1, width, height - 1);
			g.drawLine(width - 1, 0, width - 1, height);
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
					if (blockSize == 8) {
						randomPhase = true;
						discoveryPhase = false;
					}
					blockSize = blockSize / 2;
					// queue.clear();
				}
				// } else if (randomPhase) {

			} else {
				if (res > overallConfidence && res >= 0.01f) {
					overallConfidence = res;
					randomPhase = false;
				}

				if (overallConfidence >= 0.9f)
					isFinished = true;
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
		randomPhase = false;
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
		ArrayList<Parameter> list = new ArrayList<>();
		list.add(stoppingParameter);
		return list;
	}

	@Override
	public boolean isFinished() {
		if (stoppingParameter.getBoolValue()) {
			return isFinished;
		} else {
			return false;
		}
	}

	@Override
	public String getModuleDescription() {
		return "Blockrekursion: \n\n" + "Dieser Generator generiert Bilder aus verschiedenfarbigen Quadraten"
				+ ", welche durch eine rekursive Struktur angeordnet sind. Der Generator arbeitet "
				+ "dabei in zwei Phasen: In der Explorationsphase wird versucht mithilfe eines gut "
				+ "erkannten Bildes ein Startbild für die zweite Phase zu finden. In dieser Phase "
				+ "werden dann zufällig neue Blöcke hinzugefügt. Durch ein greedy Verfahren konvergieren "
				+ "die Bilder dann gegen ein Bild mit 90% Konfidenz.";
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
