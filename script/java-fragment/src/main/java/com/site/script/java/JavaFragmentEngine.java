package com.site.script.java;

import java.io.Reader;

import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

/**
 * Java Fragment engine to compile and execute Java source code on the fly.
 * <p>
 * 
 * <ul>
 * Following use cases are supported
 * <li>a piece of java code</li>
 * </ul>
 * 
 * @author Frankie Wu <qmwu2000@gmail.com>
 * @since March 17, 2012
 * @version 0.1.0
 */
public class JavaFragmentEngine extends AbstractScriptEngine {
   private ScriptEngineFactory m_factory;

   private JavaFragmentCompiler m_compiler;

   JavaFragmentEngine(JavaFragmentEngineFactory factory) {
      m_factory = factory;
      m_compiler = new JavaFragmentCompiler(this);
   }

   @Override
   public Bindings createBindings() {
      return new SimpleBindings();
   }

   @Override
   public Object eval(Reader script, ScriptContext context) throws ScriptException {
      CompiledScript compiledScript = getCompilable().compile(script);

      return compiledScript.eval(context);
   }

   @Override
   public Object eval(String script, ScriptContext context) throws ScriptException {
      CompiledScript compiledScript = getCompilable().compile(script);

      return compiledScript.eval(context);
   }

   private Compilable getCompilable() {
      return m_compiler;
   }

   @Override
   public ScriptEngineFactory getFactory() {
      return m_factory;
   }
}
