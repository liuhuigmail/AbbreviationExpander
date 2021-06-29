package Step0;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class PreOperation
{
	// 将java文件的格式预处理
	public static void main(String[] args)
	{
		File oriFile = new File(Global.LX.javaSource);
		File temp = new File(Global.LX.tempFile);
		if (temp.exists())
			temp.delete();
		FileFormater(Global.LX.javaSource);

		oriFile.delete();
		System.out.println(temp.renameTo(oriFile));
	}

	public static void FileFormater(String javaFile)
	{
		byte[] input = null;
		try
		{
			BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(javaFile));
			input = new byte[bufferedInputStream.available()];
			bufferedInputStream.read(input);
			bufferedInputStream.close();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		ASTParser astParser = ASTParser.newParser(AST.JLS8);
		astParser.setKind(ASTParser.K_COMPILATION_UNIT);
		astParser.setResolveBindings(true);
		astParser.setBindingsRecovery(true);
		astParser.setSource(new String(input).toCharArray());
		astParser.setUnitName("");
		astParser.setEnvironment(new String[] {}, new String[] {}, new String[] {}, true);
		CompilationUnit unit = (CompilationUnit) (astParser.createAST(null));
//		System.out.println(unit.toString());
		appendFile(unit.toString());
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
