import java.io.*;

/*	Statically stores values of user preferences.
 *	Members are accessible from other classes through get
 *	method invocations.
 */
public class Preferences
{
	private static String interpolation = "linear";	//step, linear, spline
	private static String unitSystem = "si";		//si, imperial

	/*	Called when an Index object is created.
	 *	Reads preferences (if they exist) from conf/prefs.ini
	 *	and overrides the default values.
	 */	
	public static void init()
	{
		
	}
}
