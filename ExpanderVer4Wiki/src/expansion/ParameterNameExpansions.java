package expansion;

import java.util.HashSet;

/**
 * Expansions for an identifier
 */
public class ParameterNameExpansions {
	public HashSet<String> type;
	public HashSet<String> comment;
	public HashSet<String> parameterArgument;
	public HashSet<String> enclosingMethod;
	public HashSet<String> argument;
	public HashSet<String> assignment;
	public HashSet<String> methodInvocated;

	public ParameterNameExpansions() {
		super();
		this.type = new HashSet<>();
		this.comment = new HashSet<>();
		this.parameterArgument = new HashSet<>();
		this.enclosingMethod = new HashSet<>();
		this.argument = new HashSet<>();
		this.assignment = new HashSet<>();
		this.methodInvocated = new HashSet<>();

	}

	@Override
	public String toString() {
		return "ParameterNameExpansions [type=" + type + ", comment=" + comment + ", parameterArgument="
				+ parameterArgument + ", enclosingMethod=" + enclosingMethod + ", argument=" + argument
				+ ", assignment=" + assignment + ", methodInvocated=" + methodInvocated + "]";
	}	
	
	
}
