package Step1;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.jdt.core.dom.Expression;

import Global.LX;
import entity.Identifier;
import visitor.SimpleVisitor;

public class Utils
{
	public static String getStringFromFile(String javaFile) throws IOException
	{
		BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(javaFile));
		byte[] input = null;

		input = new byte[bufferedInputStream.available()];
		bufferedInputStream.read(input);
		bufferedInputStream.close();
		return new String(input);
	}

	//////////////////////
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

	public static void put(HashMap<String, ArrayList<String>> hashMap, String key, String value)
	{
		if (hashMap.containsKey(key))
		{
			hashMap.get(key).add(value);
		} else
		{
			ArrayList<String> arrayList = new ArrayList<>();
			arrayList.add(value);
			hashMap.put(key, arrayList);
		}
	}

	public static void putHashSet(HashMap<String, HashSet<String>> hashMap, String key, String value)
	{
		if (hashMap.containsKey(key))
		{
			hashMap.get(key).add(value);
		} else
		{
			HashSet<String> arrayList = new HashSet<>();
			arrayList.add(value);
			hashMap.put(key, arrayList);
		}
	}

	public static ArrayList<Identifier> parseExpression(Expression expression)
	{
		if (expression == null)
		{
			return null;
		}
		SimpleVisitor simpleVisitor = new SimpleVisitor();
		expression.accept(simpleVisitor);
		return simpleVisitor.identifiers;
	}

	public static void appendFile(String line)
	{
		FileWriter fw = null;
		try
		{
			File f = new File(Global.LX.tempFile);

			fw = new FileWriter(f, true);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		PrintWriter pw = new PrintWriter(fw);
		pw.print(line);
		pw.flush();
		try
		{
			fw.flush();
			pw.close();
			fw.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
