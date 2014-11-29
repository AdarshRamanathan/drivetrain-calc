import java.io.*;
import java.security.*;

public class IntegrityCheck
{
	public static final String[] files = {	"Index.class"
										};
	public static final String[] chksm = {	"8460368dbdd27e52ec78247543b31b20"
										};
	
	public static boolean run()
	{
		//run integrity check on all files - return true if passed
		for(int i = 0 ; i < files.length ; i++)
		{
			String md5 = "";
			try
			{
				md5 = getMD5Checksum(files[i]);
			}
			catch(FileNotFoundException fnfe)
			{
				return false;
			}
			catch(Exception e)
			{
				System.out.println(e);
				e.printStackTrace();
				return false;
			}

			System.out.println(md5);
			if(!chksm[i].equals(md5))
				return false;
		}
		return true;
	}

	public static byte[] createChecksum(String filename) throws IOException, FileNotFoundException, NoSuchAlgorithmException
	{
		InputStream fis =  new FileInputStream(filename);
		byte[] buffer = new byte[1024];
		MessageDigest complete = MessageDigest.getInstance("MD5");
		int numRead;
		do
		{
			numRead = fis.read(buffer);
			if (numRead > 0)
			{
				complete.update(buffer, 0, numRead);
			}
		}while(numRead != -1);
		
		fis.close();
		return complete.digest();
	}

	public static String getMD5Checksum(String filename) throws IOException, FileNotFoundException, NoSuchAlgorithmException
	{
		byte[] b = createChecksum(filename);
		String result = "";
		for (int i=0; i < b.length; i++)
		{
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	 }
}
