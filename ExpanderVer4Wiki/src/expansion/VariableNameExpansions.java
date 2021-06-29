package expansion;

import java.util.HashSet;

/**
 * Expansions for an identifier
 */
public class VariableNameExpansions {
	public HashSet<String> type;
	public HashSet<String> methodInvocated;
	public HashSet<String> comment;
	public HashSet<String> enclosingMethod;
	public HashSet<String> enclosingClass;
	public HashSet<String> parameterArgument;
	public HashSet<String> assignment;

	public VariableNameExpansions() {
		super();
		this.type = new HashSet<>();
		this.methodInvocated = new HashSet<>();
		this.comment = new HashSet<>();
		this.enclosingMethod = new HashSet<>();
		this.enclosingClass = new HashSet<>();
		this.parameterArgument = new HashSet<>();
		this.assignment = new HashSet<>();
	}

	@Override
	public String toString() {
		return "VariableNameExpansions [type=" + type + ", methodInvocated=" + methodInvocated + ", comment=" + comment
				+ ", enclosingMethod=" + enclosingMethod + ", enclosingClass=" + enclosingClass + ", parameterArgument="
				+ parameterArgument + ", assignment=" + assignment + "]";
	}	
	
}
