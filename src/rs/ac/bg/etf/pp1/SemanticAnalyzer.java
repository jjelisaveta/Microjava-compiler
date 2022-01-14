package rs.ac.bg.etf.pp1;


import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.lang.model.type.NoType;
import javax.swing.text.TabableView;

import org.apache.log4j.Logger;


import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.etf.pp1.symboltable.concepts.*;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.*;

public class SemanticAnalyzer extends VisitorAdaptor {
	
	static final Struct boolType = Tab.insert(Obj.Type, "bool", new Struct(Struct.Bool)).getType();
	static final Struct recordType = Tab.insert(Obj.Type, "record", new Struct(Struct.Class)).getType();
	
	Obj currentMethod = null;
	Obj currentRecord = null;
	Obj currentClass = null;
	Stack<Obj> currentMethodCallStack = new Stack<>();
	Obj currentDesignator = null;
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
		if (Tab.find(varName) == Tab.noObj &&  !(names.size() > 0 && names.contains(varName))) {   
			names.add(varName);
			variableTypes.add("var");
		} else {
			report_error("Semanticka greska: globalna promenljiva " + varName + " je vec deklarisana", null);
		}
	}
	
	public void visit(GlobalOneArrayVariableDecl oneArrayVariableDecl) {
		String varName = oneArrayVariableDecl.getVarName();
		if (Tab.find(varName) == Tab.noObj &&  !(names.size() > 0 && names.contains(varName))) {  
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
				Obj obj = Tab.insert(Obj.Var, varName, varType);
				report_info("Deklarisana globalna promenljiva " + varName, null);
			} else {
				Obj obj = Tab.insert(Obj.Var, varName, varArrayType);
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
		if (Tab.currentScope().findSymbol(name) == null || (Tab.currentScope().findSymbol(name)!= null &&  Tab.currentScope().findSymbol(name).getKind() != Obj.Meth)) {
			if (name.equals("main")) 
				mainOk = true;
			currentMethod = Tab.insert(Obj.Meth, name, currentType);
			currentType = null;
			report_info("metoda " + name + " " + methodName.getLine(), null);
			Tab.openScope();
		} else {
			report_error("Semanticka greska: identifikator " + name + " je vec iskoriscen.", null);
		}
	}
	
	public void visit(MethodDeclStmt methodDecl) {
		Tab.chainLocalSymbols(currentMethod);
		Tab.closeScope();
		
		if(!returnFound && currentMethod.getType() != Tab.noType){
			report_error("Semanticka greska: izostavljena return naredba u metodi " + currentMethod.getName() + methodDecl.getLine(), null);
		}
		currentMethod = null;
		returnFound = false;
	}
	
	public void visit(MethodDeclNoStmt methodDecl) {
		Tab.chainLocalSymbols(currentMethod);
		Tab.closeScope();
		
		if(!returnFound && currentMethod.getType() != Tab.noType){
			report_error("Semanticka greska: izostavljena return naredba u metodi "+ currentMethod.getName(), null);
		}
		currentMethod = null;
		returnFound = false;
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
		
		if (Tab.currentScope().findSymbol(paramName) == null) {
			Obj obj = Tab.insert(Obj.Var, paramName, type);
			obj.setFpPos(params.size());
			params.add(obj);
			currentMethod.setLevel(params.size());
			report_info("Deklarisan formalni parametar " + paramName + " broj " + currentMethod.getLevel(), null);
		} else {
			report_error("Semanticka greska: ime " + paramName + " je vec deklarisano.", null);
		}	
	}
	
	public void visit(OneArrayFormParam oneFormParam) {
		String paramName = oneFormParam.getParamName();
		Struct type = new Struct(Struct.Array, oneFormParam.getType().struct);
		if (Tab.currentScope().findSymbol(paramName) == null) {
			Obj obj = Tab.insert(Obj.Var, paramName, type);
			obj.setFpPos(params.size());
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
		if (Tab.currentScope().findSymbol(varName) == null &&  !(names.size() > 0 && names.contains(varName))) {   //nije deklarisan
			names.add(varName);
			variableTypes.add("var");
		} else {
			report_error("Semanticka greska: lokalna promenljiva " + varName + " je vec deklarisana", null);
		}
		
	}
	
	public void visit(OneArrayVariableDecl oneArrayVariableDecl) {
		String varName = oneArrayVariableDecl.getVarName();
		if (Tab.currentScope().findSymbol(varName) == null &&  !(names.size() > 0 && names.contains(varName))) {   //nije deklarisan
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
	
	/* constants */
	public void visit(ConstantDecl constantDecl) {
		currentType = constantDecl.getType().struct;
		if (!currentType.equals(Tab.intType) && !currentType.equals(Tab.charType) && !currentType.equals(boolType)) {
			report_error("Semanticka greska: konstanta mora biti tipa int, char ili bool.", null);
		}
		String constName = constantDecl.getConstName();
		if (Tab.find(constName) == Tab.noObj) {
			if (constantDecl.getTypeConst().obj.getType() == currentType) {
				names.add(constName);
				constValues.add(constantDecl.getTypeConst().obj);
			} else {
				report_error("Semanticka greska: konstanta " + constName + " nije odgovarajuceg tipa.", null);
			}
		}  else {
			report_error("Semanticka greska: identifikator " + constName + " je vec iskoriscen.", null);
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
		Tab.chainLocalSymbols(currentRecord.getType());
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
			currentClass = Tab.insert(Obj.Type, name, new Struct(Struct.Class));
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
			currentClass = Tab.insert(Obj.Type, name, new Struct(Struct.Class));
			
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
		int i = 1;
		while (!names.isEmpty()) {
			String varName = names.remove(0);
			String type = variableTypes.remove(0);
			currentClass.setLevel(currentClass.getLevel() + 1);
			if (type.equals("var")) {
				Obj node = Tab.insert(Obj.Fld, varName, varType);
				node.setFpPos(i);
				i++;
				report_info("Deklarisano polje " + varName, null);
			} else {
				Obj node = Tab.insert(Obj.Fld, varName, varArrayType);
				node.setFpPos(i);
				i++;
				report_info("Deklarisano polje " + varName, null);
			}
		}
	}
		
	public void visit(ClassDeclMethodsNoConstr classDeclMethodsNoConstr) {
		Tab.chainLocalSymbols(currentClass.getType());
		Tab.closeScope();
	}
	
	public void visit(ClassDeclMethods classDeclMethods) {
		Tab.chainLocalSymbols(currentClass.getType());
		Tab.closeScope();
	}
	
	public void visit(ClassDeclNoMethods classDeclNoMethods) {
		Tab.chainLocalSymbols(currentClass.getType());
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
	
	/* factor */
	
	/* function call rvalue */
	public void visit(FactorNoParen factorNoParen) {
		factorNoParen.obj = factorNoParen.getDesignator().obj;
		// PITANJE: da li treba jos nesto
	}
	
	public void visit(FactorParen factorParen) {
		factorParen.obj = factorParen.getDesignator().obj;
		report_info("Pronadjen poziv funkcije " + factorParen.obj.getName() + " na liniji " + factorParen.getLine(), null);
	}
	
	public void visit(FactorTypeConst factorTypeConst) {
		factorTypeConst.obj = factorTypeConst.getTypeConst().obj;
	}

	public void visit(FactorType factorType) {
		if (factorType.getType().struct.getKind() != Struct.Class) {
			factorType.obj = Tab.noObj;
			report_error("Semanticka greska: moze se instancirati samo klasni tip.", null);
		} else {
			factorType.obj = new Obj(Obj.Var, "", factorType.getType().struct);
		}
	}
	
	public void visit(FactorArrayType factorType) {
		if (factorType.getType().struct.getKind() != Struct.Class) {
			factorType.obj = Tab.noObj;
			report_error("Semanticka greska: moze se instancirati samo klasni tip.", null);
		} else {
			factorType.obj = new Obj(Obj.Var, "", new Struct(Struct.Array, factorType.getType().struct));
		}
	}
	
	public void visit(FactorExpr factorExpr) {
		factorExpr.obj = factorExpr.getExpr().obj;
	}
	
	/* addop list */
	public void visit(OneAddop oneAddop) {
		oneAddop.obj = oneAddop.getTerm().obj;
	}
	
	public void visit(AddOpsList addOpsList) {
		Obj el1 = addOpsList.getAddopList().obj;
		Obj el2 = addOpsList.getTerm().obj;
		
		if (el1.getType().getKind() == el2.getType().getKind() && el1.getType().getKind() == Struct.Int) {
			addOpsList.obj = new Obj(Obj.Var, "", el1.getType());
		} else {
			addOpsList.obj = Tab.noObj;
			report_error("Semanticka greska na liniji " + addOpsList.getLine() + ": tipovi moraju biti isti i moraju biti tipa int."  , null);
		}
	}
	
	/* term */
	public void visit(TermFactor termFactor) {
		termFactor.obj = termFactor.getFactor().obj;
	}
	
	public void visit(Terms terms) {
		Obj el1 = terms.getTerm().obj;
		Obj el2 = terms.getFactor().obj;
		
		if (el1.getType().getKind() == el2.getType().getKind() && el1.getType().getKind() == Struct.Int) {
			terms.obj = new Obj(Obj.Var, "", el1.getType());
		} else {
			terms.obj = Tab.noObj;
			report_error("Semanticka greska na liniji " + terms.getLine() + ": tipovi moraju biti isti i moraju biti tipa int."  , null);
		}
	}
	
	/* expr */
	public void visit(ExprPlus exprPlus) {
		exprPlus.obj = exprPlus.getTerm().obj;
	}
	
	public void visit(ExprPlusAddop exprPlusAddop) {
		Obj el1 = exprPlusAddop.getTerm().obj;
		Obj el2 = exprPlusAddop.getAddopList().obj;
		
		if (el1.getType().getKind() == el2.getType().getKind() && el1.getType().getKind() == Struct.Int) {
			exprPlusAddop.obj = new Obj(Obj.Var, "", el1.getType());
		} else {
			exprPlusAddop.obj = Tab.noObj;
			report_error("Semanticka greska na liniji " + exprPlusAddop.getLine() + ": tipovi moraju biti isti i moraju biti int."  , null);
		}
	}
	
	public void visit(ExprMinus exprMinus) {
		if (exprMinus.getTerm().obj.getType().getKind() == Struct.Int) {
			exprMinus.obj = exprMinus.getTerm().obj;
		} else {
			exprMinus.obj = Tab.noObj;
			report_error("Semanticka greska na liniji " + exprMinus.getLine() + ": tip mora biti int."  , null);
		}
	}
	
	public void visit(ExprMinusAddop exprMinusAddop) {
		Obj el1 = exprMinusAddop.getTerm().obj;
		Obj el2 = exprMinusAddop.getAddopList().obj;
		
		if (el1.getType().getKind() == el2.getType().getKind() && el1.getType().getKind() == Struct.Int) {
			exprMinusAddop.obj = new Obj(Obj.Var, "", el1.getType());
		} else {
			exprMinusAddop.obj = Tab.noObj;
			report_error("Semanticka greska na liniji " + exprMinusAddop.getLine() + ": tipovi moraju biti isti i moraju biti int."  , null);
		}
	}
	
	/* cond term */
	public void visit(ConditionCondFact conditionCondFact) {
		conditionCondFact.obj = conditionCondFact.getCondFact().obj;
	}
	
	public void visit(CondTerms condTerms) {
		condTerms.obj = new Obj(Obj.NO_VALUE, "", condTerms.getCondTerm().obj.getType());
	}
	
	/* cond fact */
	public void visit(CondFactExpr condFactExpr) {
		condFactExpr.obj = condFactExpr.getExpr().obj;
	}
	
	public void visit(CondFactExprRelop condFactExprRelop) {
		Obj el1 = condFactExprRelop.getExpr().obj;
		Obj el2 = condFactExprRelop.getExpr1().obj;
		
		if (el1.getType().getKind() == el2.getType().getKind()) {
			condFactExprRelop.obj = new Obj(Obj.Var, "", el2.getType());
		} else {
			condFactExprRelop.obj = Tab.noObj;
			report_error("Semanticka greska na liniji " + condFactExprRelop.getLine() + ": tipovi moraju biti isti."  , null);
		}
	}
	
	/* designator */

	public void visit(DesignatorIdent designator) {
		Obj obj = Tab.find(designator.getName());
    	if(obj == Tab.noObj){
    		designator.obj = Tab.noObj;
			report_error("Greska na liniji " + designator.getLine()+ " : ime "+ designator.getName() + " nije deklarisano! ", null);
    	} else {
    		designator.obj = obj;
	    	if (Tab.currentScope().findSymbol(designator.getName()) != null && designator.obj.getKind() == Obj.Var) {
	    		report_info("Pristup lokalnoj promenljivoj " + designator.getName() + " na liniji " + designator.getLine(), null);
	    	}
	    	else  if (obj.getLevel() == 0) {
				report_info("Pristup globalnoj promenljivoj " + designator.getName() + " na liniji " + designator.getLine(), null);	
	    	} 
	    	if (designator.obj.getKind() == Obj.Meth) {
	    		currentMethodCallStack.push(obj);
	    	}
    	}
	}
	
	public void visit(DesignatorBracket designator) {
		/*Obj obj = Tab.find(designator.getDesignator().ob);
    	if(obj == Tab.noObj){
			report_error("Greska na liniji " + designator.getLine()+ " : ime "+designator.getName()+" nije deklarisano! ", null);
    	} else {
    		designator.obj = obj;
    		if (obj.getType().getKind() == Struct.Array) {
	    		report_info("Pristup nizu " + designator.getName() + " na liniji " + designator.getLine(), null);
    	}
    */
		
	}	
	
	public void visit(DesignatorPoint designator) {
		Obj prevDesignator = designator.getDesignator().obj;
		if (prevDesignator.getKind() == Obj.Meth)
		prevDesignator.getLocalSymbols();
		prevDesignator.getType().getMembers();
	}
	
	/* desginator statement */
	
	public void visit(DesignatorStmtFuncCall designatorStmtFuncCall) {
		Obj func = designatorStmtFuncCall.getDesignator().obj;
    	if(Obj.Meth == func.getKind()){
    		currentMethodCallStack.pop();
			report_info("Pronadjen poziv funkcije " + func.getName() + " na liniji " + designatorStmtFuncCall.getLine(), null);
			designatorStmtFuncCall.obj = func;
    	}else{
			report_error("Greska na liniji " + designatorStmtFuncCall.getLine()+" : ime " + func.getName() + " nije funkcija!", null);
			//designatorStmtFuncCall.struct = Tab.noType;
    	}
	}
	
	public void visit(DesignatorStmtAssign designatorStmtAssign) {
		
	}
	
	public void visit(AssignStmt assignStmt) {
		Designator designator = assignStmt.getDesignator();
		Expr expr = assignStmt.getExpr();
		
		if (designator.obj.getType().equals(expr.obj.getType()) 
				&& (designator.obj.getKind() == Obj.Fld || designator.obj.getKind() == Obj.Var 
						|| designator.obj.getKind() == Obj.Elem)) {
			//assignStmt.obj = sve ok
		} else {
			if (!designator.obj.getType().equals(expr.obj.getType())) {
				report_error("Greska na liniji " + assignStmt.getLine()+" : Tipovi nisu isti.", null);
			}
			if (!(designator.obj.getKind() == Obj.Fld || designator.obj.getKind() == Obj.Var 
				|| designator.obj.getKind() == Obj.Elem)) {
				report_error("Greska na liniji " + assignStmt.getLine()+" :  Designator mora biti promenljiva, element niza ili polje objekta unutrašnje klase.", null);
			}
		}
	}
	
	public void visit(DesignatorStmtInc designatorStmtInc) {
		Designator designator = designatorStmtInc.getDesignator();
		if (designator.obj.getType().getKind() == Struct.Int 
				&& (designator.obj.getKind() == Obj.Fld || designator.obj.getKind() == Obj.Var 
				|| designator.obj.getKind() == Obj.Elem)) {
			designatorStmtInc.obj = designator.obj;
		} else {
			
			if (designator.obj.getType().getKind() != Struct.Int) {
				report_error("Greska na liniji " + designatorStmtInc.getLine()+" : Designator mora biti tipa int.", null);
			}
			if (!(designator.obj.getKind() == Obj.Fld || designator.obj.getKind() == Obj.Var 
				|| designator.obj.getKind() == Obj.Elem)) {
				report_error("Greska na liniji " + designatorStmtInc.getLine()+" :  Designator mora biti promenljivu, element niza ili polje objekta unutrašnje klase. .", null);
			}
			designator.obj = Tab.noObj;
		}
	}
	
	public void visit(DesignatorStmtDec designatorStmtDec) {
		Designator designator = designatorStmtDec.getDesignator();
		if (designator.obj.getType().getKind() == Struct.Int 
				&& (designator.obj.getKind() == Obj.Fld || designator.obj.getKind() == Obj.Var 
				|| designator.obj.getKind() == Obj.Elem)) {
			designatorStmtDec.obj = designator.obj;
		} else {
			
			if (designator.obj.getType().getKind() != Struct.Int) {
				report_error("Greska na liniji " + designatorStmtDec.getLine()+" : Designator mora biti tipa int.", null);
			}
			if (!(designator.obj.getKind() == Obj.Fld || designator.obj.getKind() == Obj.Var 
				|| designator.obj.getKind() == Obj.Elem)) {
				report_error("Greska na liniji " + designatorStmtDec.getLine()+" :  Designator mora biti promenljivu, element niza ili polje objekta unutrašnje klase. .", null);
			}
			designator.obj = Tab.noObj;
		}
	}
	
	/* method call */
	public void visit(ReturnStmtExpr returnStmtExpr) {
		returnFound = true;
	}
	
	public void visit(ReturnStmt returnStmt) {
		returnFound = true;
	}
	/* actual parameters */
	public void visit(ActParamsList actParsList) {
		if ((currentMethodCallStack.peek().getLevel() == 0  && params.size() != 0) || (currentMethodCallStack.peek().getLevel()!= params.size())) {  //proveri za level
			report_error("Greska na liniji " + actParsList.getLine()+ " : parametri metode "+ currentMethodCallStack.peek().getName() +" nisu ispravni! ", null);
		} else {
			int i = 0;
			for (Obj param : currentMethodCallStack.peek().getLocalSymbols()) {
				if (i++ != 0) {
					Obj actParam = params.remove(0);
					if (!actParam.getType().equals(param.getType())) {
						report_error("Greska na liniji " + actParsList.getLine()+ " : parametar "+ param.getName() +" nije odgovarajuceg tipa!", null);
					}
				}
			}
		}
		params.clear();
	}
	
	public void visit(NoActParams noActParams) {
		
	}
	
	public void visit(ActParam actParam) {
		//proveriti tip i 
		params.add(actParam.getExpr().obj);

	}
	

}

