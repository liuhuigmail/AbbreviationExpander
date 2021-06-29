package jiangyanjie.menuHandles;

import java.util.ArrayList;

import Step1.Utils;
import entity.Argument;
import entity.ClassName;
import entity.Field;
import entity.Identifier;
import entity.MethodName;
import entity.Parameter;
import entity.Variable;
import expansion.AllExpansions;
import relation.AssignInfo;
import relation.ClassInfo;
import relation.CommentInfo;
import relation.MethodDeclarationInfo;
import relation.MethodInvocationInfo;
import relation.RelationBase;
public class HandleOneFile
{
	// relationBase that are selected from visitors in Package visitor
	public ArrayList<RelationBase> relationBases = new ArrayList<>();

	public void parse()
	{
		for (int i = 0; i < relationBases.size(); i++)
		{
			RelationBase relationBase = relationBases.get(i);
			if (relationBase instanceof AssignInfo)
			{
				handleAssign((AssignInfo) relationBase);
			} else if (relationBase instanceof ClassInfo)
			{
				handleExtend((ClassInfo) relationBase);
			} else if (relationBase instanceof MethodDeclarationInfo)
			{
				handleMethodDeclaration((MethodDeclarationInfo) relationBase);
			} else if (relationBase instanceof MethodInvocationInfo)
			{
				handleMethodInvocation((MethodInvocationInfo) relationBase);
			} else if (relationBase instanceof CommentInfo)
			{
				handleComment((CommentInfo) relationBase);
			}
		}
	}

	private void handleComment(CommentInfo commentInfo)
	{
		int startLine = commentInfo.startLine;
		int endLine = commentInfo.line;

		boolean currentLine = true;
		// comment in current line
		for (int i = 0; i < relationBases.size(); i++)
		{
			if (relationBases.get(i).line == startLine && relationBases.get(i) != commentInfo)
			{
				currentLine = false;
				handleRelationBaseAndCommentInfo(relationBases.get(i), commentInfo);
			}
		}
		if (currentLine == false)
		{
			return;
		}
		// comment in previous line
		for (int i = 0; i < relationBases.size(); i++)
		{
			if (relationBases.get(i).line == (endLine + 1))
			{
				handleRelationBaseAndCommentInfo(relationBases.get(i), commentInfo);
			}
		}
	}

	private void handleRelationBaseAndCommentInfo(RelationBase relationBase, CommentInfo commentInfo)
	{
		if (relationBase instanceof AssignInfo)
		{
			AssignInfo assignInfo = (AssignInfo) relationBase;
			ArrayList<Identifier> left = assignInfo.left;
			ArrayList<Identifier> right = assignInfo.right;
			for (int i = 0; i < left.size(); i++)
			{
				Utils.putHashSet(AllExpansions.idToComments, left.get(i).id, commentInfo.content);
			}
			if (right != null)
			{
				for (int i = 0; i < right.size(); i++)
				{
					Utils.putHashSet(AllExpansions.idToComments, right.get(i).id, commentInfo.content);
				}
			}
		} else if (relationBase instanceof ClassInfo)
		{
			ClassInfo extendInfo = (ClassInfo) relationBase;
			ClassName className = extendInfo.className;
			Utils.putHashSet(AllExpansions.idToComments, className.id, commentInfo.content);

			ArrayList<ClassName> classNames = extendInfo.expans;
			for (int j = 0; j < classNames.size(); j++)
			{
				Utils.putHashSet(AllExpansions.idToComments, classNames.get(j).id, commentInfo.content);
			}
		} else if (relationBase instanceof MethodDeclarationInfo)
		{
			MethodDeclarationInfo methodDeclarationInfo = (MethodDeclarationInfo) relationBase;
			Utils.putHashSet(AllExpansions.idToComments, methodDeclarationInfo.methodName.id, commentInfo.content);

			for (int j = 0; j < methodDeclarationInfo.parameters.size(); j++)
			{
				Utils.putHashSet(AllExpansions.idToComments, methodDeclarationInfo.parameters.get(j).id,
						commentInfo.content);
			}

			// not constructor
			if (methodDeclarationInfo.methodName.type != null)
			{
				Utils.putHashSet(AllExpansions.idToComments, methodDeclarationInfo.methodName.type.id,
						commentInfo.content);
			}

		} else if (relationBase instanceof MethodInvocationInfo)
		{
			MethodInvocationInfo methodInvocationInfo = (MethodInvocationInfo) relationBase;
			Utils.putHashSet(AllExpansions.idToComments, methodInvocationInfo.methodName.type.id, commentInfo.content);

			for (int j = 0; j < methodInvocationInfo.arguments.size(); j++)
			{
				Argument argument = methodInvocationInfo.arguments.get(j);
				for (int k = 0; k < argument.identifiers.size(); k++)
				{
					Utils.putHashSet(AllExpansions.idToComments, argument.identifiers.get(k).id, commentInfo.content);
				}
			}
		}
	}

