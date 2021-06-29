package expansion;

import java.util.HashSet;

/**
 * Expansions for an identifier
 */
public class ClassNameExpansions {
	public HashSet<String> subclass;
	public HashSet<String> subsubclass;
	public HashSet<String> parents;
	public HashSet<String> ancestor;
	public HashSet<String> methods;
	public HashSet<String> fields;
	public HashSet<String> comment;

	public ClassNameExpansions() {
		super();
		this.subclass = new HashSet<>();
		this.subsubclass = new HashSet<>();
		this.parents = new HashSet<>();
		this.ancestor = new HashSet<>();
		this.methods = new HashSet<>();
		this.fields = new HashSet<>();
		this.comment = new HashSet<>();
	}

	@Override
	public String toString() {
		return "ClassNameExpansions [subclass=" + subclass + ", subsubclass=" + subsubclass + ", parents=" + parents
				+ ", ancestor=" + ancestor + ", methods=" + methods + ", fields=" + fields + ", comment=" + comment
				+ "]";
	}	
}
