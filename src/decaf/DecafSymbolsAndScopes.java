package decaf;
import org.antlr.symtab.FunctionSymbol;
import org.antlr.symtab.GlobalScope;
import org.antlr.symtab.LocalScope;
import org.antlr.symtab.Scope;
import org.antlr.symtab.VariableSymbol;
import org.antlr.symtab.Symbol;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import java.util.*;


/**
 * This class defines basic symbols and scopes for Decaf language
 */
public class DecafSymbolsAndScopes extends DecafParserBaseListener {
    ParseTreeProperty<Scope> scopes = new ParseTreeProperty<Scope>();
    GlobalScope globals;
    Scope currentScope; // define symbols in this scope
    Boolean possuiMain = false;
    ArrayList<String> variaveis = new ArrayList<String>();
    ArrayList<String> variaveisAndTypes = new ArrayList<String>();
    ArrayList<String> metodos = new ArrayList<String>();
    ArrayList<String> typeParams = new ArrayList<String>();
    Boolean typeVoid;
    String typeMethod;

    @Override
    public void enterProgram(DecafParser.ProgramContext ctx) {
        globals = new GlobalScope(null);

        for(int i=0; i<ctx.method_decl().size();i++){
            if(ctx.method_decl().get(i).ID().getText().equals("main")){
                this.possuiMain = true;
            }
        }
        pushScope(globals);
    }

    @Override
    public void exitProgram(DecafParser.ProgramContext ctx) {
        System.out.println(globals);
        if(this.possuiMain == false){
            this.error(ctx.method_decl().get(0).ID().getSymbol(),"No main method");
            System.exit(0);
        }

//        System.out.println("Variáveis declaradas: "+this.variaveis.toString());
//        System.out.println("Methods: "+this.metodos.toString());
//        System.out.println("MethodsType: "+this.typeParams.toString());
        System.out.println("Variáveis and Types: "+this.variaveisAndTypes);
    }

    @Override
    public void enterMethod_decl(DecafParser.Method_declContext ctx) {
        String name = ctx.ID().getText();
        this.typeVoid = false;
        this.metodos.add("{"+name + "," + ctx.decl().size()+"}"); // {nome,quantidade de parametros}

        for(int i=0;i<ctx.decl().size();i++){
            this.typeParams.add("{"+name+","+i+","+ctx.decl().get(i).type().getText()+"}"); // {nome, posição do parametro, tipo do parametro}
        }

        try{
            this.typeMethod = ctx.type().getText();
        }catch (Exception e){}

        try{
            if(ctx.VOID().getText().equals("void")){
                this.typeVoid = true;
            }
        }catch (Exception e){}

//        int typeTokenType = ctx.type().start.getType();
//        DecafSymbol.Type type = this.getType(typeTokenType);

        // push new scope by making new one that points to enclosing scope
        FunctionSymbol function = new FunctionSymbol(name);
        // function.setType(type); // Set symbol type

        currentScope.define(function); // Define function in current scope
        saveScope(ctx, function);
        pushScope(function);
    }

    @Override
    public void exitMethod_decl(DecafParser.Method_declContext ctx) {
        try{
            //metodo void
            if(typeVoid == true){
                for(int i=0;i<ctx.block().statement().size();i++){
                    if(ctx.block().statement().get(i).RETURN().getText().equals("return")){
                        this.error(ctx.block().statement().get(i).RETURN().getSymbol(),"should not return value");
                        System.exit(0);
                    }
                }
            }else{ //metodo não é void

//                System.out.println("Tipo do retorno -> "+this.typeMethod);

                for(int i=0;i<ctx.block().statement().size();i++){
                    //verifica o retorno
                    if(ctx.block().statement().get(i).RETURN().getText().equals("return")){
                        for(int j=0;j<ctx.block().statement().get(i).expr().size();j++) {

                            //entra no literal (boolean ou int)
                            if(ctx.block().statement().get(i).expr().get(j).literal() != null){

                                String literal = ctx.block().statement().get(i).expr().get(j).literal().getText();

                                //caso o tipo seja 'boolean' e receba um valor diferente
                                if(this.typeMethod.equals("boolean") && !(literal.equals("false") || literal.equals("true"))){
                                    System.out.println("Esperando um boolean");
                                    this.error(ctx.ID().getSymbol(),"return value has wrong type");
                                    System.exit(0);
                                }

                                //caso o tipo seja 'int' e receba um valor diferente
                                if(this.typeMethod.equals("int") && !(this.SoTemNumeros(literal))){
                                    System.out.println("Esperando um int");
                                    this.error(ctx.ID().getSymbol(),"return value has wrong type");
                                    System.exit(0);
                                }
                            }

                            // entra no location (variável)
                            if(ctx.block().statement().get(i).expr().get(i).location() != null){

                            }

                        }
                    }
                }
            }
        }catch (Exception e){}
        popScope();
    }

