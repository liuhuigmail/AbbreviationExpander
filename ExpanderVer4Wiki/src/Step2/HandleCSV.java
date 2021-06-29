package Step2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class HandleCSV
{
	public static void main(String[] args) throws IOException
	{
//		Step1.Main.main(null);
		A();

	}

	public static void A() throws IOException
	{
		HashMap<String, ArrayList<String>> fileData = readParseResult(Global.LX.tempFile);

		for (String id : fileData.keySet())
		{
//			System.out.println(id);
			ArrayList<String> value = fileData.get(id);

			String nameOfIdentifier = value.get(1);
			if (nameOfIdentifier.trim().equals(""))
				continue;
			String[] partsRaw = (Util.CamelToUnderline(new StringBuffer(nameOfIdentifier))).toString().split("_");

			ArrayList<String> parts = new ArrayList<String>();
			for (String string : partsRaw)
			{
				if (!string.equals(""))
					parts.add(string);
			}
//			System.out.println("name=" + nameOfIdentifier);

			String expansionFull = "";
			ArrayList<String> possiWordArrayList = null;
			for (String part : parts)
			{
//				System.out.println(part);
//				System.out.println("part=" + part);
//				Wiki.Worm.wikili.put(part, Wiki.Worm.Wiki(part));

				possiWordArrayList = new ArrayList<String>();
				// Dictionary
				if (isInEnglishDic(part))
				{
					continue;
				}
				isInAbbrDic(possiWordArrayList, part);
				isIncomputerAbbr(possiWordArrayList, part);
				// Dic

				for (int i = 3; i < 17; i++) // lx '<='-->'<'
				{
					if (i != 9 && !value.get(i).equals(""))
					{
						String rawValue = value.get(i);

//						System.out.println("raw=" + rawValue);
						if (rawValue.contains(";"))
						{
							String[] values = rawValue.split(";");
							for (String string : values)
							{
								if (string.split(":").length <= 1)
									continue;
								String trueValue = string.split(":")[1];
//								System.out.println("Value=" + trueValue);
								if (Step2.Heu.H1(part, trueValue) || Step2.Heu.H2(part, trueValue)
										|| Step2.Heu.H3(part, trueValue))
								{
//										System.err.println("param="+trueValue);
									if (!possiWordArrayList.contains(trueValue))
										possiWordArrayList.add(trueValue);
								}
							}
						}
					}
					if (i == 9)
					{
						String comment = value.get(i);
//							System.err.println(comment);
						for (String string : PossibleExpansionFromComment(part, comment))
						{
							if (!possiWordArrayList.contains(string))
								possiWordArrayList.add(string);
						}
					}
					if (i == 16)
					{
						String possiExp = "";
						possiExp += part;
						for (String string : possiWordArrayList)
						{
							possiExp += "(" + string + ")";
						}
						if (possiWordArrayList.size() == 0)
						{
							HashSet<String> wiki = Wiki.Worm.Wiki(part);
							if (wiki != null)
								for (String string : wiki)
								{
									System.out.println("wiki=" + string);
								}
							if (wiki != null)
								for (String string : wiki)
								{
									possiExp += "[" + string + "]";
								}
						}
						possiExp += ";";
						expansionFull += possiExp;
					}

				}

//				for (String string : possiWordArrayList)
//				{
//					System.out.println("possi=" + string);
//				}

			}
//			System.out.println("=============================================");
			WriteNode.writerNodes.add(new WriteNode(id, nameOfIdentifier + "-->" + expansionFull));
		}
		FileReader();

	}

	static BufferedReader br;
	static BufferedWriter bw;

	public static void FileReader() throws IOException
	{
		br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(Global.LX.javaSource))));
		bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(Global.LX.javaDest))));
		String line;
		int lineNum = 0;
		while ((line = br.readLine()) != null)
		{
			lineNum++;
			bw.write(line);
			for (String id : Global.LX.IdLineInFile.keySet())
			{
				if (Global.LX.IdLineInFile.get(id) == lineNum)
				{
//					System.out.println(lineNum + "||" + id + "||" + WriteNode.getCommentByID(id));
					String exp = WriteNode.getCommentByID(id);

					if (!exp.trim().equals(""))
					{
						bw.write("//");
						bw.write(exp);
						System.out.println(exp);
					}
					break;
				}
			}
			bw.write("\n");
		}
		bw.flush();
	}

	// englishDicHashSet\computerAbbrDicHashMap

	public static boolean isInEnglishDic(String word)
	{
		if (word.length() == 1)
			return false;
		if (Dic.englishDicHashSet.contains(word.toLowerCase()))
			return true;
		return false;
	}

	public static void isInAbbrDic(ArrayList<String> possiWordList, String word)
	{
		for (String string : Dic.abbrDicHashMap.keySet())
		{
			if (string.equals(word))
			{
				if (!possiWordList.contains(word))
					possiWordList.add(Dic.abbrDicHashMap.get(word));
			}
		}

	}

	public static void isIncomputerAbbr(ArrayList<String> possiWordList, String word)
	{
		for (String string : Dic.computerAbbrDicHashMap.keySet())
		{
			if (string.equals(word))
			{
				String compDic = Dic.computerAbbrDicHashMap.get(word);
				for (String string2 : compDic.split(";"))
				{
					if (!possiWordList.contains(string2))
						possiWordList.add(string2);
				}
			}
		}
	}

	public static ArrayList<String> PossibleExpansionFromComment(String subWord, String comment)
	{
		ArrayList<String> temp = new ArrayList<String>();

		comment = comment.replaceAll(";", " ");
		String[] fullWords = comment.split(" "); // 注释中所有的单词
		for (String naturalWord : fullWords)
		{
			if (Step2.Heu.H1(subWord, naturalWord) || Step2.Heu.H2(subWord, naturalWord)
					|| Step2.Heu.H3(subWord, naturalWord))
			{
				temp.add(naturalWord);
			}
		}
		return temp;
	}

	private static HashMap<String, ArrayList<String>> readParseResult(String fileName)
	{
		HashMap<String, ArrayList<String>> idToInfos = new HashMap<>();
		ArrayList<String> lines = readFile(fileName);
		for (int i = 1; i < lines.size(); i++)
		{
			String line = lines.get(i);
			String[] temp = line.split(",", -1);
			if (idToInfos.containsKey(temp[0]))
			{
//					System.err.println(temp[0]);
//					System.err.println("duplicate keys");
				continue;
			}
			// id name typeOfIdentifier Assign Extend MethodDeclaration MethodInvocation
			// Type Comment
			ArrayList<String> value = new ArrayList<>();
			for (int j = 1; j < temp.length; j++)
			{
				value.add(temp[j]);
			}
			idToInfos.put(temp[0], value);
		}
		return idToInfos;
	}

	// convert text file to ArrayList<String> line by line
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
