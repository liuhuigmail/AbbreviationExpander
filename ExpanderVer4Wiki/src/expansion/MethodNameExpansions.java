package expansion;

import java.util.HashSet;

/**
 * Expansions for an identifier
 */
public class MethodNameExpansions {
	public HashSet<String> type;
	public HashSet<String> comment;
	public HashSet<String> enclosingClass;
	public HashSet<String> parameter;
	public HashSet<String> assignment;
	public HashSet<String> parameterArgument;
	public HashSet<String> methodInvocated;


	public MethodNameExpansions() {
		super();
		this.type = new HashSet<>();
		this.comment = new HashSet<>();
		this.enclosingClass = new HashSet<>();
		this.parameter = new HashSet<>();
		this.assignment = new HashSet<>();
		this.parameterArgument = new HashSet<>();
		this.methodInvocated = new HashSet<>();
	}


	@Override
	public String toString() {
		return "MethodNameExpansions [type=" + type + ", comment=" + comment + ", enclosingClass=" + enclosingClass
				+ ", parameter=" + parameter + ", assignment=" + assignment + ", parameterArgument=" + parameterArgument
				+ ", methodInvocated=" + methodInvocated + "]";
	}	
	
	
}
