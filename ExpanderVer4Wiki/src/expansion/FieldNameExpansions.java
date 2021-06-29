package expansion;

import java.util.HashSet;

/**
 * Expansions for an identifier
 */
public class FieldNameExpansions {
	public HashSet<String> type;
	public HashSet<String> comment;
	public HashSet<String> enclosingClass;
	public HashSet<String> assignment;
	public HashSet<String> methodInvocated;
	public HashSet<String> parameterArgument;

	public FieldNameExpansions() {
		super();
		this.type = new HashSet<>();
		this.comment = new HashSet<>();
		this.enclosingClass = new HashSet<>();
		this.assignment = new HashSet<>();
		this.methodInvocated = new HashSet<>();
		this.parameterArgument = new HashSet<>();
	}

	@Override
	public String toString() {
		return "FieldNameExpansions [type=" + type + ", comment=" + comment + ", enclosingClass=" + enclosingClass
				+ ", assignment=" + assignment + ", methodInvocated=" + methodInvocated + ", parameterArgument="
				+ parameterArgument + "]";
	}	
	
	
}