    @Override public void enterMethod_call(DecafParser.Method_callContext ctx) {
//        System.out.println("Method_cal ->"+ctx.method_name().ID().getText());
//        System.out.println("Method_cal ->"+ctx.expr().size());

        try{
            //verifica se passou a quantidade de parâmentro correto
            if(!this.metodos.contains("{"+ctx.method_name().ID().getText()+","+ctx.expr().size()+"}")){
                this.error(ctx.method_name().ID().getSymbol(),"argument mismatch");
                System.exit(0);
            }

            //verifica se passou o(s) tipo do(s) parametro(s) certo
            for(int i=0;i<ctx.expr().size();i++) {

                //entrando no literal
                if(ctx.expr().get(i).literal() != null){
//                System.out.println("LITERAL -> "+ctx.expr().get(i).literal().getText());
                    if(ctx.expr().get(i).literal().getText().equals("false") || ctx.expr().get(i).literal().getText().equals("true")){
                        if(!this.typeParams.contains("{"+ctx.method_name().ID().getText()+","+i+",boolean}")){
                            this.error(ctx.method_name().ID().getSymbol(),"types don't match signature");
                            System.exit(0);
                        }
                    }else{
                        if(!this.typeParams.contains("{"+ctx.method_name().ID().getText()+","+i+",int}")){
                            this.error(ctx.method_name().ID().getSymbol(),"types don't match signature");
                            System.exit(0);
                        }
                    }
                }

                //entrando no location
                if(ctx.expr().get(i).location() != null){
//                System.out.println("LOCATION -> "+ ctx.expr().get(i).location().getText());
                }
            }
        }catch(Exception e){}
    }

    @Override
    public void enterField_decl(DecafParser.Field_declContext ctx) {
        for(int i=0; i<ctx.ID().size(); i++){

            try{
                //verifica se criou um array vazio
                if(Integer.parseInt(ctx.int_literal().getText()) <= 0){
                    this.error(ctx.int_literal().HEXLIT().getSymbol(),"bad array size");
                    System.exit(0);
                }
            }catch (Exception e){ }

            String field = ctx.ID().get(i).getSymbol().getText();
            this.variaveis.add(field);


            if(ctx.LCOLCHETE() == null){
                //adicionando variáveis associado ao tipo
                this.variaveisAndTypes.add("{"+field+","+ctx.type().getText()+"}");
            }else{
                this.variaveisAndTypes.add("{"+field+","+ctx.type().getText()+","+"array"+"}");
            }

            VariableSymbol fieldSymbol = new VariableSymbol(field);
            currentScope.define(fieldSymbol);
        }
    }

    @Override
    public void exitField_decl(DecafParser.Field_declContext ctx) {
        for(int i=0; i<ctx.ID().size(); i++) {

            String name = ctx.ID().get(i).getSymbol().getText();
            Symbol field = currentScope.resolve(name);

            if (field == null) {
                this.error(ctx.ID().get(i).getSymbol(), "no such variable: " + name);
            }
            if (field instanceof FunctionSymbol) {
                this.error(ctx.ID().get(i).getSymbol(), name + " is not a variable");
            }
        }
    }


    @Override
    public void enterBlock(DecafParser.BlockContext ctx) {
//        LocalScope l = new LocalScope(currentScope);
//        saveScope(ctx, currentScope);
//        pushScope(l);
    }

    @Override
    public void exitBlock(DecafParser.BlockContext ctx) {
//        popScope();
    }

    @Override public void enterStatement(DecafParser.StatementContext ctx) {
        try{
            if(!this.variaveis.contains(ctx.location().ID().getText())){
                this.error(ctx.location().ID().getSymbol(),"identifier used before being declared");
                System.exit(0);
            }

            if(ctx.location().LCOLCHETE() != null){
                //verificando se o valor expr é inteiro ou não
                try{
                    int verify = Integer.parseInt(ctx.location().expr().getText());
                }catch (NumberFormatException e){
                    if(!this.variaveisAndTypes.contains("{"+ctx.location().expr().getText()+","+"int"+"}")){
                        this.error(ctx.location().ID().getSymbol(),"array index has wrong type");
                        System.exit(0);
                    }
                }
            }

            if(ctx.assign_op().getText() != null){
                String verifyType = "";
                String verifyArray = "";

                if(this.variaveisAndTypes.contains("{"+ctx.location().ID().getText()+","+"int"+","+"array"+"}")){
                    verifyType = "int";
                    verifyArray = "array";
                }else if(this.variaveisAndTypes.contains("{"+ctx.location().ID().getText()+","+"boolean"+","+"array"+"}")){
                    verifyType = "boolean";
                    verifyArray = "array";
                }else if (this.variaveisAndTypes.contains("{"+ctx.location().ID().getText()+","+"int"+"}")){
                    verifyType = "int";
                    verifyArray = null;
                }else if (this.variaveisAndTypes.contains("{"+ctx.location().ID().getText()+","+"boolean"+"}")){
                    verifyType = "boolean";
                    verifyArray = null;
                }

                for(int i=0;i<ctx.expr().size();i++){
                    if(verifyType == "int" && this.variaveisAndTypes.contains("{"+ctx.expr().get(i).getText()+","+"int"+","+"array"+"}")){
                        this.error(ctx.location().ID().getSymbol(), "bad type, rhs should be an int");
                        System.exit(0);
                    }else if (verifyType == "boolean"
                            && !this.variaveisAndTypes.contains("{"+ctx.expr().get(i).getText()+","+"boolean"+"}")
                            && (!ctx.expr().get(i).getText().equals("true") && !ctx.expr().get(i).getText().equals("false"))){

                        this.error(ctx.location().ID().getSymbol(), "bad type, rhs should be an int");
                        System.exit(0);
                    }
                }
            }
        }catch (Exception e){}
    }

