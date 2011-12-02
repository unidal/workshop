package com.site.wdbc.http.dsl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import com.site.wdbc.WdbcQuery;
import com.site.wdbc.query.WdbcFilter;

public abstract class AbstractConfigurator {
   private List<Query> m_queries = new ArrayList<Query>();

   private List<Flow> m_flows = new ArrayList<Flow>();

   public void accept(Visitor visitor) {
      visitor.visit(this);
   }

   public abstract void configure();

   protected SampleField field(String name, String path, String... samples) {
      return new SampleField(name, path, samples);
   }

   protected QueryFilter filter(Class<? extends WdbcFilter> filterClass) {
      return new QueryFilter(filterClass);
   }

   protected Query findQuery(String name) {
      for (Query query : m_queries) {
         if (query.getName().equals(name)) {
            return query;
         }
      }

      throw new IllegalArgumentException(String.format("Query(%s) should be defined before been used!", name));
   }

   protected SimpleFlow flow() {
      SimpleFlow flow = new SimpleFlow("default");

      m_flows.add(flow);
      return flow;
   }

   public List<Flow> getFlows() {
      return Collections.unmodifiableList(m_flows);
   }

   public List<Query> getQueries() {
      return Collections.unmodifiableList(m_queries);
   }

   protected QueryHandler handler() {
      return new QueryHandler();
   }

   protected NodeParam param(String name) {
      return new NodeParam(name);
   }

   protected LeafParam param(String name, String value) {
      return new LeafParam(name, value);
   }

   protected QueryProcessor processor(String name, Class<? extends com.site.wdbc.http.Processor> processorClass) {
      return new QueryProcessor(name, processorClass);
   }

   protected FormQuery query(String name) {
      FormQuery query = new FormQuery(name);

      m_queries.add(query);
      return query;
   }

   protected LocalQuery query(String name, Class<? extends WdbcQuery> queryClass) {
      LocalQuery query = new LocalQuery(name, queryClass);

      m_queries.add(query);
      return query;
   }

   public static final class Attribute {
      private String m_name;

      private String m_value;

      public Attribute(String name, String value) {
         m_name = name;
         m_value = value;
      }

      public String getName() {
         return m_name;
      }

      public String getValue() {
         return m_value;
      }
   }

   @SuppressWarnings("unchecked")
   public static abstract class BaseComponent<T extends BaseComponent<T>> {
      private ParamManager m_paramManager = new ParamManager();

      private DependencyManager m_dependencyManager = new DependencyManager();

      public List<Dependency> getDependencies() {
         return m_dependencyManager.getDependencies();
      }

      public List<Param> getParams() {
         return m_paramManager.getParams();
      }

      public T req(Class<?>... roles) {
         m_dependencyManager.req(roles);
         return (T) this;
      }

      public T req(Class<?> role, String roleHint) {
         m_dependencyManager.req(role, roleHint);
         return (T) this;
      }

      public T req(Class<?> role, String roleHint, String field) {
         m_dependencyManager.req(role, roleHint, field);
         return (T) this;
      }

      public T withParams(Param... params) {
         m_paramManager.add(params);
         return (T) this;
      }
   }

   @SuppressWarnings("unchecked")
   public static abstract class BaseQuery<T extends BaseQuery<T>> extends BaseComponent<T> implements Query {
      private ParamManager m_formParamManager = new ParamManager();

      private FieldManager m_formFieldManager = new FieldManager();

      private DependencyManager m_formDependencyManager = new DependencyManager();

      private QueryFilter m_filter;

      private State m_state;

      public BaseQuery(State defaultState) {
         m_state = defaultState;
      }

      public void accept(Visitor visitor) {
         visitor.visitQuery(this);
      }

      public T filterBy(Class<? extends WdbcFilter> filterClass) {
         if (m_filter != null) {
            throw new IllegalStateException("Only one filter can be supported!");
         }

         m_filter = new QueryFilter(filterClass);
         m_filter.setName(getName());
         return (T) this;
      }

      public T filterBy(QueryFilter filter) {
         m_filter = filter;
         m_filter.setName(getName());
         return (T) this;
      }

      public T forFields(Field... fields) {
         m_formFieldManager.add(fields);
         return (T) this;
      }

      public QueryFilter getFilter() {
         return m_filter;
      }

      public List<Dependency> getFormDependencies() {
         return m_formDependencyManager.getDependencies();
      }

      public List<Field> getFormFields() {
         return m_formFieldManager.getFields();
      }

      public List<Param> getFormParams() {
         return m_formParamManager.getParams();
      }

      @Override
      public T req(Class<?>... roles) {
         if (m_state == State.FORM) {
            m_formDependencyManager.req(roles);
         } else {
            super.req(roles);
         }

         return (T) this;
      }

