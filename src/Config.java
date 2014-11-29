import java.io.*;
import catmullrom.CatmullRomSplineUtils;

/*	Statically stores configuration specific data.
 *	Members are accessible from other classes through get
 *	method invocations.
 */
public class Config
		implements Serializable
{	
	public static final int CVT_MAX_RATIO = 0x1000;
	public static final int CVT_MIN_RATIO = 0x1001;
	public static final int GEARBOX_RATIO = 0x1002;
	public static final int CVT_EFFICIENCY = 0x1003;
	public static final int GEARBOX_EFFICIENCY = 0x1004;
	public static final int FIREWALL_AREA = 0x1005;
	public static final int WHEELBASE = 0x1006;
	public static final int ROAD_INCLINE = 0x1007;
	public static final int TYRE_RADIUS = 0x1008;
	public static final int VEHICLE_MASS = 0x1009;
	public static final int REAR_WEIGHT_PERCENT = 0x100A;
	public static final int COG_HEIGHT = 0x100B;
	public static final int ENGINE_IDLE_RPM = 0x100C;
	public static final int ENGINE_REDLINE = 0x100D;
	public static final int CVT_ENGAGEMENT_RPM = 0x100E;
	public static final int TORQUE_CURVE_RPM = 0x2000;
	public static final int TORQUE_CURVE = 0x2001;
	public static final int SHIFT_CURVE_RPM = 0x2002;
	public static final int SHIFT_CURVE = 0x2003;
	public static final int SHIFT_CURVE_TYPE = 0x3000;
	public static final int LINEAR = 0x3001;
	public static final int EXPONENTIAL = 0x3002;
	public static final int LOGARITHMIC = 0x3003;
	public static final int ISODYNE = 0x3004;
	public static final int CUSTOM = 0x3005;
	public static final int TORQUE_CURVE_TYPE = 0x4000;
	public static final int NEW_ENGINE = 0x4001;
	public static final int OLD_ENGINE = 0x4002;
	
	private static final int torqueCurvePoints = 48;
	private static final int shiftCurvePoints = 48;
	private static final double isodyneAccuracy = 0.001;

	private static Config instance = null;

	private boolean allConfigsGreen = false;
	private double cVTMaximumReduction;
	private double cVTMinimumReduction;
	private double gearboxReduction;
	private double cVTEfficiency;
	private double gearboxEfficiency;
	private double firewallArea;
	private double wheelbase;
	private double roadIncline;
	private double tyreRadius;
	private double vehicleMass;
	private double percentWeightOnRearAxle;
	private double centreOfGravityHeight;
	private double engineIdleRPM;
	private double engineRedlineRPM;
	private double cVTEngagementRPM;
	private double torqueCurveRPM[];
	private double engineTorqueCurve[];
	private double shiftCurveRPM[];
	private double cVTShiftCurve[];
	
	private int shiftCurveType, torqueCurveType;

	protected Config()
	{
		cVTMaximumReduction = 1.0;
		cVTMinimumReduction = 1.0;
		gearboxReduction = 1.0;
		cVTEfficiency = 1.0;
		gearboxEfficiency = 1.0;
		firewallArea = 1.0;
		wheelbase = 1.0;
		roadIncline = 0.0;
		tyreRadius = 0.254;
		vehicleMass = 240;
		percentWeightOnRearAxle = 0.5;
		centreOfGravityHeight = 0.4;
		engineIdleRPM = 1500;
		engineRedlineRPM = 3800;
		cVTEngagementRPM = 1700;
		torqueCurveType = -1;
		generateShiftCurve(LINEAR);
	}

	public static void createNew()
	{
		instance = new Config();
	}

	public void clear()
	{
		instance = null;
	}

	public static void saveToFile(File file)
	{
		try
		{
			FileOutputStream fout = new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(fout);
			out.writeObject(instance);
			out.close();
			fout.close();
		}
		catch(IOException ioe)
		{
			System.out.println(ioe);
			ioe.printStackTrace();
		}
	}

	public static void loadFromFile(File file)
	{
		try
		{
			FileInputStream fin = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(fin);
			instance = (Config)in.readObject();
			in.close();
			fin.close();
		}
		catch(IOException ioe)
		{
			System.out.println(ioe);
			ioe.printStackTrace();
		}
		catch(ClassNotFoundException cnfe)
		{
			System.out.println(cnfe);
			cnfe.printStackTrace();
		}
	}

	public static Config getInstance()
	{
		return instance;
	}

	public double getSpeed(double rpm)
	{
		double pow = 2 * Math.PI * (rpm / 60) * getScalar(TORQUE_CURVE, rpm);
		//pow *= getScalar(CVT_EFFICIENCY) * getScalar(GEARBOX_EFFICIENCY) * Constants.getFudgeFactor();
		
		double lb = 0;
		double ub = 10;

		System.out.println("Finding speed at " + rpm + " rpm...");
		System.out.println("Power at engine is " + pow + " kw.");
		pow *= getScalar(CVT_EFFICIENCY) * getScalar(GEARBOX_EFFICIENCY) * Constants.getFudgeFactor();
		System.out.println("Power at wheels is " + pow + " kw.");

		//ballpark the speed first, in exponential increments
		double spow = 0;/*air drag + grade + rolling resistance*/
		while(true)
		{
			spow = 0.5 * Constants.getAirDensity() * Math.pow(ub, 3) * Constants.getDragCoefficient() * firewallArea;
			spow += Constants.getRollingResistance() * vehicleMass * Constants.getGravitationalAcceleration() * Math.cos((roadIncline *  Math.PI) / 180.0) * Math.pow(ub, 1);
			spow += vehicleMass * Constants.getGravitationalAcceleration() * Math.sin((roadIncline * Math.PI) / 180.0) * ub;
			
			if(spow > pow)
			{
				break;
			}
			
			lb = ub;
			ub *= 2;
		}

		//got a ballpark, now narrow the range in exponentially decaying steps, like a biinary search
		while((ub - lb) > isodyneAccuracy)
		{
			double mid = (ub + lb) / 2;
			spow = 0.5 * Constants.getAirDensity() * Math.pow(mid, 3) * Constants.getDragCoefficient() * firewallArea;
			spow += Constants.getRollingResistance() * vehicleMass * Constants.getGravitationalAcceleration() * Math.cos((roadIncline * Math.PI) / 180.0) * Math.pow(mid, 1);
			spow += vehicleMass * Constants.getGravitationalAcceleration() * Math.sin((roadIncline * Math.PI) / 180.0) * ub;

			if(spow > pow)
			{
				ub = mid;
			}
			else if(spow < pow)
			{
				lb = mid;
			}
			else
			{
				System.out.println("Calculated speed is " + mid + " m/s.");
			}
		}

		System.out.println("Calculated speed is " + (ub + lb) / 2 + " m/s.");
		return (ub + lb) / 2;
	}

	public void generateShiftCurve()
	{
		shiftCurveRPM = new double[shiftCurvePoints];
		cVTShiftCurve = new double[shiftCurvePoints];

		System.out.println("Generating " + shiftCurvePoints + " points on the shift curve.\n");

		switch(shiftCurveType)
		{
			case LINEAR:
				int waste = 0;
				for(int i = 0 ; i < shiftCurvePoints ; i++)
				{
					shiftCurveRPM[i] = engineIdleRPM + (engineRedlineRPM - engineIdleRPM) * (i / (double)(shiftCurvePoints - 1));
					if(shiftCurveRPM[i] < cVTEngagementRPM)
					{
						cVTShiftCurve[i] = cVTMaximumReduction;
						waste ++;
					}
					else
					{
						cVTShiftCurve[i] = cVTMaximumReduction - (cVTMaximumReduction - cVTMinimumReduction) * ((i - waste) / (double)(shiftCurvePoints - waste - 1));
					}
				}
			break;

			case EXPONENTIAL:
				waste = 0;
				for(int i = 0 ; i < shiftCurvePoints ; i++)
				{
					shiftCurveRPM[i] = engineIdleRPM + (engineRedlineRPM - engineIdleRPM) * (i / (double)(shiftCurvePoints - 1));
					if(shiftCurveRPM[i] < cVTEngagementRPM)
					{
						cVTShiftCurve[i] = cVTMaximumReduction;
						waste ++;
					}
					else
					{
						cVTShiftCurve[i] = cVTMaximumReduction - (cVTMaximumReduction - cVTMinimumReduction) * Math.sqrt(((i - waste) / (double)(shiftCurvePoints - waste - 1)));
					}
				}
			break;

			case LOGARITHMIC:
				waste = 0;
				for(int i = 0 ; i < shiftCurvePoints ; i++)
				{
					shiftCurveRPM[i] = engineIdleRPM + (engineRedlineRPM - engineIdleRPM) * (i / (double)(shiftCurvePoints - 1));
					if(shiftCurveRPM[i] < cVTEngagementRPM)
					{
						cVTShiftCurve[i] = cVTMaximumReduction;
						waste ++;
					}
					else
					{
						cVTShiftCurve[i] = cVTMaximumReduction - (cVTMaximumReduction - cVTMinimumReduction) * Math.pow(((i - waste) / (double)(shiftCurvePoints -waste - 1)), 2);
					}
				}
			break;

			case ISODYNE:
				waste = 0;
				for(int i = 0 ; i < shiftCurvePoints ; i++)
				{
					shiftCurveRPM[i] = engineIdleRPM + (engineRedlineRPM - engineIdleRPM) * (i / (double)(shiftCurvePoints - 1));
					if(shiftCurveRPM[i] < cVTEngagementRPM)
					{
						cVTShiftCurve[i] = cVTMaximumReduction;
						waste ++;
					}
					else
					{
						double secondaryRPM = getSpeed(shiftCurveRPM[i]) / (2 * Math.PI * tyreRadius);
						secondaryRPM *= 60 * gearboxReduction;
						cVTShiftCurve[i] = shiftCurveRPM[i] / secondaryRPM;
						cVTShiftCurve[i] = (cVTShiftCurve[i] > cVTMaximumReduction) ? (cVTMaximumReduction) : (cVTShiftCurve[i]);
						cVTShiftCurve[i] = (cVTShiftCurve[i] < cVTMinimumReduction) ? (cVTMinimumReduction) : (cVTShiftCurve[i]);
						System.out.println("Calculated CVT ratio is " + cVTShiftCurve[i]);
					}
				}
			break;
		}
		for(int i = 0 ; i < cVTShiftCurve.length ; i++)
		{
			//System.out.println(shiftCurveRPM[i] + " " + cVTShiftCurve[i]);
		}
	}

	public void generateShiftCurve(int code)
	{
		if(code < LINEAR || code > ISODYNE)
			throw new EnumConstantNotPresentException(null, Integer.toString(code));

		shiftCurveType = code;
		generateShiftCurve();
	}

	public void generateShiftCurve(String[][] data)
	{
		shiftCurveType = CUSTOM;
	}

	public double getScalar(int code)
	{
		switch(code)
		{
			case CVT_MAX_RATIO:
				return cVTMaximumReduction;

			case CVT_MIN_RATIO:
				return cVTMinimumReduction;

			case GEARBOX_RATIO:
				return gearboxReduction;

			case CVT_EFFICIENCY:
				return cVTEfficiency;

			case GEARBOX_EFFICIENCY:
				return gearboxEfficiency;

			case FIREWALL_AREA:
				return firewallArea;

			case WHEELBASE:
				return wheelbase;

			case ROAD_INCLINE:
				return roadIncline;

			case TYRE_RADIUS:
				return tyreRadius;

			case VEHICLE_MASS:
				return vehicleMass;

			case REAR_WEIGHT_PERCENT:
				return percentWeightOnRearAxle;

			case COG_HEIGHT:
				return centreOfGravityHeight;

			case ENGINE_IDLE_RPM:
				return engineIdleRPM;

			case ENGINE_REDLINE:
				return engineRedlineRPM;

			case CVT_ENGAGEMENT_RPM:
				return cVTEngagementRPM;

			case SHIFT_CURVE_TYPE:
				return shiftCurveType;

			case TORQUE_CURVE_TYPE:
				return torqueCurveType;

			default:
				throw new EnumConstantNotPresentException(null, Integer.toString(code));
		}
	}

	public void setScalar(int code, double d)
	{
		switch(code)
		{
			case CVT_MAX_RATIO:
				cVTMaximumReduction = d;
			break;

			case CVT_MIN_RATIO:
				cVTMinimumReduction = d;
			break;

			case GEARBOX_RATIO:
				 gearboxReduction = d;
			break;

			case CVT_EFFICIENCY:
				 cVTEfficiency = d;
			break;

			case GEARBOX_EFFICIENCY:
				 gearboxEfficiency = d;
			break;

			case FIREWALL_AREA:
				 firewallArea = d;
			break;

			case WHEELBASE:
				 wheelbase = d;
			break;

			case ROAD_INCLINE:
				 roadIncline = d;
			break;

			case TYRE_RADIUS:
				 tyreRadius = d;
			break;

			case VEHICLE_MASS:
				 vehicleMass = d;
			break;

			case REAR_WEIGHT_PERCENT:
				 percentWeightOnRearAxle = d;
			break;

			case COG_HEIGHT:
				 centreOfGravityHeight = d;
			break;

			case ENGINE_IDLE_RPM:
				 engineIdleRPM = d;
			break;

			case ENGINE_REDLINE:
				 engineRedlineRPM = d;
			break;

			case CVT_ENGAGEMENT_RPM:
				 cVTEngagementRPM = d;
			break;

			case SHIFT_CURVE_TYPE:
				if(d < 0x3001 || d > 0x3004)
					throw new EnumConstantNotPresentException(null, Double.toString(code));
				shiftCurveType = (int)d;
			break;

			case TORQUE_CURVE_TYPE:
				if(d != 4001 && d != 4002 && d != 3005)
					throw new EnumConstantNotPresentException(null, Double.toString(code));
				torqueCurveType = (int)d;

			default:
				throw new EnumConstantNotPresentException(null, Integer.toString(code));
		}
	}

	public double getScalar(int code, double rpm)
	{
		switch(code)
		{
			case TORQUE_CURVE:
				return CatmullRomSplineUtils.interpolate(CatmullRomSplineUtils.createPoints(torqueCurveRPM, engineTorqueCurve), rpm);

			case SHIFT_CURVE:
				for(int i = 0 ; i < shiftCurveRPM.length ; i++)
				{
					if(rpm > shiftCurveRPM[i])
					{
						//still a ways to go
						//so, od nothing
					}
					else if(i < shiftCurveRPM.length && rpm == shiftCurveRPM[i])
					{
						//found an exact match
						//return the value
						return cVTShiftCurve[i];
					}
					else if(i < shiftCurveRPM.length)
					{
						//overshot
						//interpolate between points for a value
						return cVTShiftCurve[i - 1] + (cVTShiftCurve[i] - cVTShiftCurve[i - 1]) * (rpm - shiftCurveRPM[i - 1]) / (shiftCurveRPM[i] - shiftCurveRPM[i - 1]);
						//simple linear interpolation... same as above
					}
				}
			break;

			default:
				throw new EnumConstantNotPresentException(null, Integer.toString(code));
		}
		return cVTMinimumReduction;
	}


	public void setScalar(int code, double d, double rpm)
	{
		switch(code)
		{
			case TORQUE_CURVE:
				//find index of value by searching torquecurverpm array
				//modify value if present
			break;

			case SHIFT_CURVE:
				//find index of value by searching in shiftcurverpm array
				//modify value if present
			break;

			default:
				throw new EnumConstantNotPresentException(null, Integer.toString(code));
		}
	}

	public double[] getVector(int code)
	{
		switch(code)
		{
			case TORQUE_CURVE_RPM:
				return torqueCurveRPM;

			case TORQUE_CURVE:
				return engineTorqueCurve;

			case SHIFT_CURVE_RPM:
				return shiftCurveRPM;

			case SHIFT_CURVE:
				return cVTShiftCurve;

			default:
				throw new EnumConstantNotPresentException(null, Integer.toString(code));
		}
	}

	public void setVector(int code, double[] rpm, double[] d)
	{
		switch(code)
		{
			case TORQUE_CURVE:
			case TORQUE_CURVE_RPM:
				assert rpm.length == d.length;
				torqueCurveRPM = new double[d.length];
				engineTorqueCurve = new double[d.length];
				for(int i = 0 ; i < d.length ; i++)
				{
					torqueCurveRPM[i] = rpm[i];
					engineTorqueCurve[i] = d[i];
				}
			break;

			case SHIFT_CURVE:
			case SHIFT_CURVE_RPM:
				assert rpm.length == d.length;
				shiftCurveRPM = new double[d.length];
				cVTShiftCurve = new double[d.length];
				for(int i = 0 ; i < d.length ; i++)
				{
					shiftCurveRPM[i] = rpm[i];
					cVTShiftCurve[i] = d[i];
				}
			break;

			default:
				throw new EnumConstantNotPresentException(null, Integer.toString(code));
		}
	}
}
