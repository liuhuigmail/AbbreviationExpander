package visitor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import entity.ClassName;
import entity.Field;
import entity.Identifier;
import entity.MethodName;
import entity.Variable;
import jiangyanjie.menuHandles.HandleOneFile;
import relation.AssignInfo;
import relation.ClassInfo;
import relation.MethodDeclarationInfo;

/**
 * parse names of super class and super interface
 */
public class ClassVisitor extends ASTVisitor
{
	private CompilationUnit compilationUnit;
	private HandleOneFile globalVariables;

	public ClassVisitor(CompilationUnit compilationUnit, HandleOneFile globalVariables)
	{
		super();
		this.compilationUnit = compilationUnit;
		this.globalVariables = globalVariables;
	}

	@Override
	public boolean visit(TypeDeclaration node)
	{
//		System.err.println(node.resolveBinding().getKey());
//		int nodeStartLineNum = Global.LX.unit.getLineNumber(node.getStartPosition());
//		int docLine = 0;
//		if (node.getJavadoc() != null)
//			docLine = jiangyanjie.menuHandles.CommentNode.GetLineNum(nodeStartLineNum);
		// get line number
		Javadoc javaDoc = node.getJavadoc();
		int line;
		if (javaDoc == null)
		{
			line = compilationUnit.getLineNumber(node.getStartPosition());
		} else
		{
			line = compilationUnit.getLineNumber(node.getStartPosition() + javaDoc.getLength()) + 1;
		}

		SimpleVisitor simpleVisitor = new SimpleVisitor();
		node.accept(simpleVisitor);
		ArrayList<Identifier> identifiers = simpleVisitor.identifiers;
		ArrayList<MethodName> methodNames = new ArrayList<>();
		MethodDeclaration[] methods = node.getMethods();
		for (MethodDeclaration methodDeclaration : methods)
		{
			MethodDeclarationInfo methodDeclarationInfo = MethodDeclarationVisitor
					.getMethodDeclarationInfo(methodDeclaration, compilationUnit);
			methodNames.add(methodDeclarationInfo.methodName);
		}

		ArrayList<Field> fields = new ArrayList<>();
		FieldDeclaration[] fieldDeclarations = node.getFields();
		for (FieldDeclaration fieldDeclaration : fieldDeclarations)
		{

			List<VariableDeclarationFragment> fragments = fieldDeclaration.fragments();
			for (VariableDeclarationFragment variableDeclarationFragment : fragments)
			{
//				System.out.println(variableDeclarationFragment.getName());
//				System.out.println(variableDeclarationFragment.resolveBinding().getKey());
//				System.out.println(compilationUnit.getLineNumber(variableDeclarationFragment.getStartPosition()));
				Global.LX.IdLineInFile.put(variableDeclarationFragment.resolveBinding().getKey(), line);

				AssignInfo assignInfo = AssignVistor.getAssignInfo(variableDeclarationFragment, compilationUnit);

				if (assignInfo.left.size() != 1)
				{
					System.err.println("field size not 1");
				}
				if (!(assignInfo.left.get(0) instanceof Variable))
				{
					System.err.println("field not variable");
				}
				Variable variable = (Variable) assignInfo.left.get(0);
				Field field = new Field(variable);
				fields.add(field);
			}
		}

		if (node.resolveBinding() == null)
		{
			System.err.println(node.toString());
			return super.visit(node);
		}
		String id1 = node.resolveBinding().getKey();
		String name1 = node.getName().toString();
		Global.LX.IdLineInFile.put(id1, line);

		ClassName className1 = new ClassName(id1, name1);
		ArrayList<ClassName> expans = new ArrayList<>();
		// super class
		Type temp = node.getSuperclassType();
		if (temp != null)
		{
			if (temp.resolveBinding() == null)
			{
				System.err.println(node.toString());
				return super.visit(node);
			}
			String id2 = temp.resolveBinding().getKey();
			String name2 = temp.resolveBinding().getName();
			Global.LX.IdLineInFile.put(id2, line);

			ClassName className2 = new ClassName(id2, name2);
			expans.add(className2);
		}
		// super interfaces
		List<?> interfaces = node.superInterfaceTypes();
		for (Object object : interfaces)
		{
			Type type = (Type) object;
			if (type.resolveBinding() == null)
			{
				System.err.println(node.toString());
				return super.visit(node);
			}
			String id = type.resolveBinding().getKey();
			String name = type.resolveBinding().getName();
			Global.LX.IdLineInFile.put(id, compilationUnit.getLineNumber(node.getStartPosition()));
			ClassName className = new ClassName(id, name);
			expans.add(className);
		}

		// get line number
//		Javadoc javaDoc = node.getJavadoc();
//		int line;
//		if (javaDoc == null)
//		{
//			line = compilationUnit.getLineNumber(node.getStartPosition());
//		} else
//		{
//			line = compilationUnit.getLineNumber(node.getStartPosition() + javaDoc.getLength()) + 1;
//		}

		ClassInfo extendInfo = new ClassInfo(line, className1, expans, fields, methodNames, identifiers);
		globalVariables.relationBases.add(extendInfo);
		return super.visit(node);
	}
}
