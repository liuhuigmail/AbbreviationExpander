package relation;

import java.util.ArrayList;

import entity.Argument;
import entity.MethodName;

public class MethodInvocationInfo extends RelationBase {
	public MethodName methodName;
	public ArrayList<Argument> arguments;
	public MethodInvocationInfo(int line, MethodName methodName, ArrayList<Argument> arguments) {
		super(line);
		this.methodName = methodName;
		this.arguments = arguments;
	}
	@Override
	public String toString() {
		return "MethodInvocationInfo [line=" + line + ", methodName=" + methodName + ", arguments=" + arguments + "]";
	}

	
}
