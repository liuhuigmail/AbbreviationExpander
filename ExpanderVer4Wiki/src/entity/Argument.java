package entity;

import java.util.ArrayList;

/**
 * An argument is an expression
 */
public class Argument {
	// the type of the expression
	public ClassName type;
	// the identifiers in the expression
	public ArrayList<Identifier> identifiers;
	public Argument(ClassName type, ArrayList<Identifier> identifiers) {
		super();
		this.type = type;
		this.identifiers = identifiers;
	}
	@Override
	public String toString() {
		return "Argument [type=" + type + ", identifiers=" + identifiers + "]";
	}
}
