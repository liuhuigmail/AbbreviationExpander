package visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BlockComment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.LineComment;

import jiangyanjie.menuHandles.HandleOneFile;
import relation.CommentInfo;

public class CommentVisitor extends ASTVisitor
{
	CompilationUnit compilationUnit;
	// source code
	private String[] source;
	private HandleOneFile globalVariables;

	public CommentVisitor(CompilationUnit compilationUnit, String[] source, HandleOneFile globalVariables)
	{
		super();
		this.compilationUnit = compilationUnit;
		this.source = source;
		this.globalVariables = globalVariables;

	}

	@Override
	public boolean visit(Javadoc node)
	{
		int startLineNumber = compilationUnit.getLineNumber(node.getStartPosition());
		int endLineNumber = compilationUnit.getLineNumber(node.getStartPosition() + node.getLength());

		CommentInfo comment = new CommentInfo(startLineNumber, endLineNumber, node.toString());
		globalVariables.relationBases.add(comment);
		return super.visit(node);
	}

	@Override
	public boolean visit(LineComment node)
	{
		int startLineNumber = compilationUnit.getLineNumber(node.getStartPosition());
		String content = source[startLineNumber - 1].trim();
		content = content.substring(content.indexOf("//"));
		CommentInfo comment = new CommentInfo(startLineNumber, startLineNumber, content);
		globalVariables.relationBases.add(comment);
		return true;
	}

	@Override
	public boolean visit(BlockComment node)
	{
		int startLineNumber = compilationUnit.getLineNumber(node.getStartPosition());
		int endLineNumber = compilationUnit.getLineNumber(node.getStartPosition() + node.getLength());

		StringBuffer blockComment = new StringBuffer();
		for (int lineCount = startLineNumber; lineCount <= endLineNumber; lineCount++)
		{
			String blockCommentLine = source[lineCount - 1].trim();
			blockComment.append(blockCommentLine);
			if (lineCount != endLineNumber)
			{
				blockComment.append("\n");
			}
		}
		CommentInfo comment = new CommentInfo(startLineNumber, endLineNumber, blockComment.toString());
		globalVariables.relationBases.add(comment);
		return true;
	}
}