    @Override
    public void enterDecl(DecafParser.DeclContext ctx) {
        defineVar(ctx.type(), ctx.ID().getSymbol());
        this.variaveis.add(ctx.ID().getText());
        this.variaveisAndTypes.add("{"+ctx.ID().getText()+","+ctx.type().getText()+"}");
    }

    @Override
    public void exitDecl(DecafParser.DeclContext ctx) {
        String name = ctx.ID().getSymbol().getText();
        Symbol var = currentScope.resolve(name);
        if ( var==null ) {
            this.error(ctx.ID().getSymbol(), "no such variable: "+name);
        }
        if ( var instanceof FunctionSymbol ) {
            this.error(ctx.ID().getSymbol(), name+" is not a variable");
        }
    }

    @Override
    public void enterVar_decl(DecafParser.Var_declContext ctx) {
        for(int i=0; i<ctx.ID().size(); i++){
            this.variaveis.add(ctx.ID().get(i).getText());
            this.variaveisAndTypes.add("{"+ctx.ID().get(i).getText()+","+ctx.decl().type().getText()+"}");
            defineVar(ctx.ID().get(i).getSymbol());
        }
    }

    @Override
    public void exitVar_decl(DecafParser.Var_declContext ctx) {
        for(int i=0; i<ctx.ID().size(); i++) {

            String name = ctx.ID().get(i).getSymbol().getText();
            Symbol var = currentScope.resolve(name);

            if (var == null) {
                this.error(ctx.ID().get(i).getSymbol(), "no such variable: " + name);
            }
            if (var instanceof FunctionSymbol) {
                this.error(ctx.ID().get(i).getSymbol(), name + " is not a variable");
            }
        }
    }


    void defineVar(DecafParser.TypeContext typeCtx, Token nameToken) {

        int typeTokenType = typeCtx.start.getType();
        VariableSymbol var = new VariableSymbol(nameToken.getText());

//         DecafSymbol.Type type = this.getType(typeTokenType);
//         var.setType(type);

        currentScope.define(var); // Define symbol in current scope
    }

    void defineVar(Token nameToken) {
        VariableSymbol var = new VariableSymbol(nameToken.getText());

        // DecafSymbol.Type type = this.getType(typeTokenType);
        // var.setType(type);

        currentScope.define(var); // Define symbol in current scope
    }

    /**
     * Método que atualiza o escopo para o atual e imprime o valor
     *
     * @param s
     */
    private void pushScope(Scope s) {
        currentScope = s;
        System.out.println("entering: "+currentScope.getName()+":"+s);
    }

    /**
     * Método que cria um novo escopo no contexto fornecido
     *
     * @param ctx
     * @param s
     */
    void saveScope(ParserRuleContext ctx, Scope s) {
        scopes.put(ctx, s);
    }

    /**
     * Muda para o contexto superior e atualia o escopo
     */
    private void popScope() {
        System.out.println("leaving: "+currentScope.getName()+":"+currentScope);
        currentScope = currentScope.getEnclosingScope();
    }

    public static void error(Token t, String msg) {
        System.err.printf("line %d:%d %s\n", t.getLine(), t.getCharPositionInLine(),
                msg);
    }

    /**
     * Valida tipos encontrados na linguagem para tipos reais
     *
     * @param tokenType
     * @return
     */
    public static DecafSymbol.Type getType(int tokenType) {
        switch ( tokenType ) {
            case DecafParser.VOID :  return DecafSymbol.Type.tVOID;
            case DecafParser.INTEGER_LITERAL :   return DecafSymbol.Type.tINT;
        }
        return DecafSymbol.Type.tINVALID;
    }

    /**
     * Verifica se um texto só tem números
     *
     * @param texto
     * @return
     */

    public boolean SoTemNumeros(String texto) {
        for (int i = 0; i < texto.length(); i++) {
            if (!Character.isDigit(texto.charAt(i))) {
                return false;
            }
        }
        return true;
    }


}
