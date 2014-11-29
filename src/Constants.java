import java.io.*;

/*	Statically stores values of world constants.
 *	Members are accessible from other classes through get
 *	method invocations.
 */
public class Constants
{
	private static double gravitationalAcceleration = 9.80665;
	private static double coefficientOfDrag = 0.97;
	private static double densityOfAir = 1.225;
	private static double coefficientOfStaticFriction = 0.8;
	private static double coefficientOfRollingResistance = 0.03;
	private static double globalFudgeFactor = 1.0;

	/*	Called when an Index object is created.
	 *	Reads constants (if they exist) from conf/constants.ini
	 *	and overrides the default values.
	 */	
	public static void init()
	{
		try
		{
			BufferedReader br = new BufferedReader(new FileReader("../conf/constants"));
			gravitationalAcceleration = Double.parseDouble(br.readLine());
			coefficientOfDrag = Double.parseDouble(br.readLine());
			densityOfAir = Double.parseDouble(br.readLine());
			coefficientOfStaticFriction = Double.parseDouble(br.readLine());
			coefficientOfRollingResistance = Double.parseDouble(br.readLine());
			globalFudgeFactor = Double.parseDouble(br.readLine());
		}
		catch(FileNotFoundException | NumberFormatException nfe)
		{
			System.out.print("Constants table corrupted.\nReinitializing with defaults... ");
			try
			{
				PrintWriter pw = new PrintWriter("../conf/constants", "UTF-8");
				pw.println("9.80665");
				pw.println("0.97000");
				pw.println("1.22500");
				pw.println("0.80000");
				pw.println("0.03000");
				pw.println("1.00000");
				pw.close();
				System.out.println("Done.");
			}
			catch(IOException ioe)
			{
				System.out.println(ioe);
				ioe.printStackTrace();
			}
		}
		catch(IOException ioe)
		{
			System.out.println(ioe);
			ioe.printStackTrace();
		}
	}

	public static double getGravitationalAcceleration()
	{
		return gravitationalAcceleration;
	}

	public static double getDragCoefficient()
	{
		return coefficientOfDrag;
	}

	public static double getAirDensity()
	{
		return densityOfAir;
	}

	public static double getStaticFriction()
	{
		return coefficientOfStaticFriction;
	}

	public static double getRollingResistance()
	{
		return coefficientOfRollingResistance;
	}

	public static double getFudgeFactor()
	{
		return globalFudgeFactor;
	}

	public static void setGravitationalAcceleration(double d)
	{
		gravitationalAcceleration = d;
	}

	public static void setDragCoefficient(double d)
	{
		coefficientOfDrag = d;
	}

	public static void setAirDensity(double d)
	{
		densityOfAir = d;
	}

	public static void setStaticFriction(double d)
	{
		coefficientOfStaticFriction = d;
	}

	public static void setRollingResistance(double d)
	{
		coefficientOfRollingResistance = d;
	}

	public static void setFudgeFactor(double d)
	{
		globalFudgeFactor = d;
	}
}
