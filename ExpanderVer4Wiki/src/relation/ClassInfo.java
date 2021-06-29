package relation;

import java.util.ArrayList;

import entity.ClassName;
import entity.Field;
import entity.Identifier;
import entity.MethodName;

public class ClassInfo extends RelationBase {
	public ClassName className;
	public ArrayList<ClassName> expans;
	public ArrayList<Field> fields;
	public ArrayList<MethodName> methodNames;
	public ArrayList<Identifier> identifiers;

	public ClassInfo(int line, ClassName className, ArrayList<ClassName> expans, ArrayList<Field> assignInfos,
			ArrayList<MethodName> methodDeclarationInfos, ArrayList<Identifier> identifiers) {
		super(line);
		this.className = className;
		this.expans = expans;
		this.fields = assignInfos;
		this.methodNames = methodDeclarationInfos;
		this.identifiers = identifiers;
	}

	@Override
	public String toString() {
		return "ExtendInfo [line=" + line + ", className=" + className + ", expans=" + expans + "]";
	}
	
	
}
