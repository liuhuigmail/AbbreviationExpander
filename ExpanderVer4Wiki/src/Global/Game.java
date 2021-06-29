package Global;

import java.io.File;
import java.io.IOException;

public class Game
{
	public static void main(String[] args) throws IOException
	{
		if (args.length == 1)
		{
			Global.LX.javaSource = args[0];
			Global.LX.javaDest = args[0] + "_Expand.java";
		} else if (args.length == 2)
		{
			Global.LX.javaSource = args[0];
			Global.LX.javaDest = args[1];
		} else
		{
			System.out.println("please input souceCodeLocation[souceCodeDestion]");
		}
		if (!CheckLegalInput())
		{
			System.out.println("Error input");
			return;
		}
		Global.LX.tempFile = Global.LX.javaSource + "_temp.csv";

		Step0.PreOperation.main(null);
		Step1.Main.main(null);
		Step2.HandleCSV.main(null);
	}

	public static boolean CheckLegalInput()
	{
		if (new File(Global.LX.javaSource).exists()
				&& (!new File(Global.LX.javaDest).exists() || Global.LX.javaDest.equals(Global.LX.javaSource)))
		{
			return true;
		}
		return false;
	}

}