	private void handleMethodInvocation(MethodInvocationInfo methodInvocationInfo)
	{

		Utils.putHashSet(AllExpansions.idToFiles, methodInvocationInfo.methodName.id, Global.LX.tempFile);
		AllExpansions.idToMethodName.put(methodInvocationInfo.methodName.id, methodInvocationInfo.methodName);

		Utils.putHashSet(AllExpansions.idToFiles, methodInvocationInfo.methodName.type.id, Global.LX.tempFile);
		AllExpansions.idToClassName.put(methodInvocationInfo.methodName.type.id, methodInvocationInfo.methodName.type);

		ArrayList<Argument> arguments = methodInvocationInfo.arguments;
		for (Argument argument : arguments)
		{
			ClassName type = argument.type;

			Utils.putHashSet(AllExpansions.idToFiles, type.id, Global.LX.tempFile);
			AllExpansions.idToClassName.put(type.id, type);

			ArrayList<Identifier> identifiers = argument.identifiers;

			for (Identifier identifier : identifiers)
			{
				Utils.putHashSet(AllExpansions.idToFiles, identifier.id, Global.LX.tempFile);
				AllExpansions.idToIdentifier.put(identifier.id, identifier);

				if (identifier instanceof Parameter)
				{
					Parameter parameter = (Parameter) identifier;
					Utils.putHashSet(AllExpansions.idToFiles, parameter.type.id, Global.LX.tempFile);
					AllExpansions.idToClassName.put(parameter.type.id, parameter.type);

				} else if (identifier instanceof Field)
				{
					Field parameter = (Field) identifier;
					Utils.putHashSet(AllExpansions.idToFiles, parameter.type.id, Global.LX.tempFile);
					AllExpansions.idToClassName.put(parameter.type.id, parameter.type);

				} else if (identifier instanceof Variable)
				{
					Variable parameter = (Variable) identifier;
					Utils.putHashSet(AllExpansions.idToFiles, parameter.type.id, Global.LX.tempFile);
					AllExpansions.idToClassName.put(parameter.type.id, parameter.type);

				} else if (identifier instanceof MethodName)
				{
					MethodName parameter = (MethodName) identifier;
					if (parameter.type != null)
					{
						Utils.putHashSet(AllExpansions.idToFiles, parameter.type.id, Global.LX.tempFile);
						AllExpansions.idToClassName.put(parameter.type.id, parameter.type);
					}
				}
			}
		}

		AllExpansions.methodInvocationInfos.add(methodInvocationInfo);
	}

