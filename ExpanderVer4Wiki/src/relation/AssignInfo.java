package relation;

import java.util.ArrayList;

import entity.Identifier;

public class AssignInfo extends RelationBase {
	public ArrayList<Identifier> left;
	public ArrayList<Identifier> right;
	public AssignInfo(int line, ArrayList<Identifier> left, ArrayList<Identifier> right) {
		super(line);
		this.left = left;
		this.right = right;
	}
	@Override
	public String toString() {
		return "AssignInfo [line=" + line + ", left=" + left + ", right=" + right + "]";
	}

	
}
