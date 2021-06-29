package Global;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.jdt.core.dom.CompilationUnit;


public class LX
{
	public static String javaSource = "E:/materials/FFF/111.java";
	public static String tempFile = "E:/materials/FFF/111_after.csv";
	public static String javaDest = "E:/materials/FFF/111_Exp.java";

	public static String rawSource;
	public static String[] splitedRawSource;

	public static CompilationUnit unit;

	public static HashMap<String, Integer> IdLineInFile = new HashMap<String, Integer>();

}
