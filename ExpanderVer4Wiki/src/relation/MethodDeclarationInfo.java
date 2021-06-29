package relation;

import java.util.ArrayList;

import entity.Identifier;
import entity.MethodName;
import entity.Parameter;

public class MethodDeclarationInfo extends RelationBase {
	public MethodName methodName;
	public ArrayList<Parameter> parameters;
	public ArrayList<Identifier> identifiers;

	public MethodDeclarationInfo(int line, MethodName methodName, ArrayList<Parameter> parameters,
			ArrayList<Identifier> identifiers) {
		super(line);
		this.methodName = methodName;
		this.parameters = parameters;
		this.identifiers = identifiers;
	}

	@Override
	public String toString() {
		return "MethodDeclarationInfo [methodName=" + methodName + ", parameters=" + parameters + ", line=" + line
				+ "]";
	}
}
