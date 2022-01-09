package rs.ac.bg.etf.pp1;


import java.util.ArrayList;
import java.util.List;

import javax.lang.model.type.NoType;

import org.apache.log4j.Logger;


import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.etf.pp1.symboltable.concepts.*;
import rs.ac.bg.etf.pp1.ast.BoolConst;
import rs.ac.bg.etf.pp1.ast.CharConst;
import rs.ac.bg.etf.pp1.ast.ClassDeclMethods;
import rs.ac.bg.etf.pp1.ast.ClassDeclMethodsNoConstr;
import rs.ac.bg.etf.pp1.ast.ClassDeclNoMethods;
import rs.ac.bg.etf.pp1.ast.ClassFields;
import rs.ac.bg.etf.pp1.ast.ClassName;
import rs.ac.bg.etf.pp1.ast.ClassNameExtends;
import rs.ac.bg.etf.pp1.ast.ClassNameSingle;
import rs.ac.bg.etf.pp1.ast.ClassVariableDecl;
import rs.ac.bg.etf.pp1.ast.ClassVariableDeclList;
import rs.ac.bg.etf.pp1.ast.ConstDecl;
import rs.ac.bg.etf.pp1.ast.ConstantDecl;
import rs.ac.bg.etf.pp1.ast.ConstantDecls;
import rs.ac.bg.etf.pp1.ast.ConstructorDecl;
import rs.ac.bg.etf.pp1.ast.ConstructorName;
import rs.ac.bg.etf.pp1.ast.DesignatorBracket;
import rs.ac.bg.etf.pp1.ast.GlobalOneArrayVariableDecl;
import rs.ac.bg.etf.pp1.ast.GlobalOneVarDecl;
import rs.ac.bg.etf.pp1.ast.GlobalOneVariableDecl;
import rs.ac.bg.etf.pp1.ast.GlobalVarDeclList;
import rs.ac.bg.etf.pp1.ast.GlobalVarDecls;
import rs.ac.bg.etf.pp1.ast.MethodDecl;
import rs.ac.bg.etf.pp1.ast.MethodDeclNoStmt;
import rs.ac.bg.etf.pp1.ast.MethodDeclStmt;
import rs.ac.bg.etf.pp1.ast.MethodFormParams;
import rs.ac.bg.etf.pp1.ast.MethodName;
import rs.ac.bg.etf.pp1.ast.MethodReturnValue;
import rs.ac.bg.etf.pp1.ast.NumConst;
import rs.ac.bg.etf.pp1.ast.OneArrayFormParam;
import rs.ac.bg.etf.pp1.ast.OneArrayVariableDecl;
import rs.ac.bg.etf.pp1.ast.OneFormParam;
import rs.ac.bg.etf.pp1.ast.OneVarDecl;
import rs.ac.bg.etf.pp1.ast.OneVariableDecl;
import rs.ac.bg.etf.pp1.ast.ProgName;
import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.ast.RecordDecl;
import rs.ac.bg.etf.pp1.ast.RecordName;
import rs.ac.bg.etf.pp1.ast.RecordVariableDeclaration;
import rs.ac.bg.etf.pp1.ast.ReturnType;
import rs.ac.bg.etf.pp1.ast.ReturnVoid;
import rs.ac.bg.etf.pp1.ast.SyntaxNode;
import rs.ac.bg.etf.pp1.ast.Type;
import rs.ac.bg.etf.pp1.ast.TypeConst;
import rs.ac.bg.etf.pp1.ast.VarDecl;
import rs.ac.bg.etf.pp1.ast.VariableDecl;
import rs.etf.pp1.symboltable.*;

public class SemanticAnalyzer extends VisitorAdaptor {
	
	static final Struct boolType = Tab.insert(Obj.Type, "bool", new Struct(Struct.Bool)).getType();
	static final Struct recordType = Tab.insert(Obj.Type, "record", new Struct(Struct.Class)).getType();
	static final Struct classType = Tab.insert(Obj.Type, "class", new Struct(Struct.Class)).getType();
	
	int printCallCount = 0;
	int varDeclCount = 0;
	Obj currentMethod = null;
	Obj currentRecord = null;
	Obj currentClass = null;
	Struct currentType = null;
	boolean returnFound = false;
	boolean errorDetected = false;
	int nVars;
	
	List<String> names = new ArrayList<>();
	List<String> variableTypes = new ArrayList<>();
	List<Obj> constValues = new ArrayList<>();
	List<Obj> params = new ArrayList<>();
	boolean mainOk = false;
	
