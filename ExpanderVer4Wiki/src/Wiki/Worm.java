package Wiki;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Worm
{
	public static void main(String[] args)
	{
//		Wiki("xml");
//		Wiki("HTTP");
//		Wiki("L4D");
//		Wiki("hk");
//		Wiki("calc");
	Wiki("ci");
	}

	private static String Parse(String rawStr)
	{// extract rawStr
		String raw = rawStr;
		if (raw.contains("<"))
		{
			raw = raw.substring(raw.indexOf(">") + 1, raw.lastIndexOf("<"));
		}
		System.out.println(raw);
		return raw;
	}

	private static boolean CheckOk(String item, String Possi)
	{
		System.out.println(item + "||" + Possi);
		System.out.println(Step2.Heu.H1(item, Possi) || Step2.Heu.H2(item, Possi) || Step2.Heu.H3(item, Possi));

		return (Step2.Heu.H1(item, Possi) || Step2.Heu.H2(item, Possi) || Step2.Heu.H3(item, Possi));
//		return false;
	}

	public static HashSet<String> Wiki(String item)
	{
		if (item.length() == 1)
			return null;
		HashSet<String> res = new HashSet<String>();
//			Document doc = Jsoup.parse(new File("E:/materials/test.html"),"utf8");

		Document doc = null;
		try
		{
			doc = Jsoup.parse(new URL("https://en.jinzhao.wiki/wiki/" + item), 3000);
		} catch (IOException e)
		{
			try
			{
				doc = Jsoup.parse(new URL("https://en.jinzhao.wiki/w/index.php?search=" + item), 3000);
			} catch (IOException e1)
			{
				System.out.println("无法连接至此站点，请更换维基百科镜像地址重试！");
				return null;
			}
		}
//		System.out.println(doc.baseUri());
//			doc = Jsoup.parse(new URL("https://en.jinzhao.wiki/w/index.php?search=" + item), 3000);

//		System.out.println("////////////////////////////////////");
		Element redir = doc.getElementById("firstHeading");
		{
			if (CheckOk(item, redir.text()))
				res.add(redir.text());
		}
		// 0. L4D
		if (redir.getElementsByTag("i").size() != 0)
		{
//			System.out.println("重定向页面的标题" + redir.getElementsByTag("i").first().text());
			if (CheckOk(item, redir.getElementsByTag("i").first().text()))
				res.add(redir.getElementsByTag("i").first().text());
		}
//		System.out.println("////////////////////////////////////");
		// cov19
		Element ele2 = doc.getElementsByClass("searchdidyoumean").first();
		if (ele2 != null)
		{
//			System.out.println(ele2.getElementsByTag("a").first().text());
			if (CheckOk(item, ele2.getElementsByTag("a").first().text()))
				res.add(ele2.getElementsByTag("a").first().text());
//			System.out.println("////////////////////////////////////");
		}
//		for (String string : res)
//		{
//			System.out.println(string);
//		}

//			if (res.size() >= 1)
//				return res;
		// 1. xml \ html
		Elements possi1 = doc.select("b");
		int num = 0;
		for (Element element : possi1)
		{
			if (num++ >= 5)
				break;
//			System.out.println(element.toString());
			String str = Parse(element.toString());
			if (!str.contains("<") && CheckOk(item, str) && !item.equals(str))
			{
//				System.out.println(str);
				res.add(str);
			}
		}
//		System.out.println("////////////////////////////////////");
		// 2. cat dog
		if (res.size() != 0)
		{
			Element content = doc.getElementById("mw-content-text");
			Elements Ps = content.getElementsByTag("p");
			num = 0;
			for (Element element : Ps)
			{
				if (num++ < 3)
					System.out.println(element.text());
			}
		}
		System.out.println("////////////////////////////////////");
//		 cn mj
		Element ele1 = doc.getElementById("mw-content-text");
		Elements elems = ele1.select("li");
		for (Element element : elems)
		{
			if (element.getElementsByTag("a").size() != 0)
				System.out.println(element.getElementsByTag("a").first().text());
		}
		System.out.println("////////////////////////////////////");

		return res;

	}

//	public static void GetText() throws IOException
//	{
//		Document doc = Jsoup.parse(new File("E:/materials/test.html"), "utf8");
//		String str = doc.select("div").text();
//		String str2 = doc.select("span").text();
//		String str3 = doc.select("li").text();
//		System.out.println(str + "\n" + str2 + "\n" + str3);
//	}
//
//	public static void testDom() throws Exception
//	{
//		Document doc = Jsoup.parse(new File("E:/materials/test.html"), "utf8");
//		// 1 getElementById
//		Element ele = doc.getElementById("siteSub");
//		System.out.println(ele.text());
	// 2 getElementByYag
//			Elements elems = doc.getElementsByTag("span");
//			for (Element element : elems)
//			{
//				System.out.println(element.text());
//			}

	// 3 getElementByClass
//			Elements elems1 = doc.getElementsByClass("dmbox-disambig");
//			for (Element element : elems1)
//			{
//				System.out.println(element.text());
//			} 
	// 4 getElementByAttribute
//			Elements elems2 = doc.getElementsByAttribute("role");
//			for (Element element : elems2)
//			{
//				System.out.println(element.text());
//			}
//		Elements elems = doc.getElementsByAttributeValue("id", "p-namespaces-label");
//			for (Element element : elems)
//			{
//				System.out.println(element.text());
//			}

//	}

//	public static void GetData() throws Exception
//	{
//		Document doc = Jsoup.parse(new File("E:/materials/test.html"), "utf8");
//		System.out.println(doc);
////			Element ele = doc.getElementById("siteSub");
////			System.out.println(ele.text()+"~~"+ele.id()+"~~"+ele.className());
////			System.out.println(ele.attr("class"));
////			System.out.println(ele.attributes());
//	}
//
//	public static void testUrl() throws Exception
//	{
//		Document doc = Jsoup.parse(new URL("https://en.jinzhao.wiki/wiki/DG"), 1000);
//		// label selector
//		String title = doc.getElementsByTag("title").first().text();
//		System.out.println(title);
//
//	}
//
//	public static void testString() throws Exception
//	{
//		String content = FileUtils.readFileToString(new File("C:/Users/Liu Xiang/Desktop/test.html"), "utf8");
//		Document doc = Jsoup.parse(content);
//		String title = doc.getElementsByTag("title").first().text();
//		System.out.println(title);
//	}
//
//	public static void testFile() throws Exception
//	{
//		Document doc = Jsoup.parse(new File("C:/Users/Liu Xiang/Desktop/test.html"), "utf8");
//		String title = doc.getElementsByTag("title").first().text();
//		System.out.println(title);
//
//	}

}