      @Override
      public T req(Class<?> role, String roleHint) {
         if (m_state == State.FORM) {
            m_formDependencyManager.req(role, roleHint);
         } else {
            super.req(role, roleHint);
         }

         return (T) this;
      }

      @Override
      public T req(Class<?> role, String roleHint, String field) {
         if (m_state == State.FORM) {
            m_formDependencyManager.req(role, roleHint, field);
         } else {
            super.req(role, roleHint, field);
         }

         return (T) this;
      }

      public void setState(State state) {
         m_state = state;
      }

      @Override
      public T withParams(Param... params) {
         if (m_state == State.FORM) {
            m_formParamManager.add(params);
         } else {
            super.withParams(params);
         }

         return (T) this;
      }
   }

   public static final class Dependency implements Visitable {
      private Class<?> m_role;

      private String m_roleHint;

      private String m_field;

      public Dependency(Class<?> role) {
         m_role = role;
      }

      public Dependency(Class<?> role, String roleHint) {
         this(role);
         m_roleHint = roleHint;
      }

      public Dependency(Class<?> role, String roleHint, String field) {
         this(role, roleHint);
         m_field = field;
      }

      public void accept(Visitor visitor) {
         visitor.visitDependency(this);
      }

      public String getField() {
         return m_field;
      }

      public Class<?> getRole() {
         return m_role;
      }

      public String getRoleHint() {
         return m_roleHint;
      }
   }

   public static final class DependencyManager implements Requirement {
      private List<Dependency> m_dependencies;

      public void accept(Visitor visitor) {
         visitor.visitRequirement(this);
      }

      public List<Dependency> getDependencies() {
         if (m_dependencies == null) {
            return Collections.emptyList();
         } else {
            return Collections.unmodifiableList(m_dependencies);
         }
      }

      private void initialize() {
         if (m_dependencies == null) {
            m_dependencies = new ArrayList<Dependency>();
         }
      }

      public DependencyManager req(Class<?>... roles) {
         initialize();

         for (Class<?> role : roles) {
            if (role != null) {
               m_dependencies.add(new Dependency(role));
            }
         }

         return this;
      }

      public DependencyManager req(Class<?> role, String roleHint) {
         initialize();
         m_dependencies.add(new Dependency(role, roleHint));
         return this;
      }

      public DependencyManager req(Class<?> role, String roleHint, String field) {
         initialize();
         m_dependencies.add(new Dependency(role, roleHint, field));
         return this;
      }
   }

   public static interface Execution extends Visitable {

   }

   public static interface Field extends Visitable {
      public String getName();
   }

   public static final class FieldManager {
      private List<Field> m_fields;

      public void add(Field... fields) {
         initialize();

         for (Field field : fields) {
            if (field != null) {
               m_fields.add(field);
            }
         }
      }

      public List<Field> getFields() {
         if (m_fields == null) {
            return Collections.emptyList();
         } else {
            return Collections.unmodifiableList(m_fields);
         }
      }

      private void initialize() {
         if (m_fields == null) {
            m_fields = new ArrayList<Field>();
         }
      }
   }

   public static interface Filter extends Requirement {
      public Class<? extends WdbcFilter> getFilterClass();

      public String getName();

      public Filter withParams(Param... params);
   }

   public static interface Flow extends Visitable {
      public List<Execution> getExecutions();

      public String getName();
   }

   public static class FormQuery extends BaseQuery<FormQuery> implements Query {
      private String m_name;

      private String m_samplePage;

      public FormQuery(String name) {
         super(State.FORM);

         m_name = name;
      }

      public FormQuery form() {
         setState(State.FORM);
         return this;
      }

      public FormQuery from(String action) {
         withParams(new LeafParam("action", action));
         return this;
      }

      public String getName() {
         return m_name;
      }

      public String getSamplePage() {
         return m_samplePage;
      }

      public FormQuery query() {
         setState(State.QUERY);
         return this;
      }

      public FormQuery usePost() {
         withParams(new LeafParam("method", "POST"));
         return this;
      }

      public FormQuery withSamplePage(String samplePage) {
         m_samplePage = samplePage;
         return this;
      }
   }

   public static interface Handler extends Visitable {
      public List<Execution> getExecutions();
   }

   public static class HandlerExecution implements Execution {
      private Handler m_handler;

      public HandlerExecution(Handler handler) {
         m_handler = handler;
      }

      public void accept(Visitor visitor) {
         visitor.visitExecution(this);
      }

      public Handler getHandler() {
         return m_handler;
      }
   }

   public static class LeafParam implements Param {
      private String m_name;

      private String m_value;

      private List<Attribute> m_attributes;

