package Step2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util
{
	public static String englishDicFile = "dic/EnglishDic.txt";
	public static String abbrDicFile = "dic/abbrDic.txt";
	public static String computerAbbrDicFile = "dic/computerAbbr.txt";

	public static StringBuffer CamelToUnderline(StringBuffer str)
	{
		Pattern pattern = Pattern.compile("[A-Z]");
		Matcher matcher = pattern.matcher(str);
		StringBuffer sb = new StringBuffer(str);
		if (matcher.find())
		{
			sb = new StringBuffer();
			matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
			matcher.appendTail(sb);
		} else
		{
			return sb;
		}
		return CamelToUnderline(sb);
	}

	public static boolean isLetter(char c)
	{
		if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
		{
			return true;
		} else
		{
			return false;
		}
	}

	public static boolean isNum(char c)
	{
		if (c >= '0' && c <= '9')
		{
			return true;
		} else
		{
			return false;
		}
	}

	// str only consists of letters
	public static ArrayList<String> splitBigLetter(String str)
	{
		char[] list = str.toCharArray();
		str = "A" + str + "A";
		char[] tempList = str.toCharArray();

		for (int i = 1; i < tempList.length - 1; i++)
		{
			if (tempList[i] >= 'A' && tempList[i] <= 'Z' && tempList[i - 1] >= 'A' && tempList[i - 1] <= 'Z'
					&& tempList[i + 1] >= 'A' && tempList[i + 1] <= 'Z')
			{
				list[i - 1] = (char) (tempList[i] - 'A' + 'a');
			} else
			{
				list[i - 1] = tempList[i];
			}
		}

		str = new String(list);
		ArrayList<String> result = new ArrayList<String>();
		int startPositionOfSubstring = 0;
		str = str + 'A';
		for (int endPositionOfSubstring = 0; endPositionOfSubstring < str.length(); endPositionOfSubstring++)
		{
			if (str.charAt(endPositionOfSubstring) >= 'A' && str.charAt(endPositionOfSubstring) <= 'Z')
			{
				// to exclude initial up case letter
				if (str.substring(startPositionOfSubstring, endPositionOfSubstring).length() > 0)
				{
					// to lower case
					result.add(str.substring(startPositionOfSubstring, endPositionOfSubstring).toLowerCase());
					startPositionOfSubstring = endPositionOfSubstring;
				}
			}
		}
		return result;
	}

	public static ArrayList<String> readFile(String fileName)
	{
		ArrayList<String> result = new ArrayList<String>();
		File file = new File(fileName);
		BufferedReader reader;
		try
		{
			reader = new BufferedReader(new FileReader(file));
			String tempString;
			while ((tempString = reader.readLine()) != null)
			{
				if (!tempString.equals(""))
				{
					result.add(tempString);
				}
			}
			reader.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

}
