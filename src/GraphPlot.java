import java.text.*;
import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import catmullrom.CatmullRomSplineUtils;

//implement mouselistener, and pass clicked coordinates to parent frame
public class GraphPlot extends JPanel
	implements MouseListener
{
	private final int PAD = 20;
	
	private double[] xRaw, yRaw;
	private double[] xInt, yInt;
	private double xMin, xMax, yMin, yMax;
	private double maximaX, minimaX;
	private double xScale, yScale;
	private String xLabel, yLabel;
	private GraphWindow parent;

	private boolean isPainted = false;
	private boolean drawGrid;
	private boolean drawPoints;
	private boolean drawMaxima;
	private boolean drawMinima;

	protected GraphPlot()
	{
		
	}
	
	public GraphPlot(double[] xRaw, double[] yRaw, String xLabel, String yLabel, boolean drawGrid, boolean drawPoints, boolean drawMaxima, boolean drawMinima, Container parent)
	{
		assert xRaw.length == yRaw.length;
		assert parent != null;

		this.parent = (GraphWindow)parent;
		this.xRaw = new double[xRaw.length];
		this.yRaw = new double[yRaw.length];
		for(int i = 0 ; i < xRaw.length ; i++)
		{
			this.xRaw[i] = xRaw[i];
			this.yRaw[i] = yRaw[i];
		}
		sortByX();
		interpolate();
		getMax();
		getMin();

		addMouseListener(this);

		this.xLabel = new String(xLabel);
		this.yLabel = new String(yLabel);

		this.drawGrid = drawGrid;
		this.drawPoints = drawPoints;
		this.drawMaxima = drawMaxima;
		this.drawMinima = drawMinima;
	}
	
	public GraphPlot(double[] xRaw, double[] yRaw, double xMin, double yMin, double xMax, double yMax, String xLabel, String yLabel, boolean drawGrid, boolean drawPoints, boolean drawMaxima, boolean drawMinima, Container parent)
	{
		assert xRaw.length == yRaw.length;
		assert xMin >= 0;
		assert yMin >= 0;
		assert xMin < xMax;
		assert yMin < yMax;
		assert parent != null;

		this.parent = (GraphWindow)parent;
		this.xRaw = new double[xRaw.length];
		this.yRaw = new double[yRaw.length];
		for(int i = 0 ; i < xRaw.length ; i++)
		{
			this.xRaw[i] = xRaw[i];
			this.yRaw[i] = yRaw[i];
		}
		sortByX();
		interpolate();
		this.xMin = xMin;
		this.yMin = yMin;
		this.xMax = xMax;
		this.yMax = yMax;

		addMouseListener(this);

		this.xLabel = new String(xLabel);
		this.yLabel = new String(yLabel);

		this.drawGrid = drawGrid;
		this.drawPoints = drawPoints;
		this.drawMaxima = drawMaxima;
		this.drawMinima = drawMinima;
	}

	private void getMax()
	{
		xMax = xRaw[0];
		yMax = yRaw[0];
		maximaX = xRaw[0];

		for(int i = 0 ; i < xRaw.length ; i++)
		{
			xMax = (xRaw[i] > xMax) ? xRaw[i] : xMax;
			if(yRaw[i] > yMax)
			{
				yMax = yRaw[i];
				maximaX = xRaw[i];
			}
		}

		for(int i = 0 ; i < xInt.length ; i++)
		{
			xMax = (xInt[i] > xMax) ? xInt[i] : xMax;
			if(yInt[i] > yMax)
			{
				yMax = yInt[i];
				maximaX = xInt[i];
			}
		}
	}

	private void getMin()
	{
		xMin = xRaw[0];
		yMin = yRaw[0];
		minimaX = xRaw[0];

		for(int i = 0 ; i < xRaw.length ; i++)
		{
			xMin = (xRaw[i] < xMin) ? xRaw[i] : xMin;
			if(yRaw[i] < yMin)
			{
				yMin = yRaw[i];
				minimaX = xRaw[i];
			}
		}

		for(int i = 0 ; i < xInt.length ; i++)
		{
			xMin = (xInt[i] < xMin) ? xInt[i] : xMin;
			if(yInt[i] < yMin)
			{
				yMin = yInt[i];
				minimaX = xInt[i];
			}
		}
	}

	private void interpolate()
	{
		catmullrom.Point[] p = CatmullRomSplineUtils.createPoints(xRaw, yRaw);
		catmullrom.Point[] q = CatmullRomSplineUtils.subdividePoints(p, 20);

		xInt = new double[q.length];
		yInt = new double[q.length];

		for(int i = 0 ; i < q.length ; i++)
		{
			xInt[i] = q[i].getX();
			yInt[i] = q[i].getY();
		}
	}

	private void sortByX()
	{
		for(int i = 0 ; i < xRaw.length ; i++)
		{
			boolean flag = true;
			for(int j = 0 ; j < xRaw.length - i - 1 ; j++)
			{
				if(xRaw[j] > xRaw[j + 1])
				{
					flag = false;
					double temp = xRaw[j];
					xRaw[j] = xRaw[j + 1];
					xRaw[j + 1] = temp;
					temp = yRaw[j];
					yRaw[j] = yRaw[j + 1];
					yRaw[j + 1] = temp;
				}
			}
			if(flag)
				break;
		}
	}

	private double roundToSignificantFigures(double num, int n)
	{
		if(num == 0)
		{
			return 0;
		}

		final double d = Math.ceil(Math.log10(Math.abs(num)));
		final int power = n - (int)d;

		final double magnitude = Math.pow(10, power);
		final long shifted = Math.round(num * magnitude);
		
		return (shifted / magnitude);
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		if(!isPainted)
		{
			parent.setStatus("Working...");
		}
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		int w = getWidth();
		int h = getHeight();

		g2.setPaint(Color.black);
		//draw axes
		g2.draw(new Line2D.Double(PAD, PAD, PAD, h - PAD));
		g2.draw(new Line2D.Double(PAD, h - PAD, w - PAD, h - PAD));

		Font font = g2.getFont();
		FontRenderContext frc = g2.getFontRenderContext();
		LineMetrics lm = font.getLineMetrics("0", frc);
		float sh = lm.getAscent() + lm.getDescent();
		
		//y axis labe;
		String s = yLabel;
		float sy = PAD + ((h - 2 * PAD) - s.length() * sh) / 2 + lm.getAscent();
		for(int i = 0 ; i < s.length() ; i++)
		{
			String letter = String.valueOf(s.charAt(i));
			float sw = (float)font.getStringBounds(letter, frc).getWidth();
			float sx = (PAD - sw) / 2;
			g2.drawString(letter, sx, sy);
			sy += sh;
		}
		
		//x axis label
		s = xLabel;
		sy = h - PAD + (PAD - sh) / 2 + lm.getAscent();
		float sw = (float)font.getStringBounds(s, frc).getWidth();
		float sx = (w - sw) / 2;
		g2.drawString(s, sx, sy);
		
		//write out ymax
		DecimalFormat myFormat = new DecimalFormat("#.##");
		s = myFormat.format(roundToSignificantFigures(yMax, 2));
		sw = (float)font.getStringBounds(s, frc).getWidth();
		sx = Math.max((PAD - sw / 2), 0);
		sy = PAD / 2;
		g2.drawString(s, sx, sy);

		//write out xmax
		s = myFormat.format(roundToSignificantFigures(xMax, 2));
		sw = (float)font.getStringBounds(s, frc).getWidth();
		sx = Math.min((w - PAD - sw / 2), (w - sw));
		sy = h - PAD + (PAD - sh) / 2 + lm.getAscent();
		g2.drawString(s, sx, sy);

		//calculate scale
		xScale = (w - 2 * PAD) / xMax;
		yScale = (h - 2 * PAD) / yMax;

		//add graduations - y axis
		for(int i = 1 ; i < 10 ; i++)
		{
			int x1 = PAD;
			int y1 = (int)(h - PAD - i * (yMax / 10) * yScale);
			int x2 = PAD + 4;
			int y2 = y1;

			g2.draw(new Line2D.Double(x1, y1, x2, y2));
		}

		//add graduations - x axis
		for(int i = 1 ; i < 10 ; i++)
		{
			int x1 = (int)(PAD + i * (xMax / 10) * xScale);
			int y1 = h - PAD;
			int x2 = x1;
			int y2 = h - PAD - 4;

			g2.draw(new Line2D.Double(x1, y1, x2, y2));
		}

		//draw grid
		if(drawGrid)
		{
			g2.setPaint(Color.white.darker());
			for(int i = 1 ; i < 10 ; i++)
			{
				int x1 = PAD + 5;
				int y1 = (int)(h - PAD - i * (yMax / 10) * yScale);
				int x2 = w - PAD;
				int y2 = y1;

				g2.draw(new Line2D.Double(x1, y1, x2, y2));
			}

			for(int i = 1 ; i < 10 ; i++)
			{
				int x1 = (int)(PAD + i * (xMax / 10) * xScale);
				int y1 = h - PAD - 5;
				int x2 = x1;
				int y2 = PAD;

				g2.draw(new Line2D.Double(x1, y1, x2, y2));
			}
		}

		//draw graph line
		g2.setPaint(Color.green.darker());
		Path2D.Double path = new Path2D.Double();

		path.moveTo(PAD + xInt[0] * xScale, h - PAD - yInt[0] * yScale);

		for(int i = 1 ; i < xInt.length ; i++)
			path.lineTo(PAD + xInt[i] * xScale, h - PAD - yInt[i] * yScale);

		g2.draw(path);

		//mark out raw data points
		if(drawPoints)
		{
			g2.setPaint(Color.red);
			for(int i = 0 ; i < xRaw.length ; i++)
			{
				double x1 = PAD + xRaw[i] * xScale;
				double y1 = h - PAD - yRaw[i] * yScale;
				g2.fill(new Ellipse2D.Double(x1 - 2, y1 - 2, 4, 4));
			}
		}
		
		//draw maxima
		if(drawMaxima)
		{
			g2.setPaint(Color.blue.darker());
			
			double x1 = PAD + maximaX * xScale;
			double y1 = h - PAD - yMax * yScale;

			g2.fill(new Ellipse2D.Double(x1 - 2, y1 - 2, 4, 4));
			DecimalFormat format = new DecimalFormat("#.###");
			String x = format.format(roundToSignificantFigures(maximaX, 4));
			String y = format.format(roundToSignificantFigures(yMax, 4));
			String txt = (new String("(")).concat(x).concat(new String(", ")).concat(y).concat(new String(")"));
			sw = (float)font.getStringBounds(txt, frc).getWidth();
			sx = (int)(PAD + maximaX * xScale - sw / 2);
			
			sx = (sx > w - sw) ? w - sw : sx;
			sx = (sx < PAD) ? PAD : sx;
			
			sy = (int)(h - PAD - yMax * yScale - 0.5 * (lm.getAscent() + lm.getDescent()));
			g2.drawString(txt, sx, sy);
		}
		
		//draw minima
		if(drawMinima)
		{
			g2.setPaint(Color.blue.darker());
			
			double x1 = PAD + minimaX * xScale;
			double y1 = h - PAD - yMin * yScale;

			g2.fill(new Ellipse2D.Double(x1 - 2, y1 - 2, 4, 4));
			DecimalFormat format = new DecimalFormat("#.###");
			String x = format.format(roundToSignificantFigures(minimaX, 4));
			String y = format.format(roundToSignificantFigures(yMin, 4));
			String txt = (new String("(")).concat(x).concat(new String(", ")).concat(y).concat(new String(")"));
			sw = (float)font.getStringBounds(txt, frc).getWidth();
			sx = (int)(PAD + minimaX * xScale - sw / 2);
			sy = (int)(h - PAD - yMin * yScale + 1.5 * (lm.getAscent() + lm.getDescent()));
			
			sx = (sx > w - sw) ? w - sw : sx;
			sx = (sx < PAD) ? PAD : sx;

			g2.drawString(txt, sx, sy);
		}

		if(!isPainted)
		{
			isPainted = true;
			parent.setStatus("Done.");
		}
	}

	@Override
	public void mouseEntered(MouseEvent me)
	{
		
	}

	@Override
	public void mouseExited(MouseEvent me)
	{
		
	}

	@Override
	public void mouseClicked(MouseEvent me)
	{
		
	}

	@Override
	public void mousePressed(MouseEvent me)
	{
		
	}

	@Override
	public void mouseReleased(MouseEvent me)
	{
		double x = (me.getX() - PAD) / xScale;
		double y = (getHeight() - me.getY() - PAD) / yScale;
		x = roundToSignificantFigures(x, 4);
		y = roundToSignificantFigures(y, 4);

		System.out.println(x + ", " + y);

		parent.setStatus(x + ", " + y);
	}
}