      public LeafParam(String name, String value) {
         m_name = name;
         m_value = value;
      }

      public void accept(Visitor visitor) {
         visitor.visitParam(this);
      }

      public LeafParam attr(String name, String value) {
         initialize();

         m_attributes.add(new Attribute(name, value));
         return this;
      }

      public List<Attribute> getAttributes() {
         if (m_attributes == null) {
            return Collections.emptyList();
         } else {
            return Collections.unmodifiableList(m_attributes);
         }
      }

      public String getName() {
         return m_name;
      }

      public String getValue() {
         return m_value;
      }

      private void initialize() {
         if (m_attributes == null) {
            m_attributes = new ArrayList<Attribute>();
         }
      }
   }

   public static class LocalQuery extends BaseQuery<LocalQuery> implements Query {
      private String m_name;

      private Class<? extends WdbcQuery> m_queryClass;

      public LocalQuery(String name, Class<? extends WdbcQuery> queryClass) {
         super(State.QUERY);

         m_name = name;
         m_queryClass = queryClass;
      }

      public String getName() {
         return m_name;
      }

      public Class<? extends WdbcQuery> getQueryClass() {
         return m_queryClass;
      }
   }

   public static class LoopQueryExecution implements Execution {
      private Query m_query;

      private int m_queryIntervalInMillis;

      private Handler m_handler;

      public LoopQueryExecution(Query query, int queryIntervalInMillis, Handler handler) {
         m_query = query;
         m_queryIntervalInMillis = queryIntervalInMillis;
         m_handler = handler;
      }

      public void accept(Visitor visitor) {
         visitor.visitExecution(this);
      }

      public Handler getHandler() {
         return m_handler;
      }

      public Query getQuery() {
         return m_query;
      }

      public int getQueryIntervalInSecond() {
         return m_queryIntervalInMillis;
      }
   }

   public static class NodeParam implements Param {
      private String m_name;

      private List<Param> m_params;

      public NodeParam(String name) {
         m_name = name;
      }

      public void accept(Visitor visitor) {
         visitor.visitParam(this);
      }

      public NodeParam add(Param... params) {
         initialize();

         for (Param param : params) {
            if (param != null) {
               m_params.add(param);
            }
         }

         return this;
      }

      public String getName() {
         return m_name;
      }

      public List<Param> getParams() {
         if (m_params == null) {
            return Collections.emptyList();
         } else {
            return Collections.unmodifiableList(m_params);
         }
      }

      private void initialize() {
         if (m_params == null) {
            m_params = new ArrayList<Param>();
         }
      }
   }

   public static interface Param extends Visitable {
      public String getName();
   }

   public static final class ParamManager {
      private List<Param> m_params;

      public void add(Param... params) {
         initialize();

         for (Param param : params) {
            if (param != null) {
               m_params.add(param);
            }
         }
      }

      public List<Param> getParams() {
         if (m_params == null) {
            return Collections.emptyList();
         } else {
            return Collections.unmodifiableList(m_params);
         }
      }

      private void initialize() {
         if (m_params == null) {
            m_params = new ArrayList<Param>();
         }
      }
   }

   public static interface Processor extends Requirement {
      public String getName();

      public Class<? extends com.site.wdbc.http.Processor> getProcessorClass();

      public Processor withParams(Param... params);
   }

   public static interface Query extends Requirement {
      public Query filterBy(Class<? extends WdbcFilter> filterClass);

      public Query forFields(Field... fields);

      public String getName();

      public Query withParams(Param... params);
   }

   public class QueryExecution implements Execution {
      private Query m_query;

      public QueryExecution(Query query) {
         m_query = query;
      }

      public void accept(Visitor visitor) {
         visitor.visitExecution(this);
      }

      public Query getQuery() {
         return m_query;
      }
   }

   public static class QueryFilter extends BaseComponent<QueryFilter> implements Filter {
      private String m_name;

      private Class<? extends WdbcFilter> m_filterClass;

      public QueryFilter(Class<? extends WdbcFilter> filterClass) {
         m_filterClass = filterClass;
      }

      public void accept(Visitor visitor) {
         visitor.visitFilter(this);
      }

      public Class<? extends WdbcFilter> getFilterClass() {
         return m_filterClass;
      }

      public String getName() {
         return m_name;
      }

      void setName(String name) {
         m_name = name;
      }
   }

   public class QueryHandler implements Handler {
      private List<Execution> m_executions = new ArrayList<Execution>();

      public void accept(Visitor visitor) {
         visitor.visitHandler(this);
      }

      public QueryHandler executeQuery(String... names) {
         for (String name : names) {
            Query query = findQuery(name);

            m_executions.add(new QueryExecution(query));
         }

         return this;
      }

