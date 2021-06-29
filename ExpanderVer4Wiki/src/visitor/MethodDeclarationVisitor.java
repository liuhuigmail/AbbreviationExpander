package visitor;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

import entity.ClassName;
import entity.Identifier;
import entity.MethodName;
import entity.Parameter;
import jiangyanjie.menuHandles.HandleOneFile;
import relation.MethodDeclarationInfo;

public class MethodDeclarationVisitor extends ASTVisitor
{
	private CompilationUnit compilationUnit;
	private HandleOneFile globalVariables;

	public MethodDeclarationVisitor(CompilationUnit compilationUnit, HandleOneFile globalVariables)
	{
		super();
		this.compilationUnit = compilationUnit;
		this.globalVariables = globalVariables;

	}

	@Override
	public boolean visit(MethodDeclaration node)
	{
		MethodDeclarationInfo methodDeclarationInfo = getMethodDeclarationInfo(node, compilationUnit);
		if (methodDeclarationInfo != null)
		{
			globalVariables.relationBases.add(methodDeclarationInfo);
		}
		return super.visit(node);
	}

	public static MethodDeclarationInfo getMethodDeclarationInfo(MethodDeclaration node,
			CompilationUnit compilationUnit)
	{
		SimpleVisitor simpleVisitor = new SimpleVisitor();
		node.accept(simpleVisitor);
		ArrayList<Identifier> identifiers = simpleVisitor.identifiers;

		if (node.resolveBinding() == null)
		{
			System.err.println(node.toString());
			return null;
		}
		// method name
		String id = node.resolveBinding().getKey();
		String name = node.getName().toString();
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
//		System.out.println(id + "||" + name);
//		System.out.println(line);
		Global.LX.IdLineInFile.put(id, line);
		MethodName methodName;
		if (!node.isConstructor())
		{
			if (node.getReturnType2().resolveBinding() == null)
			{
				System.err.println(node.toString());
				return null;
			}
			String typeid = node.getReturnType2().resolveBinding().getKey();
			String typename = node.getReturnType2().resolveBinding().getName().toString();
//			System.out.println(typeid+"||"+typename);
			Global.LX.IdLineInFile.put(typeid, line);
			ClassName className = new ClassName(typeid, typename);
			methodName = new MethodName(id, name, className);
		} else
		{
			methodName = new MethodName(id, name, null);
		}

		// parameters
		ArrayList<Parameter> parameters = new ArrayList<>();
		for (Object object : node.parameters())
		{
			SingleVariableDeclaration svd = (SingleVariableDeclaration) object;
			String id2 = svd.resolveBinding().getKey();
			String name2 = svd.getName().toString();
			Global.LX.IdLineInFile.put(id2, line);
			if (svd.getType().resolveBinding() == null)
			{
				System.err.println(node.toString());
				return null;
			}

			Parameter parameter = new Parameter(id2, name2, new ClassName(svd.getType().resolveBinding().getKey(),
					svd.getType().resolveBinding().getName().toString()));
			parameters.add(parameter);
		}

		// get line number
//		Javadoc javaDoc = node.getJavadoc();
//		int line;
//		if (javaDoc == null) {
//			line = compilationUnit.getLineNumber(node.getStartPosition());
//		} else {
//			line = compilationUnit.getLineNumber(node.getStartPosition()+javaDoc.getLength())+1;
//		}

		MethodDeclarationInfo methodDeclarationInfo = new MethodDeclarationInfo(line, methodName, parameters,
				identifiers);
		return methodDeclarationInfo;
	}
}
