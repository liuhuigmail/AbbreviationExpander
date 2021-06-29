package Step2;

import java.io.File;

public class Heu
{
	// return true if subword is a sub word of fullWord
	// H3----SB-->StringBuffer
	public static boolean H1(String subWord, String fullWord)
	{
		int id = 0;
		for (int i = 0; i < fullWord.length(); i++)
		{
			if (fullWord.charAt(i) <= 'Z' && fullWord.charAt(i) >= 'A')
			{
				if (subWord.charAt(id++) != fullWord.charAt(i) + ('a' - 'A'))
					return false;
			}
			if (id == subWord.length())
				break;
		}
		if (id != subWord.length())
			return false;
		return true;
	}

	// return true if subword is a sub word of fullWord
	// H2----Str-->String
	public static boolean H2(String subWord, String fullWord)
	{
		if (H1(subWord, fullWord) == false)
			fullWord = fullWord.toLowerCase();
		if (fullWord.indexOf(subWord) == -1)
			return false;
		return true;
	}

	// return true if subword is a sub word of fullWord
	// H1----compare-->cmp
	public static boolean H3(String subWord, String fullWord)
	{
		int j = 0;
		for (int i = 0; j < subWord.length() && i < fullWord.length(); i++)
		{
//			System.out.println(fullWord.charAt(i) + "||" + subWord.charAt(j));
			if (((fullWord.toLowerCase().charAt(i)) == subWord.charAt(j)))
			{
				j++;
			}
		}
		if (j == subWord.length())
		{
			return true;
		}
		return false;
	}
//
//	public static void main(String[] args)
//	{
//		System.out.println(H1("sb", "String Buffer"));
//		System.out.println(H2("str", "String"));
//		System.out.println(H3("cmp", "compare"));
//	}

}
