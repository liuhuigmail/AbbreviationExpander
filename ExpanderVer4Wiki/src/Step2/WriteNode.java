package Step2;

import java.util.ArrayList;

public class WriteNode
{
	public static ArrayList<WriteNode> writerNodes = new ArrayList<WriteNode>();
	public String ID;
	public String comment;

	public WriteNode(String ID, String comment)
	{
		super();
		this.ID = ID;
		this.comment = comment;
	}

	public static String getCommentByID(String id)
	{
		for (WriteNode writeNode : writerNodes)
		{
			if (writeNode.ID.equals(id))
				return writeNode.comment;
		}
		return "";
	}

	@Override
	public String toString()
	{
		return comment.trim();
	}

}