	private void handleMethodDeclaration(MethodDeclarationInfo methodDeclarationInfo)
	{
		AllExpansions.methodDeclarationInfos.add(methodDeclarationInfo);
		Utils.putHashSet(AllExpansions.idToFiles, methodDeclarationInfo.methodName.id, Global.LX.tempFile);
		AllExpansions.idToMethodName.put(methodDeclarationInfo.methodName.id, methodDeclarationInfo.methodName);

		if (methodDeclarationInfo.methodName.type != null)
		{
			Utils.putHashSet(AllExpansions.idToFiles, methodDeclarationInfo.methodName.type.id, Global.LX.tempFile);
			AllExpansions.idToClassName.put(methodDeclarationInfo.methodName.type.id,
					methodDeclarationInfo.methodName.type);
		}

		ArrayList<Parameter> parameters = methodDeclarationInfo.parameters;
		for (int i = 0; i < parameters.size(); i++)
		{
			Utils.putHashSet(AllExpansions.idToFiles, parameters.get(i).id, Global.LX.tempFile);
			AllExpansions.idToParameter.put(parameters.get(i).id, parameters.get(i));

			Utils.putHashSet(AllExpansions.idToFiles, parameters.get(i).type.id, Global.LX.tempFile);
			AllExpansions.idToClassName.put(parameters.get(i).type.id, parameters.get(i).type);
		}

		ArrayList<Identifier> identifiers = methodDeclarationInfo.identifiers;

		for (Identifier identifier : identifiers)
		{
			Utils.putHashSet(AllExpansions.idToFiles, identifier.id, Global.LX.tempFile);
			AllExpansions.idToIdentifier.put(identifier.id, identifier);

			if (identifier instanceof Parameter)
			{
				Parameter parameter = (Parameter) identifier;
				Utils.putHashSet(AllExpansions.idToFiles, parameter.type.id, Global.LX.tempFile);
				AllExpansions.idToClassName.put(parameter.type.id, parameter.type);

			} else if (identifier instanceof Field)
			{
				Field parameter = (Field) identifier;
				Utils.putHashSet(AllExpansions.idToFiles, parameter.type.id, Global.LX.tempFile);
				AllExpansions.idToClassName.put(parameter.type.id, parameter.type);

			} else if (identifier instanceof Variable)
			{
				Variable parameter = (Variable) identifier;
				Utils.putHashSet(AllExpansions.idToFiles, parameter.type.id, Global.LX.tempFile);
				AllExpansions.idToClassName.put(parameter.type.id, parameter.type);

			} else if (identifier instanceof MethodName)
			{
				MethodName parameter = (MethodName) identifier;
				if (parameter.type != null)
				{
					Utils.putHashSet(AllExpansions.idToFiles, parameter.type.id, Global.LX.tempFile);
					AllExpansions.idToClassName.put(parameter.type.id, parameter.type);
				}
			}
		}

	}

	private void handleExtend(ClassInfo classInfo)
	{
		AllExpansions.classInfos.add(classInfo);

		ClassName className = classInfo.className;
		Utils.putHashSet(AllExpansions.idToFiles, className.id, Global.LX.tempFile);
		AllExpansions.idToClassName.put(className.id, className);

		ArrayList<ClassName> classNames = classInfo.expans;
		for (int i = 0; i < classNames.size(); i++)
		{
			Utils.putHashSet(AllExpansions.idToFiles, classNames.get(i).id, Global.LX.tempFile);
			AllExpansions.idToClassName.put(classNames.get(i).id, classNames.get(i));
			Utils.put(AllExpansions.childToParent, className.id, classNames.get(i).id);
			Utils.put(AllExpansions.parentToChild, classNames.get(i).id, className.id);
		}

		ArrayList<Field> fields = classInfo.fields;
		for (int i = 0; i < fields.size(); i++)
		{
			Field field = fields.get(i);
			Utils.putHashSet(AllExpansions.idToFiles, field.id, Global.LX.tempFile);
			AllExpansions.idToField.put(field.id, field);

			Utils.putHashSet(AllExpansions.idToFiles, field.type.id, Global.LX.tempFile);
			AllExpansions.idToClassName.put(field.type.id, field.type);
		}

		ArrayList<MethodName> methodNames = classInfo.methodNames;
		for (int i = 0; i < methodNames.size(); i++)
		{
			MethodName methodName = methodNames.get(i);
			Utils.putHashSet(AllExpansions.idToFiles, methodName.id, Global.LX.tempFile);
			AllExpansions.idToMethodName.put(methodName.id, methodName);

			if (methodName.type != null)
			{
				Utils.putHashSet(AllExpansions.idToFiles, methodName.type.id, Global.LX.tempFile);
				AllExpansions.idToClassName.put(methodName.type.id, methodName.type);
			}
		}
		ArrayList<Identifier> identifiers = classInfo.identifiers;

		for (Identifier identifier : identifiers)
		{
			Utils.putHashSet(AllExpansions.idToFiles, identifier.id, Global.LX.tempFile);
			AllExpansions.idToIdentifier.put(identifier.id, identifier);

			if (identifier instanceof Parameter)
			{
				Parameter parameter = (Parameter) identifier;
				Utils.putHashSet(AllExpansions.idToFiles, parameter.type.id, Global.LX.tempFile);
				AllExpansions.idToClassName.put(parameter.type.id, parameter.type);

			} else if (identifier instanceof Field)
			{
				Field parameter = (Field) identifier;
				Utils.putHashSet(AllExpansions.idToFiles, parameter.type.id, Global.LX.tempFile);
				AllExpansions.idToClassName.put(parameter.type.id, parameter.type);

			} else if (identifier instanceof Variable)
			{
				Variable parameter = (Variable) identifier;
				Utils.putHashSet(AllExpansions.idToFiles, parameter.type.id, Global.LX.tempFile);
				AllExpansions.idToClassName.put(parameter.type.id, parameter.type);

			} else if (identifier instanceof MethodName)
			{
				MethodName parameter = (MethodName) identifier;
				if (parameter.type != null)
				{
					Utils.putHashSet(AllExpansions.idToFiles, parameter.type.id, Global.LX.tempFile);
					AllExpansions.idToClassName.put(parameter.type.id, parameter.type);
				}
			}
		}
	}