	Logger log = Logger.getLogger(getClass());

	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message); 
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.info(msg.toString());
	}
	
	
	public void visit(ProgName progName) {
		progName.obj = Tab.insert(Obj.Prog, progName.getProgName(), Tab.noType);
		Tab.openScope();
	}
	
	public void visit(Program program) {
		Tab.chainLocalSymbols(program.getProgName().obj);
		Tab.closeScope();
	}
	
	public void visit(Type type) {
		Obj typeNode = Tab.find(type.getTypeName());
		if(typeNode == Tab.noObj){
    		report_error("Semanticka greska: Tip " + type.getTypeName() + " nije pronadjen u tabeli simbola! ", null);
    		type.struct = Tab.noType;
    	}else{
    		if(Obj.Type == typeNode.getKind()){
    			type.struct = typeNode.getType();
    		}else{
    			report_error("Greska: Ime " + type.getTypeName() + " ne predstavlja tip!", type);
    			type.struct = Tab.noType;
    		}
    	}
	}
	
	
	/* global variables */
	public void visit(GlobalOneVariableDecl oneVariableDecl) {
		String varName = oneVariableDecl.getVarName();
		if (Tab.find(varName) == Tab.noObj &&  !(names.size() > 0 && names.contains(varName))) {   //nije deklarisan
			names.add(varName);
			variableTypes.add("var");
		} else {
			report_error("Semanticka greska: globalna promenljiva " + varName + " je vec deklarisana", null);
		}
		
	}
	
	public void visit(GlobalOneArrayVariableDecl oneArrayVariableDecl) {
		String varName = oneArrayVariableDecl.getVarName();
		if (Tab.find(varName) == Tab.noObj &&  !(names.size() > 0 && names.contains(varName))) {   //nije deklarisan
			names.add(varName);
			variableTypes.add("array");
		} else {
			report_error("Semanticka greska: globalni niz " + varName + " je vec deklarisan", null);
		}
	}
	
	public void visit(GlobalVarDecls globalVarDecls) {
		Struct varType = globalVarDecls.getType().struct;
		Struct varArrayType = new Struct(Struct.Array, varType);
		while (!names.isEmpty()) {
			String varName = names.remove(0);
			String type = variableTypes.remove(0);
			if (type.equals("var")) {
				Obj node = Tab.insert(Obj.Var, varName, varType);
				report_info("Deklarisana globalna promenljiva " + varName, null);
			} else {
				Tab.insert(Obj.Var, varName, varArrayType);
				report_info("Deklarisan globalni niz " + varName, null);
			}
		}
		
	}
	
	/* methods */
	public void visit(ReturnType returnType) {
		currentType = returnType.getType().struct;
	}
	
	public void visit(ReturnVoid returnVoid) {
		currentType = Tab.noType;
	}
	
	public void visit(MethodName methodName) {
		String name = methodName.getMethodName();
		if (Tab.find(name) == Tab.noObj || Tab.find(name).getKind() != Obj.Meth) {
			if (name.equals("main")) 
				mainOk = true;
			currentMethod = Tab.insert(Obj.Meth, name, currentType);
			currentType = null;
			//names.add(name);
			Tab.openScope();
		} else {
			report_error("Semanticka greska: metoda sa imenom " + name + " je vec deklarisana.", null);
		}
	}
	
	public void visit(MethodDeclStmt methodDecl) {
		Tab.chainLocalSymbols(currentMethod);
		Tab.closeScope();
		currentMethod = null;
	}
	
	public void visit(MethodDeclNoStmt methodDecl) {
		Tab.chainLocalSymbols(currentMethod);
		Tab.closeScope();
		currentMethod = null;
	}
	
	/* formal parameters */
	public void visit(MethodFormParams methodFormParams) {
		params.clear();
		if (currentMethod.getName().equals("main")) {
			report_error("Semanticka greska: main metoda ne sme da ima parametre.", null);
		} 
		if (currentMethod.getName().equals("main") && currentMethod.getType() != Tab.noType) {
			report_error("Semanticka greska: povratna vrednost main metode mora biti void.", null);
		}
	}
	
	public void visit(OneFormParam oneFormParam) {
		String paramName = oneFormParam.getParamName();
		Struct type = oneFormParam.getType().struct;
		
		if (Tab.find(paramName) == Tab.noObj) {
			Obj obj = Tab.insert(Obj.Var, paramName, type);
			params.add(obj);
			currentMethod.setLevel(params.size());
			report_info("Deklarisan formalni parametar " + paramName , null);
		} else {
			report_error("Semanticka greska: ime " + paramName + " je vec deklarisano.", null);
		}	
	}
	
	public void visit(OneArrayFormParam oneFormParam) {
		String paramName = oneFormParam.getParamName();
		Struct type = new Struct(Struct.Array, oneFormParam.getType().struct);
		if (Tab.find(paramName) == Tab.noObj) {
			Obj obj = Tab.insert(Obj.Var, paramName, type);
			params.add(obj);
			currentMethod.setLevel(params.size());
			report_info("Deklarisan formalni parametar " + paramName, null);
		} else {
			report_error("Semanticka greska: ime " + paramName + " je vec deklarisano.", null);
		}	
	}
	
	/* local variables */
	public void visit(OneVariableDecl oneVariableDecl) {
		String varName = oneVariableDecl.getVarName();
		if (Tab.find(varName) == Tab.noObj &&  !(names.size() > 0 && names.contains(varName))) {   //nije deklarisan
			names.add(varName);
			variableTypes.add("var");
		} else {
			report_error("Semanticka greska: lokalna promenljiva " + varName + " je vec deklarisana", null);
		}
		
	}
	
	public void visit(OneArrayVariableDecl oneArrayVariableDecl) {
		String varName = oneArrayVariableDecl.getVarName();
		if (Tab.find(varName) == Tab.noObj &&  !(names.size() > 0 && names.contains(varName))) {   //nije deklarisan
			names.add(varName);
			variableTypes.add("array");
		} else {
			report_error("Semanticka greska: lokalni niz " + varName + " je vec deklarisan", null);
		}
	}
	
	public void visit(VariableDecl variableDecl) {
		if (Tab.find(variableDecl.getType().getTypeName()) == Tab.noObj) {
			report_error("Semanticka greska: tip " + variableDecl.getType().getTypeName() + " nije pronadjen u tabeli simbola." , null);
			return;
		}
		currentType = variableDecl.getType().struct;
	}
	
	public void visit(VarDecl varDecl) {
		Struct varType = currentType;
		Struct varArrayType = new Struct(Struct.Array, currentType);
		while (!names.isEmpty()) {
			String localVarName = names.remove(0);
			String type = variableTypes.remove(0);
			if (type.equals("var")) {
				Obj node = Tab.insert(Obj.Var, localVarName, varType);
				report_info("Deklarisana lokalna promenljiva " + localVarName, null);
			} else {
				Tab.insert(Obj.Var, localVarName, varArrayType);
				report_info("Deklarisan lokalni niz " + localVarName, null);
			}
		}
		currentType = null;
	}
	
	public void visit(ConstantDecl constantDecl) {
		currentType = constantDecl.getType().struct;
		if (!currentType.equals(Tab.intType) && !currentType.equals(Tab.charType) && !currentType.equals(boolType)) {
			report_error("Semanticka greska: konstanta mora biti tipa int, char ili bool.", null);
		}
		String constName = constantDecl.getConstName();
		if (Tab.find(constName) == Tab.noObj) {
			names.add(constName);
			constValues.add(constantDecl.getTypeConst().obj);
		}  else {
			report_error("Semanticka greska: konstanta " + constName + " je vec deklarisana.", null);
		}
	}
	
	public void visit(ConstantDecls constantDecls) {
		String constName = constantDecls.getConstName();
		if (Tab.find(constName) == Tab.noObj && !(names.size() > 0 && names.contains(constName))) {
			if (constantDecls.getTypeConst().obj.getType() == currentType) {
				names.add(constName);
				constValues.add(constantDecls.getTypeConst().obj);
			} else {
				report_error("Semanticka greska: konstanta " + constName + " nije odgovarajuceg tipa.", null);
			}
			
		} else {
			report_error("Semanticka greska: konstanta " + constName + " je vec deklarisana.", null);
		}
	}
	
	public void visit(ConstDecl constDecl) {
		while (!names.isEmpty()) {
			String constName = names.remove(0);
			Obj constObj = constValues.remove(0);
			Obj obj = Tab.insert(Obj.Con, constName, currentType);
			obj.setAdr(constObj.getAdr());
			report_info("Deklarisana konstanta " + constName, null);
		}
		currentType = null;
	}
	
	public void visit(NumConst numConst) {
		numConst.obj = new Obj(Obj.Con, null, Tab.intType);
		numConst.obj.setAdr(numConst.getNumConst());
	}
	
	public void visit(CharConst charConst) {
		charConst.obj = new Obj(Obj.Con, null, Tab.charType);
		charConst.obj.setAdr(charConst.getCharConst());
	}
	
	public void visit(BoolConst boolConst) {
		boolConst.obj = new Obj(Obj.Con, null, boolType);
		Integer intValue = boolConst.getBoolConst().equals("true") ? 1 : 0;
		boolConst.obj.setAdr(intValue);
	}
	
	/* record */
	public void visit(RecordName recordName) {
		String name = recordName.getRecordName();
		if (Tab.find(name) == Tab.noObj) {
			currentRecord = Tab.insert(Obj.Type, name, recordType);
			report_info("Deklarisan record " + name, null);
			Tab.openScope();
		} else {
			report_error("Semanticka greska: ime " + name + " je vec deklarisano.", null);
		}
	}
	
	
	public void visit(RecordDecl recordDecl) {
		/*Struct varType = currentType;
		Struct varArrayType = new Struct(Struct.Array, currentType);
		while (!names.isEmpty()) {
			String varName = names.remove(0);
			String type = variableTypes.remove(0);
			if (type.equals("var")) {
				Obj node = Tab.insert(Obj.Fld, varName, varType);
				report_info("Deklarisano polje u record-u " + varName, null);
			} else {
				Tab.insert(Obj.Fld, varName, varArrayType);
				report_info("Deklarisano polje u record-u " + varName, null);
			}
		}*/
		Tab.chainLocalSymbols(currentRecord);
		Tab.closeScope();
		currentRecord = null;
	}
	
	public void visit(RecordVariableDeclaration varDecl) {
		Struct varType = currentType;
		Struct varArrayType = new Struct(Struct.Array, currentType);
		while (!names.isEmpty()) {
			String varName = names.remove(0);
			String type = variableTypes.remove(0);
			if (type.equals("var")) {
				Obj node = Tab.insert(Obj.Fld, varName, varType);
				report_info("Deklarisano polje u record-u " + varName, null);
			} else {
				Tab.insert(Obj.Fld, varName, varArrayType);
				report_info("Deklarisano polje u record-u " + varName, null);
			}
		}
		currentType = null;
	}
	
	/* class */
	public void visit(ClassNameSingle className) {
		String name = className.getClassName();
		
		if (Tab.find(name) == Tab.noObj) {			
			currentClass = Tab.insert(Obj.Type, name, classType);
			Tab.openScope();
		} else {
			report_error("Semanticka greska: ime " + name + " je vec deklarisano.", null);
		}
	}
	
	public void visit(ClassNameExtends className) {
		String name = className.getClassName();
		Struct superClass = className.getType().struct;
		//gde ovo sta sa ovim
		if (Tab.find(name) == Tab.noObj) {			
			currentClass = Tab.insert(Obj.Type, name, classType);
			//Tab.openScope();
			Tab.openScope();
		} else {
			report_error("Semanticka greska: ime " + name + " je vec deklarisano.", null);
		}
	}
	
	/* class fields */
	public void visit(ClassVariableDecl classVariableDecl) {
		currentType  = classVariableDecl.getType().struct;
	}
	
	public void visit(ClassVariableDeclList classVariableDeclList) {
		Struct varType = currentType;
		Struct varArrayType = new Struct(Struct.Array, currentType);
		
		while (!names.isEmpty()) {
			String varName = names.remove(0);
			String type = variableTypes.remove(0);
			currentClass.setLevel(currentClass.getLevel() + 1);
			if (type.equals("var")) {
				Obj node = Tab.insert(Obj.Fld, varName, varType);
				report_info("Deklarisano polje " + varName, null);
			} else {
				Tab.insert(Obj.Fld, varName, varArrayType);
				report_info("Deklarisano polje " + varName, null);
			}
		}
	}
		
	public void visit(ClassDeclMethodsNoConstr classDeclMethodsNoConstr) {
		Tab.chainLocalSymbols(currentClass);
		Tab.closeScope();
	}
	
	public void visit(ClassDeclMethods classDeclMethods) {
		Tab.chainLocalSymbols(currentClass);
		Tab.closeScope();
	}
	
	public void visit(ClassDeclNoMethods classDeclNoMethods) {
		Tab.chainLocalSymbols(currentClass);
		Tab.closeScope();
	}
	
	public void visit(ConstructorName constructorName) {
		String name = constructorName.getClassName();
		
		if (!currentClass.getName().equals(name)) {
			report_error("Semanticka greska: konstruktor mora imati isto ime kao klasa " + name, null);
		} else {
			currentMethod = Tab.insert(Obj.Meth, name+"()", Tab.noType);
			Tab.openScope();
		}
	}

	public void visit(ConstructorDecl constructorDecl) {
		Tab.chainLocalSymbols(currentMethod);
		Tab.closeScope();
	}
	
	
	
}

