package relation;

public class CommentInfo extends RelationBase {
	public int startLine;
	public String content;
	public CommentInfo(int startLine, int line, String content) {
		super(line); // end line
		this.startLine = startLine;
		this.content = content.replaceAll("\n", " ");
	}
	@Override
	public String toString() {
		return "CommentInfo [startLine=" + startLine + ", content=" + content + ", line=" + line + "]";
	}
}