	private void handleAssign(AssignInfo assignInfo)
	{
		AllExpansions.assignInfos.add(assignInfo);

		ArrayList<Identifier> left = assignInfo.left;
		ArrayList<Identifier> right = assignInfo.right;
		for (int i = 0; i < left.size(); i++)
		{
			Utils.putHashSet(AllExpansions.idToFiles, left.get(i).id, Global.LX.tempFile);
			AllExpansions.idToIdentifier.put(left.get(i).id, left.get(i));

			Identifier identifier = left.get(i);
			if (identifier instanceof Parameter)
			{
				Parameter parameter = (Parameter) identifier;
				Utils.putHashSet(AllExpansions.idToFiles, parameter.type.id, Global.LX.tempFile);
				AllExpansions.idToClassName.put(parameter.type.id, parameter.type);

			} else if (identifier instanceof Field)
			{
				Field parameter = (Field) identifier;
				Utils.putHashSet(AllExpansions.idToFiles, parameter.type.id, Global.LX.tempFile);
				AllExpansions.idToClassName.put(parameter.type.id, parameter.type);

			} else if (identifier instanceof Variable)
			{
				Variable parameter = (Variable) identifier;
				Utils.putHashSet(AllExpansions.idToFiles, parameter.type.id, Global.LX.tempFile);
				AllExpansions.idToClassName.put(parameter.type.id, parameter.type);

			} else if (identifier instanceof MethodName)
			{
				MethodName parameter = (MethodName) identifier;
				if (parameter.type != null)
				{
					Utils.putHashSet(AllExpansions.idToFiles, parameter.type.id, Global.LX.tempFile);
					AllExpansions.idToClassName.put(parameter.type.id, parameter.type);
				}
			}
		}
		if (right != null)
		{
			for (int i = 0; i < right.size(); i++)
			{
				Utils.putHashSet(AllExpansions.idToFiles, right.get(i).id, Global.LX.tempFile);
				AllExpansions.idToIdentifier.put(right.get(i).id, right.get(i));

				Identifier identifier = right.get(i);
				if (identifier instanceof Parameter)
				{
					Parameter parameter = (Parameter) identifier;
					Utils.putHashSet(AllExpansions.idToFiles, parameter.type.id, Global.LX.tempFile);
					AllExpansions.idToClassName.put(parameter.type.id, parameter.type);

				} else if (identifier instanceof Field)
				{
					Field parameter = (Field) identifier;
					Utils.putHashSet(AllExpansions.idToFiles, parameter.type.id, Global.LX.tempFile);
					AllExpansions.idToClassName.put(parameter.type.id, parameter.type);

				} else if (identifier instanceof Variable)
				{
					Variable parameter = (Variable) identifier;
					Utils.putHashSet(AllExpansions.idToFiles, parameter.type.id, Global.LX.tempFile);
					AllExpansions.idToClassName.put(parameter.type.id, parameter.type);

				} else if (identifier instanceof MethodName)
				{
					MethodName parameter = (MethodName) identifier;
					if (parameter.type != null)
					{
						Utils.putHashSet(AllExpansions.idToFiles, parameter.type.id, Global.LX.tempFile);
						AllExpansions.idToClassName.put(parameter.type.id, parameter.type);
					}
				}
			}
		}
	}
}