      public QueryHandler forEach(String queryName, Handler handler) {
         return forEach(queryName, 0, handler);
      }

      public QueryHandler forEach(String queryName, int queryIntervalInMillis, Handler handler) {
         Query query = findQuery(queryName);

         m_executions.add(new LoopQueryExecution(query, queryIntervalInMillis, handler));
         return this;
      }

      public List<Execution> getExecutions() {
         return Collections.unmodifiableList(m_executions);
      }

      public QueryHandler with(Handler handler) {
         m_executions.add(new HandlerExecution(handler));
         return this;
      }

      public QueryHandler processBy(Processor processor) {
         m_executions.add(new ProcessorExecution(processor));
         return this;
      }
   }

   public static class QueryProcessor extends BaseComponent<QueryProcessor> implements Processor {
      private String m_name;

      private Class<? extends com.site.wdbc.http.Processor> m_processorClass;

      public QueryProcessor(String name, Class<? extends com.site.wdbc.http.Processor> processorClass) {
         m_name = name;
         m_processorClass = processorClass;
      }

      public void accept(Visitor visitor) {
         visitor.visitProcessor(this);
      }

      public String getName() {
         return m_name;
      }

      public Class<? extends com.site.wdbc.http.Processor> getProcessorClass() {
         return m_processorClass;
      }
   }

   public static class ProcessorExecution implements Execution {
      private Processor m_processor;

      public ProcessorExecution(Processor processor) {
         m_processor = processor;
      }

      public void accept(Visitor visitor) {
         visitor.visitExecution(this);
      }

      public Processor getProcessor() {
         return m_processor;
      }
   }

   public static interface Requirement extends Visitable {
      public List<Dependency> getDependencies();

      public Requirement req(Class<?>... roles);

      public Requirement req(Class<?> role, String roleHint);

      public Requirement req(Class<?> role, String roleHint, String field);
   }

   public class SampleField implements Field {
      private String m_name;

      private String m_path;

      private String[] m_samples;

      public SampleField(String name, String path, String... samples) {
         m_name = name;
         m_path = path;
         m_samples = samples;
      }

      public void accept(Visitor visitor) {
         visitor.visitField(this);
      }

      public String getName() {
         return m_name;
      }

      public String getPath() {
         return m_path;
      }

      public String[] getSamples() {
         return m_samples;
      }
   }

   public class SimpleFlow implements Flow {
      private String m_name;

      private List<Execution> m_executions;

      public SimpleFlow(String name) {
         m_name = name;
      }

      public void accept(Visitor visitor) {
         visitor.visitFlow(this);
      }

      public SimpleFlow executeQuery(String... names) {
         initialize();

         for (String name : names) {
            Query query = findQuery(name);

            if (query == null) {
               throw new IllegalArgumentException(String.format("Query(%s) should be defined before been used!", name));
            }

            m_executions.add(new QueryExecution(query));
         }

         return this;
      }

      public List<Execution> getExecutions() {
         if (m_executions == null) {
            return Collections.emptyList();
         } else {
            return Collections.unmodifiableList(m_executions);
         }
      }

      public String getName() {
         return m_name;
      }

      private void initialize() {
         if (m_executions == null) {
            m_executions = new ArrayList<Execution>();
         }
      }

      public SimpleFlow with(Handler handler) {
         initialize();
         m_executions.add(new HandlerExecution(handler));
         return this;
      }
   }

   public static enum State {
      FORM,

      QUERY;
   }

   public static class TrackingSupport {
      private Stack<Object> m_stack = new Stack<Object>();

      private boolean m_verbose;

      public TrackingSupport(boolean verbose) {
         m_verbose = verbose;
      }

      protected boolean isVerbose() {
         return m_verbose;
      }

      protected void log(String format, Object... args) {
         if (m_verbose) {
            System.out.println(String.format(format, args));
         }
      }

      @SuppressWarnings("unchecked")
      protected <T> T peek() {
         return (T) m_stack.peek();
      }

      protected void pop() {
         m_stack.pop();
      }

      protected void push(Object obj) {
         m_stack.push(obj);
      }
   }

   public static interface Visitable {
      public void accept(Visitor visitor);
   }

   public static interface Visitor {
      public void visit(AbstractConfigurator configurator);

      public void visitDependency(Dependency dependency);

      public void visitExecution(Execution execution);

      public void visitField(Field field);

      public void visitFilter(Filter filter);

      public void visitFlow(Flow flow);

      public void visitHandler(Handler handler);

      public void visitParam(Param param);

      public void visitProcessor(Processor processor);

      public void visitQuery(Query query);

      public void visitRequirement(Requirement requirement);
   }
}
